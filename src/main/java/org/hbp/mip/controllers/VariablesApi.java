/**
 * Created by mirco on 04.12.15.
 */

package org.hbp.mip.controllers;


import io.swagger.annotations.*;
import org.hbp.mip.model.Value;
import org.hbp.mip.model.Variable;
import org.hbp.mip.utils.HibernateUtil;
import org.hibernate.Session;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedList;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/variables", produces = {APPLICATION_JSON_VALUE})
@Api(value = "/variables", description = "the variables API")
public class VariablesApi {

    @ApiOperation(value = "Get variables", response = Variable.class, responseContainer = "List")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Success") })
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<Variable>> getVariables(
            @ApiParam(value = "List of groups formatted like : (\"val1\", \"val2\", ...)") @RequestParam(value = "group", required = false) String group,
            @ApiParam(value = "List of subgroups formatted like : (\"val1\", \"val2\", ...)") @RequestParam(value = "subgroup", required = false) String subgroup,
            @ApiParam(value = "Boolean value formatted like : (\"0\") or (\"1\") or (\"false\") or (\"true\")") @RequestParam(value = "isVariable", required = false) String isVariable,
            @ApiParam(value = "Boolean value formatted like : (\"0\") or (\"1\") or (\"false\") or (\"true\")") @RequestParam(value = "isGrouping", required = false) String isGrouping,
            @ApiParam(value = "Boolean value formatted like : (\"0\") or (\"1\") or (\"false\") or (\"true\")") @RequestParam(value = "isCovariable", required = false) String isCovariable,
            @ApiParam(value = "Boolean value formatted like : (\"0\") or (\"1\") or (\"false\") or (\"true\")") @RequestParam(value = "isFilter", required = false) String isFilter
    )  {

        // Get variables from DB
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        List<Variable> variables = new LinkedList<>();
        try{
            session.beginTransaction();
            variables = session.createQuery("from Variable").list();
            session.getTransaction().commit();
        } catch (Exception e)
        {
            if(session.getTransaction() != null)
            {
                session.getTransaction().rollback();
            }
        }

        return new ResponseEntity<List<Variable>>(HttpStatus.OK).ok(variables);
    }

    @ApiOperation(value = "Get a variable", response = Variable.class)
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Found"), @ApiResponse(code = 404, message = "Not found") })
    @RequestMapping(value = "/{code}", method = RequestMethod.GET)
    public ResponseEntity<Variable> getAVariable(
            @ApiParam(value = "code of the variable ( multiple codes are allowed, separated by \",\" )", required = true) @PathVariable("code") String code
    )  {

        // Query DB
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Variable variable = null;
        try{
            session.beginTransaction();
            org.hibernate.Query query = session.createQuery("from Variable where code= :code");
            query.setString("code", code);
            variable = (Variable) query.uniqueResult();
            session.getTransaction().commit();
        } catch (Exception e)
        {
            if(session.getTransaction() != null)
            {
                session.getTransaction().rollback();
            }
        }

        return new ResponseEntity<Variable>(HttpStatus.OK).ok(variable);
    }


    @ApiOperation(value = "Get values from a variable", response = Value.class, responseContainer = "List")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Found"), @ApiResponse(code = 404, message = "Not found") })
    @RequestMapping(value = "/{code}/values", method = RequestMethod.GET)
    public ResponseEntity<List<Value>> getValuesFromAVariable(
            @ApiParam(value = "code", required = true) @PathVariable("code") String code,
            @ApiParam(value = "Pattern to match") @RequestParam(value = "q", required = false) String q
    )  {

        // Query DB
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        List<Value> values = new LinkedList<>();
        try{
            session.beginTransaction();
            values = session.createQuery("select values from Variable where code= :code").setString("code", code).list();
            session.getTransaction().commit();
        } catch (Exception e)
        {
            if(session.getTransaction() != null)
            {
                session.getTransaction().rollback();
            }
        }

        return new ResponseEntity<List<Value>>(HttpStatus.OK).ok(values);
    }


}
