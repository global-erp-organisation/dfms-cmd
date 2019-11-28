package com.expedia.content.media.processing.pipeline.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.Test;

public class OuterDomainTest {

    @Test
    public void testToStringNullFields() {
        OuterDomain domain = new OuterDomain(Domain.LODGING, "abc", "def", null, null);
        assertEquals("OuterDomain{domain='Lodging', domainId='abc', provider='def', derivativeCategory='null'}", domain.toString());
    }

    @Test
    public void testToStringWithBuilder() {
        OuterDomain domain = OuterDomain.builder().domain(Domain.LODGING).domainId("abc").mediaProvider("def").build();
        assertEquals("OuterDomain{domain='Lodging', domainId='abc', provider='def', derivativeCategory='null'}", domain.toString());
    }

    @Test
    public void testToStringWithFields() {
        Map<String, Object> fields = new HashMap<>();
        fields.put("key1", "value1");
        OuterDomain domain = new OuterDomain(Domain.LODGING, "abc", "def", null, fields);
        assertEquals("OuterDomain{domain='Lodging', domainId='abc', provider='def', derivativeCategory='null', domainFields={key1='value1'}}",
                domain.toString());
    }

    @Test
    public void testToStringWithFieldsBuilder() {
        OuterDomain domain = OuterDomain.builder().domain(Domain.LODGING).domainId("abc").mediaProvider("def").addField("key1", "value1").build();
        assertEquals("OuterDomain{domain='Lodging', domainId='abc', provider='def', derivativeCategory='null', domainFields={key1='value1'}}",
                domain.toString());
    }

    @Test
    public void testGetDomainFieldValueNotNull() {
        OuterDomain domain = OuterDomain.builder().domain(Domain.LODGING).domainId("abc").mediaProvider("def").addField("key1", "value1").build();
        assertNotNull(domain.getDomainFieldValue("key1"));
    }

    @Test
    public void testGetDomainFieldValueNull() {
        OuterDomain domain = OuterDomain.builder().domain(Domain.LODGING).domainId("abc").mediaProvider("def").build();
        assertNull(domain.getDomainFieldValue("x"));
    }

    @Test
    public void testNullThenAdd() {
        assertNotNull(OuterDomain.builder().fields(null).addField("a", "b").build());
    }

    @Test
    public void testToStringWithANullValueInDomainFields() {
        Map<String, Object> domainDataFields = new LinkedHashMap<>();
        domainDataFields.put("category", "71013");
        domainDataFields.put("propertyHero", null);
        OuterDomain domainData = new OuterDomain(Domain.LODGING, "123", "jumboTour", "VirtualTour", domainDataFields);
        String expectedDomain =
                "OuterDomain{domain='Lodging', domainId='123', provider='jumboTour', derivativeCategory='VirtualTour', domainFields={category='71013',propertyHero='null'}}";
        assertEquals(expectedDomain, domainData.toString());
    }
}
