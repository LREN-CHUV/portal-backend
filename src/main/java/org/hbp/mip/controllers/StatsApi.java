/**
 * Created by mirco on 18.01.16.
 */

package org.hbp.mip.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.hbp.mip.model.GeneralStats;
import org.hbp.mip.repositories.ArticleRepository;
import org.hbp.mip.repositories.UserRepository;
import org.hbp.mip.repositories.VariableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/stats", produces = {APPLICATION_JSON_VALUE})
@Api(value = "/stats", description = "the stats API")
public class StatsApi {

    @Autowired
    UserRepository userRepository;

    @Autowired
    ArticleRepository articleRepository;

    @Autowired
    VariableRepository variableRepository;

    @ApiOperation(value = "Get general statistics", response = GeneralStats.class)
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Found"), @ApiResponse(code = 404, message = "Not found") })
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<GeneralStats> getGeneralStatistics()  {

        GeneralStats stats = new GeneralStats();

        stats.setUsers(userRepository.count());
        stats.setArticles(articleRepository.count());
        stats.setVariables(variableRepository.count());

        return ResponseEntity.ok(stats);
    }

}
