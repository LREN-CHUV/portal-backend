package eu.hbp.mip.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.JWT;

public class JWTUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(JWT.class);

    public static String getJWT(String secret, String subject) {
        LOGGER.info("getJWT");
        Algorithm algorithm = Algorithm.HMAC512(secret);
        String token = JWT.create().withIssuer("mip.humanbrainproject.eu").withSubject(subject).sign(algorithm);

        return token;
    }
}