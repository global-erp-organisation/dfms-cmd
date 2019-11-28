package com.expedia.content.media.processing.pipeline.exception;

/**
 * This exception happens when JSON image message cannot be parsed into ImageMessage}
 */
@SuppressWarnings("serial")
public class ImageMessageException extends RuntimeException {
    public ImageMessageException(String message, Throwable cause) {
        super(message, cause);
    }
}
