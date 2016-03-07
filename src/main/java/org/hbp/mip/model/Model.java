/**
 * Created by mirco on 04.12.15.
 */

package org.hbp.mip.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "`model`")
@ApiModel
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


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public Query getQuery() {
        return query;
    }

    public void setQuery(Query query) {
        this.query = query;
    }


    public Dataset getDataset() {
        return dataset;
    }

    public void setDataset(Dataset dataset) {
        this.dataset = dataset;
    }


    public Boolean getValid() {
        return valid;
    }

    public void setValid(Boolean valid) {
        this.valid = valid;
    }


    public Config getConfig() {
        return config;
    }

    public void setConfig(Config config) {
        this.config = config;
    }


    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }


    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }


    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }


    public User getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(User updatedBy) {
        this.updatedBy = updatedBy;
    }


    public String getTextQuery() {
        return textQuery;
    }

    public void setTextQuery(String textQuery) {
    this.textQuery = textQuery;
    }

}
