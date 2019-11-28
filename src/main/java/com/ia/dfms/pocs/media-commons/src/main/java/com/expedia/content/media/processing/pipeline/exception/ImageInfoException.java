package com.expedia.content.media.processing.pipeline.exception;

import java.net.URL;

/**
 * This exception happens when tries to retrieve image information fail
 */
@SuppressWarnings("serial")
public class ImageInfoException extends RuntimeException {
    private final URL filenameUrl;

    public ImageInfoException(URL filenameUrl, String message, Throwable cause) {
        super(message, cause);
        this.filenameUrl = filenameUrl;
    }

    public URL getFilenameUrl() {
        return filenameUrl;
    }
}
