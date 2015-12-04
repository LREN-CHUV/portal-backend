/**
 * Created by mirco on 04.12.15.
 */

package org.hbp.mip.model;

public class Filter {
    private int id;
    private Variable variable;
    private String operator;

    public Filter() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Variable getVariable() {
        return variable;
    }

    public void setVariable(Variable variable) {
        this.variable = variable;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }
}
