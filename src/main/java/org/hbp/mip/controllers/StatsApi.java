package org.hbp.mip.controllers;

import io.swagger.annotations.*;
import org.hbp.mip.model.GeneralStats;
import org.hbp.mip.model.Statistics;
import org.hbp.mip.utils.HibernateUtil;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Controller
@RequestMapping(value = "/stats", produces = {APPLICATION_JSON_VALUE})
@Api(value = "/stats", description = "the stats API")
public class StatsApi {

    @ApiOperation(value = "Get general statistics", response = GeneralStats.class)
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Found"), @ApiResponse(code = 404, message = "Not found") })
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<GeneralStats> getGeneralStatistics() throws NotFoundException {
        GeneralStats stats = new GeneralStats();

        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        Query countUsersQuery = session.createQuery("SELECT COUNT(*) FROM User");
        Query countArticlesQuery = session.createQuery("SELECT COUNT(*) FROM Article");
        Query countVariablesQuery = session.createQuery("SELECT COUNT(*) FROM Variable");
        Long nbUsers = (Long) countUsersQuery.uniqueResult();
        Long nbArticles = (Long) countArticlesQuery.uniqueResult();
        Long nbVariables = (Long) countVariablesQuery.uniqueResult();
        session.getTransaction().commit();

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
    ) throws NotFoundException {
        // TODO: Implement this method
        return new ResponseEntity<List<Statistics>>(HttpStatus.OK);
    }

}
