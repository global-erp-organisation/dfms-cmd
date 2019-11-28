package com.expedia.content.media.processing.pipeline.reporting;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.LoggingEvent;
import ch.qos.logback.core.Appender;

import com.expedia.content.media.processing.pipeline.domain.Domain;

import java.io.IOException;
import java.util.Date;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate;

public class LogActivityRunnableTest {
    
    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();
    
    @Captor
    private ArgumentCaptor<LoggingEvent> captorLoggingEvent = ArgumentCaptor.forClass(LoggingEvent.class);
    
    @Captor
    private ArgumentCaptor<LogEntry> captorLogEntry = ArgumentCaptor.forClass(LogEntry.class);


    @Test
    public void testThreadRun() throws InterruptedException, IOException {
        Reporting mockReporting = mock(Reporting.class);
        LogEntry mockEntry = new LogEntry("s3://sthree.com/my/great/file.jpg", "111111", Activity.UNKNOWN, new Date(),
                Domain.LODGING, "123", null);
        Thread logThread = new Thread(new LogActivityRunnable(mockEntry, mockReporting));
        logThread.start();
        logThread.join();
        
        verify(mockReporting, times(1)).writeLogEntry(captorLogEntry.capture());
        final LogEntry logEntry = captorLogEntry.getValue();
        assertEquals(logEntry.getActivityName(), Activity.UNKNOWN.getName());
    }

    @Test
    public void testQueueLogRun() throws InterruptedException, IOException {
        Reporting mockReporting = mock(Reporting.class);
        QueueMessagingTemplate mockMessageTemplate = mock(QueueMessagingTemplate.class);
        doThrow(new RuntimeException()).when(mockReporting).writeLogEntry(any(LogEntry.class));
        LogEntry mockEntry = new LogEntry("s3://sthree.com/my/great/file.jpg", "111111", Activity.UNKNOWN, new Date(),
                Domain.LODGING, "123", null);
        Thread logThread = new Thread(new LogActivityRunnableWIthDLQueue(mockEntry, mockReporting, "dl-queue", mockMessageTemplate));
        logThread.start();
        logThread.join();
        verify(mockReporting, times(1)).writeLogEntry(captorLogEntry.capture());
        verify(mockMessageTemplate, times(1)).send(anyString(), anyObject());
    }
    
    @SuppressWarnings({"rawtypes", "unchecked"})
    @Test
    public void testThreadRunException() throws InterruptedException, IOException {
        Logger logger = (Logger) LoggerFactory.getLogger(LogActivityRunnable.class);
        Appender mockAppender = mock(Appender.class);
        logger.addAppender(mockAppender);
        
        String fileLocation = "s3://sthree.com/my/great/file.jpg";
        LogEntry mockEntry = new LogEntry(fileLocation, "111111", Activity.UNKNOWN, new Date(), Domain.LODGING, "123", null);
        Reporting mockReporting = mock(Reporting.class);
        doThrow(new RuntimeException()).when(mockReporting).writeLogEntry(any(LogEntry.class));
        
        LogActivityRunnable logActivityRunnable = new LogActivityRunnable(mockEntry, mockReporting);
        Thread logThread = new Thread(logActivityRunnable);
        logThread.start();
        logThread.join();
        
        verify(mockReporting, times(1)).writeLogEntry(captorLogEntry.capture());
        final LogEntry logEntry = captorLogEntry.getValue();
        assertEquals(logEntry.getActivityName(), Activity.UNKNOWN.getName());
        
        verify(mockAppender, times(2)).doAppend(captorLoggingEvent.capture());
        final LoggingEvent loggingEvent = captorLoggingEvent.getValue();
        assertEquals(loggingEvent.getLevel(), Level.ERROR);
        String expected = "\"Failed to log activity\" Activity=Unknown Domain=Lodging DomainId=123 FileName=s3://sthree.com/my/great/file.jpg " +
                "Error=\"java.lang.RuntimeException\n" +
                "\tat com.expedia.content.media.processing.pipeline.reporting.LogActivityRunnable.run(LogActivityRunnable.java:24)\n" +
                "\tat java.lang.Thread.run(Thread.java:745)\n" +
                "\"";
        assertEquals(expected, loggingEvent.getFormattedMessage());
    }
}
