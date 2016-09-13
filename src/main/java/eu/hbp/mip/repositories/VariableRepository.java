package eu.hbp.mip.repositories;

import eu.hbp.mip.model.Variable;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by mirco on 13.09.16.
 */
public interface VariableRepository extends CrudRepository<Variable, String> {
}
