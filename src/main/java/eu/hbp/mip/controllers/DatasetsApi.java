/*
 * Created by mirco on 04.12.15.
 */

package eu.hbp.mip.controllers;


import com.google.gson.Gson;
import eu.hbp.mip.model.Dataset;
import eu.hbp.mip.model.Variable;
import eu.hbp.mip.repositories.VariableRepository;
import io.swagger.annotations.*;
import eu.hbp.mip.repositories.DatasetRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;


@RestController
@RequestMapping(value = "/datasets", produces = {APPLICATION_JSON_VALUE})
@Api(value = "/datasets", description = "the datasets API")
public class DatasetsApi {

    private static final Logger LOGGER = LoggerFactory.getLogger(DatasetsApi.class);
    private static final Gson gson = new Gson();

    @Autowired
    private DatasetRepository datasetRepository;

    @Autowired
    private VariableRepository variableRepository;

    @Value("#{'${spring.featuresDatasource.datasets:adni,ppmi,edsd}'}")
    private String datasets;

    @PostConstruct
    public void init() {
        for (String dataset: datasets.split(",")) {
            Variable v = variableRepository.findOne(dataset);
            if (v == null) {
                v = new Variable(dataset);
                variableRepository.save(v);
            }
        }
    }

    @ApiOperation(value = "Get dataset list", response = Dataset.class, responseContainer = "List")
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity getDatasets(
    )  {
        LOGGER.info("Get dataset list");

        return ResponseEntity.ok(datasets.split(","));
    }

}
