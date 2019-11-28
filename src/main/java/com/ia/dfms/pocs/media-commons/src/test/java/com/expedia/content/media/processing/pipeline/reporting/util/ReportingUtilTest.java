package com.expedia.content.media.processing.pipeline.reporting.util;

import org.junit.Test;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;

public class ReportingUtilTest {

    @Test
    public void testNumberExpediaId() {
        String fileName = "1234_file_t.jpg";
        final int inputNumber = 1234;
        Integer expediaId = ReportingUtil.extractExpediaID(fileName);
        assertTrue(expediaId == inputNumber);
    }

    @Test
    public void testStringExpediaId() {
        String fileName = "E1234_file_t.jpg";
        Integer expediaId = ReportingUtil.extractExpediaID(fileName);
        assertNull(expediaId);
    }
}
