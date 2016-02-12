/**
 * Created by mirco on 04.12.15.
 */

package org.hbp.mip.controllers;


import io.swagger.annotations.*;
import org.hbp.mip.MIPApplication;
import org.hbp.mip.model.*;
import org.hbp.mip.utils.CSVUtil;
import org.hbp.mip.utils.HibernateUtil;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/models", produces = {APPLICATION_JSON_VALUE})
@Api(value = "/models", description = "the models API")
@javax.annotation.Generated(value = "class io.swagger.codegen.languages.SpringMVCServerCodegen", date = "2016-01-07T07:38:20.227Z")
public class ModelsApi {

    private static final String DATA_FILE = "data/values.csv";

    @ApiOperation(value = "Get models", response = Model.class, responseContainer = "List")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Success") })
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<Model>> getModels(
            @ApiParam(value = "Max number of results") @RequestParam(value = "limit", required = false) Integer limit,
            @ApiParam(value = "Only ask own models") @RequestParam(value = "own", required = false) Boolean own,
            @ApiParam(value = "Only ask models from own team") @RequestParam(value = "team", required = false) Boolean team,
            Principal principal
    ) throws NotFoundException {

        // Get current user
        User user = MIPApplication.getUser(principal);

        // Prepare HQL query from Model and User tables
        String queryString = "select m from Model m, User u where m.createdBy=u.id";
        if(own != null && own)
        {
            queryString += " and u.username= :username";
        }
        else
        {
            if(team != null && team)
            {
                queryString += " and u.team= :team";
            }
        }

        // Query DB
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        Query query = session.createQuery(queryString);
        if(own != null && own)
        {
            query.setString("username", user.getUsername());
        }
        else
        {
            if(team != null && team)
            {
                query.setString("team", user.getTeam());
            }
        }
        if(limit != null)
        {
            query.setMaxResults(limit);  // Pagination : Use query.setFirstResult(...) to set begining index
        }
        List<Model> models = query.list();
        session.getTransaction().commit();

        return new ResponseEntity<List<Model>>(HttpStatus.OK).ok(models);
    }


