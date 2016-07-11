package org.hbp.mip.repositories;

import org.hbp.mip.model.App;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by mirco on 11.07.16.
 */
public interface AppRepository extends CrudRepository<App, String> {
}
