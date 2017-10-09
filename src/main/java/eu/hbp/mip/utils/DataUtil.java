package eu.hbp.mip.utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

/**
 * Created by mirco on 14.09.16.
 */

public class DataUtil {

    private static final int MAX_NB_SAMPLES = 200;

    private JdbcTemplate jdbcTemplate;
    private String featuresMainTable;

    public DataUtil(JdbcTemplate jdbcTemplate, String featuresMainTable)
    {
        this.jdbcTemplate = jdbcTemplate;
        this.featuresMainTable = featuresMainTable;
    }

    @Cacheable("varsdata")
    public JsonObject getDataFromVariables(List<String> vars, String filters)
    {
        JsonObject data = new JsonObject();

        if (filters == null) {
            filters = "";
        }

        if (filters.length() > 0) {
            filters = filters.replaceAll("\\\\'", "''");  // Quick and dirty workaround
            filters = String.format("AND %s", filters);
        }

        for (String var : vars) {
            JsonArray currentVarData = new JsonArray();

            long nbRows = countDatasetRows();

            if (nbRows >= 1) {
                List<Object> queryResult;
                synchronized(this){
                    // Dirty
                    jdbcTemplate.execute("SELECT SETSEED(0.42)");
                    // Dirty
                    queryResult = jdbcTemplate.queryForList(
                            String.format("SELECT %s FROM %s WHERE %s IS NOT NULL %s ORDER BY Random() LIMIT %d",
                                    var, featuresMainTable, var, filters, MAX_NB_SAMPLES),
                            Object.class);
                }
                for (Object value : queryResult) {
                    if (value == null) {
                        currentVarData.add((String) null);
                    } else {
                        if (value instanceof Double) {
                            currentVarData.add((Double) value);
                        } else if (value instanceof Float) {
                            currentVarData.add((Float) value);
                        } else if (value instanceof Integer) {
                            currentVarData.add((Integer) value);
                        } else if (value instanceof Long) {
                            currentVarData.add((Long) value);
                        } else {
                            String strValue = String.valueOf(value);
                            try {
                                double numValue = Double.parseDouble(strValue);
                                currentVarData.add(numValue);
                            } catch (NumberFormatException e2) {
                                currentVarData.add(strValue);
                            }
                        }
                    }
                }
                data.add(var, currentVarData);
            }
        }

        return data;
    }

    public JsonObject getDataFromVariables(List<String> vars)
    {
        return getDataFromVariables(vars);
    }

    @Cacheable("colscount")
    public long countVariables()
    {
        // Dirty
        return jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS " +
                        "WHERE table_name = '"+featuresMainTable+"'", Long.class);
    }

    @Cacheable("rowscount")
    public long countDatasetRows()
    {
        // Dirty
        return jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM "+featuresMainTable, Long.class);
    }

}
