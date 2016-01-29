/**
 * Created by mirco on 04.12.15.
 */

package org.hbp.mip.controllers;


import io.swagger.annotations.*;
import org.hbp.mip.MIPApplication;
import org.hbp.mip.model.Model;
import org.hbp.mip.model.User;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.*;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Controller
@RequestMapping(value = "/models", produces = {APPLICATION_JSON_VALUE})
@Api(value = "/models", description = "the models API")
@javax.annotation.Generated(value = "class io.swagger.codegen.languages.SpringMVCServerCodegen", date = "2016-01-07T07:38:20.227Z")
public class ModelsApi {


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

        // Inject mock data
        List<Object> values = new LinkedList<>();
        values.add(18422);
        values.add(16972);
        values.add(17330);
        values.add(16398);
        values.add(21614);
        values.add(21386);
        values.add(20474);
        values.add(19867);
        values.add(20398);
        values.add(19741);
        values.add(18595);
        values.add(18018);
        model.getDataset().getData().put("MidTemp", values);
        model.getDataset().getHeader().add("MidTemp");

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
