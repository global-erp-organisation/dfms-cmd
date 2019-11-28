package com.expedia.content.media.processing.pipeline.util;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.LoggingEvent;
import ch.qos.logback.core.Appender;
import com.expedia.content.media.processing.pipeline.domain.Derivative;
import com.expedia.content.media.processing.pipeline.domain.Domain;
import com.expedia.content.media.processing.pipeline.domain.Fingerprint;
import com.expedia.content.media.processing.pipeline.domain.ImageMessage;
import com.expedia.content.media.processing.pipeline.domain.MessageConstants;
import com.expedia.content.media.processing.pipeline.domain.Metadata;
import com.expedia.content.media.processing.pipeline.domain.OuterDomain;
import com.expedia.content.media.processing.pipeline.reporting.Activity;
import com.expedia.content.media.processing.pipeline.reporting.App;
import com.expedia.content.media.processing.pipeline.reporting.LogEntry;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import ch.qos.logback.classic.Logger;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

// CHECKSTYLE:OFF
@RunWith(MockitoJUnitRunner.class)
public class FormattedLoggerTest {

    @Mock
    private ImageMessage mockImageMessage;

    @Mock
    private Appender mockAppender;

    @Captor
    private final ArgumentCaptor<LoggingEvent> captorLoggingEvent = ArgumentCaptor.forClass(LoggingEvent.class);

    @Before
    public void setUp() {
        final Logger logger = (Logger) new FormattedLogger(LoggerTest.class).getLogger();
        logger.addAppender(mockAppender);
    }

    @Test
    public void testImageMessageFieldExtraction() {
        LoggerTest loggerTest = new LoggerTest();
        loggerTest.logImageMessageFieldExtraction();

        verify(mockAppender, times(1)).doAppend(captorLoggingEvent.capture());

        final LoggingEvent loggingEvent = captorLoggingEvent.getValue();
        assertTrue(loggingEvent.getFormattedMessage()
                .contains("\"Logger Message\" MediaGuid=aaaaaaa-1010-bbbb-292929229 RequestId=bbbbbb-1010-bbbb-292929229 " +
                        "ClientId=EPC FileName=original_file_name.png FileUrl=file:/my/files/are/awesome.jpg Domain=Lodging " +
                        "Fingerprints=\"[{values=[01010101010101001010101], algorithm=PHash}, {values=[1234, 5678], algorithm=SHA1}]\" "));
        assertEquals(Level.INFO, loggingEvent.getLevel());
    }

    @Test
    public void testImageMessageFieldExtractionError() {
        LoggerTest loggerTest = new LoggerTest();
        loggerTest.logImageMessageFieldExtractionError();

        verify(mockAppender, times(1)).doAppend(captorLoggingEvent.capture());

        final LoggingEvent loggingEvent = captorLoggingEvent.getValue();
        assertTrue(loggingEvent.getFormattedMessage()
                .contains("\"Logger Message\" MediaGuid=aaaaaaa-1010-bbbb-292929229 RequestId=bbbbbb-1010-bbbb-292929229 " +
                        "ClientId=EPC FileName=original_file_name.png FileUrl=file:/my/files/are/awesome.jpg Domain=Lodging " +
                        "Fingerprints=\"[{values=[01010101010101001010101], algorithm=PHash}, {values=[1234, 5678], algorithm=SHA1}]\" " +
                        "Error=\"java.lang.NullPointerException"));
        assertEquals(Level.ERROR, loggingEvent.getLevel());
    }

    @Test
    public void testJSONMessageFieldExtraction() {
        LoggerTest loggerTest = new LoggerTest();
        loggerTest.logJSONMessageFieldExtraction();

        verify(mockAppender, times(1)).doAppend(captorLoggingEvent.capture());

        final LoggingEvent loggingEvent = captorLoggingEvent.getValue();
        assertTrue(loggingEvent.getFormattedMessage()
                .contains("\"Logger Message\" MediaGuid=aaaaaaa-1010-bbbb-292929229 FileUrl=http://images.com/dir1/img1.jpg Domain=Lodging " +
                        "Fingerprints=\"[{algorithm=SHA1, values=[444444444444]}, {algorithm=PHash, values=[zzzzzzzzzzzz]}]\" "));
        assertEquals(Level.INFO, loggingEvent.getLevel());
    }