    @ApiOperation(value = "Create a model", response = Void.class)
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Model created") })
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Void> addAModel(
            @RequestBody @ApiParam(value = "Model to create", required = true) Model model,
            Principal principal
    ) throws NotFoundException {

        // Get current user
        User user = MIPApplication.getUser(principal);

        // Set up model
        model.setSlug(model.getTitle().toLowerCase());
        model.setValid(true);
        model.setCreatedBy(user);
        model.setCreatedAt(new Date());

        // Save model into DB
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        session.save(model);
        session.getTransaction().commit();

        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    @ApiOperation(value = "Get SVG", response = Model.class)
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Found"), @ApiResponse(code = 404, message = "Not found") })
    @RequestMapping(value = "/{slug}.svg", produces = {"image/svg+xml"}, method = RequestMethod.GET)
    public ResponseEntity<String> getSVG(
            @ApiParam(value = "slug", required = true) @PathVariable("slug") String slug
    ) throws NotFoundException {

        // Query DB
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        org.hibernate.Query query = session.createQuery("from Model where slug= :slug");
        query.setString("slug", slug);
        Model model = (Model) query.uniqueResult();
        session.getTransaction().commit();

        return new ResponseEntity<String>(HttpStatus.OK).ok(model.getChart().getSvg());
    }

    @ApiOperation(value = "Get a model", response = Model.class)
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Found"), @ApiResponse(code = 404, message = "Not found") })
    @RequestMapping(value = "/{slug}", method = RequestMethod.GET)
    public ResponseEntity<Model> getAModel(
            @ApiParam(value = "slug", required = true) @PathVariable("slug") String slug
    ) throws NotFoundException {

        // Query DB
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        org.hibernate.Query query = session.createQuery("from Model where slug= :slug");
        query.setString("slug", slug);
        Model model = (Model) query.uniqueResult();
        session.getTransaction().commit();

        if(model != null) {
            session = HibernateUtil.getSessionFactory().getCurrentSession();
            session.beginTransaction();
            query = session.createQuery("from Query where id= :id");
            query.setLong("id", model.getQuery().getId());
            org.hbp.mip.model.Query q = (org.hbp.mip.model.Query) query.uniqueResult();
            session.getTransaction().commit();

            List<Variable> vars = new LinkedList<>();
            for (Variable var : q.getVariables()) {
                Variable v = new Variable();
                v.setCode(var.getCode());
                vars.add(v);
            }

            List<Variable> covs = new LinkedList<>();
            for (Variable cov : q.getCovariables()) {
                Variable v = new Variable();
                v.setCode(cov.getCode());
                covs.add(v);
            }

            List<Variable> grps = new LinkedList<>();
            for (Variable grp : q.getGrouping()) {
                Variable v = new Variable();
                v.setCode(grp.getCode());
                grps.add(v);
            }

            List<Filter> fltrs = new LinkedList<>();
            for (Filter fltr : q.getFilters()) {
                Filter f = new Filter();
                f.setId(fltr.getId());
                f.setOperator(fltr.getOperator());
                f.setValues(fltr.getValues());
                f.setVariable(fltr.getVariable());
                fltrs.add(f);
            }

            org.hbp.mip.model.Query myQuery = new org.hbp.mip.model.Query();
            myQuery.setId(q.getId());
            myQuery.setVariables(vars);
            myQuery.setCovariables(covs);
            myQuery.setGrouping(grps);
            myQuery.setFilters(fltrs);

            model.setQuery(myQuery);

            Dataset ds = CSVUtil.parseValues(DATA_FILE, model.getQuery());
            model.setDataset(ds);
        }

        return new ResponseEntity<Model>(HttpStatus.OK).ok(model);
    }


    @ApiOperation(value = "Update a model", response = Void.class)
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Model updated") })
    @RequestMapping(value = "/{slug}", method = RequestMethod.PUT)
    public ResponseEntity<Void> updateAModel(
            @ApiParam(value = "slug", required = true) @PathVariable("slug") String slug,
            @RequestBody @ApiParam(value = "Model to update", required = true) Model model,
            Principal principal
    ) throws NotFoundException {

        // Get current user
        User user = MIPApplication.getUser(principal);

        // Query DB
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        session.update(model);
        session.getTransaction().commit();

        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    @ApiOperation(value = "Copy a model", response = Model.class)
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Model copied"), @ApiResponse(code = 404, message = "Not found") })
    @RequestMapping(value = "/{slug}/copies", method = RequestMethod.POST)
    public ResponseEntity<Model> copyAModel(
            @ApiParam(value = "slug", required = true) @PathVariable("slug") String slug,
            @RequestBody @ApiParam(value = "Model to update", required = true) Model model,
            Principal principal
    ) throws NotFoundException {
        // Get current user
        User user = MIPApplication.getUser(principal);

        // Set slug
        String originalSlug = model.getSlug();
        String copySlug;
        do {
            copySlug = originalSlug+" copy_"+randomStr(20);
        } while (getAModel(copySlug) == null);
        model.setSlug(copySlug);

        // Save model into DB
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        session.save(model);
        session.getTransaction().commit();

        return new ResponseEntity<Model>(HttpStatus.OK).ok(model);
    }

    private String randomStr(int length) {
        char[] chars = "abcdefghijklmnopqrstuvwxyz0123456789".toCharArray();
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            char c = chars[random.nextInt(chars.length)];
            sb.append(c);
        }
        return sb.toString();
    }

    @ApiOperation(value = "Delete a model", response = Void.class)
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Model deleted") })
    @RequestMapping(value = "/{slug}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> deleteAModel(
            @ApiParam(value = "slug", required = true) @PathVariable("slug") String slug
    ) throws NotFoundException {

        // TODO : Implement delete method

        return new ResponseEntity<Void>(HttpStatus.OK);
    }


}
