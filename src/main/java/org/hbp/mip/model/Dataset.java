/**
 * Created by mirco on 04.12.15.
 */

package org.hbp.mip.model;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Dataset {
    private int id;
    private String code;
    private Date date;
    private List<String> header;
    private List<Map<String, List<Object>>> data;

    public Dataset() {
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public List<String> getHeader() {
        return header;
    }

    public void setHeader(List<String> header) {
        this.header = header;
    }

    public List<Map<String, List<Object>>> getData() {
        return data;
    }

    public void setData(List<Map<String, List<Object>>> data) {
        this.data = data;
    }

    public void addHeaderValue(String value) {
        if(this.header == null) {
            this.header = new LinkedList<>();
        }
        this.header.add(value);
    }
    public void addDataMap(Map<String, List<Object>> map) {
        if(this.data == null) {
            this.data = new LinkedList<>();
        }
        this.data.add(map);
    }
}
