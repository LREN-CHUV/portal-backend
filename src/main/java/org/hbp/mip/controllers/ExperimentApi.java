package org.hbp.mip.controllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import io.swagger.annotations.*;
import org.hbp.mip.MIPApplication;
import org.hbp.mip.model.Experiment;
import org.hbp.mip.model.Model;
import org.hbp.mip.model.User;
import org.hbp.mip.model.algorithm.Algorithm;
import org.hbp.mip.model.algorithm.Catalog;
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
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
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
                    } catch (ProtocolException pe) {} // ignore; won't happen
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
            transaction.rollback();
            e.printStackTrace();
            // 400 here probably
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        try {
            sendPost(experiment);
        } catch (MalformedURLException mue) {} // ignore

        return new ResponseEntity<>(gson.toJson(experiment), HttpStatus.OK);
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
            session.getTransaction().rollback();
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
            transaction.rollback();
            throw e;
        }

        if (experiment == null) {
            return new ResponseEntity<>("Not found", HttpStatus.NOT_FOUND);
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
            transaction.rollback();
            throw e;
        }

        if (experiment == null) {
            return new ResponseEntity<>("Not found", HttpStatus.NOT_FOUND);
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
            session.getTransaction().rollback();
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

        Catalog catalog = new Gson().fromJson(response.toString(), Catalog.class);
        for (Algorithm algo: catalog.getAlgorithms()) {
            algo.setSource("ML");
        }

        InputStream is = ExperimentApi.class.getClassLoader().getResourceAsStream(EXAREME_ALGO_JSON_FILE);
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(isr);
        Algorithm exaremeGLR = new Gson().fromJson(br, Algorithm.class);
        exaremeGLR.setSource("exareme");
        catalog.getAlgorithms().add(exaremeGLR);

        return new ResponseEntity<>(new Gson().toJson(catalog), HttpStatus.valueOf(code));
    }
}
