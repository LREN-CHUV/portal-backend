/**
 * Created by mirco on 20.05.16.
 */

package org.hbp.mip.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "`app`")
@ApiModel
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(value = { "votes" })
public class App {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;

    private String name;

    @Column(columnDefinition = "text")
    private String description;

    private String author;

    private String email;

    private String category;

    private String link;

    @Column(columnDefinition = "text")
    private String image;

    @OneToMany(mappedBy = "app", fetch = FetchType.EAGER)
    private Set<Vote> votes = new HashSet<>();


    public App() {
        /*
        *  Empty constructor is needed by Hibernate
        */
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


    public Set<Vote> getVotes() {
        return votes;
    }

    public void setVotes(Set<Vote> votes) {
        this.votes = votes;
    }


    public int getRatingCount() {
        return votes.size();
    }


    public long getRatingSum() {
        long sum = 0;
        for(Vote vote : votes) {
            sum += vote.getValue();
        }
        return sum;
    }
}
