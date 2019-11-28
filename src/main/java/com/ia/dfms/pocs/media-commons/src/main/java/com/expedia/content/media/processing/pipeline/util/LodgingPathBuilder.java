package com.expedia.content.media.processing.pipeline.util;

import com.expedia.content.media.processing.pipeline.domain.ImageMessage;

/**
 * Builds a storage path for lodging images.
 */
public class LodgingPathBuilder implements ImagePathBuilder {
    
    @Override
    public String buildPath(ImageMessage message) {
        Integer hotelId = message.getExpediaId();
        if (message.getOuterDomainData() != null && message.getOuterDomainData().getDomainId() != null) {
            hotelId = Integer.parseInt(message.getOuterDomainData().getDomainId());
        }
        return LodgingUtil.buildFolderPath(hotelId);
    }
    
}
