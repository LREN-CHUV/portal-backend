package org.hbp.mip.repositories;

import org.hbp.mip.model.App;
import org.hbp.mip.model.User;
import org.hbp.mip.model.Vote;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by mirco on 11.07.16.
 */

public interface VoteRepository extends CrudRepository<Vote, Long> {
    Iterable<Vote> find(User user, App app);
}
