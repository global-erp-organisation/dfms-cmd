package com.expedia.content.media.processing.pipeline.reporting;

import com.expedia.content.media.processing.pipeline.util.FormattedLogger;

/**
 * Logs activities for image files.
 */
public class LogActivityRunnable implements Runnable {
    
    private static final FormattedLogger LOGGER = new FormattedLogger(LogActivityRunnable.class);
    
    private final LogEntry logEntry;
    private final Reporting reporting;
    
    public LogActivityRunnable(final LogEntry logEntry, final Reporting reporting) {
        this.logEntry = logEntry;
        this.reporting = reporting;
    }
    
    @Override
    public void run() {
        try {
            LOGGER.info("++> {}", logEntry);
            reporting.writeLogEntry(logEntry);
        } catch (Exception e) {
            
            final String domainName = (logEntry.getDomain() == null) ? null : logEntry.getDomain().getDomain();
            final String activityName = (logEntry.getActivity() == null) ? null : logEntry.getActivity().getName();
            
            LOGGER.error(e, "Failed to log activity Activity={} Domain={} DomainId={} FileName={}", activityName,
                    domainName, logEntry.getDomainId(), logEntry.getFileName());
        }
    }
}
