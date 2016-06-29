package org.hbp.mip.controllers;

import com.google.gson.*;
import io.swagger.annotations.*;
import org.apache.log4j.Logger;
import org.hbp.mip.MIPApplication;
import org.hbp.mip.model.*;
import org.hbp.mip.utils.HTTPUtil;
import org.hbp.mip.utils.HibernateUtil;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.exception.DataException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.net.*;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * Created by habfast on 21/04/16.
 */
@RestController
@RequestMapping(value = "/experiments", produces = {APPLICATION_JSON_VALUE})
@Api(value = "/experiments", description = "the experiments API")
@javax.annotation.Generated(value = "class io.swagger.codegen.languages.SpringMVCServerCodegen", date = "2016-01-07T07:38:20.227Z")
public class ExperimentApi {

    private Logger logger = Logger.getLogger(this.getClass());

    private static final String EXAREME_ALGO_JSON_FILE="data/exareme_algorithms.json";

    private static final Gson gson = new GsonBuilder()
            .serializeNulls()
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
            .excludeFieldsWithoutExposeAnnotation()
            .create();

    @Value("#{'${workflow.experimentUrl:http://dockerhost:8087/experiment}'}")
    private String experimentUrl;

    @Value("#{'${workflow.listMethodsUrl:http://dockerhost:8087/list-methods}'}")
    private String listMethodsUrl;

    @Value("#{'${workflow.miningExaremeUrl:http://hbps2.chuv.ch:9090/mining/query}'}")
    private String miningExaremeQueryUrl;

    @Autowired
    MIPApplication mipApplication;

