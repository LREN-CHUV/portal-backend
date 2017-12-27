/*
 * Created by mirco on 04.12.15.
 */

package eu.hbp.mip.controllers;


import io.swagger.annotations.*;
import eu.hbp.mip.model.Dataset;
import eu.hbp.mip.repositories.DatasetRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/datasets", produces = {APPLICATION_JSON_VALUE})
@Api(value = "/datasets", description = "the datasets API")
public class DatasetsApi {

    private static final Logger LOGGER = Logger.getLogger(DatasetsApi.class);

    @Autowired
    private DatasetRepository datasetRepository;

    @ApiOperation(value = "Get a dataset", response = Dataset.class)
    @RequestMapping(value = "/{code}", method = RequestMethod.GET)
    public ResponseEntity<Dataset> getADataset(
            @ApiParam(value = "code", required = true) @PathVariable("code") String code
    )  {
        LOGGER.info("Get a dataset");

        return ResponseEntity.ok(datasetRepository.findOne(code));
    }

}
