package eu.hbp.mip.repositories;

import eu.hbp.mip.model.Tag;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by mirco on 11.07.16.
 */

public interface TagRepository extends CrudRepository<Tag, String> {
}
