/**
 * Created by mirco on 02.03.16.
 */

package org.hbp.mip.controllers;

import com.google.gson.*;
import io.swagger.annotations.*;
import org.hbp.mip.model.algorithm.Algorithm;
import org.hbp.mip.model.algorithm.Catalog;
import org.hbp.mip.utils.HTTPUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.UnknownHostException;

@RestController
@RequestMapping(value = "/mining")
@Api(value = "/mining", description = "Forward mining API")
public class MiningApi {

    private static final String EXAREME_ALGO_JSON_FILE="data/exareme_algorithms.json";

    private static final String ML_SOURCE = "ML";

    private static final String EXAREME_SOURCE = "exareme";

    @Value("#{'${workflow.listMethodsUrl:http://hbps1.chuv.ch:8087/list-methods}'}")
    private String listMethodsUrl;

    @Value("#{'${workflow.miningMipUrl:http://hbps1.chuv.ch:8087/mining}'}")
    private String miningMipUrl;

    @Value("#{'${workflow.miningExaremeUrl:http://hbps2.chuv.ch:9090/mining/query}'}")
    private String miningExaremeQueryUrl;

    @ApiOperation(value = "Send a request to the workflow for data mining", response = String.class)
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Success") })
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<String> postMining(
            @RequestBody @ApiParam(value = "Query for the data mining", required = true) String query
    ) throws Exception {

        StringBuilder response = new StringBuilder();

        int code = HTTPUtil.sendGet(listMethodsUrl, response);
        if (code < 200 || code > 299) {
            return new ResponseEntity<>(response.toString(), HttpStatus.valueOf(code));
        }

        Catalog catalog = new Gson().fromJson(response.toString(), Catalog.class);
        for (Algorithm algo: catalog.getAlgorithms()) {
            algo.setSource(ML_SOURCE);
        }

        InputStream is = MiningApi.class.getClassLoader().getResourceAsStream(EXAREME_ALGO_JSON_FILE);
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(isr);
        Algorithm exaremeGLR = new Gson().fromJson(br, Algorithm.class);
        exaremeGLR.setSource(EXAREME_SOURCE);
        catalog.getAlgorithms().add(exaremeGLR);

        String algoCode = new JsonParser().parse(query).getAsJsonObject()
                .get("algorithm").getAsJsonObject()
                .get("code").getAsString();

        for(Algorithm algo : catalog.getAlgorithms())
        {
            if (algo.getCode().equals(algoCode))
            {
                if(algo.getSource().equals(ML_SOURCE)) {
                    return postMipMining(query);
                }
                else if(algo.getSource().equals(EXAREME_SOURCE))
                {
                    return postExaremeMining("WP_STATISTICS", query);
                }
            }
        }

        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    private ResponseEntity<String> postMipMining(String query) throws Exception {
        try {
            StringBuilder results = new StringBuilder();
            int code = HTTPUtil.sendPost(miningMipUrl, query, results);

            return new ResponseEntity<>(results.toString(), HttpStatus.valueOf(code));
        }
        catch(UnknownHostException uhe) {
            uhe.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_GATEWAY);
        }
    }


    private ResponseEntity<String> postExaremeMining(String algo, String query) throws Exception {
        try {

            /* Launch computation */

            String url = miningExaremeQueryUrl +"/"+algo+"/?format=true";
            StringBuilder results = new StringBuilder();
            int code = HTTPUtil.sendPost(url, query, results);
            if (code < 200 || code > 299)
            {
                return new ResponseEntity<>(results.toString(), HttpStatus.valueOf(code));
            }

            JsonParser parser = new JsonParser();
            String key = parser.parse(results.toString()).getAsJsonObject().get("queryKey").getAsString();

            /* Wait for result */

            url = miningExaremeQueryUrl +"/"+key+"/status";
            double progress = 0;

            while (progress < 100) {
                Thread.sleep(200);
                results = new StringBuilder();
                code = HTTPUtil.sendPost(url, query, results);
                if (code < 200 || code > 299)
                {
                    return new ResponseEntity<>(results.toString(), HttpStatus.valueOf(code));
                }
                progress = parser.parse(results.toString()).getAsJsonObject().get("status").getAsDouble();
            }

            /* Get result */

            url = miningExaremeQueryUrl +"/"+key+"/result";
            results = new StringBuilder();
            code = HTTPUtil.sendPost(url, query, results);

            return new ResponseEntity<>(results.toString(), HttpStatus.valueOf(code));
        }
        catch(UnknownHostException uhe) {
            uhe.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_GATEWAY);
        }
    }

}
