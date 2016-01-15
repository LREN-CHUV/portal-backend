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
@Table(name = "`variable`")
@ApiModel(description = "")
@javax.annotation.Generated(value = "class io.swagger.codegen.languages.SpringMVCServerCodegen", date = "2016-01-06T09:32:22.266Z")
@JsonIgnoreProperties(value = { "id", "idxPathGrp" })
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class Variable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id = null;
    @Column(unique = true)
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
    private List<Value> values = new LinkedList<Value>();
    private Integer idxPathGrp = null;  // Trick attribut used to know which of group.getGroups() is the path group

    public Variable() {
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
     * Group
     **/
    @ApiModelProperty(value = "Group")
    @JsonProperty("group")
    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    /**
     * Code
     **/
    @ApiModelProperty(value = "Code")
    @JsonProperty("code")
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    /**
     * Label
     **/
    @ApiModelProperty(value = "Label")
    @JsonProperty("label")
    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    /**
     * Type
     **/
    @ApiModelProperty(value = "Type")
    @JsonProperty("type")
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    /**
     * Length
     **/
    @ApiModelProperty(value = "Length")
    @JsonProperty("length")
    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    /**
     * Is it a covariable ?
     **/
    @ApiModelProperty(value = "Is it a covariable ?")
    @JsonProperty("isVariable")
    public Boolean getIsVariable() {
        return isVariable;
    }

    public void setIsVariable(Boolean isVariable) {
        this.isVariable = isVariable;
    }

    /**
     * Is it a grouping variable ?
     **/
    @ApiModelProperty(value = "Is it a grouping variable ?")
    @JsonProperty("isGrouping")
    public Boolean getIsGrouping() {
        return isGrouping;
    }

    public void setIsGrouping(Boolean isGrouping) {
        this.isGrouping = isGrouping;
    }

    /**
     * Is it a co-variable ?
     **/
    @ApiModelProperty(value = "Is it a co-variable ?")
    @JsonProperty("isCovariable")
    public Boolean getIsCovariable() {
        return isCovariable;
    }

    public void setIsCovariable(Boolean isCovariable) {
        this.isCovariable = isCovariable;
    }

    /**
     * Is it a filter ?
     **/
    @ApiModelProperty(value = "Is it a filter ?")
    @JsonProperty("isFilter")
    public Boolean getIsFilter() {
        return isFilter;
    }

    public void setIsFilter(Boolean isFilter) {
        this.isFilter = isFilter;
    }

    /**
     **/
    @ApiModelProperty(value = "")
    @JsonProperty("values")
    public List<Value> getValues() {
        return values;
    }

    public void setValues(List<Value> values) {
        this.values = values;
    }

    /**
     * Minimum value (only for numbers)
     **/
    @ApiModelProperty(value = "Minimum value (only for numbers)")
    @JsonProperty("minValue")
    public Double getMinValue() {
        return minValue;
    }

    public void setMinValue(Double minValue) {
        this.minValue = minValue;
    }

    /**
     * Maximum value (only for numbers)
     **/
    @ApiModelProperty(value = "Maximum value (only for numbers)")
    @JsonProperty("maxValue")
    public Double getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(Double maxValue) {
        this.maxValue = maxValue;
    }

    /**
     * Units
     **/
    @ApiModelProperty(value = "Units")
    @JsonProperty("units")
    public String getUnits() {
        return units;
    }

    public void setUnits(String units) {
        this.units = units;
    }

    /**
     * Subgroup index for variable path
     **/
    @ApiModelProperty(value = "Subgroup index for variable path")
    @JsonProperty("idxPathGrp")
    public Integer getIdxPathGrp() {
        return idxPathGrp;
    }

    public void setIdxPathGrp(Integer idxPathGrp) {
        this.idxPathGrp = idxPathGrp;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class Variable {\n");

        sb.append("  id: ").append(id).append("\n");
        sb.append("  group: ").append(group).append("\n");
        sb.append("  code: ").append(code).append("\n");
        sb.append("  label: ").append(label).append("\n");
        sb.append("  type: ").append(type).append("\n");
        sb.append("  length: ").append(length).append("\n");
        sb.append("  isVariable: ").append(isVariable).append("\n");
        sb.append("  isGrouping: ").append(isGrouping).append("\n");
        sb.append("  isGrouping: ").append(isCovariable).append("\n");
        sb.append("  isFilter: ").append(isFilter).append("\n");
        sb.append("  values: ").append(values).append("\n");
        sb.append("}\n");
        return sb.toString();
    }

    public void addValue(Value value) {
        this.values.add(value);
    }
}
