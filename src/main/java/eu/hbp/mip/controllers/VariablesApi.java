/**
 * Created by mirco on 04.12.15.
 */

package eu.hbp.mip.controllers;


import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import eu.hbp.mip.model.Variable;
import io.swagger.annotations.*;
import org.apache.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/variables", produces = {APPLICATION_JSON_VALUE})
@Api(value = "/variables", description = "the variables API")
public class VariablesApi {

    private static final Logger LOGGER = Logger.getLogger(VariablesApi.class);

    private static final String VARIABLES_FILE = "data/variables.json";

    private static LinkedList<String> variables;


    @ApiOperation(value = "Get variables", response = List.class, responseContainer = "List")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Success") })
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

        loadVariables();

        LinkedList<Object> variablesObjects = new LinkedList<>();

        for (String var : variables)
        {
            variablesObjects.add(new Gson().fromJson(var, Object.class));
        }

        return ResponseEntity.ok(variablesObjects);
    }

    @ApiOperation(value = "Get a variable", response = Object.class)
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Found"), @ApiResponse(code = 404, message = "Not found") })
    @RequestMapping(value = "/{code}", method = RequestMethod.GET)
    public ResponseEntity<Object> getAVariable(
            @ApiParam(value = "code of the variable ( multiple codes are allowed, separated by \",\" )", required = true) @PathVariable("code") String code
    )  {
        LOGGER.info("Get a variable");

        loadVariables();

        for (String var : variables)
        {
            JsonObject varObj = new Gson().fromJson(var, JsonElement.class).getAsJsonObject();
            if (varObj.get("code").getAsString().equals(code))
            {
                return ResponseEntity.ok(new Gson().fromJson(varObj, Object.class));
            }
        }

        LOGGER.warn("Variable " + code + " not found ! ");

        return ResponseEntity.ok(null);
    }


    @ApiOperation(value = "Get values from a variable", response = List.class, responseContainer = "List")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Found"), @ApiResponse(code = 404, message = "Not found") })
    @RequestMapping(value = "/{code}/values", method = RequestMethod.GET)
    public ResponseEntity<Iterable> getValuesFromAVariable(
            @ApiParam(value = "code", required = true) @PathVariable("code") String code,
            @ApiParam(value = "Pattern to match") @RequestParam(value = "q", required = false) String q
    )  {
        LOGGER.info("Get values from a variable");

        loadVariables();

        for (String var : variables)
        {
            JsonObject varObj = new Gson().fromJson(var, JsonElement.class).getAsJsonObject();
            if (varObj.get("code").getAsString().equals(code))
            {
                JsonArray values = varObj.get("enumerations").getAsJsonArray();
                LinkedList<Object> valuesObjects = new LinkedList<>();
                for (JsonElement value : values){
                    valuesObjects.add(new Gson().fromJson(value, Object.class));
                }
                return ResponseEntity.ok(valuesObjects);
            }
        }

        LOGGER.warn("Variable " + code + " not found ! ");

        return ResponseEntity.ok(null);
    }

    @ApiOperation(value = "Get groups and variables hierarchy", response = Object.class)
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Found"), @ApiResponse(code = 404, message = "Not found") })
    @RequestMapping(value = "/hierarchy", method = RequestMethod.GET)
    public ResponseEntity<Object> getVariablesHierarchy(
    )  {
        LOGGER.info("Get groups and variables hierarchy");

        InputStream is = Variable.class.getClassLoader().getResourceAsStream(VARIABLES_FILE);
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(isr);

        Object hierarchy = new Gson().fromJson(new JsonReader(br), Object.class);

        return ResponseEntity.ok(hierarchy);
    }


    private static void loadVariables() {
        if(variables == null)
        {
            InputStream is = VariablesApi.class.getClassLoader().getResourceAsStream(VARIABLES_FILE);
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);

            JsonObject root = new Gson().fromJson(new JsonReader(br), JsonObject.class);
            variables = new LinkedList<>();

            extractVariablesRecursive(root);
        }
    }

    private static void extractVariablesRecursive(JsonObject element) {
        if (element.has("groups")){
            for(JsonElement child : element.getAsJsonArray("groups")) {
                extractVariablesRecursive(child.getAsJsonObject());
            }
        }
        if (element.has("variables")){
            for (JsonElement var : element.getAsJsonArray("variables")){
                JsonObject grp = new JsonObject();
                grp.addProperty("code", element.getAsJsonPrimitive("code").getAsString());
                grp.addProperty("label", element.getAsJsonPrimitive("label").getAsString());
                var.getAsJsonObject().add("group", grp);
                variables.add(new Gson().toJson(var));
            }
        }
    }


}
