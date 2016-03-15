/**
 * Created by mirco on 18.01.16.
 */

package org.hbp.mip.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.hbp.mip.model.GeneralStats;
import org.hbp.mip.utils.HibernateUtil;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/stats", produces = {APPLICATION_JSON_VALUE})
@Api(value = "/stats", description = "the stats API")
public class StatsApi {

    @ApiOperation(value = "Get general statistics", response = GeneralStats.class)
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Found"), @ApiResponse(code = 404, message = "Not found") })
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<GeneralStats> getGeneralStatistics()  {

        GeneralStats stats = new GeneralStats();
        Long nbUsers = 0L;
        Long nbArticles = 0L;
        Long nbVariables = 0L;

        Session session = HibernateUtil.getSessionFactory().getCurrentSession();

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
                throw e;
            }
        }

        stats.setUsers(nbUsers);
        stats.setArticles(nbArticles);
        stats.setVariables(nbVariables);

        return ResponseEntity.ok(stats);
    }

}