    @Test
    public void testJSONMessageFieldExtractionError() {
        LoggerTest loggerTest = new LoggerTest();
        loggerTest.logJSONMessageFieldExtractionError();

        verify(mockAppender, times(1)).doAppend(captorLoggingEvent.capture());

        final LoggingEvent loggingEvent = captorLoggingEvent.getValue();
        assertTrue(loggingEvent.getFormattedMessage()
                .contains("\"Logger Message\" MediaGuid=aaaaaaa-1010-bbbb-292929229 FileUrl=http://images.com/dir1/img1.jpg " +
                        "Domain=Lodging Fingerprints=\"[{algorithm=SHA1, values=[444444444444]}, {algorithm=PHash, values=[zzzzzzzzzzzz]}]\" " +
                        "Error=\"java.lang.NullPointerException"));
        assertEquals(Level.ERROR, loggingEvent.getLevel());
    }

    @Test
    public void testImageMessageWithFormat() {
        LoggerTest loggerTest = new LoggerTest();
        loggerTest.logImageMessageWithFormat();

        verify(mockAppender, times(1)).doAppend(captorLoggingEvent.capture());

        final LoggingEvent loggingEvent = captorLoggingEvent.getValue();
        assertTrue(loggingEvent.getFormattedMessage()
                .contains("\"Logger SprocName Message\" Field1=[V\"al\" ue1] Field2=Value2 Filed3=\"Value 3\" Field4=Value4 " +
                        "Field5=10 Field6=432 MediaGuid=aaaaaaa-1010-bbbb-292929229 RequestId=bbbbbb-1010-bbbb-292929229 ClientId=EPC " +
                        "FileName=original_file_name.png FileUrl=file:/my/files/are/awesome.jpg Domain=Lodging Fingerprints=\"[{values=[01010101010101001010101], " +
                        "algorithm=PHash}, {values=[1234, 5678], algorithm=SHA1}]\""));
        assertEquals(Level.INFO, loggingEvent.getLevel());
    }

    @Test
    public void testImageMessageWithFormatError() {
        LoggerTest loggerTest = new LoggerTest();
        loggerTest.logImageMessageWithFormatError();

        verify(mockAppender, times(1)).doAppend(captorLoggingEvent.capture());

        final LoggingEvent loggingEvent = captorLoggingEvent.getValue();
        assertTrue(loggingEvent.getFormattedMessage()
                .contains("\"Logger SprocName Message\" Field1=[V\"al\" ue1] Field2=Value2 Filed3=\"Value 3\" Field4=Value4 " +
                        "MediaGuid=aaaaaaa-1010-bbbb-292929229 RequestId=bbbbbb-1010-bbbb-292929229 ClientId=EPC FileName=original_file_name.png " +
                        "FileUrl=file:/my/files/are/awesome.jpg Domain=Lodging Fingerprints=\"[{values=[01010101010101001010101], algorithm=PHash}, " +
                        "{values=[1234, 5678], algorithm=SHA1}]\" Error=\"java.lang.NullPointerException"));
        assertEquals(Level.ERROR, loggingEvent.getLevel());
    }

    @Test
    public void testJSONMessageWithFormat() {
        LoggerTest loggerTest = new LoggerTest();
        loggerTest.logJSONMessageWithFormat();

        verify(mockAppender, times(1)).doAppend(captorLoggingEvent.capture());

        final LoggingEvent loggingEvent = captorLoggingEvent.getValue();
        assertTrue(loggingEvent.getFormattedMessage()
                .contains("\"Logger SprocName Message\" Field1=[V\"al\" ue1] Field2=Value2 Filed3=\"Value 3\" Field4=Value4 " +
                        "MediaGuid=aaaaaaa-1010-bbbb-292929229 FileUrl=http://images.com/dir1/img1.jpg Domain=Lodging " +
                        "Fingerprints=\"[{algorithm=SHA1, values=[444444444444]}, {algorithm=PHash, values=[zzzzzzzzzzzz]}]\""));
        assertEquals(Level.INFO, loggingEvent.getLevel());
    }

