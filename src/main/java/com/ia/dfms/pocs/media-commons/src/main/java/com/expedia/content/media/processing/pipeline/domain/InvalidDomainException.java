package com.expedia.content.media.processing.pipeline.domain;

/**
 * Exception to raise when an unrecognized domain is used.
 */
@SuppressWarnings("serial")
public class InvalidDomainException extends RuntimeException {
    public InvalidDomainException(String message) {
        super(message);
    }
}
