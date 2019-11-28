package com.expedia.content.media.processing.pipeline.exception;

/**
 * Exception to raise when the using an unrecognized resizing exception.
 */
@SuppressWarnings("serial")
public class DerivativeManagerInvalidResizeMethodException extends RuntimeException {

    public DerivativeManagerInvalidResizeMethodException(final String message) {
        super(message);
    }

}
