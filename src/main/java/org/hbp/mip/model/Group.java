/**
 * Created by mirco on 04.12.15.
 */

package org.hbp.mip.model;

import java.util.LinkedList;
import java.util.List;

public class Group {
    private Long id;
    private String code;
    private String label;
    private List<Group> groups;

    public Group() {
    }

    public Long getId() {
        return id;
    }

    private void setId(Long id) {
        this.id = id;
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

    public List<Group> getGroups() {
        return groups;
    }

    public void setGroups(List<Group> groups) {
        this.groups = groups;
    }

    public void addGroup(Group group) {
        if(this.groups == null) {
            this.groups = new LinkedList<>();
        }
        this.groups.add(group);
    }
}
