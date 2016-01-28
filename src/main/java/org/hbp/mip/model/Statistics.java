package org.hbp.mip.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Objects;


@ApiModel(description = "")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Statistics  {
  
  public enum DataTypeEnum {
     SummaryStatistics,  DatasetStatistics, 
  };
  private DataTypeEnum dataType = null;

  
  /**
   * Type of the data
   **/
  @ApiModelProperty(required = true, value = "Type of the data")
  @JsonProperty("dataType")
  public DataTypeEnum getDataType() {
    return dataType;
  }
  public void setDataType(DataTypeEnum dataType) {
    this.dataType = dataType;
  }

  

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Statistics statistics = (Statistics) o;
    return Objects.equals(dataType, statistics.dataType);
  }

  @Override
  public int hashCode() {
    return Objects.hash(dataType);
  }

  @Override
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class Statistics {\n");
    
    sb.append("  dataType: ").append(dataType).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}
