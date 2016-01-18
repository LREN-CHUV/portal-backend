package org.hbp.mip.controllers;

import io.swagger.annotations.*;
import org.hbp.mip.model.User;
import org.hibernate.Session;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * Created by mirco on 14.01.16.
 */

@Controller
@RequestMapping(value = "/users", produces = {APPLICATION_JSON_VALUE})
@Api(value = "/users", description = "the users API")
@javax.annotation.Generated(value = "class io.swagger.codegen.languages.SpringMVCServerCodegen", date = "2016-01-07T07:38:20.227Z")
public class UsersApi {

    @ApiOperation(value = "Get a user", notes = "", response = User.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Found"),
            @ApiResponse(code = 404, message = "Not found")})
    @RequestMapping(value = "/{username}", produces = {"application/json"}, method = RequestMethod.GET)
    public ResponseEntity<User> getAUser(
            @ApiParam(value = "username", required = true) @PathVariable("username") String username) throws NotFoundException {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        org.hibernate.Query query = session.createQuery("from User where username= :username");
        query.setString("username", username);
        User user = (User) query.uniqueResult();
        session.getTransaction().commit();
        return new ResponseEntity<User>(HttpStatus.OK).ok(user);
    }
}
