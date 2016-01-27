/**
 * Created by mirco on 04.12.15.
 */

package org.hbp.mip.controllers;


import io.swagger.annotations.*;
import org.hbp.mip.model.Group;
import org.hbp.mip.model.Value;
import org.hbp.mip.model.Variable;
import org.hibernate.Session;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.LinkedList;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Controller
@RequestMapping(value = "/variables", produces = {APPLICATION_JSON_VALUE})
@Api(value = "/variables", description = "the variables API")
@javax.annotation.Generated(value = "class io.swagger.codegen.languages.SpringMVCServerCodegen", date = "2016-01-07T07:38:20.227Z")
public class VariablesApi {


    @ApiOperation(value = "Get variables", notes = "", response = Variable.class, responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success")})
    @RequestMapping(value = "", produces = { APPLICATION_JSON_VALUE }, method = RequestMethod.GET)
    public ResponseEntity<List<Variable>> getVariables(
            @ApiParam(value = "List of groups formatted like : (\"val1\", \"val2\", ...)") @RequestParam(value = "group", required = false) String group,
            @ApiParam(value = "List of subgroups formatted like : (\"val1\", \"val2\", ...)") @RequestParam(value = "subgroup", required = false) String subgroup,
            @ApiParam(value = "Boolean value formatted like : (\"0\") or (\"1\") or (\"false\") or (\"true\")") @RequestParam(value = "isVariable", required = false) String isVariable,
            @ApiParam(value = "Boolean value formatted like : (\"0\") or (\"1\") or (\"false\") or (\"true\")") @RequestParam(value = "isGrouping", required = false) String isGrouping,
            @ApiParam(value = "Boolean value formatted like : (\"0\") or (\"1\") or (\"false\") or (\"true\")") @RequestParam(value = "isCovariable", required = false) String isCovariable,
            @ApiParam(value = "Boolean value formatted like : (\"0\") or (\"1\") or (\"false\") or (\"true\")") @RequestParam(value = "isFilter", required = false) String isFilter) throws NotFoundException {

        // Get variales from DB
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        List<Variable> variables = session.createQuery("from Variable").list();
        session.getTransaction().commit();

        for(Variable v : variables)
        {
            Group g = null;
            Group fils = null;
            for(int i=v.getGrpPath().size()-1; i >= 0; i--) {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                org.hibernate.Query query = session.createQuery("from Group where code= :code");
                query.setString("code", v.getGrpPath().get(i));
                g = (Group) query.uniqueResult();
                session.getTransaction().commit();
                g.setGroups(new LinkedList<>());
                if(fils != null)
                {
                    g.addGroup(fils);
                }
                fils = g.clone();
            }
            v.setGroup(fils);
        }

        return new ResponseEntity<List<Variable>>(HttpStatus.OK).ok(variables);
    }

    @ApiOperation(value = "Get a variable", notes = "", response = Variable.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Found"),
            @ApiResponse(code = 404, message = "Not found")})
    @RequestMapping(value = "/{code}", produces = { APPLICATION_JSON_VALUE }, method = RequestMethod.GET)
    public ResponseEntity<Variable> getAVariable(
            @ApiParam(value = "code of the variable ( multiple codes are allowed, separated by \",\" )", required = true) @PathVariable("code") String code) throws NotFoundException {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        org.hibernate.Query query = session.createQuery("from Variable where code= :code");
        query.setString("code", code);
        Variable variable = (Variable) query.uniqueResult();
        session.getTransaction().commit();
        return new ResponseEntity<Variable>(HttpStatus.OK).ok(variable);
    }


    @ApiOperation(value = "Get values from a variable", notes = "", response = Value.class, responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Found"),
            @ApiResponse(code = 404, message = "Not found")})
    @RequestMapping(value = "/{code}/values", produces = { APPLICATION_JSON_VALUE }, method = RequestMethod.GET)
    public ResponseEntity<List<Value>> getValuesFromAVariable(
            @ApiParam(value = "code", required = true) @PathVariable("code") String code,
            @ApiParam(value = "Pattern to match") @RequestParam(value = "q", required = false) String q) throws NotFoundException {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        List<Value> values = session.createQuery("select values from Variable where code= :code").setString("code", code).list();
        session.getTransaction().commit();
        return new ResponseEntity<List<Value>>(HttpStatus.OK).ok(values);
    }


}
