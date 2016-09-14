package eu.hbp.mip.utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Map;

/**
 * Created by mirco on 14.09.16.
 */

public class DataUtil {

    private static final int NB_ROWS_SAMPLING = 200;

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
            List<Map<String, Object>> queryResult = jdbcTemplate.queryForList(
                    "SELECT " + var + " FROM science.adni_merge " +
                            "TABLESAMPLE SYSTEM ("+ samplingPercentage +") REPEATABLE ( 42 )");
            for (Map resultMap : queryResult)
            {
                String strValue = String.valueOf(resultMap.get(var));
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
