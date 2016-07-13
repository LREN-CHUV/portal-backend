package org.hbp.mip.controllers;

import com.google.common.collect.Iterables;
import com.google.gson.*;
import io.swagger.annotations.*;
import org.apache.log4j.Logger;
import org.hbp.mip.MIPApplication;
import org.hbp.mip.model.Experiment;
import org.hbp.mip.model.User;
import org.hbp.mip.repositories.ExperimentRepository;
import org.hbp.mip.repositories.ModelRepository;
import org.hbp.mip.utils.HTTPUtil;
import org.hbp.mip.utils.JSONUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.util.Iterator;
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

    private static final Logger LOGGER = Logger.getLogger(ExperimentApi.class);

    private static final String EXAREME_ALGO_JSON_FILE="data/exareme_algorithms.json";

    private static final Gson gson = new GsonBuilder()
            .serializeNulls()
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
            .excludeFieldsWithoutExposeAnnotation()
            .create();

    private static final String EXAREME_LR_ALGO = "WP_LINEAR_REGRESSION";

    @Value("#{'${workflow.experimentUrl:http://dockerhost:8087/experiment}'}")
    private String experimentUrl;

    @Value("#{'${workflow.listMethodsUrl:http://dockerhost:8087/list-methods}'}")
    private String listMethodsUrl;

    @Value("#{'${workflow.miningExaremeUrl:http://hbps2.chuv.ch:9090/mining/query}'}")
    private String miningExaremeQueryUrl;

    @Autowired
    MIPApplication mipApplication;

    @Autowired
    ModelRepository modelRepository;

    @Autowired
    ExperimentRepository experimentRepository;


    @ApiOperation(value = "Send a request to the workflow to run an experiment", response = Experiment.class)
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Success") })
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<String> runExperiment(@RequestBody String incomingQueryString) {
        JsonObject incomingQuery = gson.fromJson(incomingQueryString, JsonObject.class);

        Experiment experiment = new Experiment();
        experiment.setUuid(UUID.randomUUID());
        User user = mipApplication.getUser();

        experiment.setAlgorithms(incomingQuery.get("algorithms").toString());
        experiment.setValidations(incomingQuery.get("validations").toString());
        experiment.setName(incomingQuery.get("name").getAsString());
        experiment.setCreatedBy(user);
        experiment.setModel(modelRepository.findOne(incomingQuery.get("model").getAsString()));
        experimentRepository.save(experiment);

        try {
            if(isExaremeAlgo(experiment))
            {
                sendExaremeExperiment(experiment);
            }
            else
            {
                sendExperiment(experiment);
            }
        } catch (MalformedURLException mue) { LOGGER.trace(mue.getMessage()); } // ignore

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
            LOGGER.trace(iae);
            LOGGER.warn("An invalid Experiment UUID was received !");
            return ResponseEntity.badRequest().body("Invalid Experiment UUID");
        }

        experiment = experimentRepository.findOne(experimentUuid);

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
            LOGGER.trace(iae);
            LOGGER.warn("An invalid Experiment UUID was received !");
            return ResponseEntity.badRequest().body("Invalid Experiment UUID");
        }

        experiment = experimentRepository.findOne(experimentUuid);;
        if (!experiment.getCreatedBy().getUsername().equals(user.getUsername()))
            return new ResponseEntity<>("You're not the owner of this experiment", HttpStatus.BAD_REQUEST);
        experiment.setResultsViewed(true);
        experimentRepository.save(experiment);
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

        if (maxResultCount <= 0 && (modelSlug == null || "".equals(modelSlug))) {
            return new ResponseEntity<>("You must provide at least a slug or a limit of result", HttpStatus.BAD_REQUEST);
        }

        return doListExperiments(false, maxResultCount, modelSlug);
    }

    @ApiOperation(value = "List available methods and validations", response = String.class)
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Success") })
    @RequestMapping(path = "/methods", method = RequestMethod.GET)
    public ResponseEntity<String> listAvailableMethodsAndValidations() throws IOException {

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

    private ResponseEntity<String> doListExperiments(
            boolean mine,
            int maxResultCount,
            String modelSlug
    ) {
        User user = mipApplication.getUser();
        Iterable<Experiment> experiments = null;

        Iterable<Experiment> myExperiments = experimentRepository.findByUser(user);
        if(!mine)
        {
            Iterable<Experiment> sharedExperiments = experimentRepository.findShared(true);
            experiments = Iterables.concat(myExperiments, sharedExperiments);
        }

        if (modelSlug != null && !"".equals(modelSlug)) {
            for(Iterator<Experiment> i = myExperiments.iterator(); i.hasNext(); )
            {
                Experiment e = i.next();
                e.setResult(null);
                e.setAlgorithms(null);
                e.setValidations(null);
                if(!e.getModel().getSlug().equals(modelSlug))
                {
                    i.remove();
                }
            }
        }

        return new ResponseEntity<>(gson.toJson(experiments), HttpStatus.OK);

    }

    private ResponseEntity<String> doMarkExperimentAsShared(String uuid, boolean shared) {
        Experiment experiment;
        UUID experimentUuid;
        User user = mipApplication.getUser();
        try {
            experimentUuid = UUID.fromString(uuid);
        } catch (IllegalArgumentException iae) {
            LOGGER.trace(iae);
            LOGGER.warn("An invalid Experiment UUID was received !");
            return ResponseEntity.badRequest().body("Invalid Experiment UUID");
        }

        experiment = experimentRepository.findOne(experimentUuid);

        if (!experiment.getCreatedBy().getUsername().equals(user.getUsername()))
            return new ResponseEntity<>("You're not the owner of this experiment", HttpStatus.BAD_REQUEST);

        experiment.setShared(shared);
        experimentRepository.save(experiment);

        return new ResponseEntity<>(gson.toJson(experiment), HttpStatus.OK);
    }

    private void sendExperiment(Experiment experiment) throws MalformedURLException {
        // this runs in the background. For future optimization: use a thread pool
        new Thread() {
            @Override
            public void run() {
                    String url = experimentUrl;
                    String query = experiment.computeQuery();

                    // Results are stored in the experiment object
                try {
                    executeExperiment(url, query, experiment);
                } catch (IOException e) {
                    LOGGER.trace(e);
                    LOGGER.warn("Experiment failed to run properly !");
                    setExperimentError(e, experiment);
                }

                experiment.finish();
            }
        }.start();
    }

    private void sendExaremeExperiment(Experiment experiment) {
        // this runs in the background. For future optimization: use a thread pool
        new Thread() {
            @Override
            public void run() {
                String query = experiment.computeExaremeQuery();
                String url = miningExaremeQueryUrl + "/" + EXAREME_LR_ALGO;

                // Results are stored in the experiment object
                try {
                    executeExperiment(url, query, experiment);
                } catch (IOException e) {
                    LOGGER.trace(e);
                    LOGGER.warn("Exareme experiment failed to run properly !");
                    setExperimentError(e, experiment);
                }

                if(!JSONUtil.isJSONValid(experiment.getResult()))
                    {
                        experiment.setResult("Unsupported variables !");
                    }

                experiment.finish();
            }
        }.start();
    }

    private static void executeExperiment(String url, String query, Experiment experiment) throws IOException {
        StringBuilder results = new StringBuilder();
        int code = HTTPUtil.sendPost(url, query, results);
        experiment.setResult(results.toString().replace("\0", ""));
        experiment.setHasError(code >= 400);
        experiment.setHasServerError(code >= 500);
    }

    private static void setExperimentError(IOException e, Experiment experiment) {
        experiment.setHasError(true);
        experiment.setHasServerError(true);
        experiment.setResult(e.getMessage());
    }

    private static boolean isExaremeAlgo(Experiment experiment)  {
        JsonArray algorithms = new JsonParser().parse(experiment.getAlgorithms()).getAsJsonArray();
        String algoCode = algorithms.get(0).getAsJsonObject().get("code").getAsString();
        return "glm_exareme".equals(algoCode);
    }

}
