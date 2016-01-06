/**
 * Created by mirco on 04.12.15.
 * Based on gregturn code at : 'https://github.com/spring-guides/tut-spring-boot-oauth2'.
 */

/*
 * Copyright 2012-2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.hbp.mip;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.*;
import org.hbp.mip.model.*;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
import org.springframework.boot.autoconfigure.security.oauth2.resource.UserInfoTokenServices;
import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
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
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.web.bind.annotation.*;
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
import java.util.Date;
import java.util.List;

@SpringBootApplication
@RestController
@EnableOAuth2Client
@Api(value = "/", description = "MIP API")
@javax.annotation.Generated(value = "class io.swagger.codegen.languages.SpringMVCServerCodegen", date = "2016-01-06T09:32:22.266Z")
public class MIPApplication extends WebSecurityConfigurerAdapter {

    @Autowired
    OAuth2ClientContext oauth2ClientContext;

    @RequestMapping("/user")
    @ResponseBody
    public Principal user(Principal principal, HttpServletResponse response) {
        ObjectMapper mapper = new ObjectMapper();

        try {
            String userJSON = mapper.writeValueAsString(getUser(principal));
            Cookie cookie = new Cookie("user", URLEncoder.encode(userJSON,"UTF-8"));
            cookie.setPath("/");
            response.addCookie(cookie);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return principal;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // @formatter:off
        http.antMatcher("/**")
                .authorizeRequests()
                .antMatchers("/", "/frontend/**", "/webjars/**").permitAll()
                .anyRequest().authenticated()
                .and().exceptionHandling().authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/"))
                .and().logout().logoutSuccessUrl("/").permitAll()
                .and().csrf().csrfTokenRepository(csrfTokenRepository())
                .and().addFilterAfter(csrfHeaderFilter(), CsrfFilter.class)
                .addFilterBefore(ssoFilter(), BasicAuthenticationFilter.class);
        // @formatter:on
    }

    public static void main(String[] args) {
        SpringApplication.run(MIPApplication.class, args);
    }

    @Bean
    public FilterRegistrationBean oauth2ClientFilterRegistration(
            OAuth2ClientContextFilter filter) {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(filter);
        registration.setOrder(-100);
        return registration;
    }

    private Filter ssoFilter() {
        OAuth2ClientAuthenticationProcessingFilter hbpFilter = new OAuth2ClientAuthenticationProcessingFilter("/login/hbp");
        OAuth2RestTemplate hbpTemplate = new OAuth2RestTemplate(hbp(), oauth2ClientContext);
        hbpFilter.setRestTemplate(hbpTemplate);
        hbpFilter.setTokenServices(new UserInfoTokenServices(hbpResource().getUserInfoUri(), hbp().getClientId()));
        return hbpFilter;
    }

    @Bean
    @ConfigurationProperties("hbp.client")
    OAuth2ProtectedResourceDetails hbp() {
        return new AuthorizationCodeResourceDetails();
    }

    @Bean
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
        System.out.println(userAuthentication.getDetails().toString());
        return userAuthentication.getDetails().toString();
    }

    private User getUser(Principal principal) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        org.hibernate.Query query = session.createQuery("from User where username= :username");
        query.setString("username", principal.getName());
        User user = (User) query.uniqueResult();
        session.getTransaction().commit();
        if(user == null)
        {
            session = HibernateUtil.getSessionFactory().getCurrentSession();
            session.beginTransaction();
            user = new User(getUserInfos());
            session.save(user);
            session.getTransaction().commit();
        }
        return user;
    }

    @RequestMapping(value = "/articles", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "Get articles", notes = "", response = Article.class, responseContainer = "List")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Success") })
    public ResponseEntity<List<Article>> getArticles(@ApiParam(value = "Only ask own articles") @RequestParam(value = "own", required = false) Boolean own,
                                                     @ApiParam(value = "Only ask results matching status") @RequestParam(value = "status", required = false) String status,
                                                     @ApiParam(value = "Only ask articles from own team") @RequestParam(value = "team", required = false) Boolean team,
                                                     @ApiParam(value = "Only ask valid articles") @RequestParam(value = "valid", required = false) Boolean valid
    ) throws NotFoundException {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        List<Article> articles = session.createQuery("from Article").list();
        session.getTransaction().commit();
        return new ResponseEntity<List<Article>>(HttpStatus.OK).ok(articles);
    }

    @RequestMapping(value = "/articles", method = RequestMethod.POST)
    @ApiOperation(value = "Create an article", notes = "", response = Void.class)
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Article created") })
    public ResponseEntity<Void> addAnArticle(
            @RequestBody @ApiParam(value = "Article to create" ,required=true ) Article article, Principal principal
    )
            throws NotFoundException {
        User user = getUser(principal);
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        article.setCreatedAt(new Date());
        if(article.getStatus().equals("published")) {
            article.setPublishedAt(new Date());
        }
        article.setSlug(article.getTitle().toLowerCase());
        article.setCreatedBy(user);
        session.save(article);
        session.getTransaction().commit();
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    @RequestMapping(value = "/articles/{slug}", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "Get an article", notes = "", response = Article.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Found"),
            @ApiResponse(code = 404, message = "Not found") })
    public ResponseEntity<Article> getAnArticle(
            @ApiParam(value = "slug",required=true ) @PathVariable("slug") String slug
    )
            throws NotFoundException {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        org.hibernate.Query query = session.createQuery("from Article where slug= :slug");
        query.setString("slug", slug);
        Article article = (Article) query.uniqueResult();
        session.getTransaction().commit();
        return new ResponseEntity<Article>(HttpStatus.OK).ok(article);
    }

    @RequestMapping(value = "/articles/{slug}", method = RequestMethod.PUT)
    @ApiOperation(value = "Update an article", notes = "", response = Void.class)
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Article updated") })
    public ResponseEntity<Void> updateAnArticle(
            @ApiParam(value = "slug",required=true ) @PathVariable("slug") String slug,
            @ApiParam(value = "Article to update" ,required=true ) Article article
    )
            throws NotFoundException {
        //TODO
        // do some magic!
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    @RequestMapping(value = "/articles/{slug}", method = RequestMethod.DELETE)
    @ApiOperation(value = "Delete an article", notes = "", response = Void.class)
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Article deleted") })
    public ResponseEntity<Void> deleteAnArticle(
            @ApiParam(value = "slug",required=true ) @PathVariable("slug") String slug

    )
            throws NotFoundException {
        //TODO
        // do some magic!
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    @RequestMapping(value = "/datasets/{code}", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "Get a dataset", notes = "", response = Dataset.class)
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Success") })
    public ResponseEntity<Dataset> getADataset(
            @ApiParam(value = "code",required=true ) @PathVariable("code") String code
    )
            throws NotFoundException {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        org.hibernate.Query query = session.createQuery("from Dataset where code= :code");
        query.setString("code", code);
        Dataset ds = (Dataset) query.uniqueResult();
        session.getTransaction().commit();
        return new ResponseEntity<Dataset>(HttpStatus.OK).ok(ds);
    }

    @RequestMapping(value = "/models", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "Get models", notes = "", response = Model.class, responseContainer = "List")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Success") })
    public ResponseEntity<List<Model>> getModels(@ApiParam(value = "Max number of results") @RequestParam(value = "limit", required = false) Integer limit,
                                                 @ApiParam(value = "Only ask own models") @RequestParam(value = "own", required = false) Boolean own,
                                                 @ApiParam(value = "Only ask models from own team") @RequestParam(value = "team", required = false) Boolean team,
                                                 @ApiParam(value = "Only ask valid models") @RequestParam(value = "valid", required = false) Boolean valid
    )
            throws NotFoundException {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        List<Model> models = session.createQuery("from Model").list();
        session.getTransaction().commit();
        return new ResponseEntity<List<Model>>(HttpStatus.OK).ok(models);
    }

    @RequestMapping(value = "/models", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value = "Create a model", notes = "", response = Void.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Model created") })
    public ResponseEntity<Void> addAModel(
            @RequestBody @ApiParam(value = "Model to create" ,required=true ) Model model
    )
            throws NotFoundException {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        model.setCreatedAt(new Date());
        model.setSlug(model.getTitle().toLowerCase());
        session.save(model);
        session.getTransaction().commit();
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    @RequestMapping(value = "/models/{slug}", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "Get a model", notes = "", response = Model.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Found"),
            @ApiResponse(code = 404, message = "Not found") })
    public ResponseEntity<Model> getAModel(
            @ApiParam(value = "slug",required=true ) @PathVariable("slug") String slug

    )
            throws NotFoundException {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        org.hibernate.Query query = session.createQuery("from Model where slug= :slug");
        query.setString("slug", slug);
        Model model = (Model) query.uniqueResult();
        session.getTransaction().commit();
        return new ResponseEntity<Model>(HttpStatus.OK).ok(model);
    }

    @RequestMapping(value = "/models/{slug}", method = RequestMethod.PUT)
    @ResponseBody
    @ApiOperation(value = "Update a model", notes = "", response = Void.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Model updated") })
    public ResponseEntity<Void> updateAModel(
            @ApiParam(value = "slug",required=true ) @PathVariable("slug") String slug,
            @ApiParam(value = "Model to update" ,required=true ) Model model
    )
            throws NotFoundException {
        // TODO
        // do some magic!
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    @RequestMapping(value = "/models/{slug}", method = RequestMethod.DELETE)
    @ApiOperation(value = "Delete a model", notes = "", response = Void.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Model deleted") })
    public ResponseEntity<Void> deleteAModel(
            @ApiParam(value = "slug",required=true ) @PathVariable("slug") String slug
    )
            throws NotFoundException {
        // TODO
        // do some magic!
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    @RequestMapping(value = "/groups")
    @ResponseBody
    @ApiOperation(value = "Get the root group (containing all subgroups)", notes = "", response = Group.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success") })
    public ResponseEntity<Group> getTheRootGroup()
            throws NotFoundException {
        String rootCode = "root";
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        org.hibernate.Query query = session.createQuery("from Group where code= :code");
        query.setString("code", rootCode);
        Group group = (Group) query.uniqueResult();
        session.getTransaction().commit();
        return new ResponseEntity<Group>(HttpStatus.OK).ok(group);
    }

    @RequestMapping(value = "/variables")
    @ResponseBody
    @ApiOperation(value = "Get variables", notes = "", response = Variable.class, responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success") })
    public ResponseEntity<List<Variable>> getVariables(@ApiParam(value = "List of groups formatted like : (\"val1\", \"val2\", ...)") @RequestParam(value = "group", required = false) String group,
                                                       @ApiParam(value = "List of subgroups formatted like : (\"val1\", \"val2\", ...)") @RequestParam(value = "subgroup", required = false) String subgroup,
                                                       @ApiParam(value = "Boolean value formatted like : (\"0\") or (\"1\") or (\"false\") or (\"true\")") @RequestParam(value = "isVariable", required = false) String isVariable,
                                                       @ApiParam(value = "Boolean value formatted like : (\"0\") or (\"1\") or (\"false\") or (\"true\")") @RequestParam(value = "isGrouping", required = false) String isGrouping,
                                                       @ApiParam(value = "Boolean value formatted like : (\"0\") or (\"1\") or (\"false\") or (\"true\")") @RequestParam(value = "isCovariable", required = false) String isCovariable,
                                                       @ApiParam(value = "Boolean value formatted like : (\"0\") or (\"1\") or (\"false\") or (\"true\")") @RequestParam(value = "isFilter", required = false) String isFilter
    )
            throws NotFoundException {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        List<Variable> variables = session.createQuery("from Variable").list();
        session.getTransaction().commit();
        return new ResponseEntity<List<Variable>>(HttpStatus.OK).ok(variables);
    }

    @RequestMapping(value = "/variables/{code}")
    @ResponseBody
    @ApiOperation(value = "Get a variable", notes = "", response = Variable.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Found"),
            @ApiResponse(code = 404, message = "Not found") })
    public ResponseEntity<Variable> getAVariable(
            @ApiParam(value = "code ( multiple codes are allowed, separeted by \",\" )",required=true ) @PathVariable("code") String code
    )
            throws NotFoundException {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        org.hibernate.Query query = session.createQuery("from Variable where code= :code");
        query.setString("code", code);
        Variable variable = (Variable) query.uniqueResult();
        session.getTransaction().commit();
        return new ResponseEntity<Variable>(HttpStatus.OK).ok(variable);
    }

    @RequestMapping(value = "/variables/{code}/values")
    @ResponseBody
    @ApiOperation(value = "Get values from a variable", notes = "", response = Value.class, responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Found"),
            @ApiResponse(code = 404, message = "Not found") })
    public ResponseEntity<List<Value>> getValuesFromAVariable(
            @ApiParam(value = "code",required=true ) @PathVariable("code") String code,
            @ApiParam(value = "Pattern to match") @RequestParam(value = "q", required = false) String q
    )
            throws NotFoundException {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        List<Value> values = session.createQuery("select values from Variable where code= :code").setString("code", code).list();
        session.getTransaction().commit();
        return new ResponseEntity<List<Value>>(HttpStatus.OK).ok(values);
    }

}

@javax.annotation.Generated(value = "class io.swagger.codegen.languages.SpringMVCServerCodegen", date = "2016-01-06T09:32:22.266Z")
class NotFoundException extends ApiException {
    private int code;
    public NotFoundException (int code, String msg) {
        super(code, msg);
        this.code = code;
    }
}

@javax.annotation.Generated(value = "class io.swagger.codegen.languages.SpringMVCServerCodegen", date = "2016-01-06T09:32:22.266Z")
class ApiException extends Exception{
    private int code;
    public ApiException (int code, String msg) {
        super(msg);
        this.code = code;
    }
}
