/*
 * Created by mirco on 04.12.15.
 */

package eu.hbp.mip.controllers;


import ch.chuv.lren.woken.messages.datasets.DatasetsQuery;
import ch.chuv.lren.woken.messages.datasets.DatasetsResponse;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import eu.hbp.mip.akka.WokenClientController;
import eu.hbp.mip.model.Dataset;
import eu.hbp.mip.model.Variable;
import eu.hbp.mip.repositories.VariableRepository;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import scala.Option;

import javax.annotation.PostConstruct;

import java.util.Set;
import java.util.stream.Stream;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;


@RestController
@RequestMapping(value = "/datasets", produces = {APPLICATION_JSON_VALUE})
@Api(value = "/datasets", description = "the datasets API")
public class DatasetsApi extends WokenClientController {

    private static final Logger LOGGER = LoggerFactory.getLogger(DatasetsApi.class);
    private static final Gson gson = new Gson();

    @Autowired
    private VariableRepository variableRepository;

    @PostConstruct
    public void init() throws Exception {
        // Pre-fill the local variable repository with the list of datasets, interpreted here as variables
        // (a bit like a categorical variable can be split into a set of variables (a.k.a one hot encoding in Data science) )
        for (ch.chuv.lren.woken.messages.datasets.Dataset dataset: fetchDatasets()) {
            final String code = dataset.dataset().code();
            Variable v = variableRepository.findOne(code);
            if (v == null) {
                v = new Variable(code);
                variableRepository.save(v);
            }
        }
    }

    @ApiOperation(value = "Get dataset list", response = Dataset.class, responseContainer = "List")
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity getDatasets(
    )  {
        LOGGER.info("Get dataset list");

        try {
            JsonArray datasets = new JsonArray();
            Stream<JsonObject> values = fetchDatasets().stream().map(d -> {
                JsonObject jsObject = new JsonObject();
                jsObject.addProperty("code", d.dataset().code());
                jsObject.addProperty("label", d.label());
                datasets.add(jsObject);
                return jsObject;
            });

            return ResponseEntity.ok(datasets);
        } catch (Exception e) {
            final String msg = "Cannot receive datasets from woken: " + e.getMessage();
            LOGGER.error(msg, e);
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(msg);
        }

    }

    private Set<ch.chuv.lren.woken.messages.datasets.Dataset> fetchDatasets() throws Exception {
        DatasetsResponse result = askWoken(new DatasetsQuery(Option.empty()), 30);
        return scala.collection.JavaConversions.setAsJavaSet(result.datasets());
    }
}
