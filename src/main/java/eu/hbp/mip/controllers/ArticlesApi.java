/**
 * Created by mirco on 04.12.15.
 */

package eu.hbp.mip.controllers;


import com.github.slugify.Slugify;
import eu.hbp.mip.model.Article;
import io.swagger.annotations.*;
import org.apache.log4j.Logger;
import eu.hbp.mip.configuration.SecurityConfiguration;
import eu.hbp.mip.model.User;
import eu.hbp.mip.repositories.ArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Scope;
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
@Scope("session")
public class ArticlesApi {

    private static final Logger LOGGER = Logger.getLogger(ArticlesApi.class);

    @Autowired
    private SecurityConfiguration securityConfiguration;

    @Autowired
    private ArticleRepository articleRepository;

    @ApiOperation(value = "Get articles", response = Article.class, responseContainer = "List")
    @Cacheable(value = "Articles", key = "(#own != null and #own).toString() + #status + #root.target")
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<Iterable> getArticles(
            @ApiParam(value = "Only ask own articles") @RequestParam(value = "own", required = false) Boolean own,
            @ApiParam(value = "Only ask results matching status", allowableValues = "draft, published") @RequestParam(value = "status", required = false) String status
    ) {
        LOGGER.info("Get articles");

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


    @ApiOperation(value = "Create an article")
    @ApiResponses(value = { @ApiResponse(code = 201, message = "Article created") })
    @CachePut(value = "Article", key = "#article.getSlug() + #root.target")
    @CacheEvict(value = "Articles", allEntries = true)
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Void> addAnArticle(
            @RequestBody @ApiParam(value = "Article to create", required = true) @Valid Article article
    ) {
        LOGGER.info("Create an article");

        User user = securityConfiguration.getUser();

        article.setCreatedAt(new Date());
        if ("published".equals(article.getStatus())) {
            article.setPublishedAt(new Date());
        }
        article.setCreatedBy(user);

        long count = 1;
        for(int i = 1; count > 0; i++)
        {
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
        }

        String slug;
        try {
            slug = new Slugify().slugify(article.getTitle());
        } catch (IOException e) {
            slug = "";
            LOGGER.trace(e);
        }

        boolean alreadyExists = true;
        for(int i = 1; alreadyExists; i++)
        {
            alreadyExists = articleRepository.exists(slug);
            if(alreadyExists)
            {
                if(i > 1)
                {
                    slug = slug.substring(0, slug.length()-2);
                }
                slug += "-"+i;
            }
            article.setSlug(slug);
        }
        articleRepository.save(article);

        LOGGER.info("Article saved");

        return new ResponseEntity<>(HttpStatus.CREATED);
    }


    @ApiOperation(value = "Get an article", response = Article.class)
    @Cacheable(value = "Article", key = "#slug + #root.target")
    @RequestMapping(value = "/{slug}", method = RequestMethod.GET)
    public ResponseEntity<Article> getAnArticle(
            @ApiParam(value = "slug", required = true) @PathVariable("slug") String slug
    ) {
        LOGGER.info("Get an article");

        User user = securityConfiguration.getUser();
        Article article;
        article = articleRepository.findOne(slug);

        if(article == null)
        {
            LOGGER.warn("Cannot find article : " + slug);
            return ResponseEntity.badRequest().body(null);
        }

        if (!"published".equals(article.getStatus()) && !article.getCreatedBy().getUsername().equals(user.getUsername()))
        {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        return ResponseEntity.ok(article);
    }


    @ApiOperation(value = "Update an article")
    @ApiResponses(value = { @ApiResponse(code = 204, message = "Article updated") })
    @CachePut(value = "Article", key = "#slug + #root.target")
    @CacheEvict(value = "Articles",allEntries = true)
    @RequestMapping(value = "/{slug}", method = RequestMethod.PUT)
    public ResponseEntity<Void> updateAnArticle(
            @ApiParam(value = "slug", required = true) @PathVariable("slug") String slug,
            @RequestBody @ApiParam(value = "Article to update", required = true) @Valid Article article
    ) {
        LOGGER.info("Update an article");

        User user = securityConfiguration.getUser();

        String author = articleRepository.findOne(slug).getCreatedBy().getUsername();

        if(!user.getUsername().equals(author))
        {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        String oldTitle = articleRepository.findOne(slug).getTitle();

        String newTitle = article.getTitle();

        if(!newTitle.equals(oldTitle)) {
            long count = 1;
            for(int i = 1; count > 0 && !newTitle.equals(oldTitle); i++)
            {
                newTitle = article.getTitle();
                count = articleRepository.countByTitle(newTitle);
                if (count > 0 && !newTitle.equals(oldTitle)) {
                    if (i > 1) {
                        newTitle = newTitle.substring(0, newTitle.length() - 4);
                    }
                    article.setTitle(newTitle + " (" + i + ")");
                }
            }
        }

        articleRepository.save(article);

        LOGGER.info("Article updated");

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
