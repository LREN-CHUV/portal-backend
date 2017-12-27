/*
 * Created by mirco on 14.01.16.
 */

package eu.hbp.mip.controllers;

import io.swagger.annotations.*;
import eu.hbp.mip.model.User;
import eu.hbp.mip.repositories.UserRepository;
import org.apache.log4j.Logger;
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

    private static final Logger LOGGER = Logger.getLogger(UsersApi.class);

    @Autowired
    private UserRepository userRepository;

    @ApiOperation(value = "Get a user", response = User.class)
    @RequestMapping(value = "/{username}", method = RequestMethod.GET)
    public ResponseEntity<User> getAUser(
            @ApiParam(value = "username", required = true) @PathVariable("username") String username
    )  {
        LOGGER.info("Get a user");

        return ResponseEntity.ok(userRepository.findOne(username));
    }
}
