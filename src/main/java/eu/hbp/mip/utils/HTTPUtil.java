package eu.hbp.mip.utils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by mirco on 20.06.16.
 */
public class HTTPUtil {

    private HTTPUtil()
    {
        /* Hide implicit public constructor */
        throw new IllegalAccessError("HTTPUtil class");
    }

    public static int sendGet(String url, StringBuilder resp) throws IOException {
        return sendHTTP(url, "", resp, "GET");
    }

    public static int sendPost(String url, String query, StringBuilder resp) throws IOException {
        return sendHTTP(url, query, resp, "POST");
    }

    public static int sendHTTP(String url, String query, StringBuilder resp, String httpVerb) throws IOException {

        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        if(!"GET".equals(httpVerb)) {
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