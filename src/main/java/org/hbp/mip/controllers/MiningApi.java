/**
 * Created by mirco on 02.03.16.
 */

package org.hbp.mip.controllers;

import com.google.gson.Gson;
import com.google.gson.JsonParser;
import io.swagger.annotations.*;
import org.hbp.mip.model.ExaremeQueryElement;
import org.hbp.mip.model.Model;
import org.hbp.mip.model.SimpleMiningQuery;
import org.hbp.mip.model.Variable;
import org.hbp.mip.model.algorithm.Algorithm;
import org.hbp.mip.model.algorithm.Catalog;
import org.hbp.mip.utils.HTTPUtil;
import org.hbp.mip.utils.HibernateUtil;
import org.hibernate.Session;
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
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

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

        String modelSlug = new JsonParser().parse(query).getAsJsonObject()
                .get("model").getAsString();

        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Model model = null;

        try {
            session.beginTransaction();
            model = (Model) session.createQuery("FROM Model WHERE slug= :slug")
                    .setString("slug", modelSlug)
                    .uniqueResult();
            session.getTransaction().commit();
        } catch (Exception e)
        {
            if(session.getTransaction() != null)
            {
                session.getTransaction().rollback();
                throw e;
            }
        }

        for(Algorithm algo : catalog.getAlgorithms())
        {
            if (algo.getCode().equals(algoCode))
            {
                if(algo.getSource().equals(ML_SOURCE)) {
                    Algorithm algorithm = new Gson().fromJson(new JsonParser().parse(query).getAsJsonObject()
                            .get("algorithm").getAsJsonObject(), Algorithm.class);
                    return postMipMining(algorithm, model);
                }
                else if(algo.getSource().equals(EXAREME_SOURCE))
                {
                    return postExaremeMining("WP_LINEAR_REGRESSION", model);
                }
            }
        }

        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    private ResponseEntity<String> postMipMining(Algorithm algorithm, Model model) throws Exception {

        SimpleMiningQuery smq = new SimpleMiningQuery();
        smq.setAlgorithm(algorithm);

        LinkedList<Map<String,String>> vars = new LinkedList<>();
        for(Variable var : model.getQuery().getVariables())
        {
            Map<String,String> m = new HashMap<>();
            m.put("code", var.getCode());
            vars.add(m);
        }
        smq.setVariables(vars);

        LinkedList<Map<String,String>> covars = new LinkedList<>();
        for(Variable var : model.getQuery().getCovariables())
        {
            Map<String,String> m = new HashMap<>();
            m.put("code", var.getCode());
            covars.add(m);
        }
        smq.setCovariables(covars);

        LinkedList<Map<String,String>> grps = new LinkedList<>();
        for(Variable var : model.getQuery().getGrouping())
        {
            Map<String,String> m = new HashMap<>();
            m.put("code", var.getCode());
            grps.add(m);
        }
        smq.setGrouping(grps);

        smq.setFilters(new LinkedList<>());

        try {
            StringBuilder results = new StringBuilder();
            String jsonQuery = new Gson().toJson(smq);

            System.out.println("****************************************");
            System.out.println("SimpleMining content : " + jsonQuery);
            System.out.println("****************************************");

            int code = HTTPUtil.sendPost(miningMipUrl, jsonQuery, results);

            return new ResponseEntity<>(results.toString(), HttpStatus.valueOf(code));
        }
        catch(UnknownHostException uhe) {
            uhe.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_GATEWAY);
        }
    }


    private ResponseEntity<String> postExaremeMining(String algoCode, Model model) throws Exception {

        LinkedList<ExaremeQueryElement> queryElements = new LinkedList<>();
        for (Variable var : model.getQuery().getVariables())
        {
            ExaremeQueryElement el = new ExaremeQueryElement();
            el.setName("variable");
            el.setDesc("");
            el.setValue(var.getCode());
            queryElements.add(el);
        }
        for (Variable var : model.getQuery().getCovariables())
        {
            ExaremeQueryElement el = new ExaremeQueryElement();
            el.setName("covariables");
            el.setDesc("");
            el.setValue(var.getCode());
            queryElements.add(el);
        }
        for (Variable var : model.getQuery().getGrouping())
        {
            ExaremeQueryElement el = new ExaremeQueryElement();
            el.setName("groupings");
            el.setDesc("");
            el.setValue(var.getCode());
            queryElements.add(el);
        }

        ExaremeQueryElement tableEl = new ExaremeQueryElement();
        tableEl.setName("showtable");
        tableEl.setDesc("");
        tableEl.setValue("TotalResults");
        queryElements.add(tableEl);

        ExaremeQueryElement formatEl = new ExaremeQueryElement();
        formatEl.setName("format");
        formatEl.setDesc("");
        formatEl.setValue("True");
        queryElements.add(formatEl);

        String jsonQuery = new Gson().toJson(queryElements);

        System.out.println("****************************************");
        System.out.println("ExaremeMining content : " + jsonQuery);
        System.out.println("****************************************");

        try {

            /* Launch computation */

            String url = miningExaremeQueryUrl +"/"+algoCode+"/?format=true";
            StringBuilder results = new StringBuilder();
            int code = HTTPUtil.sendPost(url, jsonQuery, results);
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
                code = HTTPUtil.sendPost(url, jsonQuery, results);
                if (code < 200 || code > 299)
                {
                    return new ResponseEntity<>(results.toString(), HttpStatus.valueOf(code));
                }
                progress = parser.parse(results.toString()).getAsJsonObject().get("status").getAsDouble();
            }

            /* Get result */

            url = miningExaremeQueryUrl +"/"+key+"/result";
            results = new StringBuilder();
            code = HTTPUtil.sendPost(url, jsonQuery, results);

            return new ResponseEntity<>(results.toString(), HttpStatus.valueOf(code));
        }
        catch(UnknownHostException uhe) {
            uhe.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_GATEWAY);
        }
    }

}
