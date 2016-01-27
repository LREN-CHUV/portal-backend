package org.hbp.mip.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.hbp.mip.model.Group;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * Created by mirco on 18.01.16.
 */
@Controller
@RequestMapping(value = "/queries/requests", produces = {APPLICATION_JSON_VALUE})
@Api(value = "/queries/requests", description = "the groups API")
@javax.annotation.Generated(value = "class io.swagger.codegen.languages.SpringMVCServerCodegen", date = "2016-01-07T07:38:20.227Z")
public class RequestsApi {

    @ApiOperation(value = "", notes = "", response = Group.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success")})
    @RequestMapping(value = "", produces = {"application/json"}, method = RequestMethod.POST)
    public ResponseEntity<String> postRequests() throws NotFoundException {
        // Mock data
        String response =
                "{\n" +
                "    \"code\": \"ib8ehpl4ic8cc0oc480w84w00cg4g0\",\n" +
                "    \"date\": \"2016-01-18T13:56:14+0100\",\n" +
                "    \"header\": [\n" +
                "        \"MidTemp\"\n" +
                "    ],\n" +
                "    \"data\": {\n" +
                "        \"MidTemp\": [\n" +
                "            18422,\n" +
                "            16972,\n" +
                "            17330,\n" +
                "            16398,\n" +
                "            21614,\n" +
                "            21386,\n" +
                "            20474,\n" +
                "            19867,\n" +
                "            20398,\n" +
                "            19741,\n" +
                "            18595,\n" +
                "            18018\n" +
                "        ]\n" +
                "    }\n" +
                "}\n";
        return new ResponseEntity<Group>(HttpStatus.OK).ok(response);
    }

}
