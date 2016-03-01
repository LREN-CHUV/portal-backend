/**
 * Created by mirco on 04.12.15.
 */

package org.hbp.mip.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "`model`")
@ApiModel(description = "")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Model {
    @Id
    private String slug = null;
    private String title = null;
    private String description = null;
    private Boolean valid = null;
    private Date createdAt = null;
    private Date updatedAt = null;
    @ManyToOne
    @Cascade(CascadeType.SAVE_UPDATE)
    private Query query = null;
    @ManyToOne
    @Cascade(CascadeType.SAVE_UPDATE)
    private Dataset dataset = null;
    @ManyToOne
    @Cascade(CascadeType.SAVE_UPDATE)
    private Config config = null;
    @ManyToOne
    private User createdBy = null;
    @ManyToOne
    private User updatedBy = null;
    private String textQuery = null;

    public Model() {
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

    @ApiModelProperty(value = "Config")
    @JsonProperty("config")
    public Config getConfig() {
        return config;
    }

    public void setConfig(Config config) {
        this.config = config;
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

    /**
    * Text query
    **/
    @ApiModelProperty(value = "Text query")
    @JsonProperty("textQuery")
    public String getTextQuery() {
        return textQuery;
    }

    public void setTextQuery(String textQuery) {
    this.textQuery = textQuery;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class Model {\n");

        sb.append("  title: ").append(title).append("\n");
        sb.append("  slug: ").append(slug).append("\n");
        sb.append("  description: ").append(description).append("\n");
        sb.append("  query: ").append(query).append("\n");
        sb.append("  valid: ").append(valid).append("\n");
        sb.append("  createdAt: ").append(createdAt).append("\n");
        sb.append("  updatedAt: ").append(updatedAt).append("\n");
        sb.append("  createdBy: ").append(createdBy).append("\n");
        sb.append("  updatedBy: ").append(updatedBy).append("\n");
        sb.append("  textQuery: ").append(textQuery).append("\n");
        sb.append("}\n");
        return sb.toString();
    }

}
