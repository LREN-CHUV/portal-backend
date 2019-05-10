package eu.hbp.mip.controllers;

import static org.springframework.http.MediaType.TEXT_PLAIN_VALUE;
import java.security.Key;
import java.util.Date;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import eu.hbp.mip.model.UserInfo;
import org.slf4j.Logger;
import eu.hbp.mip.model.User;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import io.jsonwebtoken.*;
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

    @ApiOperation(value = "Create a JSON Web Token", response = String.class)
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<String> createJWT() {

        LOGGER.info("Create a JSON Web Token");

        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        String apiKey = "6v2oxpJMzU14U-dqVireln5AUKTtx5fBPSEgaBZiI983d98cfa6";
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(apiKey);
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);

        User user = userInfo.getUser();

        // Set the JWT Claims
        JwtBuilder builder = Jwts.builder().setIssuedAt(now)
                .setIssuer("mip.humanbrainproject.eu").setSubject(user.getEmail()).signWith(signatureAlgorithm, signingKey);

        long expMillis = nowMillis + 86400 * 24;
        Date exp = new Date(expMillis);
        builder.setExpiration(exp);

        return ResponseEntity.status(HttpStatus.CREATED).body(builder.compact());
    }
}