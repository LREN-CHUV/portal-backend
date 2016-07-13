package org.hbp.mip.repositories;

import org.hbp.mip.model.Experiment;
import org.hbp.mip.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

/**
 * Created by mirco on 11.07.16.
 */

public interface ExperimentRepository extends CrudRepository<Experiment, UUID> {
    Iterable<Experiment> findByUser(User user);
    Iterable<Experiment> findShared(Boolean shared);
}
