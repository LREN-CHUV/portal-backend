package org.hbp.mip.repositories;

import org.hbp.mip.model.Experiment;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

/**
 * Created by mirco on 11.07.16.
 */
public interface ExperimentRepository extends CrudRepository<Experiment, UUID> {
}
