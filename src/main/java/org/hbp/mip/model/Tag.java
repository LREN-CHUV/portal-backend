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

@Entity
@Table(name = "`tag`")
@ApiModel(description = "")
@javax.annotation.Generated(value = "class io.swagger.codegen.languages.SpringMVCServerCodegen", date = "2016-01-06T09:32:22.266Z")
@JsonIgnoreProperties(value = { "id" })
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class Tag {
    @Id
    private String name = null;

    public Tag() {
    }

    /**
     * Unique identifier
     **/
    /*@ApiModelProperty(value = "Unique identifier")
    @JsonProperty("id")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }*/

    /**
     * Name
     **/
    @ApiModelProperty(value = "Name")
    @JsonProperty("name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class Tag {\n");

        //sb.append("  id: ").append(id).append("\n");
        sb.append("  name: ").append(name).append("\n");
        sb.append("}\n");
        return sb.toString();
    }
}
