/**
 * Created by mirco on 02.03.16.
 */

package org.hbp.mip.controllers;

import com.google.gson.JsonParser;
import io.swagger.annotations.*;
import org.hbp.mip.utils.HTTPUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.net.UnknownHostException;

@RestController
@RequestMapping(value = "/mining")
@Api(value = "/mining", description = "Forward mining API")
public class MiningApi {

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
        // TODO : switch between sources

        return null;
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


    public ResponseEntity<String> postExaremeMining(String algo, String query) throws Exception {
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
