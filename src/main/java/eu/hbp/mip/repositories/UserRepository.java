package eu.hbp.mip.repositories;

import eu.hbp.mip.model.User;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by mirco on 11.07.16.
 */

public interface UserRepository extends CrudRepository<User, String> {

}
