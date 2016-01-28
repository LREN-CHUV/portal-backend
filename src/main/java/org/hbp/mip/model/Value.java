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
@Table(name = "`value`")
@ApiModel(description = "")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Value {

    @Id
    private String code = null;
    private String label = null;


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


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class Value {\n");

        sb.append("  code: ").append(code).append("\n");
        sb.append("  label: ").append(label).append("\n");
        sb.append("}\n");
        return sb.toString();
    }
}
