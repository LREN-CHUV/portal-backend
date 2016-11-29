package eu.hbp.mip.controllers;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import eu.hbp.mip.configuration.SecurityConfiguration;
import eu.hbp.mip.model.Experiment;
import eu.hbp.mip.model.ExperimentQuery;
import eu.hbp.mip.model.User;
import eu.hbp.mip.repositories.ExperimentRepository;
import eu.hbp.mip.repositories.ModelRepository;
import eu.hbp.mip.utils.HTTPUtil;
import eu.hbp.mip.utils.JSONUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * Created by habfast on 21/04/16.
 */
@RestController
@RequestMapping(value = "/experiments", produces = {APPLICATION_JSON_VALUE})
@Api(value = "/experiments", description = "the experiments API")
public class ExperimentApi {

    private static final Logger LOGGER = Logger.getLogger(ExperimentApi.class);

    private static final String EXAREME_ALGO_JSON_FILE="data/exareme_algorithms.json";

    private static final Gson gson = new Gson();

    private static final Gson gsonOnlyExposed = new GsonBuilder()
            .serializeNulls()
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
            .excludeFieldsWithoutExposeAnnotation()
            .create();

    private static final String EXAREME_LR_ALGO = "WP_LINEAR_REGRESSION";

    @Value("#{'${services.woken.experimentUrl:http://dockerhost:8087/experiment}'}")
    private String experimentUrl;

    @Value("#{'${services.woken.listMethodsUrl:http://dockerhost:8087/list-methods}'}")
    private String listMethodsUrl;

    @Value("#{'${services.exareme.miningExaremeUrl:http://hbps2.chuv.ch:9090/mining/query}'}")
    private String miningExaremeQueryUrl;

    @Autowired
    private SecurityConfiguration securityConfiguration;

    @Autowired
    private ModelRepository modelRepository;

    @Autowired
    private ExperimentRepository experimentRepository;

    @Autowired
    private ActorSystem actorSystem;


    @ApiOperation(value = "Send a request to the workflow to run an experiment", response = Experiment.class)
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<String> runExperiment(@RequestBody ExperimentQuery expQuery) {
        LOGGER.info("Run an experiment");

        Experiment experiment = new Experiment();
        experiment.setUuid(UUID.randomUUID());
        User user = securityConfiguration.getUser();

        experiment.setAlgorithms(gson.toJson(expQuery.getAlgorithms()));
        experiment.setValidations(gson.toJson(expQuery.getValidations()));
        experiment.setName(expQuery.getName());
        experiment.setCreatedBy(user);
        experiment.setModel(modelRepository.findOne(expQuery.getModel()));
        experimentRepository.save(experiment);

        LOGGER.info("Experiment saved");

        try {
            if(isExaremeAlgo(expQuery))
            {
                sendExaremeExperiment(experiment);
            }
            else
            {
                sendExperiment(experiment);
            }
        } catch (MalformedURLException mue) { LOGGER.trace(mue.getMessage()); } // ignore

        return new ResponseEntity<>(gsonOnlyExposed.toJson(experiment.jsonify()), HttpStatus.OK);
    }

    @ApiOperation(value = "get an experiment", response = Experiment.class)
    @RequestMapping(value = "/{uuid}", method = RequestMethod.GET)
    public ResponseEntity<String> getExperiment(@ApiParam(value = "uuid", required = true) @PathVariable("uuid") String uuid) {
        LOGGER.info("Get an experiment");

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

        return new ResponseEntity<>(gsonOnlyExposed.toJson(experiment.jsonify()), HttpStatus.OK);
    }

    @ApiOperation(value = "Mark an experiment as viewed", response = Experiment.class)
    @RequestMapping(value = "/{uuid}/markAsViewed", method = RequestMethod.GET)
    public ResponseEntity<String> markExperimentAsViewed(@ApiParam(value = "uuid", required = true) @PathVariable("uuid") String uuid) {
        LOGGER.info("Mark an experiment as viewed");

        Experiment experiment;
        UUID experimentUuid;
        User user = securityConfiguration.getUser();
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
        experiment.setResultsViewed(true);
        experimentRepository.save(experiment);

        LOGGER.info("Experiment updated (marked as viewed)");

        return new ResponseEntity<>(gsonOnlyExposed.toJson(experiment.jsonify()), HttpStatus.OK);
    }

    @ApiOperation(value = "Mark an experiment as shared", response = Experiment.class)
    @RequestMapping(value = "/{uuid}/markAsShared", method = RequestMethod.GET)
    public ResponseEntity<String> markExperimentAsShared(@ApiParam(value = "uuid", required = true) @PathVariable("uuid") String uuid) {
        LOGGER.info("Mark an experiment as shared");

        return doMarkExperimentAsShared(uuid, true);
    }

    @ApiOperation(value = "Mark an experiment as unshared", response = Experiment.class)
    @RequestMapping(value = "/{uuid}/markAsUnshared", method = RequestMethod.GET)
    public ResponseEntity<String> markExperimentAsUnshared(@ApiParam(value = "uuid", required = true) @PathVariable("uuid") String uuid) {
        LOGGER.info("Mark an experiment as unshared");

        return doMarkExperimentAsShared(uuid, false);
    }

