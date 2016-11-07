/**
 * Created by mirco on 18.01.16.
 */

package eu.hbp.mip.controllers;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import eu.hbp.mip.model.Dataset;
import eu.hbp.mip.model.Query;
import eu.hbp.mip.utils.DataUtil;
import io.swagger.annotations.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/queries/requests", produces = {APPLICATION_JSON_VALUE})
@Api(value = "/queries/requests", description = "the requests API")
public class RequestsApi {

    private static final Logger LOGGER = Logger.getLogger(RequestsApi.class);

    @Autowired
    @Qualifier("dataUtil")
    public DataUtil dataUtil;


    @ApiOperation(value = "Post a request", response = Dataset.class)
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Success") })
    @RequestMapping(method = RequestMethod.POST)
    @Deprecated
    public ResponseEntity<Object> postRequests(
            @RequestBody @ApiParam(value = "Query to process", required = true) Query query
    )  {
        LOGGER.info("Post a request");

        JsonObject dataset = new JsonObject();

        String code = generateDSCode(query);
        Date date = new Date();
        List<String> variables = new LinkedList<>();
        List<String> groupings = new LinkedList<>();
        List<String> covariables = new LinkedList<>();
        List<String> filters = new LinkedList<>();

        Gson gson = new Gson();
        JsonObject q = gson.fromJson(gson.toJson(query, Query.class), JsonObject.class);

        JsonArray queryVars = q.getAsJsonArray("variables") != null ? q.getAsJsonArray("variables") : new JsonArray();
        JsonArray queryGrps = q.getAsJsonArray("grouping") != null ? q.getAsJsonArray("grouping") : new JsonArray();
        JsonArray queryCoVars = q.getAsJsonArray("covariables") != null ? q.getAsJsonArray("covariables") : new JsonArray();
        JsonArray queryfltrs = q.getAsJsonArray("filter") != null ? q.getAsJsonArray("filter") : new JsonArray();

        List<String> allVars = new LinkedList<>();

        for (JsonElement var : queryVars) {
            String varCode = var.getAsJsonObject().get("code").getAsString();
            variables.add(varCode);
            allVars.add(varCode);
        }

        for (JsonElement var : queryGrps) {
            String varCode = var.getAsJsonObject().get("code").getAsString();
            groupings.add(varCode);
            allVars.add(varCode);
        }

        for (JsonElement var : queryCoVars) {
            String varCode = var.getAsJsonObject().get("code").getAsString();
            covariables.add(varCode);
            allVars.add(varCode);
        }

        for (JsonElement var : queryfltrs) {
            String fltCode = var.getAsJsonObject().get("code").getAsString();
            filters.add(fltCode);
            allVars.add(fltCode);
        }

        dataset.addProperty("code", code);
        dataset.addProperty("date", date.getTime());
        dataset.add("variable", gson.toJsonTree(variables));
        dataset.add("grouping", gson.toJsonTree(groupings));
        dataset.add("header", gson.toJsonTree(covariables));
        dataset.add("filter", gson.toJsonTree(filters));
        dataset.add("data", dataUtil.getDataFromVariables(allVars));

        return ResponseEntity.ok(new Gson().fromJson(dataset, Object.class));
    }


    private String generateDSCode(Query query) {
        String prefix = "DS";
        String queryStr = Integer.toString(query.hashCode());

        Pattern p = Pattern.compile("@(\\w+)");
        Matcher m = p.matcher(queryStr);

        String memId;
        if (m.find()) {
            memId = m.group(1);
        } else {
            memId = Long.toString(new Date().getTime()); // "This should never happen"
        }

        return prefix + memId;
    }

}
