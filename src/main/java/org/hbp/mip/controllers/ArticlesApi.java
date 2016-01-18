/**
 * Created by mirco on 04.12.15.
 */

package org.hbp.mip.controllers;


import io.swagger.annotations.*;
import org.hbp.mip.MIPApplication;
import org.hbp.mip.model.Article;
import org.hbp.mip.model.User;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Date;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Controller
@RequestMapping(value = "/articles", produces = {APPLICATION_JSON_VALUE})
@Api(value = "/articles", description = "the articles API")
@javax.annotation.Generated(value = "class io.swagger.codegen.languages.SpringMVCServerCodegen", date = "2016-01-07T07:38:20.227Z")
public class ArticlesApi {


    @ApiOperation(value = "Get articles", notes = "", response = Article.class, responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success")})
    @RequestMapping(value = "", produces = {"application/json"}, method = RequestMethod.GET)
    public ResponseEntity<List<Article>> getArticles(
            @ApiParam(value = "Only ask own articles") @RequestParam(value = "own", required = false) Boolean own,
            @ApiParam(value = "Only ask results matching status") @RequestParam(value = "status", required = false) String status,
            @ApiParam(value = "Only ask articles from own team") @RequestParam(value = "team", required = false) Boolean team,
            @ApiParam(value = "Only ask valid articles") @RequestParam(value = "valid", required = false) Boolean valid) throws NotFoundException {

        String queryString = "from Article";
        if(status != null)
        {
            queryString += " where status= :status";
        }

        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        Query query = session.createQuery(queryString);

        if(status != null)
        {
            query.setString("status", status);
        }

        List<Article> articles = query.list();
        session.getTransaction().commit();
        return new ResponseEntity<List<Article>>(HttpStatus.OK).ok(articles);
    }


    @ApiOperation(value = "Create an article", notes = "", response = Void.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Article created")})
    @RequestMapping(value = "", produces = {"application/json"}, method = RequestMethod.POST)
    public ResponseEntity<Void> addAnArticle(
            @RequestBody @ApiParam(value = "Article to create", required = true) Article article, Principal principal) throws NotFoundException {
        User user = MIPApplication.getUser(principal);
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        article.setCreatedAt(new Date());
        if (article.getStatus().equals("published")) {
            article.setPublishedAt(new Date());
        }
        article.setSlug(article.getTitle().toLowerCase());
        article.setCreatedBy(user);
        session.save(article);
        session.getTransaction().commit();
        return new ResponseEntity<Void>(HttpStatus.OK);
    }


    @ApiOperation(value = "Get an article", notes = "", response = Article.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Found"),
            @ApiResponse(code = 404, message = "Not found")})
    @RequestMapping(value = "/{slug}", produces = {"application/json"}, method = RequestMethod.GET)
    public ResponseEntity<Article> getAnArticle(
            @ApiParam(value = "slug", required = true) @PathVariable("slug") String slug) throws NotFoundException {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        org.hibernate.Query query = session.createQuery("from Article where slug= :slug");
        query.setString("slug", slug);
        Article article = (Article) query.uniqueResult();
        session.getTransaction().commit();
        return new ResponseEntity<Article>(HttpStatus.OK).ok(article);
    }


    @ApiOperation(value = "Update an article", notes = "", response = Void.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Article updated")})
    @RequestMapping(value = "/{slug}", produces = {"application/json"}, method = RequestMethod.PUT)
    public ResponseEntity<Void> updateAnArticle(
            @ApiParam(value = "slug", required = true) @PathVariable("slug") String slug,
            @ApiParam(value = "Article to update", required = true) Article article) throws NotFoundException {
        // do some magic!
        return new ResponseEntity<Void>(HttpStatus.OK);
    }


    @ApiOperation(value = "Delete an article", notes = "", response = Void.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Article deleted")})
    @RequestMapping(value = "/{slug}", produces = {"application/json"}, method = RequestMethod.DELETE)
    public ResponseEntity<Void> deleteAnArticle(
            @ApiParam(value = "slug", required = true) @PathVariable("slug") String slug) throws NotFoundException {
        // do some magic!
        return new ResponseEntity<Void>(HttpStatus.OK);
    }


}
