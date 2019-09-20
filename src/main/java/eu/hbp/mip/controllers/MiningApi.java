package eu.hbp.mip.controllers;

import eu.hbp.mip.utils.HTTPUtil;

import com.google.gson.Gson;
import eu.hbp.mip.akka.WokenClientController;
import eu.hbp.mip.model.Mining;
import eu.hbp.mip.model.User;
import eu.hbp.mip.model.UserInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import scala.Option;

import java.sql.Date;
import java.util.*;
import java.io.IOException;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * Created by mirco on 06.01.17.
 */
@RestController
@RequestMapping(value = "/mining", produces = { APPLICATION_JSON_VALUE })
@Api(value = "/mining", description = "the mining API")
public class MiningApi extends WokenClientController {

    private static final Logger LOGGER = LoggerFactory.getLogger(MiningApi.class);
    private static final Gson gson = new Gson();

    @Autowired
    private UserInfo userInfo;

    @Value("#{'${services.exareme.miningExaremeUrl:http://localhost:9090/mining/query}'}")
    public String miningExaremeQueryUrl;

    @ApiOperation(value = "Run an algorithm", response = String.class)
    @Cacheable(value = "mining", condition = "#query != null and (#query.getAlgorithm().getCode() == 'histograms' or #query.getAlgorithm().getCode() == 'histograms')", key = "#query.toString()", unless = "#result.getStatusCode().value()!=200")
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity runAlgorithm(@RequestBody eu.hbp.mip.model.MiningQuery query) {
        LOGGER.info("Run an algorithm");
        User user = userInfo.getUser();

        return askWokenQuery(query.prepareQuery(user.getUsername()), 120, result -> {
            if (result.error().nonEmpty()) {
                LOGGER.error(result.error().get());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("{\"error\":\"" + result.error().get() + "\"}");
            } else {
                Mining mining = new Mining(result.jobId(), result.node(), unwrap(result.algorithm()),
                        result.type().mime(), Date.from(result.timestamp().toInstant()),
                        result.data().get().compactPrint());
                return ResponseEntity.ok(gson.toJson(mining.jsonify()));
            }
        });
    }

    @ApiOperation(value = "Create an histogram on Exareme", response = String.class)
    @RequestMapping(value = "/exareme", method = RequestMethod.POST)
    public ResponseEntity runExaremeMining(@RequestBody List<HashMap<String, String>> queryList) {
        LOGGER.info("Run an histogram");

        String query = gson.toJson(queryList);
        String url = miningExaremeQueryUrl + "/" + "HISTOGRAMS";

        try {
            StringBuilder results = new StringBuilder();
            int code = HTTPUtil.sendPost(url, query, results);

            return ResponseEntity.ok(gson.toJson(results.toString()));
        } catch (IOException e) {
            return new ResponseEntity<>("Not found", HttpStatus.BAD_REQUEST);
        }
    }

    @ApiOperation(value = "Create an descriptive statistic on Exareme", response = String.class)
    @RequestMapping(value = "/exareme-stats", method = RequestMethod.POST)
    public ResponseEntity runExaremeDescriptiveStats(@RequestBody List<HashMap<String, String>> queryList) {
        LOGGER.info("Run descriptive stats");

        String query = gson.toJson(queryList);
        String url = miningExaremeQueryUrl + "/" + "DESCRIPTIVE_STATS";

        try {
            StringBuilder results = new StringBuilder();
            int code = HTTPUtil.sendPost(url, query, results);

            return ResponseEntity.ok(gson.toJson(results.toString()));
        } catch (IOException e) {
            return new ResponseEntity<>("Not found", HttpStatus.BAD_REQUEST);
        }
    }

    private static String unwrap(Option<String> option) {
        if (option.isDefined())
            return option.get();
        else
            return "";
    }
}
