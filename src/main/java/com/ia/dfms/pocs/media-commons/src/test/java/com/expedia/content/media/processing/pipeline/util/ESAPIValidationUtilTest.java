package com.expedia.content.media.processing.pipeline.util;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ESAPIValidationUtilTest {

    @Test
    public void testValidateJsonMessage() {
        String json = "{\"userId\":\"testsenli33\",\"domain\":\"Lodging\","
                + "\"domainFields\":{\"subcategoryId\":\"22001\",\"propertyHero\":\"false\"},"
                + "\"active\":\"false\",\"comment\":\"notet23\"}";
        String validatedJson = ESAPIValidationUtil.validateJson(json);
        assertEquals(json, validatedJson);
     }
}
