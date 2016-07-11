/**
 * Created by mirco on 04.12.15.
 * Based on gregturn code at : 'https://github.com/spring-guides/tut-spring-boot-oauth2'.
 */

package org.hbp.mip;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import org.apache.log4j.Logger;
import org.hbp.mip.model.User;
import org.hbp.mip.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.Principal;

@SpringBootApplication
@Configuration
@RestController
@EnableSwagger2
@Api(value = "/", description = "MIP API")
public class MIPApplication {

    private static final Logger LOGGER = Logger.getLogger(MIPApplication.class);

    @Autowired
    UserRepository userRepository;


    public static void main(String[] args) {
        SpringApplication.run(MIPApplication.class, args);
    }

    public String getUserInfos() {
        OAuth2Authentication oAuth2Authentication = (OAuth2Authentication) SecurityContextHolder.getContext().getAuthentication();
        Authentication userAuthentication = oAuth2Authentication.getUserAuthentication();
        return userAuthentication.getDetails().toString();
    }

    /**
     * returns the user for the current session.
     * <p>
     * the "synchronized" keyword is there to avoid a bug that the transaction is supposed to protect me from.
     * To test if your solution to removing it works, do the following:
     * - clean DB from scratch
     * - restart DB and backend (no session or anything like that)
     * - log in using the front end
     * - check you have no 500 error in the network logs.
     *
     * @return
     */
    public synchronized User getUser() {
        User user = new User(getUserInfos());
        User foundUser = userRepository.findOne(user.getUsername());
        user.setAgreeNDA(foundUser.getAgreeNDA());
        userRepository.save(user);
        return user;
    }

    @RequestMapping(path = "/user", method = RequestMethod.GET)
    public Principal user(Principal principal, HttpServletResponse response) {
        ObjectMapper mapper = new ObjectMapper();

        try {
            String userJSON = mapper.writeValueAsString(getUser());
            Cookie cookie = new Cookie("user", URLEncoder.encode(userJSON, "UTF-8"));
            cookie.setSecure(true);
            cookie.setPath("/");
            response.addCookie(cookie);
        } catch (JsonProcessingException | UnsupportedEncodingException e) {
            LOGGER.trace(e);
        }
        return principal;
    }

    @RequestMapping(path = "/user", method = RequestMethod.POST)
    public ResponseEntity<Void> postUser(@ApiParam(value = "Has the user agreed on the NDA") @RequestParam(value = "agreeNDA", required = true) Boolean agreeNDA) {
        String username = getUser().getUsername();
        User user = userRepository.findOne(username);
        if (user != null) {
            user.setAgreeNDA(agreeNDA);
            userRepository.save(user);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}