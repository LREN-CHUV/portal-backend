/**
 * Created by mirco on 20.05.16.
 */

package org.hbp.mip.controllers;

import io.swagger.annotations.*;
import org.hbp.mip.MIPApplication;
import org.hbp.mip.model.App;
import org.hbp.mip.model.User;
import org.hbp.mip.model.Vote;
import org.hbp.mip.utils.HibernateUtil;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedList;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/apps", produces = {APPLICATION_JSON_VALUE})
@Api(value = "/apps", description = "the apps API")
public class AppsApi {

    @Autowired
    MIPApplication mipApplication;

    @ApiOperation(value = "Get apps", response = App.class, responseContainer = "List")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Success") })
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List> getApps(
    ) {
        List apps = new LinkedList<>();

        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        try {
            session.beginTransaction();
            Query query = session.createQuery("FROM App");
            apps = query.list();
            session.getTransaction().commit();
        } catch (Exception e)
        {
            if(session.getTransaction() != null)
            {
                session.getTransaction().rollback();
                throw e;
            }
        }

        return ResponseEntity.ok(apps);
    }

    @ApiOperation(value = "Post a vote")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Success") })
    @RequestMapping(value = "/{id}/vote/{value}", method = RequestMethod.POST)
    public ResponseEntity<Void> vote(
            @ApiParam(value = "id", required = true) @PathVariable("id") Integer id,
            @ApiParam(value = "value", required = true) @PathVariable("value") Integer value
    ) {

        User user = mipApplication.getUser();

        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        try {
            session.beginTransaction();

            App app = (App) session.createQuery("FROM App where id= :id").setLong("id", id).uniqueResult();

            Vote vote = new Vote();
            vote.setUser(user);
            vote.setValue(value);
            vote.setApp(app);

            session.save(vote);

            session.getTransaction().commit();
        } catch (Exception e)
        {
            if(session.getTransaction() != null)
            {
                session.getTransaction().rollback();
                throw e;
            }
        }

        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
