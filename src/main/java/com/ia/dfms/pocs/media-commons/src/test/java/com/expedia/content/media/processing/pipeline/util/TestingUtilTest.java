package com.expedia.content.media.processing.pipeline.util;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class TestingUtilTest {

    private static String staticField;

    private String privateField;

    @Test
    public void testGetNullField() throws Exception {
        Object fieldValue = TestingUtil.getFieldValue(this, "privateField");

        assertNull(fieldValue);
    }

    @Test
    public void testSetAndGetField() throws Exception {
        TestingUtil.setFieldValue(this, "privateField", "Hello, world");

        assertEquals("Hello, world", TestingUtil.getFieldValue(this, "privateField"));
    }

    @Test
    public void testSetInJavaReadReflection() throws Exception {
        privateField = "Hello, Java";

        assertEquals("Hello, Java", TestingUtil.getFieldValue(this, "privateField"));
    }

    @Test
    public void testSetInReflectionReadInJava() throws Exception {
        TestingUtil.setFieldValue(this, "privateField", "Hello, reflection");

        assertEquals("Hello, reflection", privateField);
    }

    @Test
    public void testSetAndGetStaticField() throws Exception {
        staticField = "Should change on the next line";
        TestingUtil.setFieldValue(this, "staticField", "Hello, world");

        assertEquals("Hello, world", TestingUtil.getFieldValue(this, "staticField"));
        assertEquals("Hello, world", staticField);
    }
}
