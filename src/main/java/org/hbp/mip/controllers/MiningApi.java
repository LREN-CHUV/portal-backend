/**
 * Created by mirco on 02.03.16.
 */

package org.hbp.mip.controllers;

import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Value;
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

@RestController
@RequestMapping(value = "/mining")
@Api(value = "/mining", description = "Forward mining API")
public class MiningApi {

    @Value("#{'${workflow.miningUrl:http://localhost:8087/mining}'}")
    private String miningUrl;

    @ApiOperation(value = "Send a request to the workflow for data mining", response = String.class)
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Success") })
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<String> postMining(
            @RequestBody @ApiParam(value = "Model to create", required = true) String query
    ) throws Exception {

        StringBuilder results = new StringBuilder();
        int code = sendPost(miningUrl, query, results);

        return new ResponseEntity<>(results.toString(), HttpStatus.valueOf(code));
    }


    private static int sendPost(String url, String query, StringBuilder resp) throws Exception {

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
        } catch (Exception ignored) {
        }
    }
}
