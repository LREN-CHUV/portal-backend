package eu.hbp.mip.controllers;

import com.google.gson.Gson;
import eu.hbp.mip.akka.WokenClientController;
import eu.hbp.mip.configuration.SecurityConfiguration;
import eu.hbp.mip.model.ExaremeQuery;
import eu.hbp.mip.model.Mining;
import eu.hbp.mip.model.User;
import eu.hbp.mip.utils.HTTPUtil;
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
import scala.Some;

import java.io.IOException;
import java.sql.Date;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * Created by mirco on 06.01.17.
 */
@RestController
@RequestMapping(value = "/mining", produces = {APPLICATION_JSON_VALUE})
@Api(value = "/mining", description = "the mining API")
public class MiningApi extends WokenClientController {

    private static final Logger LOGGER = LoggerFactory.getLogger(MiningApi.class);
    private static final Gson gson = new Gson();

    @Autowired
    private SecurityConfiguration securityConfiguration;

    @Value("#{'${services.query.miningExaremeUrl:http://localhost:9090/mining/query}'}")
    public String queryUrl;

    @ApiOperation(value = "Run an algorithm", response = String.class)
    @Cacheable(value = "mining",
            condition = "#query != null and (#query.getAlgorithm().getCode() == 'histograms' or #query.getAlgorithm().getCode() == 'histograms')",
            key = "#query.toString()",
            unless = "#result.getStatusCode().value()!=200")
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity runAlgorithm(@RequestBody eu.hbp.mip.model.MiningQuery query) {
        LOGGER.info("Run an algorithm");
        User user = securityConfiguration.getUser();

        if (isExaremeAlgo(query)) {
            LOGGER.info("isExaremeAlgo");

            String algorithm = query.getAlgorithm().getCode();
            String exaremeQuery = ExaremeQuery.query(query);
            String url = queryUrl + "/" + algorithm;

            // TODO: Threaded call
            try {
                StringBuilder results = new StringBuilder();
                int code = HTTPUtil.sendPost(url, exaremeQuery, results);
                LOGGER.info("Results " + results);

                if (code >= 500) {
                    LOGGER.error("Cannot receive algorithm result from exareme");
                    return ResponseEntity.status(code).build();
                }

                Mining mining = new Mining(
                        "exaremeJobId",
                        "federation",
                        algorithm,
                        "application/json",
                        new java.util.Date(),
                        results.toString());

                return ResponseEntity.ok(gson.toJson(mining.jsonify()));
            } catch (IOException e) {
                LOGGER.error("Cannot receive algorithm result from exareme" + e.getMessage(), e);
                return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
            }
        } else {

            return askWokenQuery(query.prepareQuery(user.getUsername()), 120,
                    result -> {
                        if (result.error().nonEmpty()) {
                            LOGGER.error(result.error().get());
                            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\":\"" + result.error().get() + "\"}");
                        } else {
                            Mining mining = new Mining(
                                    unwrap(result.jobId()),
                                    result.node(),
                                    unwrap(result.algorithm()),
                                    result.type().mime(),
                                    Date.from(result.timestamp().toInstant()),
                                    result.data().get().compactPrint()
                            );
                            return ResponseEntity.ok(gson.toJson(mining.jsonify()));
                        }
                    });
        }
    }

    private static String unwrap(Option<String> option) {
        if (option.isDefined())
            return option.get();
        else
            return "";
    }

    private static boolean isExaremeAlgo(eu.hbp.mip.model.MiningQuery query) {
        return query.getAlgorithm().getCode().length() > 0 && "WP_".equals(
                query.getAlgorithm().getCode().substring(0, 3));
    }

}
