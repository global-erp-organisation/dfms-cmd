package com.expedia.content.media.processing.pipeline.reporting.util;

import com.expedia.content.media.processing.pipeline.reporting.LogActivityProcess;
import com.expedia.content.media.processing.pipeline.reporting.LogEntry;
import com.expedia.content.media.processing.pipeline.reporting.Reporting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Groups a Reporting and a ThreadedLogger together for easier access
 */
@Component
public class Reporter {

    private final Reporting reporting;
    private final LogActivityProcess logger;

    @Autowired
    public Reporter(Reporting reporting, LogActivityProcess logger) {
        this.reporting = reporting;
        this.logger = logger;
    }

    public void logEntry(LogEntry logEntry) {
        logger.log(logEntry, reporting);
    }
}
