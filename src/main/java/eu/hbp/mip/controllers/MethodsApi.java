package eu.hbp.mip.controllers;

import com.google.gson.*;

import ch.chuv.lren.woken.messages.query.MethodsQuery$;
import ch.chuv.lren.woken.messages.query.MethodsResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import eu.hbp.mip.utils.HTTPUtil;
import org.springframework.beans.factory.annotation.Value;
import java.io.IOException;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/methods", produces = { APPLICATION_JSON_VALUE })
@Api(value = "/methods", description = "the methods API")
public class MethodsApi {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodsApi.class);

    private static final Gson gson = new Gson();

    @Value("#{'${services.exareme.algorithmsUrl:http://localhost:9090/mining/algorithms.json}'}")
    private String exaremeAlgorithmsUrl;

    @Value("#{'${services.workflows.workflowUrl}'}")
    private String workflowUrl;

    @Value("#{'${services.workflows.workflowAuthorization}'}")
    private String workflowAuthorization;

    @ApiOperation(value = "List Exareme algorithms and validations", response = String.class)
    @Cacheable(value = "exareme", unless = "#result.getStatusCode().value()!=200")
    @RequestMapping(value = "/exareme", method = RequestMethod.GET)
    public ResponseEntity<Object> getExaremeAlgorithms() {
        LOGGER.info("List Exareme algorithms and validations");

        try {
            StringBuilder response = new StringBuilder();
            HTTPUtil.sendGet(exaremeAlgorithmsUrl, response);
            JsonElement element = new JsonParser().parse(response.toString());

            return ResponseEntity.ok(gson.toJson(element));
        } catch (IOException e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }

    }

    @ApiOperation(value = "List Galaxy workflows", response = String.class)
    @RequestMapping(value = "/workflows", method = RequestMethod.GET)
    public ResponseEntity<Object> getWorkflows() {
        LOGGER.info("List Galaxy workflows");

        try {
            StringBuilder response = new StringBuilder();
            HTTPUtil.sendAuthorizedHTTP(workflowUrl + "/getAllWorkflowWithDetails", "", response, "GET", workflowAuthorization);
            LOGGER.info("************************************************* workflows");
            LOGGER.info(workflowUrl + "/getAllWorkflowWithDetails");
            LOGGER.info(workflowAuthorization);
            LOGGER.info(response.toString());
            JsonElement element = new JsonParser().parse(response.toString());

            return ResponseEntity.ok(gson.toJson(element));
        } catch (IOException e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }

    }

}
