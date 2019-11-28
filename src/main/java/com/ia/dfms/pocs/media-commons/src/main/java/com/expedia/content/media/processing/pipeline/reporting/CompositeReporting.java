package com.expedia.content.media.processing.pipeline.reporting;

import java.util.List;

import com.expedia.content.media.processing.pipeline.util.FormattedLogger;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Reports a log entry to multiple data storage. Loops through the list of provided Reporting implementations.
 */
public class CompositeReporting implements Reporting {
    
    private static final FormattedLogger LOGGER = new FormattedLogger(CompositeReporting.class);
    private final List<Reporting> reportings;
    
    @Autowired
    public CompositeReporting(final List<Reporting> reports) {
        this.reportings = reports;
    }
    
    @Override
    public void writeLogEntry(LogEntry logEntry) {
        reportings.stream().forEach(report -> {
            try {
                report.writeLogEntry(logEntry);
            } catch (Exception e) {
                LOGGER.error(e, "Error during persisting the log ErrorMessage={} Location={} MediaGuid={} LogGuid={} FileName={} Domain={} DomainId={}",
                        e.getMessage(), report.getClass().getSimpleName(), logEntry.getMediaId(), logEntry.getLogGuid(),
                        logEntry.getFileName(), logEntry.getDomain(), logEntry.getDomainId());
                throw e;
            }
        });
    }
}
