package org.hbp.mip.repositories;

import org.hbp.mip.model.Experiment;
import org.springframework.data.repository.Repository;

import java.util.UUID;

/**
 * Created by mirco on 11.07.16.
 */
public interface ExperimentRepository extends Repository<Experiment, UUID> {
}
