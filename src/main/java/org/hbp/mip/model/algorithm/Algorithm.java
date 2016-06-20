package org.hbp.mip.model.algorithm;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;

import java.util.List;

/**
 * Created by mirco on 17.06.16.
 */

@ApiModel
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Algorithm {

    private String code;

    private String label;

    private String name;

    private List type;

    private String environment;

    private String description;

    private List<AlgoParameter> parameters;

    @JsonProperty("docker_image")
    private String dockerImage;

    private AlgoConstraint constraints;

    private Boolean disable;

    private String source;


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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List getType() {
        return type;
    }

    public void setType(List type) {
        this.type = type;
    }

    public String getEnvironment() {
        return environment;
    }

    public void setEnvironment(String environment) {
        this.environment = environment;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<AlgoParameter> getParameters() {
        return parameters;
    }

    public void setParameters(List<AlgoParameter> parameters) {
        this.parameters = parameters;
    }

    public String getDockerImage() {
        return dockerImage;
    }

    public void setDockerImage(String dockerImage) {
        this.dockerImage = dockerImage;
    }

    public AlgoConstraint getConstraints() {
        return constraints;
    }

    public void setConstraints(AlgoConstraint constraints) {
        this.constraints = constraints;
    }

    public Boolean getDisable() {
        return disable;
    }

    public void setDisable(Boolean disable) {
        this.disable = disable;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}