    @Test
    public void testJSONMessageWithFormatError() {
        LoggerTest loggerTest = new LoggerTest();
        loggerTest.logJSONMessageWithFormatError();

        verify(mockAppender, times(1)).doAppend(captorLoggingEvent.capture());

        final LoggingEvent loggingEvent = captorLoggingEvent.getValue();
        assertTrue(loggingEvent.getFormattedMessage()
                .contains("\"Logger SprocName Message\" Field1=[V\"al\" ue1] Field2=Value2 Filed3=\"Value 3\" Field4=Value4 " +
                        "MediaGuid=aaaaaaa-1010-bbbb-292929229 FileUrl=http://images.com/dir1/img1.jpg Domain=Lodging " +
                        "Fingerprints=\"[{algorithm=SHA1, values=[444444444444]}, {algorithm=PHash, values=[zzzzzzzzzzzz]}]\" " +
                        "Error=\"java.lang.NullPointerException"));
        assertEquals(Level.ERROR, loggingEvent.getLevel());
    }

    @Test
    public void testLogFormat() {
        LoggerTest loggerTest = new LoggerTest();
        loggerTest.logFormat();

        verify(mockAppender, times(1)).doAppend(captorLoggingEvent.capture());

        final LoggingEvent loggingEvent = captorLoggingEvent.getValue();
        assertTrue(loggingEvent.getFormattedMessage()
                .contains("\"Logger SprocName Message\" Field1=[V\"al\" ue1] Field2=Value2 Filed3=\"Value 3\" Field4=Value4"));
        assertEquals(Level.INFO, loggingEvent.getLevel());
    }

    @Test
    public void testLogFormatError() {
        LoggerTest loggerTest = new LoggerTest();
        loggerTest.logFormatError();

        verify(mockAppender, times(1)).doAppend(captorLoggingEvent.capture());

        final LoggingEvent loggingEvent = captorLoggingEvent.getValue();
        assertTrue(loggingEvent.getFormattedMessage()
                .contains("\"Logger SprocName Message\" Field1=[V\"al\" ue1] Field2=Value2 Filed3=\"Value 3\" Field4=Value4 Error=\"java.lang.NullPointerException"));
        assertEquals(Level.ERROR, loggingEvent.getLevel());
    }

    @Test
    public void testLogValuesAsList() {
        LoggerTest loggerTest = new LoggerTest();
        loggerTest.logValuesAsList();

        verify(mockAppender, times(1)).doAppend(captorLoggingEvent.capture());

        final LoggingEvent loggingEvent = captorLoggingEvent.getValue();
        assertTrue(loggingEvent.getFormattedMessage()
                .contains("\"Logger Message\" Field1=Value1 Field2=\"Value 2\" Field3=[Value \"3\"] "));
        assertEquals(Level.INFO, loggingEvent.getLevel());
    }

    @Test
    public void testLogValuesAsListError() {
        LoggerTest loggerTest = new LoggerTest();
        loggerTest.logValuesAsListError();

        verify(mockAppender, times(1)).doAppend(captorLoggingEvent.capture());

        final LoggingEvent loggingEvent = captorLoggingEvent.getValue();
        assertTrue(loggingEvent.getFormattedMessage()
                .contains("\"Logger Message\" Field1=Value1 Field2=\"Value 2\" Field3=[Value \"3\"] Error=\"java.lang.NullPointerException"));
        assertEquals(Level.ERROR, loggingEvent.getLevel());
    }

