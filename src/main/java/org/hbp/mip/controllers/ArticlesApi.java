/**
 * Created by mirco on 04.12.15.
 */

package org.hbp.mip.controllers;


import io.swagger.annotations.*;
import org.hbp.mip.MIPApplication;
import org.hbp.mip.model.Article;
import org.hbp.mip.model.User;
import org.hbp.mip.utils.HibernateUtil;
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
public class ArticlesApi {


    @ApiOperation(value = "Get articles", response = Article.class, responseContainer = "List")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Success") })
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<Article>> getArticles(
            @ApiParam(value = "Only ask own articles") @RequestParam(value = "own", required = false) Boolean own,
            @ApiParam(value = "Only ask results matching status", allowableValues = "{values=[draft, published, closed]}") @RequestParam(value = "status", required = false) String status,
            @ApiParam(value = "Only ask articles from own team") @RequestParam(value = "team", required = false) Boolean team,
            Principal principal
    ) throws NotFoundException {

        // Get current user
        User user = MIPApplication.getUser(principal);

        // Prepare HQL query using Article and User tables
        String queryString = "SELECT a FROM Article a, User u where a.createdBy=u.id";
        if(status != null)
        {
            queryString += " and status= :status";
        }

        if(own != null && own)
        {
            queryString += " and u.username= :username";
        }
        else
        {
            if(team != null && team)
            {
                queryString += " and u.team= :team";
            }
        }

        // Query DB
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        Query query = session.createQuery(queryString);
        if(status != null)
        {
            query.setString("status", status);
        }
        if(own != null && own)
        {
            query.setString("username", user.getUsername());
        }
        else
        {
            if(team != null && team)
            {
                query.setString("team", user.getTeam());
            }
        }
        List<Article> articles = query.list();
        session.getTransaction().commit();

        return new ResponseEntity<List<Article>>(HttpStatus.OK).ok(articles);
    }


    @ApiOperation(value = "Create an article", response = Void.class)
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Article created") })
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Void> addAnArticle(
            @RequestBody @ApiParam(value = "Article to create", required = true) Article article,
            Principal principal
    ) throws NotFoundException {

        // Get current user
        User user = MIPApplication.getUser(principal);

        // Set up article to save
        article.setCreatedAt(new Date());
        if (article.getStatus().equals("published")) {
            article.setPublishedAt(new Date());
        }
        article.setSlug(article.getTitle().toLowerCase().replaceAll(" ","_"));
        article.setCreatedBy(user);

        // Save article into DB
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        session.save(article);
        session.getTransaction().commit();

        return new ResponseEntity<Void>(HttpStatus.OK);
    }


    @ApiOperation(value = "Get an article", response = Article.class)
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Found"), @ApiResponse(code = 404, message = "Not found") })
    @RequestMapping(value = "/{slug}", method = RequestMethod.GET)
    public ResponseEntity<Article> getAnArticle(
            @ApiParam(value = "slug", required = true) @PathVariable("slug") String slug
    ) throws NotFoundException {

        // Query DB
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        Query query = session.createQuery("from Article where slug= :slug");
        query.setString("slug", slug);
        Article article = (Article) query.uniqueResult();
        session.getTransaction().commit();

        return new ResponseEntity<Article>(HttpStatus.OK).ok(article);
    }


    @ApiOperation(value = "Update an article", response = Void.class)
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Article updated") })
    @RequestMapping(value = "/{slug}", method = RequestMethod.PUT)
    public ResponseEntity<Void> updateAnArticle(
            @ApiParam(value = "slug", required = true) @PathVariable("slug") String slug,
            @RequestBody @ApiParam(value = "Article to update", required = true) Article article,
            Principal principal
    ) throws NotFoundException {

        // Get current user
        User user = MIPApplication.getUser(principal);

        // Query DB
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        session.update(article);
        session.getTransaction().commit();

        return new ResponseEntity<Void>(HttpStatus.OK);
    }


    @ApiOperation(value = "Delete an article", response = Void.class)
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Article deleted") })
    @RequestMapping(value = "/{slug}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> deleteAnArticle(
            @ApiParam(value = "slug", required = true) @PathVariable("slug") String slug
    ) throws NotFoundException {

        // TODO : Implement delete method

        return new ResponseEntity<Void>(HttpStatus.OK);
    }


}
