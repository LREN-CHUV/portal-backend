/**
 * Created by mirco on 04.12.15.
 */

package org.hbp.mip.controllers;

import com.github.slugify.Slugify;
import io.swagger.annotations.*;
import org.apache.log4j.Logger;
import org.hbp.mip.MIPApplication;
import org.hbp.mip.model.*;
import org.hbp.mip.utils.CSVUtil;
import org.hbp.mip.utils.HibernateUtil;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.*;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/models", produces = {APPLICATION_JSON_VALUE})
@Api(value = "/models", description = "the models API")
@javax.annotation.Generated(value = "class io.swagger.codegen.languages.SpringMVCServerCodegen", date = "2016-01-07T07:38:20.227Z")
public class ModelsApi {

    private Logger logger = Logger.getLogger(this.getClass());

    @Autowired
    MIPApplication mipApplication;

    private static final String DATA_FILE = "data/values.csv";

    @ApiOperation(value = "Get models", response = Model.class, responseContainer = "List")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Success") })
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<Model>> getModels(
            @ApiParam(value = "Max number of results") @RequestParam(value = "limit", required = false) Integer limit,
            @ApiParam(value = "Only ask own models") @RequestParam(value = "own", required = false) Boolean own,
            @ApiParam(value = "Only ask models from own team") @RequestParam(value = "team", required = false) Boolean team,
            @ApiParam(value = "Only ask published models") @RequestParam(value = "valid", required = false) Boolean valid
    )  {

        User user = mipApplication.getUser();

        String queryString = "SELECT m FROM Model m, User u WHERE m.createdBy=u.username";
        if(valid != null && valid)
        {
            queryString += " AND m.valid= :valid";
        }
        if(own != null && own)
        {
            queryString += " AND u.username= :username";
        }
        else
        {
            queryString += " AND (m.valid=true or u.username= :username)";
            if(team != null && team)
            {
                // TODO: decide if this is needed
                //queryString += " AND u.team= :team";
            }
        }

        queryString += " ORDER BY m.createdAt DESC";

        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        List<Model> models = new LinkedList<>();
        try{
            session.beginTransaction();
            Query query = session.createQuery(queryString);
            if(valid != null)
            {
                query.setBoolean("valid", valid);
            }
            query.setString("username", user.getUsername());
            if(limit != null)
            {
                query.setMaxResults(limit);  // Pagination : Use query.setFirstResult(...) to set begining index
            }
            models = query.list();
            session.getTransaction().commit();
        } catch (Exception e)
        {
            if(session.getTransaction() != null)
            {
                session.getTransaction().rollback();
                throw e;
            }
        }

        for(Model model:models){
            String ds_code = model.getDataset().getCode();

            session = HibernateUtil.getSessionFactory().getCurrentSession();
            Dataset dataset = null;
            try{
                session.beginTransaction();
                dataset = (Dataset) session
                        .createQuery("from Dataset where code= :code")
                        .setString("code", ds_code)
                        .uniqueResult();
                session.getTransaction().commit();
            } catch (Exception e)
            {
                if(session.getTransaction() != null)
                {
                    session.getTransaction().rollback();
                    throw e;
                }
            }

            model.setDataset(dataset);
        }

        return new ResponseEntity<List<Model>>(HttpStatus.OK).ok(models);
    }


    @ApiOperation(value = "Create a model", response = Model.class)
    @ApiResponses(value = { @ApiResponse(code = 201, message = "Model created") })
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Model> addAModel(
            @RequestBody @ApiParam(value = "Model to create", required = true) Model model
    )  {

        User user = mipApplication.getUser();

        model.setTitle(model.getConfig().getTitle().get("text"));
        model.setCreatedBy(user);
        model.setCreatedAt(new Date());
        if(model.getValid() == null)
        {
            model.setValid(false);
        }

        Long count;
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        try{
            session.beginTransaction();

            int i = 0;
            do{
                i++;
                count = (Long) session
                        .createQuery("select count(*) from Model where title= :title")
                        .setString("title", model.getTitle())
                        .uniqueResult();

                if(count > 0)
                {
                    String title = model.getTitle();
                    if(i > 1)
                    {
                        title = title.substring(0, title.length()-4);
                    }
                    model.setTitle(title + " (" + i + ")");
                }
            } while(count > 0);

            Slugify slg = null;
            String slug = "";
            try {
                slg = new Slugify();
                slug = slg.slugify(model.getTitle());
            } catch (IOException e) {
                logger.trace(e);
            }

            i = 0;
            do {
                i++;
                count = (Long) session
                        .createQuery("select count(*) from Model where slug= :slug")
                        .setString("slug", slug)
                        .uniqueResult();
                if(count > 0)
                {
                    if(i > 1)
                    {
                        slug = slug.substring(0, slug.length()-2);
                    }
                    slug += "-"+i;
                }
                model.setSlug(slug);
            } while(count > 0);

            Map<String, String> map = new HashMap<>(model.getConfig().getTitle());
            map.put("text", model.getTitle());
            model.getConfig().setTitle(map);

            session.save(model);
            session.getTransaction().commit();
        } catch (Exception e)
        {
            if(session.getTransaction() != null)
            {
                session.getTransaction().rollback();
                throw e;
            }
        }

        return new ResponseEntity<Model>(HttpStatus.CREATED).ok(model);
    }