    @Test
    public void testOnlyMessage() {
        LoggerTest loggerTest = new LoggerTest();
        loggerTest.logOnlyMessage();

        verify(mockAppender, times(1)).doAppend(captorLoggingEvent.capture());

        final LoggingEvent loggingEvent = captorLoggingEvent.getValue();
        assertTrue(loggingEvent.getFormattedMessage()
                .contains("\"Things Happened\""));
        assertEquals(Level.WARN, loggingEvent.getLevel());
    }

    @Test
    public void testOnlyMessageError() {
        LoggerTest loggerTest = new LoggerTest();
        loggerTest.logOnlyMessageError();

        verify(mockAppender, times(1)).doAppend(captorLoggingEvent.capture());

        final LoggingEvent loggingEvent = captorLoggingEvent.getValue();
        assertTrue(loggingEvent.getFormattedMessage()
                .contains("\"Things Happened\" Error=\"java.lang.NullPointerException"));
        assertEquals(Level.DEBUG, loggingEvent.getLevel());
    }

    @Test
    public void testLogWholeJSONMessage() {
        LoggerTest loggerTest = new LoggerTest();
        loggerTest.logWholeJSONMessage();

        verify(mockAppender, times(1)).doAppend(captorLoggingEvent.capture());

        final LoggingEvent loggingEvent = captorLoggingEvent.getValue();
        assertTrue(loggingEvent.getFormattedMessage()
                .contains("\"Things Happened\" ReceivedMessage=[{  \n" +
                        "   \"mediaGuid\":\"aaaaaaa-1010-bbbb-292929229\",\n" +
                        "   \"fileUrl\":\"http://images.com/dir1/img1.jpg\",\n" +
                        "   \"fileSize\":1234,\n"));
        assertEquals(Level.WARN, loggingEvent.getLevel());
    }

    @Test
    public void testLogWholeJSONMessageError() {
        LoggerTest loggerTest = new LoggerTest();
        loggerTest.logWholeJSONMessageError();

        verify(mockAppender, times(1)).doAppend(captorLoggingEvent.capture());

        final LoggingEvent loggingEvent = captorLoggingEvent.getValue();
        assertTrue(loggingEvent.getFormattedMessage()
                .contains("\"Things Happened\" ReceivedMessage=[{  \n" +
                        "   \"mediaGuid\":\"aaaaaaa-1010-bbbb-292929229\",\n" +
                        "   \"fileUrl\":\"http://images.com/dir1/img1.jpg\",\n" +
                        "   \"fileSize\":1234,\n"));
        assertTrue(loggingEvent.getFormattedMessage()
                .contains("}] Error=\"java.lang.NullPointerException"));
        assertEquals(Level.WARN, loggingEvent.getLevel());
    }

    @Test
    public void testLogWholeJSONMessageWithOtherFields() {
        LoggerTest loggerTest = new LoggerTest();
        loggerTest.logWholeJSONMessageWithOtherFields();

        verify(mockAppender, times(1)).doAppend(captorLoggingEvent.capture());

        final LoggingEvent loggingEvent = captorLoggingEvent.getValue();
        assertTrue(loggingEvent.getFormattedMessage()
                .contains("\"Things Happened\" ClientID=EPC ReceivedMessage=[{  \n" +
                        "   \"mediaGuid\":\"aaaaaaa-1010-bbbb-292929229\",\n" +
                        "   \"fileUrl\":\"http://images.com/dir1/img1.jpg\",\n" +
                        "   \"fileSize\":1234,\n"));
        assertEquals(Level.DEBUG, loggingEvent.getLevel());
    }

    @Test
    public void testlogImageMessageFieldInDomainFields() {
        LoggerTest loggerTest = new LoggerTest();
        loggerTest.logImageMessageFieldInDomainFields();
        verify(mockAppender, times(1)).doAppend(captorLoggingEvent.capture());

        final LoggingEvent loggingEvent = captorLoggingEvent.getValue();
        assertTrue(loggingEvent.getFormattedMessage()
                .contains("\"Things\" MediaGuid=aaaaaaa-1010-bbbb-292929229 RequestId=bbbbbb-1010-bbbb-292929229 ClientId=EPC " +
                        "FileName=original_file_name.png FileUrl=file:/my/files/are/awesome.jpg Domain=Lodging Something=good"));
        assertEquals(Level.INFO, loggingEvent.getLevel());
    }

