package org.hbp.mip.controllers;

import io.swagger.annotations.*;
import org.hbp.mip.model.GeneralStats;
import org.hbp.mip.model.Statistics;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/stats", produces = {APPLICATION_JSON_VALUE})
@Api(value = "/stats", description = "the stats API")
public class StatsApi {

    @Autowired
	SessionFactory sessionFactoryBean;

    @ApiOperation(value = "Get general statistics", response = GeneralStats.class)
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Found"), @ApiResponse(code = 404, message = "Not found") })
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<GeneralStats> getGeneralStatistics()  {
        GeneralStats stats = new GeneralStats();


        Session session = sessionFactoryBean.getCurrentSession();
        Long nbUsers = 0L;
        Long nbArticles = 0L;
        Long nbVariables = 0L;
        try{
            session.beginTransaction();
            Query countUsersQuery = session.createQuery("SELECT COUNT(*) FROM User");
            Query countArticlesQuery = session.createQuery("SELECT COUNT(*) FROM Article");
            Query countVariablesQuery = session.createQuery("SELECT COUNT(*) FROM Variable");
            nbUsers = (Long) countUsersQuery.uniqueResult();
            nbArticles = (Long) countArticlesQuery.uniqueResult();
            nbVariables = (Long) countVariablesQuery.uniqueResult();
            session.getTransaction().commit();
        } catch (Exception e)
        {
            if(session.getTransaction() != null)
            {
                session.getTransaction().rollback();
            }
        }

        stats.setUsers(nbUsers);
        stats.setArticles(nbArticles);
        stats.setVariables(nbVariables);

        return new ResponseEntity<GeneralStats>(HttpStatus.OK).ok(stats);
    }

    @ApiOperation(value = "Get the statistics for a group or a variable", response = Statistics.class, responseContainer = "List")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Found"), @ApiResponse(code = 404, message = "Not found") })
    @RequestMapping(value = "/{code}", produces = { "application/json" }, method = RequestMethod.GET)
    public ResponseEntity<List<Statistics>> getTheStatisticsForAGroupOrAVariable(
            @ApiParam(value = "code of the group or variable",required=true ) @PathVariable("code") String code
    )  {
        // TODO: Implement this method
        return new ResponseEntity<List<Statistics>>(HttpStatus.OK);
    }

}