    @ApiOperation(value = "Get a model", response = Model.class)
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Found"), @ApiResponse(code = 404, message = "Not found") })
    @RequestMapping(value = "/{slug}", method = RequestMethod.GET)
    public ResponseEntity<Model> getAModel(
            @ApiParam(value = "slug", required = true) @PathVariable("slug") String slug
    )  {

        User user = mipApplication.getUser();

        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Model model = null;
        Query query;

        try {
            session.beginTransaction();
            model = (Model) session
                    .createQuery("FROM Model WHERE slug= :slug")
                    .setString("slug", slug)
                    .uniqueResult();
            session.getTransaction().commit();

            if (!model.getValid() && !model.getCreatedBy().getUsername().equals(user.getUsername()))
            {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }

        } catch (Exception e)
        {
            if(session.getTransaction() != null)
            {
                session.getTransaction().rollback();
                throw e;
            }
        }


        if(model != null) {
            session = HibernateUtil.getSessionFactory().getCurrentSession();
            org.hbp.mip.model.Query q = null;

            try {
                session.beginTransaction();
                q = (org.hbp.mip.model.Query) session
                        .createQuery("FROM Query WHERE id= :id")
                        .setLong("id", model.getQuery().getId())
                        .uniqueResult();
                session.getTransaction().commit();
            } catch (Exception e)
            {
                if(session.getTransaction() != null)
                {
                    session.getTransaction().rollback();
                    throw e;
                }
            }

            if(q != null) {

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
            }

            Dataset ds = CSVUtil.parseValues(DATA_FILE, model.getQuery());
            model.setDataset(ds);

        }

        return new ResponseEntity<>(HttpStatus.OK).ok(model);
    }


    @ApiOperation(value = "Update a model", response = Void.class)
    @ApiResponses(value = { @ApiResponse(code = 204, message = "Model updated") })
    @RequestMapping(value = "/{slug}", method = RequestMethod.PUT)
    public ResponseEntity<Void> updateAModel(
            @ApiParam(value = "slug", required = true) @PathVariable("slug") String slug,
            @RequestBody @ApiParam(value = "Model to update", required = true) Model model
    )  {

        User user = mipApplication.getUser();

        model.setTitle(model.getConfig().getTitle().get("text"));

        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        try{
            session.beginTransaction();

            String author = (String) session
                    .createQuery("select U.username from User U, Model M where M.createdBy = U.username and M.slug = :slug")
                    .setString("slug", slug)
                    .uniqueResult();

            if(!user.getUsername().equals(author))
            {
                session.getTransaction().commit();
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }

            String oldTitle = (String) session
                    .createQuery("select title from Model where slug= :slug")
                    .setString("slug", slug)
                    .uniqueResult();

            String newTitle = model.getTitle();

            if(!newTitle.equals(oldTitle)) {
                Long count;
                int i = 0;
                do {
                    i++;
                    newTitle = model.getTitle();
                    count = (Long) session
                            .createQuery("select count(*) from Model where title= :title")
                            .setString("title", newTitle)
                            .uniqueResult();
                    if (count > 0 && !newTitle.equals(oldTitle)) {
                        if (i > 1) {
                            newTitle = newTitle.substring(0, newTitle.length() - 4);
                        }
                        model.setTitle(newTitle + " (" + i + ")");
                    }
                } while (count > 0 && !newTitle.equals(oldTitle));
            }

            Map<String, String> map = new HashMap<>(model.getConfig().getTitle());
            map.put("text", model.getTitle());
            model.getConfig().setTitle(map);

            session.update(model);
            session.getTransaction().commit();
        } catch (Exception e)
        {
            if(session.getTransaction() != null)
            {
                session.getTransaction().rollback();
                throw e;
            }
        }

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @ApiOperation(value = "Copy a model", response = Model.class)
    @ApiResponses(value = { @ApiResponse(code = 201, message = "Model copied"), @ApiResponse(code = 404, message = "Not found") })
    @RequestMapping(value = "/{slug}/copies", method = RequestMethod.POST)
    public ResponseEntity<Model> copyAModel(
            @ApiParam(value = "slug", required = true) @PathVariable("slug") String slug,
            @RequestBody @ApiParam(value = "Model to update", required = true) Model model
    )  {

        User user = mipApplication.getUser();

        String originalSlug = model.getSlug();
        String copySlug;
        do {
            copySlug = originalSlug+" copy_"+randomStr(20);
        } while (getAModel(copySlug) == null);
        model.setSlug(copySlug);

        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        try{
            session.beginTransaction();
            session.save(model);
            session.getTransaction().commit();
        } catch (Exception e)
        {
            if(session.getTransaction() != null)
            {
                session.getTransaction().rollback();
                throw e;
            }
        }

        return new ResponseEntity<>(HttpStatus.CREATED).ok(model);
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


}
