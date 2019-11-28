package com.expedia.content.media.processing.pipeline.util;

import org.owasp.esapi.ValidationErrorList;
import org.owasp.esapi.reference.validation.StringValidationRule;

/**
 * OWASP ESAPI validation class
 */
public final class ESAPIValidationUtil {
    private static final FormattedLogger LOGGER = new FormattedLogger(ESAPIValidationUtil.class);

    private ESAPIValidationUtil() {

    }

    /**
     * use ESAPI validation interface to do JSON string validation.
     *
     * @param inputJson input JSON message.
     * @return validated message, here it should be the same as inputJSON
     */
    public static String validateJson(String inputJson) {
        String json = "";
        try {
            final ValidationErrorList vel = new ValidationErrorList();
            final StringValidationRule validationRule = new StringValidationRule("Max_Min");
            validationRule.setMinimumLength(0);
            validationRule.setMaximumLength(Integer.MAX_VALUE);
            json = (String) validationRule.getValid("", inputJson, vel);
        } catch (Exception ex) {
            LOGGER.error("validateJson error inputJson=[{}]", inputJson, ex);
            return "";
        }
        return json;
    }
}
