/**
 * Created by mirco on 04.12.15.
 */

package eu.hbp.mip.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import eu.hbp.mip.model.Group;
import eu.hbp.mip.repositories.GroupRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/groups", produces = {APPLICATION_JSON_VALUE})
@Api(value = "/groups", description = "the groups API")
public class GroupsApi {

    private static final Logger LOGGER = Logger.getLogger(GroupsApi.class);

    private static final String ROOT_CODE = "root";

    @Autowired
    GroupRepository groupRepository;

    @ApiOperation(value = "Get the root group (containing all subgroups)", response = Group.class)
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Success") })
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<Group> getTheRootGroup()  {
        LOGGER.info("Get root group and its whole sub-groups tree");

        return ResponseEntity.ok(groupRepository.findOne(ROOT_CODE));
    }


}
