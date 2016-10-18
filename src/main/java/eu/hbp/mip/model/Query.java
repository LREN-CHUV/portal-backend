/**
 * Created by mirco on 04.12.15.
 */

package eu.hbp.mip.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;

import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;

@Entity
@ApiModel
@Table(name = "`query`")
@JsonIgnoreProperties(value = { "id" })
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Query {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id = null;

    private String request = null;

    @ManyToMany
    @JoinTable(name = "query_variable", joinColumns = {
            @JoinColumn(name = "id", nullable = false, updatable = false) },
            inverseJoinColumns = { @JoinColumn(name = "code",
                    nullable = false, updatable = false) })
    private List<Variable> variables = new LinkedList<>();

    @ManyToMany
    @JoinTable(name = "query_covariable", joinColumns = {
            @JoinColumn(name = "id", nullable = false, updatable = false) },
            inverseJoinColumns = { @JoinColumn(name = "code",
                    nullable = false, updatable = false) })
    private List<Variable> covariables = new LinkedList<>();

    @ManyToMany
    @JoinTable(name = "query_grouping", joinColumns = {
            @JoinColumn(name = "id", nullable = false, updatable = false) },
            inverseJoinColumns = { @JoinColumn(name = "code",
                    nullable = false, updatable = false) })
    private List<Variable> grouping = new LinkedList<>();

    @ManyToMany
    @JoinTable(name = "query_filter", joinColumns = {
            @JoinColumn(name = "query_id", nullable = false, updatable = false) },
            inverseJoinColumns = { @JoinColumn(name = "code",
                    nullable = false, updatable = false) })
    private List<Variable> filters = new LinkedList<>();

    @Column(columnDefinition = "text", name = "sql_filter")
    private String sqlFilter = null;


    public Query() {
        /*
        *  Empty constructor is needed by Hibernate
        */
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public List<Variable> getVariables() {
        return variables;
    }

    public void setVariables(List<Variable> variables) {
        this.variables = variables;
    }


    @JsonProperty("coVariables")
    public List<Variable> getCovariables() {
        return covariables;
    }

    public void setCovariables(List<Variable> covariables) {
        this.covariables = covariables;
    }


    @JsonProperty("groupings")
    public List<Variable> getGrouping() {
        return grouping;
    }

    public void setGrouping(List<Variable> grouping) {
        this.grouping = grouping;
    }


    @JsonProperty("filter")
    public List<Variable> getFilters() {
        return filters;
    }

    public void setFilters(List<Variable> filters) {
        this.filters = filters;
    }

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }


    @JsonProperty("sql_filter")
    public String getSqlFilter() {
        return sqlFilter;
    }

    public void setSqlFilter(String sqlFilter) {
        this.sqlFilter = sqlFilter;
    }
}
