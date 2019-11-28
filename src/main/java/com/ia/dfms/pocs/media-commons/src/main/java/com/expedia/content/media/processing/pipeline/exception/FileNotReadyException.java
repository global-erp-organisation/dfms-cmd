package com.expedia.content.media.processing.pipeline.exception;

/**
 * Exception to report when a file was not ready for reading
 */
@SuppressWarnings("serial")
public class FileNotReadyException extends RuntimeException {
    public FileNotReadyException(String message, Throwable cause) {
        super(message, cause);
    }
}
