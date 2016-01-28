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
@Table(name = "`tag`")
@ApiModel(description = "")
@JsonInclude(JsonInclude.Include.NON_NULL)
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
