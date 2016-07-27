package eu.hbp.mip.repositories;

import eu.hbp.mip.model.App;
import eu.hbp.mip.model.User;
import eu.hbp.mip.model.Vote;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by mirco on 11.07.16.
 */

public interface VoteRepository extends CrudRepository<Vote, Long> {
    Iterable<Vote> findByUserAndApp(User user, App app);
}
