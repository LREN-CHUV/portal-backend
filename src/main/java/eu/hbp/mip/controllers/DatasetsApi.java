/*
 * Created by mirco on 04.12.15.
 */

package eu.hbp.mip.controllers;


import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import eu.hbp.mip.model.Dataset;
import io.swagger.annotations.*;
import eu.hbp.mip.repositories.DatasetRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;


@RestController
@RequestMapping(value = "/datasets", produces = {APPLICATION_JSON_VALUE})
@Api(value = "/datasets", description = "the datasets API")
public class DatasetsApi {

    private static final Logger LOGGER = LoggerFactory.getLogger(DatasetsApi.class);
    private static final Gson gson = new Gson();

    @Autowired
    private DatasetRepository datasetRepository;

    @ApiOperation(value = "Get dataset list", response = Dataset.class, responseContainer = "List")
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity getDatasets(
    )  {
        LOGGER.info("Get dataset list");

        JsonArray datasets = new JsonArray();

        JsonObject o = new JsonObject();
        o.addProperty("code",  "chuv");
        o.addProperty("label",  "CHUV");
        datasets.add(o);

        JsonObject p = new JsonObject();
        p.addProperty("code",  "brescia");
        p.addProperty("label",  "Brescia");
        datasets.add(p);

        return ResponseEntity.ok(gson.toJson(datasets));
    }

}
