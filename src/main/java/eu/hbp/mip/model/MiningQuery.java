package eu.hbp.mip.model;

import ch.chuv.lren.mip.portal.WokenConversions;
import com.google.gson.Gson;
import eu.hbp.mip.utils.TypesConvert;
import ch.chuv.lren.woken.messages.datasets.DatasetId;
import ch.chuv.lren.woken.messages.query.ExecutionPlan;
import ch.chuv.lren.woken.messages.query.UserId;
import ch.chuv.lren.woken.messages.query.filters.FilterRule;
import ch.chuv.lren.woken.messages.variables.FeatureIdentifier;
import scala.Option;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by mirco on 06.01.17.
 */
public class MiningQuery {

    private List<Variable> variables;
    private List<Variable> covariables;
    private List<Variable> grouping;
    private List<Variable> datasets;
    private String filters;
    private Algorithm algorithm;

    public MiningQuery() {
        this.variables = new LinkedList<>();
        this.covariables = new LinkedList<>();
        this.grouping = new LinkedList<>();
        this.datasets = new LinkedList<>();
        this.filters = "";
    }

    public List<Variable> getVariables() {
        return variables;
    }

    public void setVariables(List<Variable> variables) {
        this.variables = variables;
    }

    public  void addVariable(Variable variable) { this.variables.add(variable); }

    public List<Variable> getCovariables() {
        return covariables;
    }

    public void setCovariables(List<Variable> covariables) {
        this.covariables = covariables;
    }

    public  void addCovariable(Variable variable) { this.covariables.add(variable); }

    public List<Variable> getGrouping() {
        return grouping;
    }

    public void setGrouping(List<Variable> grouping) {
        this.grouping = grouping;
    }

    public List<Variable> getDatasets() { return datasets; }

    public void setDataset(List<Variable> datasets) {
        this.datasets = datasets;
    }

    public  void addDataset(Variable variable) { this.datasets.add(variable); }

    public  void addGrouping(Variable variable) { this.grouping.add(variable); }

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

    public ch.chuv.lren.woken.messages.query.MiningQuery prepareQuery(String user) {

        ch.chuv.lren.woken.messages.query.AlgorithmSpec scalaAlgorithm = new ch.chuv.lren.woken.messages.query.AlgorithmSpec(
                algorithm.getCode(), TypesConvert.algoParamsToScala(algorithm.getParameters()), Option.empty());

        scala.collection.immutable.List<FeatureIdentifier> variablesSeq =
                TypesConvert.variablesToIdentifiers(getVariables());
        scala.collection.immutable.List<FeatureIdentifier> covariablesSeq =
                TypesConvert.variablesToIdentifiers(getCovariables());
        scala.collection.immutable.List<FeatureIdentifier> groupingSeq =
                TypesConvert.variablesToIdentifiers(getGrouping());
        UserId userId = new UserId(user);

        WokenConversions conv = new WokenConversions();
        scala.collection.immutable.Set<DatasetId> datasets = conv.toDatasets(getDatasets());
        String filtersJson = getFilters();
        Option<FilterRule> filters = conv.toFilterRule(filtersJson);

        return new ch.chuv.lren.woken.messages.query.MiningQuery(userId,
                variablesSeq, covariablesSeq, groupingSeq, filters, Option.empty(), datasets, scalaAlgorithm, Option.empty());
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