    @Test
    public void testlogImageMessageFieldInDomainFieldsWhenDomainFieldsDoNotExist() {
        LoggerTest loggerTest = new LoggerTest();
        loggerTest.logImageMessageFieldInDomainFieldsWhenDomainFieldsDoNotExist();
        verify(mockAppender, times(1)).doAppend(captorLoggingEvent.capture());

        final LoggingEvent loggingEvent = captorLoggingEvent.getValue();
        assertTrue(loggingEvent.getFormattedMessage()
                .contains("\"Things\" MediaGuid=aaaaaaa-1010-bbbb-292929229 RequestId=bbbbbb-1010-bbbb-292929229"));
        assertEquals(Level.INFO, loggingEvent.getLevel());
    }

    @Test
    public void testlogMockImageMessage() {
        LoggerTest loggerTest = new LoggerTest();
        loggerTest.logMockImageMessage();
        verify(mockAppender, times(1)).doAppend(captorLoggingEvent.capture());

        final LoggingEvent loggingEvent = captorLoggingEvent.getValue();
        assertTrue(loggingEvent.getFormattedMessage()
                .contains("\"Things\" ImageMessage=[]"));
        assertEquals(Level.INFO, loggingEvent.getLevel());
    }

    public class LoggerTest {
        private final FormattedLogger LOGGER = new FormattedLogger(LoggerTest.class);

