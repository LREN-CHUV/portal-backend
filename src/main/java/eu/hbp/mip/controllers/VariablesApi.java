/**
 * Created by mirco on 04.12.15.
 */

package eu.hbp.mip.controllers;


import eu.hbp.mip.repositories.VariableRepository;
import io.swagger.annotations.*;
import eu.hbp.mip.model.Value;
import eu.hbp.mip.model.Variable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/variables", produces = {APPLICATION_JSON_VALUE})
@Api(value = "/variables", description = "the variables API")
public class VariablesApi {

    @Autowired
    VariableRepository variableRepository;

    @ApiOperation(value = "Get variables", response = Variable.class, responseContainer = "List")
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
        return ResponseEntity.ok(variableRepository.findAll());
    }

    @ApiOperation(value = "Get a variable", response = Variable.class)
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Found"), @ApiResponse(code = 404, message = "Not found") })
    @RequestMapping(value = "/{code}", method = RequestMethod.GET)
    public ResponseEntity<Variable> getAVariable(
            @ApiParam(value = "code of the variable ( multiple codes are allowed, separated by \",\" )", required = true) @PathVariable("code") String code
    )  {
        return ResponseEntity.ok(variableRepository.findOne(code));
    }


    @ApiOperation(value = "Get values from a variable", response = Value.class, responseContainer = "List")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Found"), @ApiResponse(code = 404, message = "Not found") })
    @RequestMapping(value = "/{code}/values", method = RequestMethod.GET)
    public ResponseEntity<List> getValuesFromAVariable(
            @ApiParam(value = "code", required = true) @PathVariable("code") String code,
            @ApiParam(value = "Pattern to match") @RequestParam(value = "q", required = false) String q
    )  {
        return ResponseEntity.ok(variableRepository.findOne(code).getValues());
    }


}
