/**
 * Created by mirco on 04.12.15.
 */

package org.hbp.mip.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "`model`")
@ApiModel(description = "")
@javax.annotation.Generated(value = "class io.swagger.codegen.languages.SpringMVCServerCodegen", date = "2016-01-06T09:32:22.266Z")
public class Model {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id = null;
    private String title = null;
    private String slug = null;
    private String description = null;
    @ManyToOne
    private Query query = null;
    @ManyToOne
    private Dataset dataset = null;
    private Boolean valid = null;
    @ManyToOne
    private Chart chart = null;
    private Date createdAt = null;
    private Date updatedAt = null;
    @ManyToOne
    private User createdBy = null;
    @ManyToOne
    private User updatedBy = null;

    public Model() {
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
     * Title
     **/
    @ApiModelProperty(value = "Title")
    @JsonProperty("title")
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * slug
     **/
    @ApiModelProperty(value = "slug")
    @JsonProperty("slug")
    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    /**
     * Description
     **/
    @ApiModelProperty(value = "Description")
    @JsonProperty("description")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Query
     **/
    @ApiModelProperty(value = "Query")
    @JsonProperty("query")
    public Query getQuery() {
        return query;
    }

    public void setQuery(Query query) {
        this.query = query;
    }

    /**
     * Dataset
     **/
    @ApiModelProperty(value = "Dataset")
    @JsonProperty("dataset")
    public Dataset getDataset() {
        return dataset;
    }

    public void setDataset(Dataset dataset) {
        this.dataset = dataset;
    }

    /**
     * Is it valid ?
     **/
    @ApiModelProperty(value = "Is it valid ?")
    @JsonProperty("valid")
    public Boolean getValid() {
        return valid;
    }

    public void setValid(Boolean valid) {
        this.valid = valid;
    }

    /**
     * Chart
     **/
    @ApiModelProperty(value = "Chart")
    @JsonProperty("chart")
    public Chart getChart() {
        return chart;
    }

    public void setChart(Chart chart) {
        this.chart = chart;
    }

    /**
     * Creation date
     **/
    @ApiModelProperty(value = "Creation date")
    @JsonProperty("createdAt")
    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * Update date
     **/
    @ApiModelProperty(value = "Update date")
    @JsonProperty("updatedAt")
    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    /**
     * Author
     **/
    @ApiModelProperty(value = "Author")
    @JsonProperty("createdBy")
    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    /**
     * Updater
     **/
    @ApiModelProperty(value = "Updater")
    @JsonProperty("updatedBy")
    public User getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(User updatedBy) {
        this.updatedBy = updatedBy;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class Model {\n");

        sb.append("  id: ").append(id).append("\n");
        sb.append("  title: ").append(title).append("\n");
        sb.append("  slug: ").append(slug).append("\n");
        sb.append("  description: ").append(description).append("\n");
        sb.append("  query: ").append(query).append("\n");
        sb.append("  dataset: ").append(dataset).append("\n");
        sb.append("  valid: ").append(valid).append("\n");
        sb.append("  chart: ").append(chart).append("\n");
        sb.append("  createdAt: ").append(createdAt).append("\n");
        sb.append("  updatedAt: ").append(updatedAt).append("\n");
        sb.append("  createdBy: ").append(createdBy).append("\n");
        sb.append("  updatedBy: ").append(updatedBy).append("\n");
        sb.append("}\n");
        return sb.toString();
    }
}
