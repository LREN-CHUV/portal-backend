/**
 * Created by mirco on 04.12.15.
 */

package eu.hbp.mip.controllers;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import eu.hbp.mip.model.Group;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.log4j.Logger;
import org.postgresql.util.PGobject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/groups", produces = {APPLICATION_JSON_VALUE})
@Api(value = "/groups", description = "the groups API")
public class GroupsApi {

    private static final Logger LOGGER = Logger.getLogger(GroupsApi.class);

    private static final Gson gson = new Gson();

    private static String groups;

    @Autowired
    @Qualifier("metaJdbcTemplate")
    private JdbcTemplate metaJdbcTemplate;


    @ApiOperation(value = "Get the root group (containing all subgroups)", response = Group.class)
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<Object> getTheRootGroup()  {
        LOGGER.info("Get root group and its whole sub-groups tree");

        loadGroups();

        return ResponseEntity.ok(gson.fromJson(groups, Object.class));
    }

    private void loadGroups() {
        if(groups == null)
        {
            String sqlQuery = "SELECT * FROM meta_variables";
            SqlRowSet data = metaJdbcTemplate.queryForRowSet(sqlQuery);
            data.next();
            String json = ((PGobject) data.getObject("hierarchy")).getValue();

            JsonObject root = gson.fromJson(json, JsonObject.class);

            removeVariablesRecursive(root);
            groups = gson.toJson(root);
        }
    }

    private void removeVariablesRecursive(JsonObject element) {
        if (element.has("groups")){
            for(JsonElement child : element.getAsJsonArray("groups")) {
                removeVariablesRecursive(child.getAsJsonObject());
            }
        }
        else {
            element.add("groups", new JsonArray());  // Only for compatibility with olf frontend
        }
        if(element.has("variables")) {
            element.remove("variables");
        }
    }

}