        private static final String jsonMessage = "{  \n" +
                "   \"mediaGuid\":\"aaaaaaa-1010-bbbb-292929229\",\n" +
                "   \"fileUrl\":\"http://images.com/dir1/img1.jpg\",\n" +
                "   \"fileSize\":1234,\n" +
                "   \"width\":123,\n" +
                "   \"height\":1234,\n" +
                "   \"stagingKey\":{  \n" +
                "      \"externalId\":\"222\",\n" +
                "      \"providerId\":\"300\",\n" +
                "      \"sourceId\":\"99\"\n" +
                "   },\n" +
                "   \"callback\":\"http:\\/\\/multi.source.callback\\/callback\",\n" +
                "   \"comment\":\"test comment!\",\n" +
                "   \"hidden\":\"true\",\n" +
                "   \"domain\": \"Lodging\",\n" +
                "   \"domainId\": \"1234\",\n" +
                "   \"domainProvider\": \"Comics\",\n" +
                "   \"domainFields\": {\n" +
                "      \"expediaId\":2001002,\n" +
                "      \"categoryIds\":[\"801\",\"304\"],\n" +
                "      \"moreFields\": {\n" +
                "         \"moreexpediaId\":11111\n" +
                "        }\n" +
                "   },\n" +
                "   \"fingerprints\": [\n" +
                "     {\n" +
                "        \"algorithm\":\"SHA1\",\n" +
                "        \"values\":[\"444444444444\"]\n" +
                "     },\n" +
                "     {\n" +
                "        \"algorithm\":\"PHash\",\n" +
                "        \"values\":[\"zzzzzzzzzzzz\"]\n" +
                "     }\n" +
                "   ],\n" +
                "   \"derivatives\": [\n" +
                "     {\n" +
                "        \"location\":\"s3://bucket/folder/file1.jpg\",\n" +
                "        \"type\":\"x\",\n" +
                "        \"fileSize\":1234,\n" +
                "        \"width\":123,\n" +
                "        \"height\":1234\n" +
                "     },\n" +
                "     {\n" +
                "        \"location\":\"s3://bucket/folder/file2.jpg\",\n" +
                "        \"type\":\"y\",\n" +
                "        \"fileSize\":1234,\n" +
                "        \"width\":123,\n" +
                "        \"height\":1234\n" +
                "     }\n" +
                "   ],\n" +
                "   \"logEntries\":[" +
                "       {\"activity\":\"Reception\",\"appName\":\"cs-media-service\",\"activityTime\":1466539639465},\n" +
                "       {\"activity\":\"MediaMessageReceived\",\"appName\":\"cs-media-service\",\"activityTime\":1466539639465},\n" +
                "       {\"activity\":\"CollectorStart\",\"appName\":\"cs-media-collector-service\",\"activityTime\":1466539639465},\n" +
                "       {\"activity\":\"CollectorDownload\",\"appName\":\"cs-media-collector-service\",\"activityTime\":1466539639465},\n" +
                "       {\"activity\":\"CollectorConvert\",\"appName\":\"cs-media-collector-service\",\"activityTime\":1466539639465},\n" +
                "       {\"activity\":\"CollectorValidation\",\"appName\":\"cs-media-collector-service\",\"activityTime\":1466539639465},\n" +
                "       {\"activity\":\"SourceArchived\",\"appName\":\"cs-media-collector-service\",\"activityTime\":1466539639465},\n" +
                "       {\"activity\":\"DcpPickup\",\"appName\":\"cs-media-derivative-creator-service\",\"activityTime\":1466539639465},\n" +
                "       {\"activity\":\"DerivativeCalculation\",\"appName\":\"cs-media-derivative-creator-service\",\"activityTime\":1466539639465},\n" +
                "       {\"activity\":\"PreProcess\",\"appName\":\"cs-media-derivative-creator-service\",\"activityTime\":1466539639465},\n" +
                "       {\"activity\":\"DerivativeCreation\",\"appName\":\"cs-media-derivative-creator-service\",\"activityTime\":1466539639465},\n" +
                "       {\"activity\":\"PostProcess\",\"appName\":\"cs-media-derivative-creator-service\",\"activityTime\":1466539639465},\n" +
                "       {\"activity\":\"PreProcess\",\"appName\":\"cs-media-data-manager-service\",\"activityTime\":1466539639465},\n" +
                "       {\"activity\":\"DataStorage\",\"appName\":\"cs-media-data-manager-service\",\"activityTime\":1466539639465},\n" +
                "       {\"activity\":\"Publish\",\"appName\":\"cs-media-data-manager-service\",\"activityTime\":1466539639465}\n" +
                "   ],\n" +
                "   \"metadataDetails\": {\n" +
                "      \"Properties:exif:BitsPerSample\":\"8, 8, 8, 8\",\n" +
                "      \"Properties:exif:DateTime\":\"2011:02:14 13:45:36\"\n" +
                "   }\n" +
                "}";

