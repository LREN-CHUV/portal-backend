package eu.hbp.mip.configuration;

import eu.hbp.mip.model.UserInfo;
import eu.hbp.mip.utils.CORSFilter;
import eu.hbp.mip.utils.CustomLoginUrlAuthenticationEntryPoint;
import eu.hbp.mip.utils.HTTPUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
import org.springframework.boot.autoconfigure.security.oauth2.resource.UserInfoTokenServices;
import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.filter.OAuth2ClientAuthenticationProcessingFilter;
import org.springframework.security.oauth2.client.filter.OAuth2ClientContextFilter;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.security.web.access.channel.ChannelProcessingFilter;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.WebUtils;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

// See https://spring.io/guides/tutorials/spring-boot-oauth2/ for reference about configuring OAuth2 login
// also http://cscarioni.blogspot.ch/2013/04/pro-spring-security-and-oauth-2.html

/**
 * Configuration for security.
 */
@Configuration
@EnableOAuth2Client
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(SecurityConfiguration.class);

    @Autowired
    private OAuth2ClientContext oauth2ClientContext;

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
    public OAuth2ProtectedResourceDetails hbp() {
        return new AuthorizationCodeResourceDetails();
    }

    @Bean(name="hbpResource")
    @ConfigurationProperties("hbp.resource")
    public ResourceServerProperties hbpResource() {
        return new ResourceServerProperties();
    }

    public boolean isAuthentication() {
        return authentication;
    }

    public String getFrontendRedirectAfterLogin() {
        return frontendRedirectAfterLogin;
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

    private class CustomLogoutHandler implements LogoutHandler {
        @Override
        public void logout(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) {

            // Hackish way of accessing to this information...
            final UserInfo userInfo = (UserInfo) httpServletRequest.getSession().getAttribute("userInfo");
            if (userInfo != null) {
                userInfo.setFakeAuth(false);
            }

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
                LOGGER.trace("Cannot notify logout", e);
            }

        }
    }
}
