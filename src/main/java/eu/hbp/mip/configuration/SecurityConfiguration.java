package eu.hbp.mip.configuration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import eu.hbp.mip.model.User;
import eu.hbp.mip.repositories.UserRepository;
import eu.hbp.mip.utils.CORSFilter;
import eu.hbp.mip.utils.CustomLoginUrlAuthenticationEntryPoint;
import eu.hbp.mip.utils.HTTPUtil;
import io.swagger.annotations.ApiParam;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
import org.springframework.boot.autoconfigure.security.oauth2.resource.UserInfoTokenServices;
import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.filter.OAuth2ClientAuthenticationProcessingFilter;
import org.springframework.security.oauth2.client.filter.OAuth2ClientContextFilter;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.web.access.channel.ChannelProcessingFilter;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.WebUtils;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.Principal;

// See https://spring.io/guides/tutorials/spring-boot-oauth2/ for reference about configuring OAuth2 login
// also http://cscarioni.blogspot.ch/2013/04/pro-spring-security-and-oauth-2.html

/**
 * Configuration for security.
 */
@Configuration
@EnableOAuth2Client
@RestController
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private static final Logger LOGGER = Logger.getLogger(SecurityConfiguration.class);

    @Autowired
    private OAuth2ClientContext oauth2ClientContext;

    @Autowired
    private UserRepository userRepository;

    /**
     * Enable HBP collab authentication (1) or disable it (0). Default is 1
     */
    @Value("#{'${hbp.authentication.enabled:1}'}")
    private boolean authentication;

    /**
     * Absolute URL to redirect to when login is required
     */
    @Value("#{'${frontend.loginUrl:/login/hbp}'}")
    private String loginUrl;

    /**
     * Absolute URL to redirect to after successful login
     */
    @Value("#{'${frontend.redirectAfterLoginUrl:http://frontend/home}'}")
    private String frontendRedirectAfterLogin;

    /**
     * Absolute URL to redirect to after logout has occurred
     */
    @Value("#{'${frontend.redirectAfterLogoutUrl:/login/hbp}'}")
    private String redirectAfterLogoutUrl;

    /**
     * URL to revoke auth token
     */
    @Value("#{'${hbp.resource.revokeTokenUri:https://services.humanbrainproject.eu/oidc/revoke}'}")
    private String revokeTokenURI;

    /**
     * Set to true if using no-auth mode and user has clicked on the login button
     */
    private boolean fakeAuth = false;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // @formatter:off
        http.addFilterBefore(new CORSFilter(), ChannelProcessingFilter.class);

        if (authentication) {
            http.antMatcher("/**")
                    .authorizeRequests()
                    .antMatchers(
                            "/", "/login/**", "/health/**", "/info/**", "/metrics/**", "/trace/**", "/frontend/**", "/webjars/**", "/v2/api-docs", "/swagger-ui.html", "/swagger-resources/**"
                    ).permitAll()
                    .anyRequest().authenticated()
                    .and().exceptionHandling().authenticationEntryPoint(new CustomLoginUrlAuthenticationEntryPoint(loginUrl))
                    .and().logout().addLogoutHandler(new CustomLogoutHandler()).logoutSuccessUrl(redirectAfterLogoutUrl)
                    .and().logout().permitAll()
                    .and().csrf().ignoringAntMatchers("/logout").csrfTokenRepository(csrfTokenRepository())
                    .and().addFilterAfter(csrfHeaderFilter(), CsrfFilter.class)
                    .addFilterBefore(ssoFilter(), BasicAuthenticationFilter.class);
        }
        else {
            http.antMatcher("/**")
                    .authorizeRequests()
                    .antMatchers("/**").permitAll().and().csrf().disable();
            User user = getUser();
            userRepository.save(user);
        }
    }

    private Filter ssoFilter() {
        OAuth2ClientAuthenticationProcessingFilter hbpFilter = new OAuth2ClientAuthenticationProcessingFilter("/login/hbp");
        OAuth2RestTemplate hbpTemplate = new OAuth2RestTemplate(hbp(), oauth2ClientContext);
        hbpFilter.setAuthenticationSuccessHandler(new SimpleUrlAuthenticationSuccessHandler(frontendRedirectAfterLogin));
        hbpFilter.setRestTemplate(hbpTemplate);
        hbpFilter.setTokenServices(new UserInfoTokenServices(hbpResource().getUserInfoUri(), hbp().getClientId()));
        return hbpFilter;
    }

    @Bean
    public FilterRegistrationBean oauth2ClientFilterRegistration(
            OAuth2ClientContextFilter filter) {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(filter);
        registration.setOrder(-100);
        return registration;
    }

    @Bean(name="hbp")
    @ConfigurationProperties("hbp.client")
    OAuth2ProtectedResourceDetails hbp() {
        return new AuthorizationCodeResourceDetails();
    }

    @Bean(name="hbpResource")
    @ConfigurationProperties("hbp.resource")
    ResourceServerProperties hbpResource() {
        return new ResourceServerProperties();
    }

    private Filter csrfHeaderFilter() {
        return new OncePerRequestFilter() {
            @Override
            protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain filterChain) throws ServletException, IOException {
                CsrfToken csrf = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
                if (csrf != null) {
                    Cookie cookie = WebUtils.getCookie(request, "XSRF-TOKEN");
                    String token = csrf.getToken();
                    if (cookie == null || token != null && !token.equals(cookie.getValue())) {
                        cookie = new Cookie("XSRF-TOKEN", token);
                        cookie.setPath("/");
                        response.addCookie(cookie);
                    }
                }
                filterChain.doFilter(request, response);
            }
        };
    }

    private CsrfTokenRepository csrfTokenRepository() {
        HttpSessionCsrfTokenRepository repository = new HttpSessionCsrfTokenRepository();
        repository.setHeaderName("X-XSRF-TOKEN");
        return repository;
    }

    private String getUserInfos() {
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
     * @return the user for the current session
     */
    public synchronized User getUser() {
        User user;

        if (!authentication)
        {
            user = new User();
            user.setUsername("anonymous");
            user.setFullname("anonymous");
            user.setPicture("images/users/default_user.png");
        }
        else
        {
            user = new User(getUserInfos());
        }
        User foundUser = userRepository.findOne(user.getUsername());
        if (foundUser != null) {
            user.setAgreeNDA(foundUser.getAgreeNDA());
        }
        userRepository.save(user);
        return user;
    }

    @RequestMapping(path = "/user", method = RequestMethod.GET)
    public Object user(Principal principal, HttpServletResponse response) {
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

        if(!authentication)
        {
            if(!fakeAuth)
            {
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
        User user = getUser();
        if (user != null) {
            user.setAgreeNDA(agreeNDA);
            userRepository.save(user);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @RequestMapping(path = "/login/hbp", method = RequestMethod.GET)
    @ConditionalOnExpression("${hbp.authentication.enabled:0}")
    public void noLogin(HttpServletResponse httpServletResponse) throws IOException {
        fakeAuth = true;
        httpServletResponse.sendRedirect(frontendRedirectAfterLogin);
    }

    private class CustomLogoutHandler implements LogoutHandler {
        @Override
        public void logout(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) {

            fakeAuth = false;

            if (oauth2ClientContext == null || oauth2ClientContext.getAccessToken() == null)
            {
                return;
            }

            String idToken = oauth2ClientContext.getAccessToken().getAdditionalInformation().get("id_token").toString();

            StringBuilder query = new StringBuilder();
            query.append("{");
            query.append("\"token\":");
            query.append("\"").append(idToken).append("\"");
            query.append("}");


            try {
                int responseCode = HTTPUtil.sendPost(revokeTokenURI, query.toString(), new StringBuilder());
                if (responseCode != 200)
                {
                    LOGGER.warn("Cannot send request to OIDC server for revocation ! ");
                }
                else{
                    LOGGER.info("Should be logged out");
                }

            } catch (IOException e) {
                LOGGER.warn("Cannot notify logout to OIDC server !");
                LOGGER.trace(e);

            }
        }
    }
}