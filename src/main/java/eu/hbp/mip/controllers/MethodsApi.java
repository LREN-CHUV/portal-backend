package eu.hbp.mip.controllers;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
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

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/methods", produces = {APPLICATION_JSON_VALUE})
@Api(value = "/methods", description = "the methods API")
public class MethodsApi extends WokenClientController {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodsApi.class);

    private static final Gson gson = new Gson();

    private static final String EXAREME_ALGO_JSON_FILE="data/exareme_algorithms.json";

    @ApiOperation(value = "List available methods and validations", response = String.class)
    @Cacheable(value = "methods", unless = "#result.getStatusCode().value()!=200")
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity listAvailableMethodsAndValidations() {
        LOGGER.info("List available methods and validations");

        return askWoken(MethodsQuery$.MODULE$, 10, r -> {
            MethodsResponse result = (MethodsResponse) r;

            // >> Temporary : should return result.methods() in the future
            JsonObject catalog = new JsonParser().parse(result.methods()).getAsJsonObject();
            InputStream is = MethodsApi.class.getClassLoader().getResourceAsStream(EXAREME_ALGO_JSON_FILE);
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            JsonObject exaremeAlgo = new JsonParser().parse(br).getAsJsonObject();
            catalog.get("algorithms").getAsJsonArray().add(exaremeAlgo);
            // << Temporary

            return ResponseEntity.ok(gson.toJson(catalog));
        });
    }

}
