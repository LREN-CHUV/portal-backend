package org.hbp.mip.controllers;

import io.swagger.annotations.*;
import org.hbp.mip.model.Statistics;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Controller
@RequestMapping(value = "/stats", produces = {APPLICATION_JSON_VALUE})
@Api(value = "/stats", description = "the stats API")
@javax.annotation.Generated(value = "class io.swagger.codegen.languages.SpringMVCServerCodegen", date = "2016-01-20T14:47:53.152Z")
public class StatsApi {
    @ApiOperation(value = "Get the statistics for a group or a variable", notes = "", response = Statistics.class, responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Found"),
            @ApiResponse(code = 404, message = "Not found") })
    @RequestMapping(value = "/{code}",
            produces = { "application/json" },

            method = RequestMethod.GET)
    public ResponseEntity<List<Statistics>> getTheStatisticsForAGroupOrAVariable(
            @ApiParam(value = "code of the group or variable",required=true ) @PathVariable("code") String code

    )
            throws NotFoundException {
        // do some magic!
        return new ResponseEntity<List<Statistics>>(HttpStatus.OK);
    }

}
