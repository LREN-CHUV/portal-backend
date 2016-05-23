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
@Table(name = "`filter`")
@ApiModel
@JsonIgnoreProperties(value = { "id" })
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Filter {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id = null;

    @ManyToOne
    private Variable variable = null;

    private String operator = null;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "filter_values", joinColumns = @JoinColumn(name = "filter_id"))
    private List<String> values = new LinkedList<>();


    public Filter() {
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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


    public List<String> getValues() {
        return values;
    }

    public void setValues(List<String> values) {
        this.values = values;
    }

}
