package com.expedia.content.media.processing.pipeline.util;

import static org.junit.Assert.assertEquals;

import com.expedia.content.media.processing.pipeline.domain.Domain;
import com.expedia.content.media.processing.pipeline.domain.ImageMessage;
import com.expedia.content.media.processing.pipeline.domain.OuterDomain;

import org.junit.Test;

public class LodgingPathBuilderTest {
    
    @Test
    public void testExpediaID() {
        ImageMessage message = new ImageMessage.ImageMessageBuilder().expediaId(12345).build();
        
        LodgingPathBuilder builder = new LodgingPathBuilder();
        String path = builder.buildPath(message);
        assertEquals("/1000000/20000/12400/12345/".replace("/", System.getProperty("file.separator")), path);
    }
    
    @Test
    public void testDomainId() {
        OuterDomain domainData = new OuterDomain(Domain.LODGING, "12345", "provider", null, null);
        ImageMessage message = new ImageMessage.ImageMessageBuilder().outerDomainData(domainData).build();
        
        LodgingPathBuilder builder = new LodgingPathBuilder();
        String path = builder.buildPath(message);
        assertEquals("/1000000/20000/12400/12345/".replace("/", System.getProperty("file.separator")), path);
    }
    
}
