/**
 * Created by mirco on 04.12.15.
 */

package org.hbp.mip.model;

import javax.persistence.*;

@Entity
@Table(name = "filter_mip")
public class Filter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private Variable variable;
    private String operator;

    public Filter() {
    }

    public Long getId() {
        return id;
    }

    private void setId(Long id) {
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
