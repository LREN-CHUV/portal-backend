package eu.hbp.mip.model;

import com.google.gson.*;

import java.util.Date;

public class Mining {

    private static final Gson gson = new Gson();

    private final String jobId;
    private final String node;
    private final String function;
    private final String shape;
    private final Date timestamp;
    private final String data;

    public Mining(String jobId, String node, String function, String shape, Date timestamp, String data) {
        this.jobId = jobId;
        this.node = node;
        this.function = function;
        this.shape = shape;
        this.timestamp = timestamp;
        this.data = data;
    }

    public String getJobId() {
        return jobId;
    }

    public String getNode() {
        return node;
    }

    public String getFunction() {
        return function;
    }

    public String getShape() {
        return shape;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public String getData() {
        return data;
    }

    public JsonObject jsonify() {
        JsonObject exp = gson.toJsonTree(this).getAsJsonObject();
        JsonParser parser = new JsonParser();

        if (this.data != null) {
            exp.remove("data");
            JsonElement jsonResult = parser.parse(this.data);
            exp.add("data", jsonResult);
        }

        return exp;
    }

}
