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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
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

    @Autowired
    @Qualifier("metaJdbcTemplate")
    private JdbcTemplate metaJdbcTemplate;

    @Value("#{'${spring.featuresDatasource.main-table:features}'}")
    private String featuresMainTable;


    @ApiOperation(value = "Get the root group (containing all subgroups)", response = Group.class)
    @Cacheable("groups")
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<Object> getTheRootGroup()  {
        LOGGER.info("Get root group and its whole sub-groups tree");

        return ResponseEntity.ok(gson.fromJson(loadGroups(), Object.class));
    }

    private String loadGroups() {
        String sqlQuery = String.format("SELECT * FROM meta_variables where target_table='%s'", featuresMainTable);
        SqlRowSet data = metaJdbcTemplate.queryForRowSet(sqlQuery);
        data.next();
        String json = ((PGobject) data.getObject("hierarchy")).getValue();

        JsonObject root = gson.fromJson(json, JsonObject.class);
        removeVariablesRecursive(root);

        return gson.toJson(root);
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
