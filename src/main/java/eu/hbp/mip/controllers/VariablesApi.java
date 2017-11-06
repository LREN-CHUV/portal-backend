/**
 * Created by mirco on 04.12.15.
 */

package eu.hbp.mip.controllers;


import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import eu.hbp.mip.model.Algorithm;
import eu.hbp.mip.model.MiningQuery;
import eu.hbp.mip.model.Variable;
import io.swagger.annotations.*;
import org.apache.log4j.Logger;
import org.postgresql.util.PGobject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/variables", produces = {APPLICATION_JSON_VALUE})
@Api(value = "/variables", description = "the variables API")
public class VariablesApi {

    private static final Logger LOGGER = Logger.getLogger(VariablesApi.class);

    private static final Gson gson = new Gson();

    @Autowired
    @Qualifier("metaJdbcTemplate")
    private JdbcTemplate metaJdbcTemplate;

    @Value("#{'${spring.featuresDatasource.main-table:features}'}")
    private String featuresMainTable;


    @ApiOperation(value = "Get variables", response = List.class, responseContainer = "List")
    @Cacheable("variables")
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<Iterable> getVariables(
            @ApiParam(value = "List of groups formatted like : (\"val1\", \"val2\", ...)") @RequestParam(value = "group", required = false) String group,
            @ApiParam(value = "List of subgroups formatted like : (\"val1\", \"val2\", ...)") @RequestParam(value = "subgroup", required = false) String subgroup,
            @ApiParam(value = "Boolean value formatted like : (\"0\") or (\"1\") or (\"false\") or (\"true\")") @RequestParam(value = "isVariable", required = false) String isVariable,
            @ApiParam(value = "Boolean value formatted like : (\"0\") or (\"1\") or (\"false\") or (\"true\")") @RequestParam(value = "isGrouping", required = false) String isGrouping,
            @ApiParam(value = "Boolean value formatted like : (\"0\") or (\"1\") or (\"false\") or (\"true\")") @RequestParam(value = "isCovariable", required = false) String isCovariable,
            @ApiParam(value = "Boolean value formatted like : (\"0\") or (\"1\") or (\"false\") or (\"true\")") @RequestParam(value = "isFilter", required = false) String isFilter
    )  {
        LOGGER.info("Get variables");

        LinkedList<Object> variablesObjects = new LinkedList<>();

        for (String var : loadVariables())
        {
            variablesObjects.add(gson.fromJson(var, Object.class));
        }

        return ResponseEntity.ok(variablesObjects);
    }

    @ApiOperation(value = "Get a variable", response = Object.class)
    @Cacheable("variable")
    @RequestMapping(value = "/{code}", method = RequestMethod.GET)
    public ResponseEntity<Object> getAVariable(
            @ApiParam(value = "code of the variable ( multiple codes are allowed, separated by \",\" )", required = true) @PathVariable("code") String code
    )  {
        LOGGER.info("Get a variable");

        for (String var : loadVariables())
        {
            JsonObject varObj = gson.fromJson(var, JsonElement.class).getAsJsonObject();
            if (varObj.get("code").getAsString().equals(code))
            {
                return ResponseEntity.ok(gson.fromJson(varObj, Object.class));
            }
        }

        LOGGER.warn("Variable " + code + " not found ! ");

        return ResponseEntity.ok(null);
    }


    @ApiOperation(value = "Get values from a variable", response = List.class, responseContainer = "List")
    @Cacheable("values")
    @RequestMapping(value = "/{code}/values", method = RequestMethod.GET)
    public ResponseEntity<Iterable> getValuesFromAVariable(
            @ApiParam(value = "code", required = true) @PathVariable("code") String code,
            @ApiParam(value = "Pattern to match") @RequestParam(value = "q", required = false) String q
    )  {
        LOGGER.info("Get values from a variable");

        for (String var : loadVariables())
        {
            JsonObject varObj = gson.fromJson(var, JsonElement.class).getAsJsonObject();
            if (varObj.get("code").getAsString().equals(code))
            {
                JsonArray values = varObj.get("enumerations").getAsJsonArray();
                LinkedList<Object> valuesObjects = new LinkedList<>();
                for (JsonElement value : values){
                    valuesObjects.add(gson.fromJson(value, Object.class));
                }
                return ResponseEntity.ok(valuesObjects);
            }
        }

        LOGGER.warn("Variable " + code + " not found ! ");

        return ResponseEntity.ok(null);
    }

    @ApiOperation(value = "Get groups and variables hierarchy", response = Object.class)
    @Cacheable("vars_hierarchy")
    @RequestMapping(value = "/hierarchy", method = RequestMethod.GET)
    public ResponseEntity<Object> getVariablesHierarchy(
    )  {
        LOGGER.info("Get groups and variables hierarchy");

        String sqlQuery = String.format(
                "SELECT * FROM meta_variables where upper(target_table)='%s'", featuresMainTable.toUpperCase());
        SqlRowSet data = metaJdbcTemplate.queryForRowSet(sqlQuery);
        data.next();
        String json = ((PGobject) data.getObject("hierarchy")).getValue();

        Object hierarchy = gson.fromJson(json, Object.class);

        return ResponseEntity.ok(hierarchy);
    }

    @ApiOperation(value = "Get query for histograms", response = Object.class)
    @Cacheable("vars_histogram_query")
    @RequestMapping(value = "/{code}/histogram_query", method = RequestMethod.GET)
    public ResponseEntity<String> getHistogramQuery(
            @ApiParam(value = "code", required = true) @PathVariable("code") String code
    )  {
        LOGGER.info("Get query for histograms");

        String sqlQuery = String.format(
                "SELECT histogram_groupings FROM meta_variables where upper(target_table)='%s'", featuresMainTable.toUpperCase());
        SqlRowSet data = metaJdbcTemplate.queryForRowSet(sqlQuery);
        data.next();
        String histogramGroupings = data.getString("histogram_groupings");

        MiningQuery query = new MiningQuery();
        query.getVariables().add(new Variable(code));
        List<String> groupings = Arrays.asList(histogramGroupings.split(","));
        query.getGrouping().addAll(groupings.stream().map(Variable::new).collect(Collectors.toList()));
        query.setAlgorithm(new Algorithm("histograms", "Histograms", false));

        return ResponseEntity.ok(histogramGroupings);
    }


    private List<String> loadVariables() {
        String sqlQuery = String.format(
                "SELECT * FROM meta_variables where upper(target_table)='%s'", featuresMainTable.toUpperCase());
        SqlRowSet data = metaJdbcTemplate.queryForRowSet(sqlQuery);

        List<String> variables = new LinkedList<>();
        if (data.next()) {
            String json = ((PGobject) data.getObject("hierarchy")).getValue();
            JsonObject root = gson.fromJson(json, JsonObject.class);
            extractVariablesRecursive(root, variables);
        }

        return variables;
    }

    private void extractVariablesRecursive(JsonObject element, List<String> variables) {
        if (element.has("groups")){
            for(JsonElement child : element.getAsJsonArray("groups")) {
                extractVariablesRecursive(child.getAsJsonObject(), variables);
            }
        }
        if (element.has("variables")){
            for (JsonElement var : element.getAsJsonArray("variables")){
                JsonObject grp = new JsonObject();
                grp.addProperty("code", element.getAsJsonPrimitive("code").getAsString());
                grp.addProperty("label", element.getAsJsonPrimitive("label").getAsString());
                var.getAsJsonObject().add("group", grp);
                var.getAsJsonObject().addProperty("isVariable", true);
                variables.add(gson.toJson(var));
            }
        }
    }


}