    private void sendPost(Experiment experiment) throws MalformedURLException {
        URL obj = new URL(experimentUrl);

        // this runs in the background. For future optimization: use a thread pool
        new Thread() {
            public void run() {
                try {
                    HttpURLConnection con = (HttpURLConnection) obj.openConnection();

                    String query = experiment.computeQuery();
                    System.out.println("Running experiment: " + query);

                    // create query
                    try {
                        con.setRequestMethod("POST");
                    } catch (ProtocolException pe) { logger.trace(pe); } // ignore; won't happen
                    con.addRequestProperty("Content-Type", "application/json");
                    con.setRequestProperty("Content-Length", Integer.toString(query.length()));
                    con.setFollowRedirects(true);
                    con.setReadTimeout(3600000); // 1 hour: 60*60*1000 ms

                    // write body of query
                    con.setDoOutput(true);
                    DataOutputStream wr = new DataOutputStream(con.getOutputStream());
                    wr.write(query.getBytes("UTF8"));
                    wr.flush();
                    wr.close();

                    // get response
                    InputStream stream = con.getResponseCode() < 400 ? con.getInputStream() : con.getErrorStream();
                    BufferedReader in = new BufferedReader(new InputStreamReader(stream));
                    String inputLine;
                    StringBuilder response = new StringBuilder();
                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine + '\n');
                    }
                    in.close();

                    // write to experiment
                    experiment.setResult(response.toString().replace("\0", ""));
                    experiment.setHasError(con.getResponseCode() >= 400);
                    experiment.setHasServerError(con.getResponseCode() >= 500);

                } catch (IOException ioe) {
                    // write error to
                    logger.trace(ioe);
                    logger.warn("Experiment failed to run properly !");
                    experiment.setHasError(true);
                    experiment.setHasServerError(true);
                    experiment.setResult(ioe.getMessage());
                }

                experiment.setFinished(new Date());

                // finally
                try {
                    Session session = HibernateUtil.getSessionFactory().openSession();
                    Transaction transaction = session.beginTransaction();
                    session.update(experiment);
                    transaction.commit();
                    session.close();
                } catch (DataException e) {
                    throw e;
                }

            }
        }.start();
    }

    @ApiOperation(value = "Send a request to the workflow to run an experiment", response = Experiment.class)
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Success") })
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<String> runExperiment(@RequestBody String incomingQueryString) {
        JsonObject incomingQuery = gson.fromJson(incomingQueryString, JsonObject.class);

        Experiment experiment = new Experiment();
        experiment.setUuid(UUID.randomUUID());
        User user = mipApplication.getUser();

        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction transaction = session.beginTransaction();

        try {

            experiment.setAlgorithms(incomingQuery.get("algorithms").toString());
            experiment.setValidations(incomingQuery.get("validations").toString());
            experiment.setName(incomingQuery.get("name").getAsString());
            experiment.setCreatedBy(user);

            Query hibernateQuery = session.createQuery("from Model as model where model.slug = :slug");
            hibernateQuery.setParameter("slug", incomingQuery.get("model").getAsString());
            experiment.setModel((Model)hibernateQuery.uniqueResult());

            session.save(experiment);
            transaction.commit();

        } catch (Exception e) {
            if(transaction != null)
            {
                transaction.rollback();
            }
            logger.trace(e);
            logger.warn("Cannot create experiment to run ! This is probably caused by a bad request !");
            // 400 here probably
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        try {
            if(isExaremeAlgo(experiment))
            {
                sendExaremePost(experiment);
            }
            else
            {
                sendPost(experiment);
            }
        } catch (MalformedURLException mue) { logger.trace(mue.getMessage()); } // ignore

        return new ResponseEntity<>(gson.toJson(experiment), HttpStatus.OK);
    }

    private void sendExaremePost(Experiment experiment) {

        Model model = experiment.getModel();
        String algoCode = "WP_LINEAR_REGRESSION";

        LinkedList<ExaremeQueryElement> queryElements = new LinkedList<>();
        for (Variable var : model.getQuery().getVariables())
        {
            ExaremeQueryElement el = new ExaremeQueryElement();
            el.setName("variable");
            el.setDesc("");
            el.setValue(var.getCode());
            queryElements.add(el);
        }
        for (Variable var : model.getQuery().getCovariables())
        {
            ExaremeQueryElement el = new ExaremeQueryElement();
            el.setName("covariables");
            el.setDesc("");
            el.setValue(var.getCode());
            queryElements.add(el);
        }
        for (Variable var : model.getQuery().getGrouping())
        {
            ExaremeQueryElement el = new ExaremeQueryElement();
            el.setName("groupings");
            el.setDesc("");
            el.setValue(var.getCode());
            queryElements.add(el);
        }

        ExaremeQueryElement tableEl = new ExaremeQueryElement();
        tableEl.setName("showtable");
        tableEl.setDesc("");
        tableEl.setValue("TotalResults");
        queryElements.add(tableEl);

        ExaremeQueryElement formatEl = new ExaremeQueryElement();
        formatEl.setName("format");
        formatEl.setDesc("");
        formatEl.setValue("True");
        queryElements.add(formatEl);

        String jsonQuery = new Gson().toJson(queryElements);

        new Thread() {
            public void run() {
                try {
                    String url = miningExaremeQueryUrl + "/" + algoCode;
                    StringBuilder results = new StringBuilder();
                    int code = HTTPUtil.sendPost(url, jsonQuery, results);

                    experiment.setResult(results.toString().replace("\0", ""));
                    experiment.setHasError(code >= 400);
                    experiment.setHasServerError(code >= 500);

                    if(!isJSONValid(experiment.getResult()))
                    {
                        experiment.setResult("Unsupported variables !");
                    }
                } catch (Exception e) {
                    logger.trace(e);
                    logger.warn("Failed to run Exareme algorithm !");
                    experiment.setHasError(true);
                    experiment.setHasServerError(true);
                    experiment.setResult(e.getMessage());
                }

                experiment.setFinished(new Date());

                try {
                    Session session = HibernateUtil.getSessionFactory().openSession();
                    Transaction transaction = session.beginTransaction();
                    session.update(experiment);
                    transaction.commit();
                    session.close();
                } catch (DataException e) {
                    throw e;
                }

            }
        }.start();
    }

    public boolean isJSONValid(String test) {
        try {
            new JsonParser().parse(test);
        } catch (JsonParseException jpe)
        {
            logger.trace(jpe); // This is the normal behavior when the input string is not JSON-ified
            return false;
        }
        return true;
    }

    private boolean isExaremeAlgo(Experiment experiment)  {
        JsonArray algorithms = new JsonParser().parse(experiment.getAlgorithms()).getAsJsonArray();
        String algoCode = algorithms.get(0).getAsJsonObject().get("code").getAsString();
        return algoCode.equals("glm_exareme");
    }

    @ApiOperation(value = "get an experiment", response = Experiment.class)
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Success") })
    @RequestMapping(value = "/{uuid}", method = RequestMethod.GET)
    public ResponseEntity<String> getExperiment(@ApiParam(value = "uuid", required = true) @PathVariable("uuid") String uuid) {

        Experiment experiment;
        UUID experimentUuid;
        try {
            experimentUuid = UUID.fromString(uuid);
        } catch (IllegalArgumentException iae) {
            logger.trace(iae);
            logger.warn("An invalid Experiment UUID was received !");
            return ResponseEntity.badRequest().body("Invalid Experiment UUID");
        }

        Session session = HibernateUtil.getSessionFactory().getCurrentSession();

        try {
            session.beginTransaction();

            Query hibernateQuery = session.createQuery("from Experiment as experiment where experiment.uuid = :uuid");
            hibernateQuery.setParameter("uuid", experimentUuid);
            experiment = (Experiment) hibernateQuery.uniqueResult();
            session.getTransaction().commit();
        } catch (Exception e) {
            // 404 here probably
            if(session.getTransaction() != null)
            {
                session.getTransaction().rollback();
            }
            throw e;
        }

        if (experiment == null) {
            return new ResponseEntity<>("Not found", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(gson.toJson(experiment), HttpStatus.OK);
    }

    @ApiOperation(value = "get an experiment", response = Experiment.class)
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Success") })
    @RequestMapping(value = "/{uuid}/markAsViewed", method = RequestMethod.GET)
    public ResponseEntity<String> markExperimentAsViewed(@ApiParam(value = "uuid", required = true) @PathVariable("uuid") String uuid) {

        Experiment experiment;
        UUID experimentUuid;
        User user = mipApplication.getUser();
        try {
            experimentUuid = UUID.fromString(uuid);
        } catch (IllegalArgumentException iae) {
            logger.trace(iae);
            logger.warn("An invalid Experiment UUID was received !");
            return ResponseEntity.badRequest().body("Invalid Experiment UUID");
        }

        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();

            Query hibernateQuery = session.createQuery("from Experiment as experiment where experiment.uuid = :uuid");
            hibernateQuery.setParameter("uuid", experimentUuid);
            experiment = (Experiment) hibernateQuery.uniqueResult();

            if (!experiment.getCreatedBy().getUsername().equals(user.getUsername()))
                return new ResponseEntity<>("You're not the owner of this experiment", HttpStatus.BAD_REQUEST);

            experiment.setResultsViewed(true);
            session.update(experiment);

            transaction.commit();
        } catch (Exception e) {
            // 404 here probably
            if(transaction != null)
            {
                transaction.rollback();
            }
            throw e;
        }

        return new ResponseEntity<>(gson.toJson(experiment), HttpStatus.OK);
    }

    public ResponseEntity<String> doMarkExperimentAsShared(String uuid, boolean shared) {

        Experiment experiment;
        UUID experimentUuid;
        User user = mipApplication.getUser();
        try {
            experimentUuid = UUID.fromString(uuid);
        } catch (IllegalArgumentException iae) {
            logger.trace(iae);
            logger.warn("An invalid Experiment UUID was received !");
            return ResponseEntity.badRequest().body("Invalid Experiment UUID");
        }

        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();

            Query hibernateQuery = session.createQuery("from Experiment as experiment where experiment.uuid = :uuid");
            hibernateQuery.setParameter("uuid", experimentUuid);
            experiment = (Experiment) hibernateQuery.uniqueResult();

            if (!experiment.getCreatedBy().getUsername().equals(user.getUsername()))
                return new ResponseEntity<>("You're not the owner of this experiment", HttpStatus.BAD_REQUEST);

            experiment.setShared(shared);
            session.update(experiment);

            transaction.commit();
        } catch (Exception e) {
            // 404 here probably
            if(transaction != null)
            {
                transaction.rollback();
            }
            throw e;
        }

        return new ResponseEntity<>(gson.toJson(experiment), HttpStatus.OK);
    }


    @ApiOperation(value = "get an experiment", response = Experiment.class)
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Success") })
    @RequestMapping(value = "/{uuid}/markAsShared", method = RequestMethod.GET)
    public ResponseEntity<String> markExperimentAsShared(@ApiParam(value = "uuid", required = true) @PathVariable("uuid") String uuid) {
        return doMarkExperimentAsShared(uuid, true);
    }

    @ApiOperation(value = "get an experiment", response = Experiment.class)
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Success") })
    @RequestMapping(value = "/{uuid}/markAsUnshared", method = RequestMethod.GET)
    public ResponseEntity<String> markExperimentAsUnshared(@ApiParam(value = "uuid", required = true) @PathVariable("uuid") String uuid) {
        return doMarkExperimentAsShared(uuid, false);
    }

    public ResponseEntity<String> doListExperiments(
            boolean mine,
            int maxResultCount,
            String modelSlug
    ) {
        List<Experiment> experiments = new LinkedList<>();
        User user = mipApplication.getUser();

        Session session = HibernateUtil.getSessionFactory().getCurrentSession();

        try {
            session.beginTransaction();

            Query hibernateQuery;
            String baseQuery = "from Experiment as e WHERE ";

            baseQuery += mine ? "e.createdBy = :user" : "(e.createdBy = :user OR e.shared is true)";

            if (modelSlug == null || modelSlug.equals("")) {
                hibernateQuery = session.createQuery(baseQuery);
            } else {
                hibernateQuery = session.createQuery(baseQuery + " AND e.model.slug = :slug");
                hibernateQuery.setParameter("slug", modelSlug);
            }
            hibernateQuery.setParameter("user", user);

            if (maxResultCount > 0)
                hibernateQuery.setMaxResults(maxResultCount);

            for (Object experiment: hibernateQuery.list()) {
                if (experiment instanceof Experiment) { // should definitely be true
                    Experiment experiment1 = (Experiment) experiment;
                    // remove some fields because it is costly and not useful to send them over the network
                    experiment1.setResult(null);
                    experiment1.setAlgorithms(null);
                    experiment1.setValidations(null);
                    experiments.add(experiment1);

                }

            }
        } catch (Exception e) {
            // 404 here probably
            throw e;
        } finally {
            if(session.getTransaction() != null)
            {
                session.getTransaction().rollback();
            }
        }

        return new ResponseEntity<>(gson.toJson(experiments), HttpStatus.OK);
    }

    @ApiOperation(value = "list experiments", response = Experiment.class, responseContainer = "List")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Success") })
    @RequestMapping(value = "/mine", method = RequestMethod.GET, params = {"maxResultCount"})
    public ResponseEntity<String> listExperiments(
        @ApiParam(value = "maxResultCount", required = false) @RequestParam int maxResultCount
    ) {
        return doListExperiments(true, maxResultCount, null);
    }

    @ApiOperation(value = "list experiments", response = Experiment.class, responseContainer = "List")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Success") })
    @RequestMapping(method = RequestMethod.GET, params = {"slug", "maxResultCount"})
    public ResponseEntity<String> listExperiments(
        @ApiParam(value = "slug", required = false) @RequestParam("slug") String modelSlug,
        @ApiParam(value = "maxResultCount", required = false) @RequestParam("maxResultCount") int maxResultCount
    ) {

        if (maxResultCount <= 0 && (modelSlug == null || modelSlug.equals(""))) {
            return new ResponseEntity<>("You must provide at least a slug or a limit of result", HttpStatus.BAD_REQUEST);
        }

        return doListExperiments(false, maxResultCount, modelSlug);
    }

    @ApiOperation(value = "List available methods and validations", response = String.class)
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Success") })
    @RequestMapping(path = "/methods", method = RequestMethod.GET)
    public ResponseEntity<String> listAvailableMethodsAndValidations() throws Exception {

        StringBuilder response = new StringBuilder();

        int code = HTTPUtil.sendGet(listMethodsUrl, response);
        if (code < 200 || code > 299) {
            return new ResponseEntity<>(response.toString(), HttpStatus.valueOf(code));
        }

        JsonObject catalog = new JsonParser().parse(response.toString()).getAsJsonObject();

        InputStream is = ExperimentApi.class.getClassLoader().getResourceAsStream(EXAREME_ALGO_JSON_FILE);
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(isr);
        JsonObject exaremeAlgo = new JsonParser().parse(br).getAsJsonObject();

        catalog.get("algorithms").getAsJsonArray().add(exaremeAlgo);

        return new ResponseEntity<>(new Gson().toJson(catalog), HttpStatus.valueOf(code));
    }
}
