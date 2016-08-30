/**
 * Created by mirco on 04.12.15.
 */

package eu.hbp.mip.controllers;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import eu.hbp.mip.model.Variable;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import eu.hbp.mip.model.Group;
import org.apache.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/groups", produces = {APPLICATION_JSON_VALUE})
@Api(value = "/groups", description = "the groups API")
public class GroupsApi {

    private static final Logger LOGGER = Logger.getLogger(GroupsApi.class);

    private static final String VARIABLES_FILE = "data/variables.json";


    @ApiOperation(value = "Get the root group (containing all subgroups)", response = Group.class)
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Success") })
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<Object> getTheRootGroup()  {
        LOGGER.info("Get root group and its whole sub-groups tree");

        InputStream is = Variable.class.getClassLoader().getResourceAsStream(VARIABLES_FILE);
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(isr);

        Object hierarchy = new Gson().fromJson(new JsonReader(br), Object.class);

        return ResponseEntity.ok(hierarchy);
    }


}
