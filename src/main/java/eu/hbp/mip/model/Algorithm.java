package eu.hbp.mip.model;

import java.util.LinkedList;

/**
 * Created by mirco on 09.11.16.
 */

public class Algorithm extends ExperimentValidator {

    private boolean validation;

    public Algorithm(String code, String name, boolean validation) {
        this.validation = validation;
        setCode(code);
        setName(name);
        setParameters(new LinkedList<>());
    }

    public boolean isValidation() {
        return validation;
    }

    public void setValidation(boolean validation) {
        this.validation = validation;
    }

}
