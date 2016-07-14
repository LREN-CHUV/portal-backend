package org.hbp.mip.repositories;

import org.hbp.mip.model.Model;
import org.hbp.mip.model.User;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by mirco on 11.07.16.
 */

public interface ModelRepository extends CrudRepository<Model, String> {
    Long countByTitle(String Title);
    Iterable<Model> findByCreatedByOrderByCreatedAt(User user);
    Iterable<Model> findByValidOrCreatedByOrderByCreatedAt(Boolean valid, User user);
}
