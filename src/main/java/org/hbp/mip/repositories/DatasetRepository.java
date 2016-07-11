package org.hbp.mip.repositories;

import org.hbp.mip.model.Dataset;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by mirco on 11.07.16.
 */

public interface DatasetRepository extends CrudRepository<Dataset, String> {
}
