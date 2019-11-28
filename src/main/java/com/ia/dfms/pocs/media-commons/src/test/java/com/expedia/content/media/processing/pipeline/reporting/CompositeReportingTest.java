package com.expedia.content.media.processing.pipeline.reporting;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.expedia.content.media.processing.pipeline.util.FormattedLogger;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.expedia.content.media.processing.pipeline.domain.Domain;
import com.expedia.content.media.processing.pipeline.reporting.dynamo.DynamoReporting;
import com.expedia.content.media.processing.pipeline.reporting.sql.LcmReporting;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.LoggingEvent;
import ch.qos.logback.core.Appender;

@SuppressWarnings("deprecation")
@RunWith(MockitoJUnitRunner.class)
public class CompositeReportingTest {
    
    private static final LogEntry LOG_ENTRY =
            new LogEntry("myawesomefile.jpg", "36a5-4adf-a80a-dd0b56d664a5", Activity.UNKNOWN, new Date(), Domain.LODGING, "123", null);
    @Mock
    private Appender mockAppender;
    
    @Captor
    private final ArgumentCaptor<LoggingEvent> captorLoggingEvent = ArgumentCaptor.forClass(LoggingEvent.class);
    
    @Mock
    private DynamoReporting mockDynamo;
    
    @Mock
    private LcmReporting mockLcm;
    
    private final List<Reporting> reportings = new ArrayList<Reporting>();
    
    @Before
    public void setUp() {
        final Logger logger = (Logger) new FormattedLogger(CompositeReporting.class).getLogger();
        logger.addAppender(mockAppender);
        reportings.add(mockDynamo);
        reportings.add(mockLcm);
        LOG_ENTRY.setAppName("AppName");
        
    }
    
    @Test
    public void testReportLogEntry() {
        
        CompositeReporting compositeReporting = new CompositeReporting(reportings);
        compositeReporting.writeLogEntry(LOG_ENTRY);
        
        verify(mockDynamo, times(1)).writeLogEntry(eq(LOG_ENTRY));
        verify(mockLcm, times(1)).writeLogEntry(eq(LOG_ENTRY));
    }
    
    @Test
    public void testKeepLoggingIfOneFails() throws Exception {
        CompositeReporting compositeReporting = new CompositeReporting(reportings);
        
        Mockito.doThrow(new RuntimeException("Unable to log in LCM")).when(mockLcm).writeLogEntry(LOG_ENTRY);
        try {
            compositeReporting.writeLogEntry(LOG_ENTRY);
        } catch (Exception e) {
            verify(mockDynamo, times(1)).writeLogEntry(eq(LOG_ENTRY));
            verify(mockLcm, times(1)).writeLogEntry(eq(LOG_ENTRY));
            verify(mockAppender, times(1)).doAppend(captorLoggingEvent.capture());

            final LoggingEvent loggingEvent = captorLoggingEvent.getValue();
            System.out.println(loggingEvent.getFormattedMessage());
            assertTrue(loggingEvent.getFormattedMessage()
                    .contains("\"Error during persisting the log\" ErrorMessage=\"Unable to log in LCM\" Location=LcmReporting"));
            assertEquals(Level.ERROR, loggingEvent.getLevel());
        }
    }
    
}