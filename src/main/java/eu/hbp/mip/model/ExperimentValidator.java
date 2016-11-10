package eu.hbp.mip.model;

import java.util.List;

/**
 * Created by mirco on 09.11.16.
 */

public class ExperimentValidator {

    private String code;
    private String name;

    private List<AlgorithmParam> parameters;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<AlgorithmParam> getParameters() {
        return parameters;
    }

    public void setParameters(List<AlgorithmParam> parameters) {
        this.parameters = parameters;
    }

}
