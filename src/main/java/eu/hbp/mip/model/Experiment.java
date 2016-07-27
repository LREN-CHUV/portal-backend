package eu.hbp.mip.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;
import org.apache.log4j.Logger;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

/**
 * Created by habfast on 21/04/16.
 */
@Entity
@Table(name = "`experiment`")
public class Experiment {

    private static final Logger LOGGER = Logger.getLogger(Experiment.class);

    private static final Gson gson = new GsonBuilder()
            .serializeNulls()
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
            .excludeFieldsWithoutExposeAnnotation()
            .create();

    @Id
    @Column(columnDefinition = "uuid")
    @org.hibernate.annotations.Type(type="pg-uuid")
    @Expose
    private UUID uuid;

    @Column(columnDefinition="TEXT")
    @Expose
    private String name;

    @Expose
    @ManyToOne
    @JoinColumn(name = "createdby_username")
    private User createdBy;

    @ManyToOne
    @Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
    @Expose
    private Model model;

    @Column(columnDefinition="TEXT")
    @Expose
    private String algorithms;

    @Column(columnDefinition="TEXT")
    @Expose
    private String validations;

    @Column(columnDefinition="TEXT")
    @Expose
    private String result;

    @Expose
    private Date created = new Date();

    @Expose
    private Date finished;

    @Expose
    private boolean hasError = false;

    @Expose
    private boolean hasServerError = false;

    @Expose
    private boolean shared = false;

    // whether or not the experiment's result have been resultsViewed by its owner
    @Expose
    private boolean resultsViewed = false;

    public Experiment() {
        /*
        *  Empty constructor is needed by Hibernate
        */
    }

    public String computeQuery() {
        JsonObject outgoingQuery = new JsonObject();
        outgoingQuery.add("algorithms", gson.fromJson(algorithms, JsonArray.class));
        outgoingQuery.add("validations", gson.fromJson(validations, JsonArray.class));
        outgoingQuery.add("covariables", gson.toJsonTree(model.getQuery().getCovariables()));
        outgoingQuery.add("variables", gson.toJsonTree(model.getQuery().getVariables()));
        outgoingQuery.add("filters", gson.toJsonTree(model.getQuery().getFilters()));
        outgoingQuery.add("grouping", gson.toJsonTree(model.getQuery().getGrouping()));
        return outgoingQuery.toString();
    }

    public String computeExaremeQuery() {
        List<ExaremeQueryElement> queryElements = new LinkedList<>();
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

        return new Gson().toJson(queryElements);
    }

    public String getValidations() {
        return validations;
    }

    public void setValidations(String validations) {
        this.validations = validations;
    }

    public String getAlgorithms() {
        return algorithms;
    }

    public void setAlgorithms(String algorithms) {
        this.algorithms = algorithms;
    }

    public Model getModel() {
        return model;
    }

    public void setModel(Model model) {
        this.model = model;
    }

    public boolean isHasError() {
        return hasError;
    }

    public void setHasError(boolean hasError) {
        this.hasError = hasError;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public Date getFinished() {
        return finished;
    }

    public void setFinished(Date finished) {
        this.finished = finished;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    public boolean isResultsViewed() {
        return resultsViewed;
    }

    public void setResultsViewed(boolean resultsViewed) {
        this.resultsViewed = resultsViewed;
    }

    public boolean isHasServerError() {
        return hasServerError;
    }

    public void setHasServerError(boolean hasServerError) {
        this.hasServerError = hasServerError;
    }

    public boolean isShared() {
        return shared;
    }

    public void setShared(boolean shared) {
        this.shared = shared;
    }
}
