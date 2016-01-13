/**
 * Created by mirco on 04.12.15.
 */

package org.hbp.mip.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@Entity
@Table(name = "`article`")
@ApiModel(description = "")
@javax.annotation.Generated(value = "class io.swagger.codegen.languages.SpringMVCServerCodegen", date = "2016-01-06T09:32:22.266Z")
@JsonIgnoreProperties(value = { "id" })
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id = null;
    private String status = null;
    @Column(unique = true)
    private String title = null;
    @Column(unique = true)
    private String slug = null;
    private String _abstract = null;
    private String content = null;
    private Date publishedAt = null;
    private Date createdAt = null;
    private Date updatedAt = null;
    @ManyToOne
    private User createdBy = null;
    @ManyToOne
    private User updatedBy = null;
    @ManyToMany(fetch = FetchType.EAGER)
    private List<Tag> tags = new LinkedList<Tag>();

    public Article() {
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
     * Status
     **/
    @ApiModelProperty(value = "Status")
    @JsonProperty("status")
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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
     * Short string identifier
     **/
    @ApiModelProperty(value = "Short string identifier")
    @JsonProperty("slug")
    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    /**
     * Short introduction
     **/
    @ApiModelProperty(value = "Short introduction")
    @JsonProperty("abstract")
    public String getAbstract() {
        return _abstract;
    }

    public void setAbstract(String _abstract) {
        this._abstract = _abstract;
    }

    /**
     * Content
     **/
    @ApiModelProperty(value = "Content")
    @JsonProperty("content")
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    /**
     * Publication date
     **/
    @ApiModelProperty(value = "Publication date")
    @JsonProperty("publishedAt")
    public Date getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(Date publishedAt) {
        this.publishedAt = publishedAt;
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
     **/
    @ApiModelProperty(value = "")
    @JsonProperty("tags")
    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class Article {\n");

        sb.append("  id: ").append(id).append("\n");
        sb.append("  status: ").append(status).append("\n");
        sb.append("  title: ").append(title).append("\n");
        sb.append("  slug: ").append(slug).append("\n");
        sb.append("  _abstract: ").append(_abstract).append("\n");
        sb.append("  content: ").append(content).append("\n");
        sb.append("  publishedAt: ").append(publishedAt).append("\n");
        sb.append("  createdAt: ").append(createdAt).append("\n");
        sb.append("  updatedAt: ").append(updatedAt).append("\n");
        sb.append("  createdBy: ").append(createdBy).append("\n");
        sb.append("  updatedBy: ").append(updatedBy).append("\n");
        sb.append("  tags: ").append(tags).append("\n");
        sb.append("}\n");
        return sb.toString();
    }

    public void addTag(Tag tag) {
        this.tags.add(tag);
    }
}
