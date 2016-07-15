/**
 * Created by mirco on 14.01.16.
 */

package org.hbp.mip.controllers;

import io.swagger.annotations.*;
import org.hbp.mip.model.User;
import org.hbp.mip.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/users", produces = {APPLICATION_JSON_VALUE})
@Api(value = "/users", description = "the users API")
public class UsersApi {

    @Autowired
    UserRepository userRepository;

    @ApiOperation(value = "Get a user", response = User.class)
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Found"), @ApiResponse(code = 404, message = "Not found") })
    @RequestMapping(value = "/{username}", method = RequestMethod.GET)
    public ResponseEntity<User> getAUser(
            @ApiParam(value = "username", required = true) @PathVariable("username") String username
    )  {
        return ResponseEntity.ok(userRepository.findOne(username));
    }
}
