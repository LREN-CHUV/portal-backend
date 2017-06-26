package eu.hbp.mip.configuration;

import org.apache.log4j.Logger;
import org.springframework.boot.autoconfigure.web.DefaultErrorAttributes;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;

import java.util.HashMap;
import java.util.Map;

@Component
public class PortalErrorAttributes extends DefaultErrorAttributes {
    private static final Logger LOGGER = Logger.getLogger(PortalErrorAttributes.class);

    @Override
    public Map<String, Object> getErrorAttributes(RequestAttributes requestAttributes, boolean includeStackTrace) {
        Map<String, Object> errorAttributes = super.getErrorAttributes(requestAttributes, includeStackTrace);

        Throwable throwable = getError(requestAttributes);
        LOGGER.warn("Reporting server error", throwable);

        Throwable cause = throwable.getCause();
        if (cause != null) {
            Map<String, Object> causeErrorAttributes = new HashMap<>();
            causeErrorAttributes.put("exception", cause.getClass().getName());
            causeErrorAttributes.put("message", cause.getMessage());
            errorAttributes.put("cause", causeErrorAttributes);
        }
        return errorAttributes;
    }
}
