package com.expedia.content.media.processing.pipeline.util;

import org.apache.commons.lang3.StringUtils;

import com.expedia.content.media.processing.pipeline.domain.ImageMessage;

/**
 * Create a path based on the "domain ID" of the images.
 */
public class ContentRepoPathBuilder implements ImagePathBuilder {

    private static final char PATH_DELIMITER = '/';
    private static final char GUID_DELIMITER = '-';

    @Override
    public String buildPath(ImageMessage message) {
        final String[] guiPathParts = StringUtils.split(message.getOuterDomainData().getDomainId(), GUID_DELIMITER);
        final StringBuilder sb = new StringBuilder();
        // omitting first and last part
        for (int i = 1; i < guiPathParts.length - 1; i++) {
            sb.append(PATH_DELIMITER).append(guiPathParts[i]);
        }
        return sb.append(PATH_DELIMITER).toString();
    }

}
