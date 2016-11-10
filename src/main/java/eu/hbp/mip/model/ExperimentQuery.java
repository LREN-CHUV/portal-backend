package eu.hbp.mip.model;

import java.util.List;

/**
 * Created by mirco on 09.11.16.
 */
public class ExperimentQuery {

    private String name;
    private String model;
    private List<ExperimentValidator> validations;
    private List<Algorithm> algorithms;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public List<ExperimentValidator> getValidations() {
        return validations;
    }

    public void setValidations(List<ExperimentValidator> validations) {
        this.validations = validations;
    }

    public List<Algorithm> getAlgorithms() {
        return algorithms;
    }

    public void setAlgorithms(List<Algorithm> algorithms) {
        this.algorithms = algorithms;
    }
}
