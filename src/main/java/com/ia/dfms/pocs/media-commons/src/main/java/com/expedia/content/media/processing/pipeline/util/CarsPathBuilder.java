package com.expedia.content.media.processing.pipeline.util;

import com.expedia.content.media.processing.pipeline.domain.ImageMessage;

import java.util.Optional;

/**
 * Builds a storage path for car images.
 */
public class CarsPathBuilder implements ImagePathBuilder {
    
    private static final String FOLDER_MARKER = "/";
    
    @Override
    public String buildPath(ImageMessage message) {
        final String fileUrl = Optional.ofNullable(message.getSourceUrl()).orElse(message.getFileUrl());
        final int fileIndex = fileUrl.lastIndexOf(FOLDER_MARKER);
        final int pathIndex = fileUrl.substring(0, fileIndex).lastIndexOf(FOLDER_MARKER);
        return fileUrl.substring(pathIndex, fileIndex + 1);
    }

}
