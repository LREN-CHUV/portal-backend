package org.hbp.mip.model.algorithm;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;

/**
 * Created by mirco on 17.06.16.
 */

@ApiModel
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ConstrVariable {

    private Boolean real;

    private Boolean binominal;

    private Boolean polynominal;


    public Boolean getReal() {
        return real;
    }

    public void setReal(Boolean real) {
        this.real = real;
    }

    public Boolean getBinominal() {
        return binominal;
    }

    public void setBinominal(Boolean binominal) {
        this.binominal = binominal;
    }

    public Boolean getPolynominal() {
        return polynominal;
    }

    public void setPolynominal(Boolean polynominal) {
        this.polynominal = polynominal;
    }
}
