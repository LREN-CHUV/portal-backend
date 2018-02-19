package eu.hbp.mip.controllers;

import akka.dispatch.OnSuccess;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import eu.hbp.mip.akka.WokenClientController;
import eu.hbp.mip.configuration.SecurityConfiguration;
import eu.hbp.mip.model.Experiment;
import eu.hbp.mip.model.ExperimentQuery;
import eu.hbp.mip.model.User;
import eu.hbp.mip.repositories.ExperimentRepository;
import eu.hbp.mip.repositories.ModelRepository;
import eu.hbp.mip.utils.HTTPUtil;
import eu.hbp.mip.utils.JSONUtil;
import ch.chuv.lren.woken.messages.query.QueryResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import scala.concurrent.ExecutionContext;
import scala.concurrent.Future;

import java.io.IOException;
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
public class ExperimentApi extends WokenClientController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExperimentApi.class);

    private static final Gson gson = new Gson();

    private static final Gson gsonOnlyExposed = new GsonBuilder()
            .serializeNulls()
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
            .excludeFieldsWithoutExposeAnnotation()
            .create();

    @Value("#{'${services.exareme.miningExaremeUrl:http://hbps2.chuv.ch:9090/mining/query}'}")
    private String miningExaremeQueryUrl;

    @Autowired
    private SecurityConfiguration securityConfiguration;

    @Autowired
    private ModelRepository modelRepository;

    @Autowired
    private ExperimentRepository experimentRepository;

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

        if (isExaremeAlgo(expQuery)) {
            String algoCode = expQuery.getAlgorithms().get(0).getCode();
            sendExaremeExperiment(experiment, algoCode);
        }
        else {
            sendExperiment(experiment);
        }

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
            LOGGER.trace("Invalid UUID", iae);
            LOGGER.warn("An invalid Experiment UUID was received ! " + uuid);
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
            LOGGER.trace("Invalid UUID", iae);
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
    @RequestMapping(method = RequestMethod.GET, params = {"maxResultCount"})
    public ResponseEntity<String> listExperiments(
            @ApiParam(value = "maxResultCount") @RequestParam int maxResultCount
    ) {
        LOGGER.info("List experiments");

        return doListExperiments(false, null);
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

    @ApiOperation(value = "list my experiments", response = Experiment.class, responseContainer = "List")
    @RequestMapping(method = RequestMethod.GET, params = {"mine"})
    public ResponseEntity<String> listMyExperiments(
            @ApiParam(value = "mine") @RequestParam("mine") boolean mine
    ) {
        LOGGER.info("List my experiments");

        return doListExperiments(true, null);
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
            LOGGER.trace("Invalid UUID", iae);
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

    private void sendExperiment(final Experiment experiment) {
        User user = securityConfiguration.getUser();

        // this runs in the background. For future optimization: use a thread pool
        final ch.chuv.lren.woken.messages.query.ExperimentQuery experimentQuery = experiment.prepareQuery(user.getUsername());
        final ExecutionContext ec = getExecutor();

        Future<Object> response = sendWokenQuery(experimentQuery, 24*3600);
        response.onSuccess(new OnSuccess<Object>() {
            public void onSuccess(Object result) {
                QueryResult queryResult = (QueryResult) result;
                UUID uuid = experiment.getUuid();
                LOGGER.info("\n\nExperimentActor received response from woken for UUID: \n" + uuid.toString());
                Experiment experiment = experimentRepository.findOne(uuid);
                if (experiment == null) {
                    LOGGER.error("Experiment with UUID=" + uuid + " not found in DB");
                } else {
                    if (queryResult.error().nonEmpty()) {
                        experiment.setHasServerError(true);
                        experiment.setResult(queryResult.error().get());
                        LOGGER.error("Experiment failed with message: " + queryResult.error().get());
                    } else {
                        experiment.setResult(queryResult.data().get().compactPrint());
                    }
                    experiment.setFinished(Date.from(queryResult.timestamp().toInstant()));
                    experimentRepository.save(experiment);
                    LOGGER.info("Experiment " + uuid + " updated (finished)");
                }
            }
        }, ec);
    }

    private void sendExaremeExperiment(Experiment experiment, String algoCode) {
        // >> Temporary: we should integrate exareme in a proper way in the future
        // this runs in the background. For future optimization: use a thread pool
        new Thread(() -> {
            String query = experiment.computeExaremeQuery();
            String url = miningExaremeQueryUrl + "/" + algoCode;
            // Results are stored in the experiment object
            try {
                StringBuilder results = new StringBuilder();
                int code = HTTPUtil.sendPost(url, query, results);
                experiment.setResult("[" + results.toString() + "]");
                experiment.setHasError(code >= 400);
                experiment.setHasServerError(code >= 500);
            } catch (IOException e) {
                LOGGER.trace("Invalid UUID", e);
                LOGGER.warn("Exareme experiment failed to run properly !");
                experiment.setHasError(true);
                experiment.setHasServerError(true);
                experiment.setResult(e.getMessage());
            }
            if(!JSONUtil.isJSONValid(experiment.getResult()))
            {
                experiment.setResult("Unsupported variables !");
            }
            finishExperiment(experiment);
        }).start();
        // << Temporary
    }

    private void finishExperiment(Experiment experiment)
    {
        experiment.setFinished(new Date());
        experimentRepository.save(experiment);

        LOGGER.info("Experiment updated (finished)");
    }

    private static boolean isExaremeAlgo(ExperimentQuery expQuery) {
        return expQuery.getAlgorithms().size() > 0 && "WP_".equals(
                expQuery.getAlgorithms().get(0).getCode().substring(0, 3));
    }

}
