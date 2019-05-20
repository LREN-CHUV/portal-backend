package eu.hbp.mip.controllers;

import com.google.gson.*;
import eu.hbp.mip.akka.WokenClientController;
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
public class MethodsApi extends WokenClientController {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodsApi.class);

    private static final Gson gson = new Gson();
d
    @Value("#{'${services.exareme.algorithmsUrl:http://localhost:9090/mining/algorithms.json}'}")
    private String exaremeAlgorithmsUrl;

    @ApiOperation(value = "List available methods and validations", response = String.class)
    @Cacheable(value = "methods", unless = "#result.getStatusCode().value()!=200")
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity listAvailableMethodsAndValidations() {
        LOGGER.info("List available methods and validations");

        return requestWoken(MethodsQuery$.MODULE$, 10, r -> {
            MethodsResponse result = (MethodsResponse) r;
            JsonObject catalog = new JsonParser().parse(result.methods().compactPrint()).getAsJsonObject();

            return ResponseEntity.ok(gson.toJson(catalog));
        });
    }

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

}
