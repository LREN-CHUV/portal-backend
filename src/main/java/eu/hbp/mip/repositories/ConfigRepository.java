package eu.hbp.mip.repositories;

import eu.hbp.mip.model.Config;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by mirco on 11.07.16.
 */

public interface ConfigRepository extends CrudRepository<Config, Long> {
}
