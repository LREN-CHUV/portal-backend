/**
 * Created by mirco on 04.12.15.
 */

package org.hbp.mip.model;

import java.util.LinkedList;
import java.util.List;

public class Chart {
    private int id;
    private String chartType;
    private String xAxis;
    private List<ChartConfigSet> chartConfigSets;

    public Chart() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getChartType() {
        return chartType;
    }

    public void setChartType(String chartType) {
        this.chartType = chartType;
    }

    public String getxAxis() {
        return xAxis;
    }

    public void setxAxis(String xAxis) {
        this.xAxis = xAxis;
    }

    public List<ChartConfigSet> getChartConfigSets() {
        return chartConfigSets;
    }

    public void setChartConfigSets(List<ChartConfigSet> chartConfigSets) {
        this.chartConfigSets = chartConfigSets;
    }

    public void addChartConfigSet(ChartConfigSet chartConfigSet) {
        if(this.chartConfigSets == null) {
            this.chartConfigSets = new LinkedList<>();
        }
        this.chartConfigSets.add(chartConfigSet);
    }
}
