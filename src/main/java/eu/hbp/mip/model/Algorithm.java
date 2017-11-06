package eu.hbp.mip.model;

/**
 * Created by mirco on 09.11.16.
 */

public class Algorithm extends ExperimentValidator {

    private boolean validation;

    public Algorithm() {
    }

    public Algorithm(String code, String name, boolean validation) {
        this.validation = validation;
        setCode(code);
        setName(name);
    }

    public boolean isValidation() {
        return validation;
    }

    public void setValidation(boolean validation) {
        this.validation = validation;
    }

}
