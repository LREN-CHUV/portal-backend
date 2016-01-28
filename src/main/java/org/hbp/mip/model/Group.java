/**
 * Created by mirco on 04.12.15.
 */

package org.hbp.mip.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;

@Entity
@Table(name = "`group`")
@ApiModel(description = "")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Group {
    @Id
    private String code = null;
    private String label = null;
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Group> groups = new LinkedList<Group>();

    public Group() {
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
     * Groups
     **/
    @ApiModelProperty(value = "Groups")
    @JsonProperty("groups")
    public List<Group> getGroups() {
        return groups;
    }

    public void setGroups(List<Group> groups) {
        this.groups = groups;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class Group {\n");

        sb.append("  code: ").append(code).append("\n");
        sb.append("  label: ").append(label).append("\n");
        sb.append("  groups: ").append(groups).append("\n");
        sb.append("}\n");
        return sb.toString();
    }

    public void addGroup(Group group) {
        this.groups.add(group);
    }

    public Group clone()
    {
        Group g = new Group();
        g.setCode(this.getCode());
        g.setLabel(this.getLabel());
        g.setGroups(this.getGroups());
        return g;
    }

}
