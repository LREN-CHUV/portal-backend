/**
 * Created by mirco on 20.05.16.
 */

package org.hbp.mip.controllers;

import io.swagger.annotations.*;
import org.apache.log4j.Logger;
import org.hbp.mip.MIPApplication;
import org.hbp.mip.model.App;
import org.hbp.mip.repositories.AppRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/apps", produces = {APPLICATION_JSON_VALUE})
@Api(value = "/apps", description = "the apps API")
public class AppsApi {

    private static final Logger LOGGER = Logger.getLogger(AppsApi.class);

    @Autowired
    MIPApplication mipApplication;

    @Autowired
    AppRepository appRepository;

    @ApiOperation(value = "Get apps", response = App.class, responseContainer = "List")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Success") })
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<Iterable> getApps(
    ) {
        return ResponseEntity.ok(appRepository.findAll());
    }

    @ApiOperation(value = "Post a vote")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Success") })
    @RequestMapping(value = "/{id}/vote/{value}", method = RequestMethod.POST)
    public ResponseEntity<Void> vote(
            @ApiParam(value = "id", required = true) @PathVariable("id") Integer id,
            @ApiParam(value = "value", required = true) @PathVariable("value") Integer value
    ) {

        /*User user = mipApplication.getUser();

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
        }*/

        return new ResponseEntity<>(HttpStatus.CREATED);

    }
}
