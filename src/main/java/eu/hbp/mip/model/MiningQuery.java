package eu.hbp.mip.model;

import com.google.gson.Gson;
import eu.hbp.mip.messages.external.VariableId;
import eu.hbp.mip.utils.TypesConvert;
import scala.collection.JavaConverters;
import scala.collection.Seq;

import java.util.List;

/**
 * Created by mirco on 06.01.17.
 */
public class MiningQuery {

    private List<Variable> variables;
    private List<Variable> covariables;
    private List<Variable> grouping;
    private String filters;
    private Algorithm algorithm;

    public List<Variable> getVariables() {
        return variables;
    }

    public void setVariables(List<Variable> variables) {
        this.variables = variables;
    }

    public List<Variable> getCovariables() {
        return covariables;
    }

    public void setCovariables(List<Variable> covariables) {
        this.covariables = covariables;
    }

    public List<Variable> getGrouping() {
        return grouping;
    }

    public void setGrouping(List<Variable> grouping) {
        this.grouping = grouping;
    }

    public String getFilters() {
        return filters;
    }

    public void setFilters(String filters) {
        this.filters = filters;
    }

    public Algorithm getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(Algorithm algorithm) {
        this.algorithm = algorithm;
    }

    public eu.hbp.mip.messages.external.MiningQuery prepareQuery() {

        eu.hbp.mip.messages.external.Algorithm scalaAlgorithm = new eu.hbp.mip.messages.external.Algorithm(
                algorithm.getCode(), algorithm.getName(), TypesConvert.algoParamsToHashMap(algorithm.getParameters()));

        Seq<VariableId> variablesSeq = JavaConverters.asScalaIteratorConverter(
                TypesConvert.variablesToVariableIds(variables).iterator()).asScala().toSeq().toList();
        Seq<VariableId> covariablesSeq = JavaConverters.asScalaIteratorConverter(
                TypesConvert.variablesToVariableIds(covariables).iterator()).asScala().toSeq().toList();
        Seq<VariableId> groupingSeq = JavaConverters.asScalaIteratorConverter(
                TypesConvert.variablesToVariableIds(grouping).iterator()).asScala().toSeq().toList();

        return new eu.hbp.mip.messages.external.MiningQuery(
                variablesSeq, covariablesSeq, groupingSeq, filters, scalaAlgorithm);
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
