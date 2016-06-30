/**
 * Created by mirco on 04.12.15.
 */

package org.hbp.mip.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;

import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;

@Entity
@Table(name = "`group`")
@ApiModel
@JsonIgnoreProperties(value = { "parent" })
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Group {

    @Id
    private String code = null;

    private String label = null;

    @ManyToOne(fetch = FetchType.EAGER)
    private Group parent = null;

    @OneToMany(fetch = FetchType.EAGER)
    private List<Group> groups = new LinkedList<>();


    public Group() {
        /*
        *  Empty constructor is needed by Hibernate
        */
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


    public Group getParent() {
        return parent;
    }

    public void setParent(Group parent) {
        this.parent = parent;
    }


    public List<Group> getGroups() {
        return groups;
    }

    public void setGroups(List<Group> groups) {
        this.groups = groups;
    }

}
