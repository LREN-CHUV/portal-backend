package org.hbp.mip.model.algorithm;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;

/**
 * Created by mirco on 17.06.16.
 */

@ApiModel
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AlgoConstraint {

    private ConstrVariable variable;

    private ConstrMinMax groupings;

    private ConstrMinMax covariables;

    private Boolean mixed;


    public ConstrVariable getVariable() {
        return variable;
    }

    public void setVariable(ConstrVariable variable) {
        this.variable = variable;
    }

    public ConstrMinMax getGroupings() {
        return groupings;
    }

    public void setGroupings(ConstrMinMax groupings) {
        this.groupings = groupings;
    }

    public ConstrMinMax getCovariables() {
        return covariables;
    }

    public void setCovariables(ConstrMinMax covariables) {
        this.covariables = covariables;
    }

    public Boolean getMixed() {
        return mixed;
    }

    public void setMixed(Boolean mixed) {
        this.mixed = mixed;
    }
}
