package com.expedia.content.media.processing.pipeline.util;

import org.junit.Before;
import org.junit.Test;

import com.expedia.content.media.processing.pipeline.domain.ImageMessage;
import com.expedia.content.media.processing.pipeline.domain.OuterDomain;

import static org.junit.Assert.assertEquals;

public class ContentRepoPathBuilderTest {

    private ImageMessage           message;
    private ContentRepoPathBuilder builder;

    @Before
    public void setUp() throws Exception {
        message = ImageMessage.builder()
                .fileUrl("image/path/test.jpg")
                .outerDomainData(OuterDomain.builder()
                .domainId("c233093a-8694-4565-b35b-d308a668368c").build())
                .build();

        builder = new ContentRepoPathBuilder();
    }

    @Test
    public void testBuildPath() {
        assertEquals("/8694/4565/b35b/", builder.buildPath(message));
    }
}
