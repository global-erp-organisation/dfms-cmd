package com.expedia.content.media.processing.pipeline.util;

import static org.junit.Assert.assertEquals;

import com.expedia.content.media.processing.pipeline.domain.ImageMessage;

import org.junit.Test;

public class CarsPathBuilderTest {

    @Test
    public void testFile() {
        ImageMessage message = new ImageMessage.ImageMessageBuilder().fileUrl("file://my/awesome/folder/car/toyota/toy_car.jpg").build();

        CarsPathBuilder builder = new CarsPathBuilder();
        String path = builder.buildPath(message);
        assertEquals("/toyota/", path);
    }
    
    @Test
    public void testSource() {
        ImageMessage message = new ImageMessage.ImageMessageBuilder().sourceUrl("file://my/awesome/folder/car/toyota/toy_car.jpg").build();
        
        CarsPathBuilder builder = new CarsPathBuilder();
        String path = builder.buildPath(message);
        assertEquals("/toyota/", path);
    }

    @Test
    public void testSourceOverridesFile() {
        ImageMessage message = new ImageMessage.ImageMessageBuilder()
                .fileUrl("file://my/awesome/folder/car/fileyota/toy_car.jpg")
                .sourceUrl("file://my/awesome/folder/car/sourceyota/toy_car.jpg").build();

        CarsPathBuilder builder = new CarsPathBuilder();
        String path = builder.buildPath(message);
        assertEquals("/sourceyota/", path);
    }
    
}
