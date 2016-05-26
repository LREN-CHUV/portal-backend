/**
 * Created by mirco on 02.03.16.
 */

package org.hbp.mip.controllers;

import com.google.gson.JsonParser;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.UnknownHostException;

@RestController
@RequestMapping(value = "/mining")
@Api(value = "/mining", description = "Forward mining API")
public class MiningApi {

    @Value("#{'${workflow.miningUrl:http://localhost:8087/mining}'}")
    private String miningUrl;

    @Value("#{'${workflow.exaremeListAlgoUrl:http://localhost:9090/mining/algorithms}'}")
    private String exaremeListAlgoUrl;

    @Value("#{'${workflow.exaremeQueryUrl:http://localhost:9090/mining/query}'}")
    private String exaremeQueryUrl;

    @ApiOperation(value = "Send a request to the workflow for data mining", response = String.class)
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Success") })
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<String> postMining(
            @RequestBody @ApiParam(value = "Query for the data mining", required = true) String query
    ) throws Exception {
        try {
            StringBuilder results = new StringBuilder();
            int code = sendPost(miningUrl, query, results);

            return new ResponseEntity<>(results.toString(), HttpStatus.valueOf(code));
        }
        catch(UnknownHostException uhe) {
            uhe.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_GATEWAY);
        }
    }

    @ApiOperation(value = "Send a request to the Exareme service to list available algorithms", response = String.class)
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Success") })
    @RequestMapping(path = "/exareme/algorithms", method = RequestMethod.GET)
    public ResponseEntity<String> getExaremeAlgoList(
    ) throws Exception {
        try {
            StringBuilder results = new StringBuilder();
            int code = sendGet(exaremeListAlgoUrl, results);

            return new ResponseEntity<>(results.toString(), HttpStatus.valueOf(code));
        }
        catch(UnknownHostException uhe) {
            uhe.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_GATEWAY);
        }
    }

    @ApiOperation(value = "Send a request to the Exareme service to run an algorithm", response = String.class)
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Success") })
    @RequestMapping(path = "/exareme/query/{algo}", method = RequestMethod.POST)
    public ResponseEntity<String> postExaremeQuery(
            @ApiParam(value = "algo", required = true) @PathVariable("algo") String algo,
            @RequestBody @ApiParam(value = "Query for the data mining", required = true) String query
    ) throws Exception {
        try {

            /* Launch computation */

            String url = exaremeQueryUrl+"/"+algo+"/?format=true";
            StringBuilder results = new StringBuilder();
            int code = sendPost(url, query, results);
            if (code < 200 || code > 299)
            {
                return new ResponseEntity<>(results.toString(), HttpStatus.valueOf(code));
            }

            JsonParser parser = new JsonParser();
            String key = parser.parse(results.toString()).getAsJsonObject().get("queryKey").getAsString();

            /* Wait for result */

            url = exaremeQueryUrl+"/"+key+"/status";
            double progress = 0;

            while (progress < 100) {
                Thread.sleep(200);
                results = new StringBuilder();
                code = sendPost(url, query, results);
                if (code < 200 || code > 299)
                {
                    return new ResponseEntity<>(results.toString(), HttpStatus.valueOf(code));
                }
                progress = parser.parse(results.toString()).getAsJsonObject().get("status").getAsDouble();
            }

            /* Get result */

            url = exaremeQueryUrl+"/"+key+"/result";
            results = new StringBuilder();
            code = sendPost(url, query, results);

            return new ResponseEntity<>(results.toString(), HttpStatus.valueOf(code));
        }
        catch(UnknownHostException uhe) {
            uhe.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_GATEWAY);
        }
    }

    private static int sendGet(String url, StringBuilder resp) throws Exception {
        return sendHTTP(url, "", resp, "GET");
    }

    private static int sendPost(String url, String query, StringBuilder resp) throws Exception {
        return sendHTTP(url, query, resp, "POST");
    }

    private static int sendHTTP(String url, String query, StringBuilder resp, String httpVerb) throws Exception {

        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        if(!httpVerb.equals("GET")) {
            con.setRequestMethod(httpVerb);
            con.addRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("Content-Length", Integer.toString(query.length()));

            con.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.write(query.getBytes("UTF8"));
            wr.flush();
            wr.close();
        }

        int respCode = con.getResponseCode();

        BufferedReader in;
        if(respCode == 200) {
            in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        }
        else
        {
            in = new BufferedReader(new InputStreamReader(con.getErrorStream()));
        }
        String inputLine;
        StringBuilder response = new StringBuilder();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        resp.append(response.toString());

        return respCode;
    }

}
