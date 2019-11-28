package com.expedia.content.media.processing.pipeline.exception;

/**
 * Exception to raise when the file name does not have a valid format
 */
@SuppressWarnings("serial")
public class InvalidFileNameFormatException extends RuntimeException {

    public InvalidFileNameFormatException(String message) {
        super(message);
    }
}
