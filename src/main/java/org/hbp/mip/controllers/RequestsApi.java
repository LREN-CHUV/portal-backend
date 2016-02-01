package org.hbp.mip.controllers;

import io.swagger.annotations.*;
import org.hbp.mip.model.Dataset;
import org.hbp.mip.model.Query;
import org.hbp.mip.utils.CSVUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * Created by mirco on 18.01.16.
 */
@Controller
@RequestMapping(value = "/queries/requests", produces = {APPLICATION_JSON_VALUE})
@Api(value = "/queries/requests", description = "the requests API")
public class RequestsApi {

    @ApiOperation(value = "Send a request", response = Dataset.class)
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Success") })
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Dataset> postRequests(
            @RequestBody @ApiParam(value = "Query to process", required = true) Query query
    ) throws NotFoundException {

        Dataset dataset = CSVUtil.parseValues("data/values.csv", query);

        return new ResponseEntity<Dataset>(HttpStatus.OK).ok(dataset);
    }

}
