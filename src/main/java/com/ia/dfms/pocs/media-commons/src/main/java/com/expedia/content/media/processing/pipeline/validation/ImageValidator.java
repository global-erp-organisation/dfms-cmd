package com.expedia.content.media.processing.pipeline.validation;

import java.net.URISyntaxException;
import java.net.URL;

import com.expedia.content.media.processing.pipeline.domain.Image;
import com.expedia.content.media.processing.pipeline.domain.ImageMessage;
import com.expedia.content.media.processing.pipeline.util.FormattedLogger;

/**
 * Validates an image.
 */
public interface ImageValidator {

    FormattedLogger LOGGER = new FormattedLogger(ImageValidator.class);

    /**
     * Validates the image. It doesn't throw an exception and leaves the decision to the caller.
     *
     * @param imageUrl URL of the image to validate.
     * @param imageMessage imageMessage Image message containing data that may affect the validation outcome.
     * @return {@code true} for valid images, {@code false} otherwise
     */
    boolean validate(URL imageUrl, ImageMessage imageMessage);
    
    /**
     * Builds a {@code Image} from the passed URL. Returns null if the URL is invalid.
     * @param imageUrl The URL to build the Image with.
     * @return The new image or null if the URL is invalid.
     */
    default Image buildImage(URL imageUrl) {
        try {
            return new Image(imageUrl);
        } catch (URISyntaxException e) {
            // This should not happen as the URL is typically validated before image validation is done.
            LOGGER.warn("The provided URL is not properly formatted.[{}]", imageUrl);
            return null;
        }
    }
}
