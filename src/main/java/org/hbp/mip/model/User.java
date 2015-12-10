/**
 * Created by mirco on 04.12.15.
 */

package org.hbp.mip.model;

import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Entity
@Table(name = "user_mip")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String fullname;
    private String username;
    private String firstname;
    private String lastname;
    private String picture;
    private String web;
    private String phone;
    private String birthday;
    private String gender;
    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> languages;
    private String city;
    private String country;
    private String password;
    private String email;
    private boolean isActive;
    private String apikey;
    private String team;
    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> roles;

    public User() {
    }

    public User(String userInfo)
    {
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
            if(m.group(1).equals("Mr"))
            {
                this.gender = "Male";
                System.out.println("->Male");
            }
            else
            {
                this.gender = "Female";
                System.out.println("->Female");
            }
        }

        if(this.picture == null || this.picture.isEmpty())
        {
            this.picture = "images/users/default_user.png";
        }

    }

    public Long getId() {
        return id;
    }

    private void setId(Long id) {
        this.id = id;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getWeb() {
        return web;
    }

    public void setWeb(String web) {
        this.web = web;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public List<String> getLanguages() {
        return languages;
    }

    public void setLanguages(List<String> languages) {
        this.languages = languages;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    public String getApikey() {
        return apikey;
    }

    public void setApikey(String apikey) {
        this.apikey = apikey;
    }

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public void addLanguage(String language) {
        if(this.languages == null) {
            this.languages = new LinkedList<>();
        }
        this.languages.add(language);
    }

    public void addRole(String role) {
        if(this.roles == null) {
            this.roles = new LinkedList<>();
        }
        this.roles.add(role);
    }
}
