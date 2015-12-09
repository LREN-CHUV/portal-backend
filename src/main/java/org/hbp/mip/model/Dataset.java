/**
 * Created by mirco on 04.12.15.
 */

package org.hbp.mip.model;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "dataset_mip")
public class Dataset {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String code;
    private Date date;
    @ElementCollection
    private List<String> header;
    //@ElementCollection
    //private List<Map<String, List<Object>>> data;

    public Dataset() {
    }

    public Long getId() {
        return id;
    }

    private void setId(Long id) {
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

    /*public List<Map<String, List<Object>>> getData() {
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
    }*/
}
