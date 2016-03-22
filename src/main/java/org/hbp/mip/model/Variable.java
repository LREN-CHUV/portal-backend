/**
 * Created by mirco on 04.12.15.
 */

package org.hbp.mip.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;

import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;

@Entity
@Table(name = "`variable`")
@ApiModel
@JsonIgnoreProperties(value = { "queries" })
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Variable {

    @Id
    private String code = null;

    private String label = null;

    private String type = null;

    private Integer length = null;

    private Double minValue = null;

    private Double maxValue = null;

    private String units = null;

    private Boolean isVariable = null;

    private Boolean isGrouping = null;

    private Boolean isCovariable = null;

    private Boolean isFilter = null;

    @ManyToOne(fetch = FetchType.EAGER)
    private Group group = null;

    @ManyToMany(fetch = FetchType.EAGER)
    private List<Value> values = new LinkedList<>();

    @ManyToMany(fetch = FetchType.EAGER, mappedBy = "variables")
    private List<Query> queries = new LinkedList<>();

    @Column(columnDefinition = "text")
    private String description = null;


    public Variable() {
    }


    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }


    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }


    @JsonProperty("isVariable")
    public Boolean getIsVariable() {
        return isVariable;
    }

    public void setIsVariable(Boolean isVariable) {
        this.isVariable = isVariable;
    }


    @JsonProperty("isGrouping")
    public Boolean getIsGrouping() {
        return isGrouping;
    }

    public void setIsGrouping(Boolean isGrouping) {
        this.isGrouping = isGrouping;
    }


    @JsonProperty("isCovariable")
    public Boolean getIsCovariable() {
        return isCovariable;
    }

    public void setIsCovariable(Boolean isCovariable) {
        this.isCovariable = isCovariable;
    }


    @JsonProperty("isFilter")
    public Boolean getIsFilter() {
        return isFilter;
    }

    public void setIsFilter(Boolean isFilter) {
        this.isFilter = isFilter;
    }


    public List<Value> getValues() {
        return values;
    }

    public void setValues(List<Value> values) {
        this.values = values;
    }


    public Double getMinValue() {
        return minValue;
    }

    public void setMinValue(Double minValue) {
        this.minValue = minValue;
    }


    public Double getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(Double maxValue) {
        this.maxValue = maxValue;
    }


    public String getUnits() {
        return units;
    }

    public void setUnits(String units) {
        this.units = units;
    }


    public List<Query> getQueries() {
        return queries;
    }

    public void setQueries(List<Query> queries) {
        this.queries = queries;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
