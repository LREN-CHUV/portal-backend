/**
 * Created by mirco on 04.12.15.
 */

package org.hbp.mip.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;

@Entity
@Table(name = "`query`")
@ApiModel(description = "")
@javax.annotation.Generated(value = "class io.swagger.codegen.languages.SpringMVCServerCodegen", date = "2016-01-06T09:32:22.266Z")
@JsonIgnoreProperties(value = { "id" })
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class Query {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id = null;
    @ManyToMany
    private List<Variable> variables = new LinkedList<Variable>();
    @ManyToMany
    private List<Variable> covariables = new LinkedList<Variable>();
    @ManyToMany
    private List<Variable> grouping = new LinkedList<Variable>();
    @ManyToMany
    private List<Filter> filters = new LinkedList<Filter>();
    private String request = null;

    public Query() {
    }

    /**
     * Unique identifier
     **/
    @ApiModelProperty(value = "Unique identifier")
    @JsonProperty("id")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Variables
     **/
    @ApiModelProperty(value = "Variables")
    @JsonProperty("variables")
    public List<Variable> getVariables() {
        return variables;
    }

    public void setVariables(List<Variable> variables) {
        this.variables = variables;
    }

    /**
     * Covariables
     **/
    @ApiModelProperty(value = "Covariables")
    @JsonProperty("covariables")
    public List<Variable> getCovariables() {
        return covariables;
    }

    public void setCovariables(List<Variable> covariables) {
        this.covariables = covariables;
    }

    /**
     * Grouping
     **/
    @ApiModelProperty(value = "Grouping")
    @JsonProperty("grouping")
    public List<Variable> getGrouping() {
        return grouping;
    }

    public void setGrouping(List<Variable> grouping) {
        this.grouping = grouping;
    }

    /**
     * Filters
     **/
    @ApiModelProperty(value = "Filters")
    @JsonProperty("filters")
    public List<Filter> getFilters() {
        return filters;
    }

    public void setFilters(List<Filter> filters) {
        this.filters = filters;
    }

    /**
     * Request
     **/
    @ApiModelProperty(value = "Request")
    @JsonProperty("request")
    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class Query {\n");

        sb.append("  id: ").append(id).append("\n");
        sb.append("  variables: ").append(variables).append("\n");
        sb.append("  covariables: ").append(covariables).append("\n");
        sb.append("  grouping: ").append(grouping).append("\n");
        sb.append("  filters: ").append(filters).append("\n");
        sb.append("  request: ").append(request).append("\n");
        sb.append("}\n");
        return sb.toString();
    }

    public void addVariable(Variable variable) {
        this.variables.add(variable);
    }

    public void addCovariable(Variable covariable) {
        this.covariables.add(covariable);
    }

    public void addGroupingVariable(Variable groupingVariable) {
        this.grouping.add(groupingVariable);
    }

    public void addFilter(Filter filter) {
        this.filters.add(filter);
    }
}
