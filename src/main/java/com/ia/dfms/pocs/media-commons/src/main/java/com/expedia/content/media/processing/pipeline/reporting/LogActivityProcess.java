package com.expedia.content.media.processing.pipeline.reporting;

/**
 * This interface should be implemented by any class whose instances
 * are intended to log an Activity Log.
 */
public interface LogActivityProcess {
    
    /**
     * Log an Activity Log record.
     *
     * @param logEntry Log entry to log.
     * @param reporting Interface that will do the actual log in DB.
     */
    void log(final LogEntry logEntry, final Reporting reporting);
}
