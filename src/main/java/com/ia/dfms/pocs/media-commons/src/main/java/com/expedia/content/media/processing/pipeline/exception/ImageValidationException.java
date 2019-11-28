package com.expedia.content.media.processing.pipeline.exception;

/**
 * Used in case of ImageValidator exceptions
 *
 * @see com.expedia.content.media.processing.pipeline.validation.ImageValidator
 */
@SuppressWarnings("serial")
public class ImageValidationException extends RuntimeException {

    public ImageValidationException(String message) {
        super(message);
    }

    public ImageValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}
