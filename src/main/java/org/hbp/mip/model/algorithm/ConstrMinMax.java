package org.hbp.mip.model.algorithm;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;

/**
 * Created by mirco on 17.06.16.
 */

@ApiModel
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ConstrMinMax {

    @JsonProperty("min_count")
    private Integer minCount;

    @JsonProperty("max_count")
    private Integer maxCount;


    public Integer getMinCount() {
        return minCount;
    }

    public void setMinCount(Integer minCount) {
        this.minCount = minCount;
    }

    public Integer getMaxCount() {
        return maxCount;
    }

    public void setMaxCount(Integer maxCount) {
        this.maxCount = maxCount;
    }
}
