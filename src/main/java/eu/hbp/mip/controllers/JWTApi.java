package eu.hbp.mip.controllers;

import static org.springframework.http.MediaType.TEXT_PLAIN_VALUE;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.JWT;

import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(value = "/jwt", produces = { TEXT_PLAIN_VALUE })
@Api(value = "/jwt", description = "the jwt API")
public class JWTApi {

    private static final Logger LOGGER = LoggerFactory.getLogger(JWTApi.class);

    @ApiOperation(value = "Create a JSON Web Token", response = String.class)
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<String> createJWT() {

        LOGGER.info("Create a JSON Web Token");

        Algorithm algorithm = Algorithm.HMAC512("secret");
        String token = JWT.create()
        .withIssuer("mip.humanbrainproject.eu")
        .withSubject("subj")
        .sign(algorithm);

        return ResponseEntity.status(HttpStatus.CREATED).body(token);
    }
}