    @ApiOperation(value = "list experiments", response = Experiment.class, responseContainer = "List")
    @RequestMapping(value = "/mine", method = RequestMethod.GET, params = {"maxResultCount"})
    public ResponseEntity<String> listExperiments(
            @ApiParam(value = "maxResultCount") @RequestParam int maxResultCount
    ) {
        LOGGER.info("List experiments");

        return doListExperiments(true, null);
    }

    @ApiOperation(value = "list experiments", response = Experiment.class, responseContainer = "List")
    @RequestMapping(method = RequestMethod.GET, params = {"slug", "maxResultCount"})
    public ResponseEntity<String> listExperiments(
            @ApiParam(value = "slug") @RequestParam("slug") String modelSlug,
            @ApiParam(value = "maxResultCount") @RequestParam("maxResultCount") int maxResultCount
    ) {
        LOGGER.info("List experiments");

        if (maxResultCount <= 0 && (modelSlug == null || "".equals(modelSlug))) {
            return new ResponseEntity<>("You must provide at least a slug or a limit of result", HttpStatus.BAD_REQUEST);
        }

        return doListExperiments(false, modelSlug);
    }

    @ApiOperation(value = "List available methods and validations", response = String.class)
    @Cacheable("methods")
    @RequestMapping(path = "/methods", method = RequestMethod.GET)
    public ResponseEntity<String> listAvailableMethodsAndValidations() throws IOException {
        LOGGER.info("List available methods and validations");

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

        return new ResponseEntity<>(gson.toJson(catalog), HttpStatus.valueOf(code));
    }

    private ResponseEntity<String> doListExperiments(
            boolean mine,
            String modelSlug
    ) {
        User user = securityConfiguration.getUser();

        Iterable<Experiment> myExperiments = experimentRepository.findByCreatedBy(user);
        List<Experiment> expList = Lists.newLinkedList(myExperiments);
        if(!mine)
        {
            Iterable<Experiment> sharedExperiments = experimentRepository.findByShared(true);
            List<Experiment> sharedExpList = Lists.newLinkedList(sharedExperiments);
            expList.addAll(sharedExpList);
        }

        if (modelSlug != null && !"".equals(modelSlug)) {
            for(Iterator<Experiment> it = expList.iterator(); it.hasNext();)
            {
                Experiment e = it.next();
                e.setResult(null);
                e.setAlgorithms(null);
                e.setValidations(null);
                if(!e.getModel().getSlug().equals(modelSlug))
                {
                    it.remove();
                }
            }
        }

        return new ResponseEntity<>(gsonOnlyExposed.toJson(expList), HttpStatus.OK);
    }

    private ResponseEntity<String> doMarkExperimentAsShared(String uuid, boolean shared) {
        Experiment experiment;
        UUID experimentUuid;
        User user = securityConfiguration.getUser();
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

        LOGGER.info("Experiment updated (marked as shared)");

        return new ResponseEntity<>(gsonOnlyExposed.toJson(experiment.jsonify()), HttpStatus.OK);
    }

    private void sendExperiment(Experiment experiment) throws MalformedURLException {
        // this runs in the background. For future optimization: use a thread pool
        final String url = experimentUrl;
        final String query = experiment.computeQuery();

        ActorRef wokenActor = actorSystem.actorFor("woken");

        // Should maybe use this instead ???
        // ActorRef wokenActor = actorSystem.actorOf(
        //        SpringExtension.SpringExtProvider.get(actorSystem).props("Woken"), "woken");

        wokenActor.tell(query, null);


        new Thread() {
            @Override
            public void run() {
                // Results are stored in the experiment object
                try {
                    executeExperiment(url, query, experiment);
                } catch (IOException e) {
                    LOGGER.trace(e);
                    LOGGER.warn("Experiment failed to run properly !");
                    setExperimentError(e, experiment);
                }
                finishExpermient(experiment);
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
                finishExpermient(experiment);
            }
        }.start();
    }

    private void finishExpermient(Experiment experiment)
    {
        experiment.setFinished(new Date());
        experimentRepository.save(experiment);

        LOGGER.info("Experiment updated (finished)");
    }

    private static void executeExperiment(String url, String query, Experiment experiment) throws IOException {
        StringBuilder results = new StringBuilder();
        int code = HTTPUtil.sendPost(url, query, results);
        experiment.setResult(results.toString());
        experiment.setHasError(code >= 400);
        experiment.setHasServerError(code >= 500);
    }

    private static void setExperimentError(IOException e, Experiment experiment) {
        experiment.setHasError(true);
        experiment.setHasServerError(true);
        experiment.setResult(e.getMessage());
    }

    private static boolean isExaremeAlgo(ExperimentQuery expQuery) {
        return expQuery.getAlgorithms().size() >= 1 && "glm_exareme".equals(expQuery.getAlgorithms().get(0).getCode());
    }

}
