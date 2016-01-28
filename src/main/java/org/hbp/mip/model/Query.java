/**
 * Created by mirco on 04.12.15.
 */

package org.hbp.mip.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;

@Entity
@Table(name = "`query`")
@ApiModel(description = "")
@JsonIgnoreProperties(value = { "id" })
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Query {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id = null;
    private String request = null;
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "query_variable", joinColumns = {
            @JoinColumn(name = "id", nullable = false, updatable = false) },
            inverseJoinColumns = { @JoinColumn(name = "code",
                    nullable = false, updatable = false) })
    private List<Variable> variables = new LinkedList<Variable>();
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "query_covariable", joinColumns = {
            @JoinColumn(name = "id", nullable = false, updatable = false) },
            inverseJoinColumns = { @JoinColumn(name = "code",
                    nullable = false, updatable = false) })
    private List<Variable> covariables = new LinkedList<Variable>();
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "query_grouping", joinColumns = {
            @JoinColumn(name = "id", nullable = false, updatable = false) },
            inverseJoinColumns = { @JoinColumn(name = "code",
                    nullable = false, updatable = false) })
    private List<Variable> grouping = new LinkedList<Variable>();
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Filter> filters = new LinkedList<Filter>();

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
    @JsonProperty("coVariables")
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
    @JsonProperty("groupings")
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
