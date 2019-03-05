package eu.hbp.mip.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import eu.hbp.mip.configuration.SecurityConfiguration;
import eu.hbp.mip.model.User;
import eu.hbp.mip.model.UserInfo;
import eu.hbp.mip.repositories.UserRepository;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.Principal;

@RestController
public class SecurityApi {

    private static final Logger LOGGER = LoggerFactory.getLogger(SecurityApi.class);

    @Autowired
    private UserInfo userInfo;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SecurityConfiguration securityConfiguration;

    @RequestMapping(path = "/user", method = RequestMethod.GET)
    public Object user(Principal principal, HttpServletResponse response) {
        ObjectMapper mapper = new ObjectMapper();

        try {
            String userJSON = mapper.writeValueAsString(userInfo.getUser());
            Cookie cookie = new Cookie("user", URLEncoder.encode(userJSON, "UTF-8"));
            cookie.setSecure(true);
            cookie.setPath("/");
            response.addCookie(cookie);
        } catch (JsonProcessingException | UnsupportedEncodingException e) {
            LOGGER.trace("Cannot read user json", e);
        }

        if (!securityConfiguration.isAuthentication()) {
            if (userInfo.isFakeAuth()) {
                response.setStatus(401);
            }
            String principalJson = "{\"principal\": \"anonymous\", \"name\": \"anonymous\", \"userAuthentication\": {" +
                    "\"details\": {\"preferred_username\": \"anonymous\"}}}";
            return new Gson().fromJson(principalJson, Object.class);
        }

        return principal;
    }

    @RequestMapping(path = "/user", method = RequestMethod.POST)
    public ResponseEntity<Void> postUser(@ApiParam(value = "Has the user agreed on the NDA") @RequestParam(value = "agreeNDA") Boolean agreeNDA) {
        User user = userInfo.getUser();
        if (user != null) {
            user.setAgreeNDA(agreeNDA);
            userRepository.save(user);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @RequestMapping(path = "/login/hbp", method = RequestMethod.GET)
    @ConditionalOnExpression("${hbp.authentication.enabled:0}")
    public void noLogin(HttpServletResponse httpServletResponse) throws IOException {
        userInfo.setFakeAuth(true);
        httpServletResponse.sendRedirect(securityConfiguration.getFrontendRedirectAfterLogin());
    }

}
