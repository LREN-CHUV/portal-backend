/**
 * Created by mirco on 04.12.15.
 */

package org.hbp.mip.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;

@Entity
@Table(name = "`chart`")
@ApiModel(description = "")
@javax.annotation.Generated(value = "class io.swagger.codegen.languages.SpringMVCServerCodegen", date = "2016-01-06T09:32:22.266Z")
@JsonIgnoreProperties(value = { "id" })
public class Chart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id = null;
    private String chartType = null;
    private String xAxis = null;
    @ManyToMany
    private List<ChartConfigSet> chartConfigSets = new LinkedList<ChartConfigSet>();

    public Chart() {
    }

    /**
     * Unique identifier
     **/
    @ApiModelProperty(value = "Unique identifier")
    @JsonProperty("id")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Chart type
     **/
    @ApiModelProperty(value = "Chart type")
    @JsonProperty("chartType")
    public String getChartType() {
        return chartType;
    }

    public void setChartType(String chartType) {
        this.chartType = chartType;
    }

    /**
     * X axis label
     **/
    @ApiModelProperty(value = "X axis label")
    @JsonProperty("xAxis")
    public String getXAxis() {
        return xAxis;
    }

    public void setXAxis(String xAxis) {
        this.xAxis = xAxis;
    }

    /**
     * Chart configuration
     **/
    @ApiModelProperty(value = "Chart configuration")
    @JsonProperty("chartConfigSets")
    public List<ChartConfigSet> getChartConfigSets() {
        return chartConfigSets;
    }

    public void setChartConfigSets(List<ChartConfigSet> chartConfigSets) {
        this.chartConfigSets = chartConfigSets;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class Chart {\n");

        sb.append("  id: ").append(id).append("\n");
        sb.append("  chartType: ").append(chartType).append("\n");
        sb.append("  xAxis: ").append(xAxis).append("\n");
        sb.append("  chartConfigSets: ").append(chartConfigSets).append("\n");
        sb.append("}\n");
        return sb.toString();
    }

    public void addChartConfigSet(ChartConfigSet chartConfigSet) {
        this.chartConfigSets.add(chartConfigSet);
    }
}