        private final ImageMessage imageMessage = ImageMessage.builder()
                .mediaGuid("aaaaaaa-1010-bbbb-292929229")
                .requestId("bbbbbb-1010-bbbb-292929229")
                .clientId("EPC")
                .userId("you")
                .rotation("90")
                .active(true)
                .fileUrl("file:/my/files/are/awesome.jpg")
                .fileName("original_file_name.png")
                .sourceUrl("s3://bucket/source/aaaaaaa-1010-bbbb-292929229.jpg")
                .rejectedFolder("rejected")
                .comment("test comment!")
                .hidden(true)
                .outerDomainData(OuterDomain.builder()
                        .addField("something", "good")
                        .addField("key", "value")
                        .domain(Domain.LODGING)
                        .domainId("123")
                        .mediaProvider("Comics")
                        .build())
                .addFingerprint(new Fingerprint("PHash", "01010101010101001010101"))
                .addFingerprint(new Fingerprint("SHA1", "1234", "5678"))
                .metadata(Metadata.builder()
                        .width(1)
                        .height(2)
                        .fileSize(12)
                        .addDetail("Properties:exif:BitsPerSample", "8, 8, 8, 8")
                        .addDetail("Properties:exif:DateTime", "2011:02:14 13:45:36")
                        .build())
                .addDerivative(new Derivative("s3://bucket/folder/file1.jpg", "x", 3, 4, 34))
                .addDerivative(new Derivative("s3://bucket/folder/file2.jpg", "y", 5, 6, 56))
                .addDerivative(new Derivative("s3://bucket/folder/file3.jpg", "z", 7, 8, 78))
                .addLogEntry(new LogEntry(App.MEDIA_SERVICE, Activity.RECEPTION, new Date()))
                .addLogEntry(new LogEntry(App.MEDIA_SERVICE, Activity.MEDIA_MESSAGE_RECEIVED, new Date()))
                .addLogEntry(new LogEntry(App.MEDIA_COLLECTOR_SERVICE, Activity.COLLECTOR_START, new Date()))
                .addLogEntry(new LogEntry(App.MEDIA_COLLECTOR_SERVICE, Activity.COLLECTOR_DOWNLOAD, new Date()))
                .addLogEntry(new LogEntry(App.MEDIA_COLLECTOR_SERVICE, Activity.COLLECTOR_CONVERT, new Date()))
                .addLogEntry(new LogEntry(App.MEDIA_COLLECTOR_SERVICE, Activity.COLLECTOR_VALIDATION, new Date()))
                .addLogEntry(new LogEntry(App.MEDIA_COLLECTOR_SERVICE, Activity.SOURCE_ARCHIVED, new Date()))
                .addLogEntry(new LogEntry(App.MEDIA_DERIVATIVE_CREATOR_SERVICE, Activity.DCP_PICKUP, new Date()))
                .addLogEntry(new LogEntry(App.MEDIA_DERIVATIVE_CREATOR_SERVICE, Activity.DERIVATIVE_CALCULATION, new Date()))
                .addLogEntry(new LogEntry(App.MEDIA_DERIVATIVE_CREATOR_SERVICE, Activity.PREPROCESS, new Date()))
                .addLogEntry(new LogEntry(App.MEDIA_DERIVATIVE_CREATOR_SERVICE, Activity.DERIVATIVES_CREATION, new Date()))
                .addLogEntry(new LogEntry(App.MEDIA_DERIVATIVE_CREATOR_SERVICE, Activity.POSTPROCESS, new Date()))
                .addLogEntry(new LogEntry(App.MEDIA_DATA_MANAGER_SERVICE, Activity.PREPROCESS, new Date()))
                .addLogEntry(new LogEntry(App.MEDIA_DATA_MANAGER_SERVICE, Activity.DATA_STORAGE, new Date()))
                .addLogEntry(new LogEntry(App.MEDIA_DATA_MANAGER_SERVICE, Activity.PUBLISH, new Date()))
                .build();

        public LoggerTest() {

        }

        public void logImageMessageFieldExtraction() {
            LOGGER.info("Logger Message", imageMessage, MessageConstants.FINGERPRINTS);
        }

        public void logImageMessageFieldExtractionError() {
            try {
                String iThrowAnError = null;
                iThrowAnError.equals("throw me");
            } catch (Exception e) {
                LOGGER.error(e, "Logger Message", imageMessage, MessageConstants.FINGERPRINTS);
            }
        }

        public void logJSONMessageFieldExtraction() {
            LOGGER.info("Logger Message", jsonMessage, MessageConstants.FINGERPRINTS);
        }

        public void logJSONMessageFieldExtractionError() {
            try {
                String iThrowAnError = null;
                iThrowAnError.equals("throw me");
            } catch (Exception e) {
                LOGGER.error(e, "Logger Message", jsonMessage, MessageConstants.FINGERPRINTS);
            }
        }

        public void logImageMessageWithFormat() {
            LOGGER.info("Logger {} Message Field1={} Field2=Value2 Filed3=\"{}\" Field4={} Field5={} Field6={}",
                    Arrays.asList("SprocName", "V\"al\" ue1", "Value 3", "Value4", 10, 432), imageMessage, MessageConstants.FINGERPRINTS);
        }

