package com.expedia.content.media.processing.pipeline.validation;

import java.net.URL;

import com.expedia.content.media.processing.pipeline.domain.Image;
import com.expedia.content.media.processing.pipeline.domain.ImageMessage;

/**
 * Validate only images that are truly jpeg images. This uses
 * {@code Image}'s identify capability to get the true type of
 * the image instead of just looking at the extension
 */
public class OnlyJPEGImageValidator implements ImageValidator {

    private static final String EXPECTED_MIMETYPE = "image/jpeg";

    @Override public boolean validate(URL imageUrl, ImageMessage imageMessage) {
        Image image = buildImage(imageUrl);
        if (image == null) {
            return false;
        }
        return EXPECTED_MIMETYPE.equals(image.getMimeType());
    }
}
