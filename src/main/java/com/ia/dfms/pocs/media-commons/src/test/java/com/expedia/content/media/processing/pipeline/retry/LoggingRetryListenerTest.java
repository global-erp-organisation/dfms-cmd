package com.expedia.content.media.processing.pipeline.retry;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.LoggingEvent;
import ch.qos.logback.core.Appender;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class LoggingRetryListenerTest {
    @SuppressWarnings("rawtypes")
    private Appender mockAppender;
    
    @Captor
    private ArgumentCaptor<LoggingEvent> captorLoggingEvent = ArgumentCaptor.forClass(LoggingEvent.class);
    
    @SuppressWarnings("unchecked")
    @Before
    public void setUp() {
        mockAppender = mock(Appender.class);
        Logger logger = (Logger) LoggerFactory.getLogger(LoggingRetryListener.class);
        logger.addAppender(mockAppender);
    }
    
    @SuppressWarnings("unchecked")
    @Test
    public void testCloseWhenThereIsNoExceptionPassed() {
        LoggingRetryListener loggingRetryListener = new LoggingRetryListener();
        Exception exceptionIsNull = null;
        loggingRetryListener.close(null, null, exceptionIsNull);
        
        verify(mockAppender, never()).doAppend(captorLoggingEvent.capture());
    }
    
    @SuppressWarnings("unchecked")
    @Test
    public void testCloseWhenThereIsAnExceptionPassed() {
        LoggingRetryListener loggingRetryListener = new LoggingRetryListener();
        Exception exception = new Exception("error");
        loggingRetryListener.close(null, null, exception);
        
        verify(mockAppender, times(1)).doAppend(captorLoggingEvent.capture());
        
        final LoggingEvent loggingEvent = captorLoggingEvent.getValue();
        assertEquals(loggingEvent.getLevel(), Level.ERROR);
        assertTrue(loggingEvent.getMessage().matches("\"Number of retrials is exhausted. Please see stacktrace for error details.\" Error=\"(?s).*\""));
    }
}
