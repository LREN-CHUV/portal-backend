package org.hbp.mip.controllers;

import io.swagger.annotations.*;
import org.hbp.mip.model.User;
import org.hbp.mip.utils.HibernateUtil;
import org.hibernate.Session;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * Created by mirco on 14.01.16.
 */

@RestController
@RequestMapping(value = "/users", produces = {APPLICATION_JSON_VALUE})
@Api(value = "/users", description = "the users API")
public class UsersApi {


    @ApiOperation(value = "Get a user", response = User.class)
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Found"), @ApiResponse(code = 404, message = "Not found") })
    @RequestMapping(value = "/{username}", method = RequestMethod.GET)
    public ResponseEntity<User> getAUser(
            @ApiParam(value = "username", required = true) @PathVariable("username") String username
    )  {

        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        User user = null;
        try{
            session.beginTransaction();
            org.hibernate.Query query = session.createQuery("from User where username= :username");
            query.setString("username", username);
            user = (User) query.uniqueResult();
            session.getTransaction().commit();
        } catch (Exception e)
        {
            if(session.getTransaction() != null)
            {
                session.getTransaction().rollback();
            }
        }

        return ResponseEntity.ok(user);
    }
}
