package com.expedia.content.media.processing.pipeline.reporting;

import com.expedia.content.media.processing.pipeline.domain.Domain;
import org.junit.Test;

import java.io.IOException;
import java.util.Date;

import static org.junit.Assert.assertEquals;

public class LogEntryTest {

    @Test
    public void testLogEntryJsonConvertMethod() throws InterruptedException, IOException {
        Date activityDate = new Date();
        LogEntry logEntry = new LogEntry("test.jpg", "111111", Activity.UNKNOWN, activityDate,
                Domain.LODGING, "123", null);
        String jsonLog = logEntry.toJSONMessage();
        LogEntry logEntryFromJSon = LogEntry.getLogFromMessage(jsonLog);
        assertEquals(logEntryFromJSon.getActivityName(), Activity.UNKNOWN.getName());
        assertEquals(logEntryFromJSon.getFileName(), "test.jpg");
        assertEquals(logEntryFromJSon.getMediaId(), "111111");
        assertEquals(logEntryFromJSon.getActivityTime(), activityDate);
        assertEquals(logEntryFromJSon.getDomain(), Domain.LODGING);
        assertEquals(logEntryFromJSon.getDomainId(), "123");

    }
}
