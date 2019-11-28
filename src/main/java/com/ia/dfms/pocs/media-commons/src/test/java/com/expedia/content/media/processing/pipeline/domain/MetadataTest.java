package com.expedia.content.media.processing.pipeline.domain;

import org.junit.Test;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class MetadataTest {

    @Test
    public void testMetadataConstructorAndToStringNoDetails() {
        Metadata metadata = new Metadata(1, 2, 12, null);
        assertEquals("Metadata{height='2', width='1', fileSize='12'}", metadata.toString());
    }

    @Test
    public void testMetadataBuilderAndToStringNoDetails() {
        Metadata metadata = Metadata.builder().width(1).height(2).fileSize(12).build();
        assertEquals("Metadata{height='2', width='1', fileSize='12'}", metadata.toString());
    }



    @Test
    public void testMetadataConstructorAndToStringWithDetails() {
        Map<String, String> details = new LinkedHashMap<>();
        details.put("detail1", "value1");
        details.put("detail2", "value2");
        Metadata metadata = new Metadata(1, 2, 12, details);
        assertEquals("Metadata{height='2', width='1', fileSize='12', details={detail1='value1',detail2='value2'}}",
                metadata.toString());
    }

    @Test
    public void testMetadataBuilderAndToStringWithDetails() {
        Metadata metadata = Metadata.builder()
                .width(1).height(2).fileSize(12)
                .addDetail("detail1", "value1")
                .addDetail("detail2", "value2").build();
        assertEquals("Metadata{height='2', width='1', fileSize='12', details={detail1='value1',detail2='value2'}}",
                metadata.toString());
    }

    @Test
    public void testNullThenAdd() {
        assertNotNull(Metadata.builder()
                .details(null)
                .addDetail("a", "b")
                .build());
    }

    @Test
    public void testEmptyMetadata() {
        Metadata empty = Metadata.builder().build();
        assertEquals("Metadata{height='null', width='null', fileSize='null'}", empty.toString());
    }
}
