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

    private Boolean binomial;

    private Boolean polynomial;


    public Boolean getReal() {
        return real;
    }

    public void setReal(Boolean real) {
        this.real = real;
    }

    public Boolean getBinomial() {
        return binomial;
    }

    public void setBinomial(Boolean binomial) {
        this.binomial = binomial;
    }

    public Boolean getPolynomial() {
        return polynomial;
    }

    public void setPolynomial(Boolean polynomial) {
        this.polynomial = polynomial;
    }
}
