/**
 * Created by mirco on 04.12.15.
 */

package org.hbp.mip.model;

import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;

@Entity
@Table(name = "variable_mip")
public class Variable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private Group group;
    private String code;
    private String label;
    private String type;
    private int length;
    private boolean isVariable;
    private boolean isGrouping;
    private boolean isCovariable;
    private boolean isFilter;
    @ManyToMany
    private List<Value> values;

    public Variable() {
    }

    public Long getId() {
        return id;
    }

    private void setId(Long id) {
        this.id = id;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public boolean isVariable() {
        return isVariable;
    }

    public void setIsVariable(boolean isVariable) {
        this.isVariable = isVariable;
    }

    public boolean isGrouping() {
        return isGrouping;
    }

    public void setIsGrouping(boolean isGrouping) {
        this.isGrouping = isGrouping;
    }

    public boolean isCovariable() {
        return isCovariable;
    }

    public void setIsCovariable(boolean isCovariable) {
        this.isCovariable = isCovariable;
    }

    public boolean isFilter() {
        return isFilter;
    }

    public void setIsFilter(boolean isFilter) {
        this.isFilter = isFilter;
    }

    public List<Value> getValues() {
        return values;
    }

    public void setValues(List<Value> values) {
        this.values = values;
    }

    public void addValue(Value value) {
        if(this.values == null) {
            this.values = new LinkedList<>();
        }
        this.values.add(value);
    }
}
