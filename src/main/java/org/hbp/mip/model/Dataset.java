/**
 * Created by mirco on 04.12.15.
 */

package org.hbp.mip.model;

import com.fasterxml.jackson.annotation.JsonInclude;
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
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Dataset {
    @Id
    private String code = null;
    private Date date = null;
    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> header = new LinkedList<String>();

    public Dataset() {
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

        sb.append("  code: ").append(code).append("\n");
        sb.append("  date: ").append(date).append("\n");
        sb.append("  header: ").append(header).append("\n");
        sb.append("}\n");
        return sb.toString();
    }

}
