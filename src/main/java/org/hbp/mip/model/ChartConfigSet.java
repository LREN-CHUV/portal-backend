/**
 * Created by mirco on 04.12.15.
 */

package org.hbp.mip.model;

public class ChartConfigSet {
    private int id;
    private String code;
    private String label;
    private String color;

    public ChartConfigSet() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
