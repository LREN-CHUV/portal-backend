/**
 * Created by mirco on 02.03.16.
 */

package org.hbp.mip.controllers;

import com.google.gson.Gson;
import com.google.gson.JsonParser;
import io.swagger.annotations.*;
import org.hbp.mip.model.algorithm.Algorithm;
import org.hbp.mip.model.algorithm.Catalog;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.UnknownHostException;

@RestController
@RequestMapping(value = "/mining")
@Api(value = "/mining", description = "Forward mining API")
public class MiningApi {

    private static final String EXAREME_ALGO_JSON_FILE="data/exareme_algorithms.json";

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

        StringBuilder response = new StringBuilder();

        int code = sendGet(listMethodsUrl, response);
        if (code < 200 || code > 299) {
            return new ResponseEntity<>(response.toString(), HttpStatus.valueOf(code));
        }

        Catalog catalog = new Gson().fromJson(response.toString(), Catalog.class);
        for (Algorithm algo: catalog.getAlgorithms()) {
            algo.setSource("ML");
        }

        InputStream is = MiningApi.class.getClassLoader().getResourceAsStream(EXAREME_ALGO_JSON_FILE);
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(isr);
        Algorithm exaremeGLR = new Gson().fromJson(br, Algorithm.class);
        exaremeGLR.setSource("exareme");
        catalog.getAlgorithms().add(exaremeGLR);

        return new ResponseEntity<>(new Gson().toJson(catalog), HttpStatus.valueOf(code));
    }

    private ResponseEntity<String> postMipMining(String query) throws Exception {
        try {
            StringBuilder results = new StringBuilder();
            int code = sendPost(miningMipUrl, query, results);

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
            int code = sendPost(url, query, results);
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
                code = sendPost(url, query, results);
                if (code < 200 || code > 299)
                {
                    return new ResponseEntity<>(results.toString(), HttpStatus.valueOf(code));
                }
                progress = parser.parse(results.toString()).getAsJsonObject().get("status").getAsDouble();
            }

            /* Get result */

            url = miningExaremeQueryUrl +"/"+key+"/result";
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
            if(query != null && query.length() > 0)
            {
                con.addRequestProperty("Content-Type", "application/json");
                con.setRequestProperty("Content-Length", Integer.toString(query.length()));

                con.setDoOutput(true);
                DataOutputStream wr = new DataOutputStream(con.getOutputStream());
                wr.write(query.getBytes("UTF8"));
                wr.flush();
                wr.close();
            }
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
