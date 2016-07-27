package eu.hbp.mip.utils;

import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import org.apache.log4j.Logger;

/**
 * Created by mirco on 01.07.16.
 */
public class JSONUtil {

    private static final Logger LOGGER = Logger.getLogger(JSONUtil.class);

    private JSONUtil() {
        /* Hide implicit public constructor */
        throw new IllegalAccessError("JSONUtil class");
    }

    public static boolean isJSONValid(String test) {
        try {
            new JsonParser().parse(test);
        } catch (JsonParseException jpe)
        {
            LOGGER.trace(jpe); // This is the normal behavior when the input string is not JSON-ified
            return false;
        }
        return true;
    }

}
