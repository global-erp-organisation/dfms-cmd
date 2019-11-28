package com.expedia.content.media.processing.pipeline.domain;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.junit.Test;

public class DomainComponentPickerTest {

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Test
    public void testPickValidDomain() throws Exception {
        Map<String, List> listMap = new HashMap<>();
        listMap.put("Lodging", mock(Vector.class));
        listMap.put("Cars", mock(LinkedList.class));
        DomainComponentPicker<List> logActivityPicker = new DomainComponentPicker(listMap);

        List logActivityProcess = logActivityPicker.getDomainTypeComponent(Domain.LODGING);
        assertTrue(logActivityProcess instanceof Vector);

        logActivityProcess = logActivityPicker.getDomainTypeComponent(Domain.CARS);
        assertTrue(logActivityProcess instanceof LinkedList);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Test(expected = InvalidDomainException.class)
    public void testPickInvalidDomain() throws Exception {
        Map<String, List> listMap = new HashMap<>();
        listMap.put("VirtualTour", mock(ArrayList.class));
        DomainComponentPicker<List> logActivityPicker = new DomainComponentPicker(listMap);
        logActivityPicker.getDomainTypeComponent(Domain.LODGING);
    }

}
