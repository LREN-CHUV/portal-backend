/**
 * Created by mirco on 18.01.16.
 */

package eu.hbp.mip.controllers;

import eu.hbp.mip.model.Dataset;
import eu.hbp.mip.model.Query;
import io.swagger.annotations.*;
import org.apache.log4j.Logger;
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

    private static final Logger LOGGER = Logger.getLogger(RequestsApi.class);

    @ApiOperation(value = "Post a request", response = Dataset.class)
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Success") })
    @RequestMapping(method = RequestMethod.POST)
    @Deprecated
    public ResponseEntity<Dataset> postRequests(
            @RequestBody @ApiParam(value = "Query to process", required = true) Query query
    )  {
        LOGGER.info("Post a request");

        return ResponseEntity.ok(null);  // TODO: Get data from second datasource
    }

}
