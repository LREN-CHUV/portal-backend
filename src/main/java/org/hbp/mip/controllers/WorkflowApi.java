package org.hbp.mip.controllers;

import io.swagger.annotations.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.URL;

/**
 * Created by mirco on 02.03.16.
 */
@RestController
@RequestMapping(value = "/workflow/{algo}")
@Api(value = "/workflow/{algo}", description = "Forward workflow API")
public class WorkflowApi {

    @ApiOperation(value = "Send a request to the workflow", response = String.class)
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Success") })
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<String> postWorkflow(
            @ApiParam(value = "algo", required = true) @PathVariable("algo") String algo,
            @RequestBody @ApiParam(value = "Model to create", required = true) String query
    ) throws Exception {

        String results = "";

        if(algo.equals("glr"))
        {
            results = sendPost("https://mip.humanbrainproject.eu/services/request", query);
        }
        else if(algo.equals("anv"))
        {
            results = "";
        }

        return new ResponseEntity<>(HttpStatus.OK).ok(results);
    }


    private static String sendPost(String url, String query) throws Exception {

        URL obj = new URL(url);
        HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
        // TODO: Remove this line for security
        allowInsecureConnection(con);

        con.setRequestMethod("POST");
        con.addRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("Content-Length", Integer.toString(query.length()));

        con.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.write(query.getBytes("UTF8"));
        wr.flush();
        wr.close();

        con.getResponseCode();

        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        return response.toString();
    }

    private static void allowInsecureConnection(HttpsURLConnection con) {
        TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }
                    public void checkClientTrusted(
                            java.security.cert.X509Certificate[] certs, String authType) {
                    }
                    public void checkServerTrusted(
                            java.security.cert.X509Certificate[] certs, String authType) {
                    }
                }
        };

        try {
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            con.setSSLSocketFactory(sc.getSocketFactory());
        } catch (Exception e) {
        }
    }
}
