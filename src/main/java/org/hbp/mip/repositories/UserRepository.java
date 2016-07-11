package org.hbp.mip.repositories;

import org.hbp.mip.model.User;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by mirco on 11.07.16.
 */
public interface UserRepository extends CrudRepository<User, String> {

}
