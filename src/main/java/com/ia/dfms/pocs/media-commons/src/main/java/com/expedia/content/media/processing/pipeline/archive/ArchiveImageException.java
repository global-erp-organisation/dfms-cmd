package com.expedia.content.media.processing.pipeline.archive;

/**
 * Exception to throw when archiving fails.
 */
@SuppressWarnings("serial")
public class ArchiveImageException extends Exception {
    
    public ArchiveImageException(String message, Exception cause) {
        super(message, cause);
    }
    
    public ArchiveImageException(String message) {
        super(message);
    }

}
