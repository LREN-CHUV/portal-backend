package org.hbp.mip.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import org.hbp.mip.model.algorithm.Algorithm;

import java.util.List;
import java.util.Map;

/**
 * Created by mirco on 20.06.16.
 */

@ApiModel
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SimpleMiningQuery {

    private Algorithm algorithm;

    private List<Map<String,String>> variables;

    private List<Map<String,String>> covariables;

    private List<Map<String,String>> grouping;

    private List<Map<String,String>> filters;


    public Algorithm getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(Algorithm algorithm) {
        this.algorithm = algorithm;
    }

    public List<Map<String, String>> getVariables() {
        return variables;
    }

    public void setVariables(List<Map<String, String>> variables) {
        this.variables = variables;
    }

    public List<Map<String, String>> getCovariables() {
        return covariables;
    }

    public void setCovariables(List<Map<String, String>> covariables) {
        this.covariables = covariables;
    }

    public List<Map<String, String>> getGrouping() {
        return grouping;
    }

    public void setGrouping(List<Map<String, String>> grouping) {
        this.grouping = grouping;
    }

    public List<Map<String, String>> getFilters() {
        return filters;
    }

    public void setFilters(List<Map<String, String>> filters) {
        this.filters = filters;
    }
}
