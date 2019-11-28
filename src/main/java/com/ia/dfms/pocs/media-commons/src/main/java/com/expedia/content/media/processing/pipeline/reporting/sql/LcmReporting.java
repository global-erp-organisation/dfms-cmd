package com.expedia.content.media.processing.pipeline.reporting.sql;

import com.expedia.content.media.processing.pipeline.reporting.LogEntry;
import com.expedia.content.media.processing.pipeline.reporting.Reporting;

import java.text.SimpleDateFormat;

import expedia.content.solutions.metrics.annotations.Meter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * LCM implementation of the Reporting interface.
 */
@Component
@Deprecated
public class LcmReporting implements Reporting {
    
    private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS XXX";
    
    private final String appName;
    private final SQLLogEntryInsertSproc logEntryInsertProcedure;
    
    @Autowired
    public LcmReporting(final SQLLogEntryInsertSproc logEntryInsertProcedure, @Value("${processname}") final String appName) {
        this.logEntryInsertProcedure = logEntryInsertProcedure;
        this.appName = appName;
    }
    
    @Override
    @Meter(name = "logEntry")
    public void writeLogEntry(final LogEntry logEntry) {
        final SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
        final String domainName = (logEntry.getDomain() == null) ? null : logEntry.getDomain().getDomain();
        final String activityName = (logEntry.getActivity() == null) ? null : logEntry.getActivity().getName();

        logEntryInsertProcedure.execute(
                logEntry.getFileName(),
                domainName,
                appName,
                activityName,
                dateFormat.format(logEntry.getActivityTime()),
                getDomainId(logEntry),
                appName);
    }

    private Integer getDomainId(LogEntry logEntry) {
        try {
            return Integer.parseInt(logEntry.getDomainId());
        } catch (Exception e) {
            return null;
        }
    }
}
