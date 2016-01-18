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

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * Created by mirco on 18.01.16.
 */
@Controller
@RequestMapping(value = "/queries/requests", produces = {APPLICATION_JSON_VALUE})
@Api(value = "/queries/requests", description = "the groups API")
@javax.annotation.Generated(value = "class io.swagger.codegen.languages.SpringMVCServerCodegen", date = "2016-01-07T07:38:20.227Z")
public class RequestsApi {

    private static String RESPONSE_SRC = "/home/mirco/Workspace/GitLab/mip/target/classes/data/requestExample.json";

    @ApiOperation(value = "", notes = "", response = Group.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success")})
    @RequestMapping(value = "", produces = {"application/json"}, method = RequestMethod.POST)
    public ResponseEntity<String> postRequests() throws NotFoundException {
        // Read data from file
        String response = "";
        try {
            response = new String(Files.readAllBytes(Paths.get(RESPONSE_SRC)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ResponseEntity<Group>(HttpStatus.OK).ok(response);
    }

}
