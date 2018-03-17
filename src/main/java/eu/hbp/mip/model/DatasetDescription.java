package eu.hbp.mip.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;

@ApiModel
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DatasetDescription {

    private String code;
    private String label;
    private String description;
    private String anonymisationLevel;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAnonymisationLevel() {
        return anonymisationLevel;
    }

    public void setAnonymisationLevel(String anonymisationLevel) {
        this.anonymisationLevel = anonymisationLevel;
    }
}
