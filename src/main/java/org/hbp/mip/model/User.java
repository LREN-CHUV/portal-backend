/**
 * Created by mirco on 04.12.15.
 */

package org.hbp.mip.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Entity
@Table(name = "`user`")
@ApiModel(description = "")
@javax.annotation.Generated(value = "class io.swagger.codegen.languages.SpringMVCServerCodegen", date = "2016-01-06T09:32:22.266Z")
@JsonIgnoreProperties(value = { "id" })
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id = null;
    private String fullname = null;
    private String username = null;
    private String firstname = null;
    private String lastname = null;
    private String picture = null;
    private String web = null;
    private String phone = null;
    private String birthday = null;
    private String gender = null;
    private String city = null;
    private String country = null;
    private String password = null;
    private String email = null;
    private String apikey = null;
    private String team = null;
    private Boolean isActive = null;
    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> languages = new LinkedList<String>();
    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> roles = new LinkedList<String>();

    public User() {
    }

    public User(String userInfo) {
        Pattern p;
        Matcher m;

        p = Pattern.compile("username=([\\w ]+)");
        m = p.matcher(userInfo);
        if (m.find()) {
            System.out.println(m.group(1));
            this.username = m.group(1);
        }

        p = Pattern.compile("displayName=([\\w ]+)");
        m = p.matcher(userInfo);
        if (m.find()) {
            System.out.println(m.group(1));
            this.fullname = m.group(1);
        }

        p = Pattern.compile("givenName=([\\w ]+)");
        m = p.matcher(userInfo);
        if (m.find()) {
            System.out.println(m.group(1));
            this.firstname = m.group(1);
        }

        p = Pattern.compile("familyName=([\\w ]+)");
        m = p.matcher(userInfo);
        if (m.find()) {
            System.out.println(m.group(1));
            this.lastname = m.group(1);
        }

        p = Pattern.compile("value=([\\w.]+@[\\w.]+)");
        m = p.matcher(userInfo);
        if (m.find()) {
            System.out.println(m.group(1));
            this.email = m.group(1);
        }

        p = Pattern.compile("title=([\\w ]+)");
        m = p.matcher(userInfo);
        if (m.find()) {
            System.out.println(m.group(1));
            if (m.group(1).equals("Mr")) {
                this.gender = "Male";
                System.out.println("->Male");
            } else {
                this.gender = "Female";
                System.out.println("->Female");
            }
        }

        if (this.picture == null || this.picture.isEmpty()) {
            this.picture = "images/users/default_user.png";
        }

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
     * Fullname
     **/
    @ApiModelProperty(value = "Fullname")
    @JsonProperty("fullname")
    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    /**
     * Username
     **/
    @ApiModelProperty(value = "Username")
    @JsonProperty("username")
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Firstname
     **/
    @ApiModelProperty(value = "Firstname")
    @JsonProperty("firstname")
    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    /**
     * Lastname
     **/
    @ApiModelProperty(value = "Lastname")
    @JsonProperty("lastname")
    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    /**
     * Path to a profile picture
     **/
    @ApiModelProperty(value = "Path to a profile picture")
    @JsonProperty("picture")
    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    /**
     * Personnal web site URL
     **/
    @ApiModelProperty(value = "Personnal web site URL")
    @JsonProperty("web")
    public String getWeb() {
        return web;
    }

    public void setWeb(String web) {
        this.web = web;
    }

    /**
     * Phone number
     **/
    @ApiModelProperty(value = "Phone number")
    @JsonProperty("phone")
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * Birthday date
     **/
    @ApiModelProperty(value = "Birthday date")
    @JsonProperty("birthday")
    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    /**
     * Gender
     **/
    @ApiModelProperty(value = "Gender")
    @JsonProperty("gender")
    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    /**
     * City
     **/
    @ApiModelProperty(value = "City")
    @JsonProperty("city")
    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    /**
     * Country
     **/
    @ApiModelProperty(value = "Country")
    @JsonProperty("country")
    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    /**
     * password
     **/
    @ApiModelProperty(value = "password")
    @JsonProperty("password")
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * E-mail address
     **/
    @ApiModelProperty(value = "E-mail address")
    @JsonProperty("email")
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * API key
     **/
    @ApiModelProperty(value = "API key")
    @JsonProperty("apikey")
    public String getApikey() {
        return apikey;
    }

    public void setApikey(String apikey) {
        this.apikey = apikey;
    }

    /**
     * Team
     **/
    @ApiModelProperty(value = "Team")
    @JsonProperty("team")
    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    /**
     * Is it active ?
     **/
    @ApiModelProperty(value = "Is it active ?")
    @JsonProperty("isActive")
    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    /**
     * Languages
     **/
    @ApiModelProperty(value = "Languages")
    @JsonProperty("languages")
    public List<String> getLanguages() {
        return languages;
    }

    public void setLanguages(List<String> languages) {
        this.languages = languages;
    }

    /**
     * Roles
     **/
    @ApiModelProperty(value = "Roles")
    @JsonProperty("roles")
    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class User {\n");

        sb.append("  id: ").append(id).append("\n");
        sb.append("  fullname: ").append(fullname).append("\n");
        sb.append("  username: ").append(username).append("\n");
        sb.append("  firstname: ").append(firstname).append("\n");
        sb.append("  lastname: ").append(lastname).append("\n");
        sb.append("  picture: ").append(picture).append("\n");
        sb.append("  web: ").append(web).append("\n");
        sb.append("  phone: ").append(phone).append("\n");
        sb.append("  birthday: ").append(birthday).append("\n");
        sb.append("  gender: ").append(gender).append("\n");
        sb.append("  city: ").append(city).append("\n");
        sb.append("  country: ").append(country).append("\n");
        sb.append("  password: ").append(password).append("\n");
        sb.append("  email: ").append(email).append("\n");
        sb.append("  apikey: ").append(apikey).append("\n");
        sb.append("  team: ").append(team).append("\n");
        sb.append("  isActive: ").append(isActive).append("\n");
        sb.append("  languages: ").append(languages).append("\n");
        sb.append("  roles: ").append(roles).append("\n");
        sb.append("}\n");
        return sb.toString();
    }

    public void addLanguage(String language) {
        this.languages.add(language);
    }

    public void addRole(String role) {
        this.roles.add(role);
    }
}
