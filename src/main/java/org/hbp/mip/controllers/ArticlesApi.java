/**
 * Created by mirco on 04.12.15.
 */

package org.hbp.mip.controllers;


import com.github.slugify.Slugify;
import io.swagger.annotations.*;
import org.hbp.mip.MIPApplication;
import org.hbp.mip.model.Article;
import org.hbp.mip.model.User;
import org.hbp.mip.utils.HibernateUtil;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/articles", produces = {APPLICATION_JSON_VALUE})
@Api(value = "/articles", description = "the articles API")
public class ArticlesApi {

    @Autowired
    MIPApplication mipApplication;

    @ApiOperation(value = "Get articles", response = Article.class, responseContainer = "List")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Success") })
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List> getArticles(
            @ApiParam(value = "Only ask own articles") @RequestParam(value = "own", required = false) Boolean own,
            @ApiParam(value = "Only ask results matching status", allowableValues = "{values=[draft, published, closed]}") @RequestParam(value = "status", required = false) String status,
            @ApiParam(value = "Only ask articles from own team") @RequestParam(value = "team", required = false) Boolean team
    ) {

        User user = mipApplication.getUser();

        String queryString = "SELECT a FROM Article a, User u WHERE a.createdBy=u.username";
        if(status != null)
        {
            queryString += " AND status= :status";
        }
        if(own != null && own)
        {
            queryString += " AND u.username= :username";
        }
        else
        {
            queryString += " AND status='published'";
            if(team != null && team)
            {
                // TODO: decide if this is needed
                //queryString += " AND u.team= :team";
            }
        }

        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        List articles = new LinkedList<>();
        try {
            session.beginTransaction();
            Query query = session.createQuery(queryString);
            if (status != null) {
                query.setString("status", status);
            }
            if (own != null && own) {
                query.setString("username", user.getUsername());
            } else {
                if (team != null && team) {
                    // TODO: decide if this is needed
                    //query.setString("team", user.getTeam());
                }
            }
            articles = query.list();
            session.getTransaction().commit();
        } catch (Exception e)
        {
            if(session.getTransaction() != null)
            {
                session.getTransaction().rollback();
                throw e;
            }
        }


        return ResponseEntity.ok(articles);
    }


    @ApiOperation(value = "Create an article", response = Void.class)
    @ApiResponses(value = { @ApiResponse(code = 201, message = "Article created") })
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Void> addAnArticle(
            @RequestBody @ApiParam(value = "Article to create", required = true) @Valid Article article
    ) {

        User user = mipApplication.getUser();

        article.setCreatedAt(new Date());
        if (article.getStatus().equals("published")) {
            article.setPublishedAt(new Date());
        }
        article.setCreatedBy(user);

        Long count;
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        try{
            session.beginTransaction();

            int i = 0;
            do{
                i++;
                count = (Long) session
                        .createQuery("select count(*) from Article where title= :title")
                        .setString("title", article.getTitle())
                        .uniqueResult();

                if(count > 0)
                {
                    String title = article.getTitle();
                    if(i > 1)
                    {
                        title = title.substring(0, title.length()-4);
                    }
                    article.setTitle(title + " (" + i + ")");
                }
            } while(count > 0);

            Slugify slg = null;
            try {
                slg = new Slugify();
            } catch (IOException e) {
                e.printStackTrace();
            }
            String slug = slg.slugify(article.getTitle());

            i = 0;
            do {
                i++;
                count = (Long) session
                        .createQuery("select count(*) from Article where slug= :slug")
                        .setString("slug", slug)
                        .uniqueResult();
                if(count > 0)
                {
                    if(i > 1)
                    {
                        slug = slug.substring(0, slug.length()-2);
                    }
                    slug += "-"+i;
                }
                article.setSlug(slug);
            } while(count > 0);

            session.save(article);
            session.getTransaction().commit();
        } catch (Exception e)
        {
            if(session.getTransaction() != null)
            {
                session.getTransaction().rollback();
                throw e;
            }
        }

        return new ResponseEntity<>(HttpStatus.CREATED);
    }


    @ApiOperation(value = "Get an article", response = Article.class)
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Found"), @ApiResponse(code = 404, message = "Not found") })
    @RequestMapping(value = "/{slug}", method = RequestMethod.GET)
    public ResponseEntity<Article> getAnArticle(
            @ApiParam(value = "slug", required = true) @PathVariable("slug") String slug
    ) {

        User user = mipApplication.getUser();

        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Article article = null;
        try{
            session.beginTransaction();

            article = (Article) session
                    .createQuery("FROM Article WHERE slug= :slug")
                    .setString("slug", slug)
                    .uniqueResult();

            session.getTransaction().commit();

            if (!article.getStatus().equals("published") && !article.getCreatedBy().getUsername().equals(user.getUsername()))
            {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
            }
        } catch (Exception e)
        {
            if(session.getTransaction() != null)
            {
                session.getTransaction().rollback();
                throw e;
            }
        }

        return ResponseEntity.ok(article);
    }


    @ApiOperation(value = "Update an article", response = Void.class)
    @ApiResponses(value = { @ApiResponse(code = 204, message = "Article updated") })
    @RequestMapping(value = "/{slug}", method = RequestMethod.PUT)
    public ResponseEntity<Void> updateAnArticle(
            @ApiParam(value = "slug", required = true) @PathVariable("slug") String slug,
            @RequestBody @ApiParam(value = "Article to update", required = true) @Valid Article article
    ) {

        User user = mipApplication.getUser();

        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        try{
            session.beginTransaction();

            String author = (String) session
                    .createQuery("select U.username from User U, Article A where A.createdBy = U.username and A.slug = :slug")
                    .setString("slug", slug)
                    .uniqueResult();

            if(!user.getUsername().equals(author))
            {
                session.getTransaction().commit();
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }

            String oldTitle = (String) session
                    .createQuery("select title from Article where slug= :slug")
                    .setString("slug", slug)
                    .uniqueResult();

            String newTitle = article.getTitle();

            if(!newTitle.equals(oldTitle)) {
                Long count;
                int i = 0;
                do {
                    i++;
                    newTitle = article.getTitle();
                    count = (Long) session
                            .createQuery("select count(*) from Article where title= :title")
                            .setString("title", newTitle)
                            .uniqueResult();
                    if (count > 0 && !newTitle.equals(oldTitle)) {
                        if (i > 1) {
                            newTitle = newTitle.substring(0, newTitle.length() - 4);
                        }
                        article.setTitle(newTitle + " (" + i + ")");
                    }
                } while (count > 0 && !newTitle.equals(oldTitle));
            }

            session.update(article);
            session.getTransaction().commit();
        } catch (Exception e)
        {
            if(session.getTransaction() != null)
            {
                session.getTransaction().rollback();
                throw e;
            }
        }

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


    @ApiOperation(value = "Delete an article", response = Void.class)
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Article deleted") })
    @RequestMapping(value = "/{slug}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> deleteAnArticle(
            @ApiParam(value = "slug", required = true) @PathVariable("slug") String slug
    ) {

        // TODO : Implement delete method

        return new ResponseEntity<>(HttpStatus.OK);
    }

}
