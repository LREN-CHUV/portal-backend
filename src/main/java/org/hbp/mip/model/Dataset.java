/**
 * Created by mirco on 04.12.15.
 */

package org.hbp.mip.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@Entity
@Table(name = "`dataset`")
@ApiModel(description = "")
@javax.annotation.Generated(value = "class io.swagger.codegen.languages.SpringMVCServerCodegen", date = "2016-01-06T09:32:22.266Z")
public class Dataset {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id = null;
    private String code = null;
    private Date date = null;
    @ElementCollection
    private List<String> header = new LinkedList<String>();

    public Dataset() {
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
     * Code
     **/
    @ApiModelProperty(value = "Code")
    @JsonProperty("code")
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    /**
     * Date
     **/
    @ApiModelProperty(value = "Date")
    @JsonProperty("date")
    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    /**
     * Header
     **/
    @ApiModelProperty(value = "Header")
    @JsonProperty("header")
    public List<String> getHeader() {
        return header;
    }

    public void setHeader(List<String> header) {
        this.header = header;
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class Dataset {\n");

        sb.append("  id: ").append(id).append("\n");
        sb.append("  code: ").append(code).append("\n");
        sb.append("  date: ").append(date).append("\n");
        sb.append("  header: ").append(header).append("\n");
        sb.append("}\n");
        return sb.toString();
    }

}
