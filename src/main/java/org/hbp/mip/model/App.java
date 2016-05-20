/**
 * Created by mirco on 20.05.16.
 */

package org.hbp.mip.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;

import javax.persistence.*;

@Entity
@Table(name = "`app`")
@ApiModel
@JsonInclude(JsonInclude.Include.NON_NULL)
public class App {

    @Id
    @GeneratedValue
    private Integer id;
    private String name;
    private String description;
    private String author;
    private String email;
    private String category;
    private String link;
    private String image;
    private Integer ratings;
    private Integer ratings_count;

    public App() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Integer getRatings() {
        return ratings;
    }

    public void setRatings(Integer ratings) {
        this.ratings = ratings;
    }

    public Integer getRatings_count() {
        return ratings_count;
    }

    public void setRatings_count(Integer ratings_count) {
        this.ratings_count = ratings_count;
    }
}