        public void logImageMessageWithFormatError() {
            try {
                String iThrowAnError = null;
                iThrowAnError.equals("throw me");
            } catch (Exception e) {
                LOGGER.error(e, "Logger {} Message Field1={} Field2=Value2 Filed3=\"{}\" Field4={}",
                        Arrays.asList("SprocName", "V\"al\" ue1", "Value 3", "Value4"), imageMessage, MessageConstants.FINGERPRINTS);
            }
        }

        public void logJSONMessageWithFormat() {
            LOGGER.info("Logger {} Message Field1={} Field2=Value2 Filed3=\"{}\" Field4={}",
                    Arrays.asList("SprocName", "V\"al\" ue1", "Value 3", "Value4"), jsonMessage, MessageConstants.FINGERPRINTS);
        }

        public void logJSONMessageWithFormatError() {
            try {
                String iThrowAnError = null;
                iThrowAnError.equals("throw me");
            } catch (Exception e) {
                LOGGER.error(e, "Logger {} Message Field1={} Field2=Value2 Filed3=\"{}\" Field4={}",
                        Arrays.asList("SprocName", "V\"al\" ue1", "Value 3", "Value4"), jsonMessage, MessageConstants.FINGERPRINTS);
            }
        }

        public void logFormat() {
            LOGGER.info("Logger {} Message Field1={} Field2=Value2 Filed3=\"{}\" Field4={}", "SprocName", "V\"al\" ue1", "Value 3", "Value4");
        }

        public void logFormatError() {
            try {
                String iThrowAnError = null;
                iThrowAnError.equals("throw me");
            } catch (Exception e) {
                LOGGER.error(e, "Logger {} Message Field1={} Field2=Value2 Filed3=\"{}\" Field4={}", "SprocName", "V\"al\" ue1", "Value 3", "Value4");
            }
        }

        public void logValuesAsList() {
            LOGGER.info("Logger Message", Arrays.asList("Value1", "Value 2", "Value \"3\""), "Field1", "Field2", "Field3");
        }

        public void logValuesAsListError() {
            try {
                String iThrowAnError = null;
                iThrowAnError.equals("throw me");
            } catch (Exception e) {
                LOGGER.error(e, "Logger Message", Arrays.asList("Value1", "Value 2", "Value \"3\""), "Field1", "Field2", "Field3");
            }
        }

        public void logOnlyMessage() {
            LOGGER.warn("Things Happened");
        }

        public void logOnlyMessageError() {
            try {
                String iThrowAnError = null;
                iThrowAnError.equals("throw me");
            } catch (Exception e) {
                LOGGER.debug(e, "Things Happened");
            }
        }

        public void logWholeJSONMessage() {
            LOGGER.warn("Things Happened ReceivedMessage={}", jsonMessage);
        }

        public void logWholeJSONMessageError() {
            try {
                String iThrowAnError = null;
                iThrowAnError.equals("throw me");
            } catch (Exception e) {
                LOGGER.warn(e, "Things Happened ReceivedMessage={}", jsonMessage);
            }
        }

        public void logWholeJSONMessageWithOtherFields() {
            LOGGER.debug("Things Happened ClientID={} ReceivedMessage={}", "EPC", jsonMessage);
        }

        public void logImageMessageFieldInDomainFields() {
            LOGGER.info("Things", imageMessage, "something");
        }

        public void logImageMessageFieldInDomainFieldsWhenDomainFieldsDoNotExist() {
            ImageMessage imageMessageWithoutDomainFields = ImageMessage.builder()
                    .mediaGuid("aaaaaaa-1010-bbbb-292929229")
                    .requestId("bbbbbb-1010-bbbb-292929229")
                    .build();
            LOGGER.info("Things", imageMessageWithoutDomainFields, MessageConstants.OUTER_DOMAIN_PROVIDER);
        }

        public void logMockImageMessage() {
            LOGGER.info("Things", mockImageMessage);
        }

    }
}
