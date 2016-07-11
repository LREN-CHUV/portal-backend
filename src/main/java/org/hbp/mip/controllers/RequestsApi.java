/**
 * Created by mirco on 18.01.16.
 */

package org.hbp.mip.controllers;

import io.swagger.annotations.*;
import org.hbp.mip.model.Dataset;
import org.hbp.mip.model.Query;
import org.hbp.mip.utils.CSVUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/queries/requests", produces = {APPLICATION_JSON_VALUE})
@Api(value = "/queries/requests", description = "the requests API")
public class RequestsApi {

    private static final String DATA_FILE = "data/values.csv";

    @ApiOperation(value = "Send a request", response = Dataset.class)
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Success") })
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Dataset> postRequests(
            @RequestBody @ApiParam(value = "Query to process", required = true) Query query
    )  {
        return ResponseEntity.ok(new CSVUtil().parseValues(DATA_FILE, query));
    }

}
