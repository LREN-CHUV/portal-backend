/*
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

    private static final Gson gson = new Gson();

    @Autowired
    @Qualifier("dataUtil")
    private DataUtil dataUtil;


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

        JsonObject q = gson.fromJson(gson.toJson(query, Query.class), JsonObject.class);

        List<String> variables = extractVarCodes(q, "variables");
        List<String> groupings = extractVarCodes(q, "grouping");
        List<String> covariables = extractVarCodes(q, "covariables");
        String filters = extractFilters(q);

        List<String> allVars = new LinkedList<>();
        allVars.addAll(variables);
        allVars.addAll(groupings);
        allVars.addAll(covariables);

        dataset.addProperty("code", code);
        dataset.addProperty("date", date.getTime());
        dataset.add("variable", gson.toJsonTree(variables));
        dataset.add("grouping", gson.toJsonTree(groupings));
        dataset.add("header", gson.toJsonTree(covariables));
        dataset.add("data", dataUtil.getDataFromVariables(allVars, filters));

        return ResponseEntity.ok(gson.fromJson(dataset, Object.class));
    }

    private List<String> extractVarCodes(JsonObject q, String field) {
        List<String> codes = new LinkedList<>();
        JsonArray elements = q.getAsJsonArray(field) != null ? q.getAsJsonArray(field) : new JsonArray();
        for (JsonElement var : elements) {
            String varCode = var.getAsJsonObject().get("code").getAsString();
            codes.add(varCode);
        }
        return codes;
    }

    private String extractFilters(JsonObject q) {
        return q.getAsJsonPrimitive("filters").getAsString();
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
