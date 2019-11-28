package com.expedia.content.media.processing.pipeline.retry;

import com.expedia.content.media.processing.pipeline.util.FormattedLogger;

import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryContext;
import org.springframework.retry.listener.RetryListenerSupport;

/**
 * A spring retry listener class that logs an error message after the number of retrials is exhausted.
 */
public class LoggingRetryListener extends RetryListenerSupport {
    private static final FormattedLogger LOGGER = new FormattedLogger(LoggingRetryListener.class);

    @Override
    public <T, E extends Throwable> void close(RetryContext context, RetryCallback<T, E> callback, Throwable throwable) {
        super.close(context, callback, throwable);
        if (throwable != null) {
            LOGGER.error(throwable, "Number of retrials is exhausted. Please see stacktrace for error details.");
        }
    }
}
