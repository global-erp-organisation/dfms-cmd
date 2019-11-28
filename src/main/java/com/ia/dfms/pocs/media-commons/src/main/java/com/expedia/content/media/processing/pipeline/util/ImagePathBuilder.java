package com.expedia.content.media.processing.pipeline.util;

import com.expedia.content.media.processing.pipeline.domain.ImageMessage;

/**
 * Takes image message data to form a path for storage.
 */
public interface ImagePathBuilder {
   
    /**
     * Builds a path for an images based on image message properties.
     * 
     * @param message The incoming image message.
     * @return The path based on data from the image message.
     */
    String buildPath(ImageMessage message);
    
}
