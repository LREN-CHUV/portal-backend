package eu.hbp.mip.controllers;

import static org.springframework.http.MediaType.TEXT_PLAIN_VALUE;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.JWT;
import eu.hbp.mip.model.User;
import eu.hbp.mip.model.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(value = "/jwt", produces = { TEXT_PLAIN_VALUE })
@Api(value = "/jwt", description = "the jwt API")
public class JWTApi {

    private static final Logger LOGGER = LoggerFactory.getLogger(JWTApi.class);

    @Autowired
    private UserInfo userInfo;

    @Value("#{'${services.workflows.JWTSecret}'}")
    private String JWTSecret;

    @ApiOperation(value = "Create a JSON Web Token", response = String.class)
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<String> createJWT() {

        LOGGER.info("Create a JSON Web Token");

        User user = userInfo.getUser();

        Algorithm algorithm = Algorithm.HMAC512(JWTSecret);
        String token = JWT.create().withIssuer("mip.humanbrainproject.eu").withSubject(user.getEmail()).sign(algorithm);

        LOGGER.info(algorithm.toString());
        LOGGER.info(token);

        return ResponseEntity.status(HttpStatus.CREATED).body(token);
    }
}