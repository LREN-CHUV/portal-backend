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
import org.hbp.mip.repositories.AppRepository;
import org.hbp.mip.repositories.VoteRepository;
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

    @Autowired
    VoteRepository voteRepository;

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

        User user = mipApplication.getUser();
        App app = appRepository.findOne(id);

        Vote vote = voteRepository.find(user, app).iterator().next();

        if (vote != null) {
            vote.setValue(value);
            voteRepository.save(vote);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        else
        {
            vote = new Vote();
            vote.setUser(user);
            vote.setValue(value);
            vote.setApp(app);
            voteRepository.save(vote);
            return new ResponseEntity<>(HttpStatus.CREATED);
        }

    }
}
