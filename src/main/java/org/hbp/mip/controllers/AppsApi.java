/**
 * Created by mirco on 20.05.16.
 */

package org.hbp.mip.controllers;

import io.swagger.annotations.*;
import org.hbp.mip.model.App;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedList;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/apps", produces = {APPLICATION_JSON_VALUE})
@Api(value = "/apps", description = "the apps API")
public class AppsApi {

    @ApiOperation(value = "Get apps", response = App.class, responseContainer = "List")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Success") })
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List> getApps(
            @ApiParam(value = "Only ask own articles") @RequestParam(value = "own", required = false) Boolean own,
            @ApiParam(value = "Only ask results matching status", allowableValues = "{values=[draft, published, closed]}") @RequestParam(value = "status", required = false) String status,
            @ApiParam(value = "Only ask articles from own team") @RequestParam(value = "team", required = false) Boolean team
    ) {
        List<App> apps = new LinkedList<>();

        return ResponseEntity.ok(apps);
    }
}
