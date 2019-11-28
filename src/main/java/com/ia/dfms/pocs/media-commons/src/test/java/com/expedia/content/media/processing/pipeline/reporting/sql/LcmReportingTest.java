package com.expedia.content.media.processing.pipeline.reporting.sql;

import com.expedia.content.media.processing.pipeline.domain.Domain;
import com.expedia.content.media.processing.pipeline.reporting.Activity;
import com.expedia.content.media.processing.pipeline.reporting.LogEntry;
import org.junit.Test;

import java.util.Date;

import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class LcmReportingTest {
    
    @SuppressWarnings("deprecation")
    @Test
    public void testWriteLogEntry() {
        SQLLogEntryInsertSproc mockLogEntryInsert = mock(SQLLogEntryInsertSproc.class);
        String appName = "lcm-reporting-test";
        LcmReporting reporting = new LcmReporting(mockLogEntryInsert, appName);
        
        LogEntry logEntry =
                new LogEntry("s3://folder/myawesomefile.jpg", "b536314c-36a5-4adf-a80a-dd0b56d664a5", Activity.UNKNOWN, new Date(), Domain.LODGING, "123",
                             null);
        reporting.writeLogEntry(logEntry);
        verify(mockLogEntryInsert).execute(eq(logEntry.getFileName()), eq(Domain.LODGING.getDomain()), eq(appName), eq(Activity.UNKNOWN.getName()),
                anyString(), eq(123), eq(appName));
    }

    @Test
    public void testWriteLogEntryDomainNotInteger() {
        SQLLogEntryInsertSproc mockLogEntryInsert = mock(SQLLogEntryInsertSproc.class);
        String appName = "lcm-reporting-test";
        LcmReporting reporting = new LcmReporting(mockLogEntryInsert, appName);

        LogEntry logEntry =
                new LogEntry("s3://folder/myawesomefile.jpg", "b536314c-36a5-4adf-a80a-dd0b56d664a5", Activity.UNKNOWN, new Date(), Domain.LODGING, "domainId",
                        null);
        reporting.writeLogEntry(logEntry);
        verify(mockLogEntryInsert).execute(eq(logEntry.getFileName()), eq(Domain.LODGING.getDomain()), eq(appName), eq(Activity.UNKNOWN.getName()),
                anyString(), isNull(), eq(appName));
    }
    
}
