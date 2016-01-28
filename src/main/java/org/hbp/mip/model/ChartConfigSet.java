/**
 * Created by mirco on 04.12.15.
 */

package org.hbp.mip.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "`chart_config_set`")
@ApiModel(description = "")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ChartConfigSet {
    @Id
    private String code = null;
    private String label = null;
    private String color = null;

    public ChartConfigSet() {
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
     * Color
     **/
    @ApiModelProperty(value = "Color")
    @JsonProperty("color")
    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class ChartConfigSet {\n");

        sb.append("  code: ").append(code).append("\n");
        sb.append("  label: ").append(label).append("\n");
        sb.append("  color: ").append(color).append("\n");
        sb.append("}\n");
        return sb.toString();
    }
}
