package eu.hbp.mip.utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

/**
 * Created by mirco on 14.09.16.
 */

public class DataUtil {

    private static final int NB_ROWS_SAMPLING = 200;
    private static final int TABLESAMPLE_SEED = 42;

    private JdbcTemplate jdbcTemplate;

    public DataUtil(JdbcTemplate jdbcTemplate)
    {
        this.jdbcTemplate = jdbcTemplate;
    }

    public JsonObject getDataFromVariables(List<String> vars)
    {
        JsonObject data = new JsonObject();

        for (String var : vars) {
            JsonArray currentVarData = new JsonArray();
            int samplingPercentage = (int) countAdniRows()/NB_ROWS_SAMPLING;
            List<Object> queryResult = jdbcTemplate.queryForList(
                    "SELECT " + var + " FROM science.adni_merge " +
                            "TABLESAMPLE SYSTEM ("+ samplingPercentage +") REPEATABLE ( "+ TABLESAMPLE_SEED +" )", Object.class);
            for (Object value : queryResult)
            {
                String strValue = String.valueOf(value);
                try {
                    double numValue = Double.parseDouble(strValue);
                    currentVarData.add(numValue);
                } catch (NumberFormatException e2) {
                    currentVarData.add(strValue);
                }
            }
            data.add(var, currentVarData);
        }

        return data;
    }

    public long countVariables()
    {
        long count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS " +
                        "WHERE table_schema = 'science' AND table_name = 'adni_merge'", Long.class);
        return count;
    }

    public long countAdniRows()
    {
        long count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM science.adni_merge", Long.class);
        return count;
    }

}
