/**
 * Created by mirco on 03.02.16.
 */

package org.hbp.mip.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;

@ApiModel
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GeneralStats {

    private Long users = null;
    private Long articles = null;
    private Long variables = null;


    public GeneralStats() {
    }


    public Long getUsers() {
        return users;
    }

    public void setUsers(Long users) {
        this.users = users;
    }


    public Long getArticles() {
        return articles;
    }

    public void setArticles(Long articles) {
        this.articles = articles;
    }


    public Long getVariables() {
        return variables;
    }

    public void setVariables(Long variables) {
        this.variables = variables;
    }
}
