package com.expedia.content.media.processing.pipeline.reporting;

/**
 * Reports a log entry to data storage.
 */
public interface Reporting {
    
    /**
     * Write a log entry for a step in the media processing pipeline.
     *
     * @param logEntry The log entry to log.
     */
    void writeLogEntry(final LogEntry logEntry);    
}
