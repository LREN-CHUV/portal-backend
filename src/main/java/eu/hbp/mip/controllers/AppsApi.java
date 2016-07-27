/**
 * Created by mirco on 20.05.16.
 */

package eu.hbp.mip.controllers;

import eu.hbp.mip.configuration.SecurityConfiguration;
import eu.hbp.mip.repositories.AppRepository;
import io.swagger.annotations.*;
import org.apache.log4j.Logger;
import eu.hbp.mip.model.App;
import eu.hbp.mip.model.User;
import eu.hbp.mip.model.Vote;
import eu.hbp.mip.repositories.UserRepository;
import eu.hbp.mip.repositories.VoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Iterator;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/apps", produces = {APPLICATION_JSON_VALUE})
@Api(value = "/apps", description = "the apps API")
public class AppsApi {

    private static final Logger LOGGER = Logger.getLogger(AppsApi.class);

    @Autowired
    SecurityConfiguration securityConfiguration;

    @Autowired
    AppRepository appRepository;

    @Autowired
    VoteRepository voteRepository;

    @Autowired
    UserRepository userRepository;

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
        User user = userRepository.findOne(securityConfiguration.getUser().getUsername());
        App app = appRepository.findOne(id);
        Vote vote;

        Iterator<Vote> voteIter = voteRepository.findByUserAndApp(user, app).iterator();
        if(voteIter.hasNext())
        {
            vote = voteIter.next();
        }
        else
        {
            vote = new Vote();
            vote.setUser(user);
            vote.setApp(app);
        }

        vote.setValue(value);
        voteRepository.save(vote);

        return new ResponseEntity<>(HttpStatus.CREATED);

    }
}
