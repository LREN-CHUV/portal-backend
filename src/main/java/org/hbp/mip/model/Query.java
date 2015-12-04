/**
 * Created by mirco on 04.12.15.
 */

package org.hbp.mip.model;

import java.util.LinkedList;
import java.util.List;

public class Query {
    private int id;
    private List<Variable> variables;
    private List<Variable> covariables;
    private List<Variable> grouping;
    private List<Filter> filters;
    private String request;

    public Query() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Variable> getVariables() {
        return variables;
    }

    public void setVariables(List<Variable> variables) {
        this.variables = variables;
    }

    public List<Variable> getCovariables() {
        return covariables;
    }

    public void setCovariables(List<Variable> covariables) {
        this.covariables = covariables;
    }

    public List<Variable> getGrouping() {
        return grouping;
    }

    public void setGrouping(List<Variable> grouping) {
        this.grouping = grouping;
    }

    public List<Filter> getFilters() {
        return filters;
    }

    public void setFilters(List<Filter> filters) {
        this.filters = filters;
    }

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    public void addVariable(Variable variable) {
        if(this.variables == null) {
            this.variables = new LinkedList<>();
        }
        this.variables.add(variable);
    }

    public void addCovariable(Variable covariable) {
        if(this.covariables == null) {
            this.covariables = new LinkedList<>();
        }
        this.covariables.add(covariable);
    }

    public void addGroupingVariable(Variable groupingVariable) {
        if(this.grouping == null) {
            this.grouping = new LinkedList<>();
        }
        this.grouping.add(groupingVariable);
    }

    public void addFilter(Filter filter) {
        if(this.filters == null) {
            this.filters = new LinkedList<>();
        }
        this.filters.add(filter);
    }
}
