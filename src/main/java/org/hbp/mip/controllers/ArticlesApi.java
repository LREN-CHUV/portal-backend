/**
 * Created by mirco on 04.12.15.
 */

package org.hbp.mip.controllers;


import com.github.slugify.Slugify;
import io.swagger.annotations.*;
import org.apache.log4j.Logger;
import org.hbp.mip.configuration.SecurityConfiguration;
import org.hbp.mip.model.Article;
import org.hbp.mip.model.User;
import org.hbp.mip.repositories.ArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.Date;
import java.util.Iterator;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/articles", produces = {APPLICATION_JSON_VALUE})
@Api(value = "/articles", description = "the articles API")
public class ArticlesApi {

    private static final Logger LOGGER = Logger.getLogger(ArticlesApi.class);

    @Autowired
    SecurityConfiguration securityConfiguration;

    @Autowired
    ArticleRepository articleRepository;

    @ApiOperation(value = "Get articles", response = Article.class, responseContainer = "List")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Success") })
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<Iterable> getArticles(
            @ApiParam(value = "Only ask own articles") @RequestParam(value = "own", required = false) Boolean own,
            @ApiParam(value = "Only ask results matching status", allowableValues = "{values=[draft, published, closed]}") @RequestParam(value = "status", required = false) String status,
            @ApiParam(value = "Only ask articles from own team") @RequestParam(value = "team", required = false) Boolean team
    ) {

        User user = securityConfiguration.getUser();
        Iterable<Article> articles;

        if(own != null && own)
        {
             articles = articleRepository.findByCreatedBy(user);
        }
        else
        {
            articles = articleRepository.findByStatusOrCreatedBy("published", user);
        }

        if(status != null)
        {
            for(Iterator<Article> i = articles.iterator(); i.hasNext();)
            {
                Article a = i.next();
                if(!status.equals(a.getStatus()))
                {
                    i.remove();
                }
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

        User user = securityConfiguration.getUser();

        article.setCreatedAt(new Date());
        if ("published".equals(article.getStatus())) {
            article.setPublishedAt(new Date());
        }
        article.setCreatedBy(user);

        Long count;
        int i = 0;
        do{
            i++;
            count = articleRepository.countByTitle(article.getTitle());

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

        String slug;
        try {
            slug = new Slugify().slugify(article.getTitle());
        } catch (IOException e) {
            slug = "";
            LOGGER.trace(e);
        }

        i = 0;
        do {
            i++;
            if(articleRepository.exists(slug))
            {
                if(i > 1)
                {
                    slug = slug.substring(0, slug.length()-2);
                }
                slug += "-"+i;
            }
            article.setSlug(slug);
        } while(count > 0);
        articleRepository.save(article);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }


    @ApiOperation(value = "Get an article", response = Article.class)
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Found"), @ApiResponse(code = 404, message = "Not found") })
    @RequestMapping(value = "/{slug}", method = RequestMethod.GET)
    public ResponseEntity<Article> getAnArticle(
            @ApiParam(value = "slug", required = true) @PathVariable("slug") String slug
    ) {

        User user = securityConfiguration.getUser();
        Article article;
        article = articleRepository.findOne(slug);
        if (!"published".equals(article.getStatus()) && !article.getCreatedBy().getUsername().equals(user.getUsername()))
        {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
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

        User user = securityConfiguration.getUser();

        String author = articleRepository.findOne(slug).getCreatedBy().getUsername();

        if(!user.getUsername().equals(author))
        {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        String oldTitle = articleRepository.findOne(slug).getTitle();

        String newTitle = article.getTitle();

        if(!newTitle.equals(oldTitle)) {
            Long count;
            int i = 0;
            do {
                i++;
                newTitle = article.getTitle();
                count = articleRepository.countByTitle(newTitle);
                if (count > 0 && !newTitle.equals(oldTitle)) {
                    if (i > 1) {
                        newTitle = newTitle.substring(0, newTitle.length() - 4);
                    }
                    article.setTitle(newTitle + " (" + i + ")");
                }
            } while (count > 0 && !newTitle.equals(oldTitle));
        }

        articleRepository.save(article);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
