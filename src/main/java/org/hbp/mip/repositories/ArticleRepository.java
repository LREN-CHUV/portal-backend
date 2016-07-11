package org.hbp.mip.repositories;

import org.hbp.mip.model.Article;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by mirco on 11.07.16.
 */
public interface ArticleRepository extends CrudRepository<Article, String> {
    Long countByTitle(String title);
}
