/**
 * Created by mirco on 20.05.16.
 */

package org.hbp.mip.controllers;

import io.swagger.annotations.*;
import org.apache.log4j.Logger;
import org.hbp.mip.MIPApplication;
import org.hbp.mip.model.App;
import org.hbp.mip.model.User;
import org.hbp.mip.model.Vote;
import org.hbp.mip.utils.HibernateUtil;
import org.hibernate.NonUniqueObjectException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.ConstraintViolationException;
import java.util.LinkedList;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/apps", produces = {APPLICATION_JSON_VALUE})
@Api(value = "/apps", description = "the apps API")
public class AppsApi {

    private Logger logger = Logger.getLogger(this.getClass());

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

            Vote vote = (Vote) session.createQuery("" +
                    "SELECT v FROM Vote v, User u, App a " +
                    "WHERE u=v.user " +
                    "AND a=v.app " +
                    "AND u.username= :username " +
                    "AND a.id= :app_id")
                    .setString("username", user.getUsername())
                    .setLong("app_id", id)
                    .uniqueResult();
            App app = (App) session.createQuery("FROM App where id= :id").setLong("id", id).uniqueResult();

            if (vote != null) {
                vote.setValue(value);

                session.update(vote);
                session.getTransaction().commit();
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            else
            {
                vote = new Vote();
                vote.setUser(user);
                vote.setValue(value);
                vote.setApp(app);

                session.save(vote);
                session.getTransaction().commit();
                return new ResponseEntity<>(HttpStatus.CREATED);
            }
        }
        catch (ConstraintViolationException cve)
        {
            logger.trace(cve);
            if(session.getTransaction() != null)
            {
                session.getTransaction().rollback();
            }
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        catch (NonUniqueObjectException nuoe)
        {
            logger.trace(nuoe);
            if(session.getTransaction() != null)
            {
                session.getTransaction().rollback();
            }
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        catch (Exception e)
        {
            if(session.getTransaction() != null)
            {
                session.getTransaction().rollback();
                throw e;
            }
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
