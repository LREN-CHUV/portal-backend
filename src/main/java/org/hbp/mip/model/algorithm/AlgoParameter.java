package org.hbp.mip.model.algorithm;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;

/**
 * Created by mirco on 17.06.16.
 */

@ApiModel
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AlgoParameter {

    private String code;

    private String label;

    private Integer defaultValue;

    private String type;

    private ParamConstraint constraints;

    private String description;


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

    public Integer getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(Integer defaultValue) {
        this.defaultValue = defaultValue;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ParamConstraint getConstraints() {
        return constraints;
    }

    public void setConstraints(ParamConstraint constraints) {
        this.constraints = constraints;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
