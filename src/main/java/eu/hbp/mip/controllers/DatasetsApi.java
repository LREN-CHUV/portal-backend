/*
 * Created by mirco on 04.12.15.
 */

package eu.hbp.mip.controllers;


import ch.chuv.lren.woken.messages.datasets.Dataset;
import ch.chuv.lren.woken.messages.datasets.DatasetsQuery;
import ch.chuv.lren.woken.messages.datasets.DatasetsResponse;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import eu.hbp.mip.akka.WokenClientController;
import eu.hbp.mip.model.DatasetDescription;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import scala.Option;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;


@RestController
@RequestMapping(value = "/datasets", produces = {APPLICATION_JSON_VALUE})
@Api(value = "/datasets", description = "the datasets API")
public class DatasetsApi extends WokenClientController {

    private static final Logger LOGGER = LoggerFactory.getLogger(DatasetsApi.class);

    @ApiOperation(value = "Get dataset list", response = Dataset.class, responseContainer = "List")
    @RequestMapping(method = RequestMethod.GET)
    @Cacheable(value = "datasets")
    public ResponseEntity getDatasets(
    )  {
        LOGGER.info("Get list of datasets");

        try {
            List<DatasetDescription> datasets = new ArrayList<>();
            for (Dataset d: fetchDatasets()) {
                DatasetDescription dataset = new DatasetDescription();
                LOGGER.info("Dataset {}", d);
                dataset.setCode(d.dataset().code());
                dataset.setLabel(d.label());
                dataset.setDescription(d.description());
                dataset.setAnonymisationLevel(d.anonymisationLevel().toString());
                datasets.add(dataset);
            }

            return ResponseEntity.ok(datasets);
        } catch (Exception e) {
            final String msg = "Cannot receive datasets from woken: " + e.getMessage();
            LOGGER.error(msg, e);
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(msg);
        }

    }

    public List<ch.chuv.lren.woken.messages.datasets.Dataset> fetchDatasets() throws Exception {
        DatasetsResponse result = askWoken(new DatasetsQuery(Option.empty()), 30);
        return new ArrayList<>(scala.collection.JavaConversions.asJavaCollection(result.datasets()));
    }
}
