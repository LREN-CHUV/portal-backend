package eu.hbp.mip.model;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Value;

import java.util.LinkedList;
import java.util.List;


public class ExaremeQuery {

    @Value("#{'${services.query.miningExaremeUrl:http://hbps2.chuv.ch:9090/mining/query}'}")
    public static String queryUrl;

    private static final Gson gson = new Gson();

    public static String query(MiningQuery query) {
        return query(query.getVariables(), query.getCovariables(), query.getGrouping(), query.getDatasets());
    }

    public static String query(Model model) {
        Query query = model.getQuery();
        return query(query.getVariables(), query.getCovariables(), query.getGrouping(), null);
    }

    private static String query(List<Variable> variables, List<Variable> covariables, List<Variable> groupings, List<Variable> datasets) {
        List<ExaremeQueryElement> queryElements = new LinkedList<>();
        for (Variable var : variables)
        {
            ExaremeQueryElement el = new ExaremeQueryElement();
            el.setName("variable");
            el.setDesc("");
            el.setValue(var.getCode());
            queryElements.add(el);
        }
        for (Variable var : covariables)
        {
            ExaremeQueryElement el = new ExaremeQueryElement();
            el.setName("covariables");
            el.setDesc("");
            el.setValue(var.getCode());
            queryElements.add(el);
        }
        for (Variable var : groupings)
        {
            ExaremeQueryElement el = new ExaremeQueryElement();
            el.setName("groupings");
            el.setDesc("");
            el.setValue(var.getCode());
            queryElements.add(el);
        }
        for (Variable var: datasets)
        {
            ExaremeQueryElement el = new ExaremeQueryElement();
            el.setName("dataset");
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

        return gson.toJson(queryElements);
    }

}
