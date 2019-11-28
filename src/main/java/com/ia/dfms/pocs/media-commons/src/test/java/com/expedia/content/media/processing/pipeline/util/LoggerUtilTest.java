package com.expedia.content.media.processing.pipeline.util;

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
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.Arrays;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class LoggerUtilTest {

    @Test
    public void testImageMessageLogFormatting() throws Exception {
        ImageMessage imageMessage = ImageMessage.builder()
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
                .callback(new URL("http://multi.source.callback/callback"))
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

        String formattedLog = LoggerUtil.buildLogMessage("I'm a log and so can you!", imageMessage);
        assertEquals(("\"I'm a log and so can you!\" MediaGuid=aaaaaaa-1010-bbbb-292929229 RequestId=bbbbbb-1010-bbbb-292929229 " +
                "ClientId=EPC FileName=original_file_name.png FileUrl=file:/my/files/are/awesome.jpg Domain=Lodging "), formattedLog);
        formattedLog = LoggerUtil.buildLogMessage("I'm a log and so can you!", imageMessage, MessageConstants.METADATA_DETAILS);
        assertEquals(("\"I'm a log and so can you!\" MediaGuid=aaaaaaa-1010-bbbb-292929229 RequestId=bbbbbb-1010-bbbb-292929229 " +
                "ClientId=EPC FileName=original_file_name.png FileUrl=file:/my/files/are/awesome.jpg Domain=Lodging " +
                "MetadataDetails=\"{Properties:exif:BitsPerSample=8, 8, 8, 8, Properties:exif:DateTime=2011:02:14 13:45:36}\" "), formattedLog);
        formattedLog = LoggerUtil.buildLogMessage("I'm a log and so can you! Thing={}", Arrays.asList("thingValue"), imageMessage, MessageConstants.METADATA_DETAILS);
        assertEquals(("\"I'm a log and so can you!\" Thing=thingValue MediaGuid=aaaaaaa-1010-bbbb-292929229 RequestId=bbbbbb-1010-bbbb-292929229 " +
                "ClientId=EPC FileName=original_file_name.png FileUrl=file:/my/files/are/awesome.jpg Domain=Lodging " +
                "MetadataDetails=\"{Properties:exif:BitsPerSample=8, 8, 8, 8, Properties:exif:DateTime=2011:02:14 13:45:36}\" "), formattedLog);
        // CHECKSTYLE:OFF
        try {
            String iThrowAnError = null;
            iThrowAnError.equals("throw me");
        } catch (Exception e) {
            formattedLog = LoggerUtil.buildLogMessage(e, "I'm a log and so can you!", imageMessage, MessageConstants.METADATA_DETAILS);
            assertTrue((formattedLog).matches("\"I'm a log and so can you!\" MediaGuid=aaaaaaa-1010-bbbb-292929229 RequestId=bbbbbb-1010-bbbb-292929229 " +
                    "ClientId=EPC FileName=original_file_name.png FileUrl=file:/my/files/are/awesome.jpg Domain=Lodging " +
                    "MetadataDetails=\"\\{Properties:exif:BitsPerSample=8, 8, 8, 8, Properties:exif:DateTime=2011:02:14 13:45:36}\" " +
                    "Error=\"java.lang.NullPointerException(?s).*\""));
            formattedLog = LoggerUtil.buildLogMessage(e, "I'm a log and so can you! Thing={}", Arrays.asList("thingValue"), imageMessage, MessageConstants.METADATA_DETAILS);
            assertTrue((formattedLog).matches("\"I'm a log and so can you!\" Thing=thingValue MediaGuid=aaaaaaa-1010-bbbb-292929229 RequestId=bbbbbb-1010-bbbb-292929229 " +
                    "ClientId=EPC FileName=original_file_name.png FileUrl=file:/my/files/are/awesome.jpg Domain=Lodging " +
                    "MetadataDetails=\"\\{Properties:exif:BitsPerSample=8, 8, 8, 8, Properties:exif:DateTime=2011:02:14 13:45:36}\" " +
                    "Error=\"java.lang.NullPointerException(?s).*\""));
        }
        // CHECKSTYLE:ON
    }

    @Test
    public void testJSONMessageLogFormatting() throws Exception {
        String jsonMessage = "{  \n" +
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
        String formattedLog = LoggerUtil.buildLogMessage("I'm being Logged!", jsonMessage);
        assertEquals(("\"I'm being Logged!\" MediaGuid=aaaaaaa-1010-bbbb-292929229 FileUrl=http://images.com/dir1/img1.jpg Domain=Lodging "), (formattedLog));
        formattedLog = LoggerUtil.buildLogMessage("I'm being Logged!", jsonMessage, MessageConstants.FINGERPRINTS);
        assertEquals(("\"I'm being Logged!\" MediaGuid=aaaaaaa-1010-bbbb-292929229 FileUrl=http://images.com/dir1/img1.jpg Domain=Lodging " +
                "Fingerprints=\"[{algorithm=SHA1, values=[444444444444]}, {algorithm=PHash, values=[zzzzzzzzzzzz]}]\" "), (formattedLog));
        formattedLog = LoggerUtil.buildLogMessage("I'm being Logged! Thing={}", Arrays.asList("thingValue"), jsonMessage, MessageConstants.FINGERPRINTS);
        assertEquals(("\"I'm being Logged!\" Thing=thingValue MediaGuid=aaaaaaa-1010-bbbb-292929229 FileUrl=http://images.com/dir1/img1.jpg Domain=Lodging " +
                "Fingerprints=\"[{algorithm=SHA1, values=[444444444444]}, {algorithm=PHash, values=[zzzzzzzzzzzz]}]\" "), (formattedLog));
        // CHECKSTYLE:OFF
        try {
            String iThrowAnError = null;
            iThrowAnError.equals("throw me");
        } catch (Exception e) {
            formattedLog = LoggerUtil.buildLogMessage(e, "I'm being Logged!", jsonMessage, MessageConstants.FINGERPRINTS);
            assertTrue((formattedLog).matches("\"I'm being Logged!\" MediaGuid=aaaaaaa-1010-bbbb-292929229 " +
                    "FileUrl=http://images.com/dir1/img1.jpg Domain=Lodging " +
                    "Fingerprints=\"\\[\\{algorithm=SHA1, values=\\[444444444444]}, \\{algorithm=PHash, values=\\[zzzzzzzzzzzz]}]\" " +
                    "Error=\"java.lang.NullPointerException(?s).*\""));
            formattedLog = LoggerUtil.buildLogMessage(e, "I'm being Logged! Thing={}", Arrays.asList("thingValue"), jsonMessage, MessageConstants.FINGERPRINTS);
            assertTrue((formattedLog).matches("\"I'm being Logged!\" Thing=thingValue MediaGuid=aaaaaaa-1010-bbbb-292929229" +
                    " FileUrl=http://images.com/dir1/img1.jpg Domain=Lodging " +
                    "Fingerprints=\"\\[\\{algorithm=SHA1, values=\\[444444444444]}, \\{algorithm=PHash, values=\\[zzzzzzzzzzzz]}]\" " +
                    "Error=\"java.lang.NullPointerException(?s).*\""));
        }
        // CHECKSTYLE:ON
    }

    @Test
    public void testLogFormatting() throws Exception {
        String formattedLog = LoggerUtil.formatLogMessage("I'm a log", Arrays.asList("CoolFile_Name Dot.jpg"),
                MessageConstants.FILE_NAME, MessageConstants.CLIENT_ID, MessageConstants.FILE_SIZE);
        assertEquals("\"I'm a log\" FileName=\"CoolFile_Name Dot.jpg\" ", formattedLog);
        formattedLog = LoggerUtil.formatLogMessage("I'm a log", Arrays.asList("CoolFile_Name Dot.jpg", "EPC_INTERNAL", "1234525"),
                MessageConstants.FILE_NAME, MessageConstants.CLIENT_ID, MessageConstants.FILE_SIZE);
        assertEquals("\"I'm a log\" FileName=\"CoolFile_Name Dot.jpg\" ClientId=EPC_INTERNAL FileSize=1234525 ", formattedLog);
        formattedLog = LoggerUtil.formatLogMessage("I'm a log", Arrays.asList("CoolFile_Name Dot.jpg", "EPC_INTERNAL", "1234525", "I should not be logged"),
                MessageConstants.FILE_NAME, MessageConstants.CLIENT_ID, MessageConstants.FILE_SIZE);
        assertEquals("\"I'm a log\" FileName=\"CoolFile_Name Dot.jpg\" ClientId=EPC_INTERNAL FileSize=1234525 ", formattedLog);
        formattedLog = LoggerUtil.buildLogMessage("I'm a log", Arrays.asList("CoolFile_Name Dot.jpg", "EPC_INTERNAL", "1234525", "I should not be logged"),
                MessageConstants.FILE_NAME, MessageConstants.CLIENT_ID, MessageConstants.FILE_SIZE);
        assertEquals("\"I'm a log\" FileName=\"CoolFile_Name Dot.jpg\" ClientId=EPC_INTERNAL FileSize=1234525 ", formattedLog);
        // CHECKSTYLE:OFF
        try {
            String iThrowAnError = null;
            iThrowAnError.equals("throw me");
        } catch (Exception e) {
            formattedLog = LoggerUtil.formatLogMessage(e, "I'm a log", Arrays.asList("CoolFile_Name Dot.jpg", "EPC_INTERNAL", "1234525"),
                    MessageConstants.FILE_NAME, MessageConstants.CLIENT_ID, MessageConstants.FILE_SIZE);
            assertTrue((formattedLog).matches("\"I'm a log\" FileName=\"CoolFile_Name Dot.jpg\" " +
                    "ClientId=EPC_INTERNAL FileSize=1234525 Error=\"java.lang.NullPointerException(?s).*\""));
        }
        // CHECKSTYLE:ON
    }

    @Test
    public void testMessageParsing() throws Exception {
        String formattedLog = LoggerUtil.parseMessageInFormat("I'm a logger Field={} Field=Value");
        assertEquals("\"I'm a logger\" Field={} Field=Value", formattedLog);

        formattedLog = LoggerUtil.buildLogMessage("I'm a logger Field={} Field=Value Field=\"{}\"", "Value 1", "Value 2");
        assertEquals("\"I'm a logger\" Field=\"Value 1\" Field=Value Field=\"Value 2\"", formattedLog);

        formattedLog = LoggerUtil.formatLogMessage("I'm a logger Field={} Field=Value Field=\"{}\"", "Value 1", "Value 2");
        assertEquals("\"I'm a logger\" Field=\"Value 1\" Field=Value Field=\"Value 2\"", formattedLog);
        formattedLog = LoggerUtil.formatLogMessage("I'm a logger Field={} Field=Value Field=\"{}\"", "Value 1", "Value 2", "Value3");
        assertEquals("\"I'm a logger\" Field=\"Value 1\" Field=Value Field=\"Value 2\"", formattedLog);
        formattedLog = LoggerUtil.formatLogMessage("I'm a log");
        assertEquals("\"I'm a log\"", formattedLog);
        formattedLog = LoggerUtil.formatLogMessage("I should be parsed like a JSON", "{\"json\":true}");
        assertEquals("\"I should be parsed like a JSON\" ImageMessage=[{\"json\":true}] ", formattedLog);
        formattedLog = LoggerUtil.formatLogMessage("I should be parsed like a JSON", "{\"json\":true}", "json");
        assertEquals("\"I should be parsed like a JSON\" Json=true ", formattedLog);
        formattedLog = LoggerUtil.formatLogMessage("Calling {} with {}={}", "SprocName", "values", "value1");
        assertEquals("\"Calling SprocName with\" values=value1", formattedLog);

        // CHECKSTYLE:OFF
        try {
            String iThrowAnError = null;
            iThrowAnError.equals("throw me");
        } catch (Exception e) {
            formattedLog = LoggerUtil.formatLogMessage(e, "I'm a logger Field={} Field=Value Field=\"{}\"", "Value 1", "Value 2");
            assertTrue((formattedLog).matches("\"I'm a logger\" Field=\"Value 1\" Field=Value Field=\"Value 2\" Error=\"java.lang.NullPointerException(?s).*\""));
            formattedLog = LoggerUtil.formatLogMessage(e, "I'm a log");
            assertTrue((formattedLog).matches("\"I'm a log\" Error=\"java.lang.NullPointerException(?s).*\""));

        }
        //CHECKSTYLE:ON
    }

    @Test
    public void testFormatParsing() throws Exception {
        String format = LoggerUtil.parseMessageInFormat("Message");
        assertEquals("\"Message\"", format);
        format = LoggerUtil.parseMessageInFormat("Message Message");
        assertEquals("\"Message Message\"", format);
        format = LoggerUtil.parseMessageInFormat("Field={}");
        assertEquals("Field={}", format);
        format = LoggerUtil.parseFormat("Field1={} Field2={} Field3={}", "Value1", "Value2", "Value\"3\"");
        assertEquals("Field1=Value1 Field2=Value2 Field3=[Value\"3\"]", format);
        format = LoggerUtil.parseFormat("Field1={} Field2={} Field3={}", "Value1", "Value2");
        assertEquals("Field1=Value1 Field2=Value2 Field3={}", format);
        format = LoggerUtil.parseFormat("Field1={} Field2={}", "Value1", "Value2", "Value\"3\"");
        assertEquals("Field1=Value1 Field2=Value2", format);
        format = LoggerUtil.parseFormat("Message Field1=\"Stuff and things\" Field2={} Field3={} Field4={}", "Value1", "{\"Value\":\"Value\"}");
        assertEquals("\"Message\" Field1=\"Stuff and things\" Field2=Value1 Field3=[{\"Value\":\"Value\"}] Field4={}", format);
    }

    @Test
    public void testStringOuterDomain() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {

        String jsonMessage = "{  \n" +
                "   \"mediaGuid\":\"aaaaaaa-1010-bbbb-292929229\",\n" +
                "   \"fileUrl\":\"http://images.com/dir1/img1.jpg\",\n" +
                "   \"domain\": \"Lodging\",\n" +
                "   \"domainId\": \"1234\",\n" +
                "   \"domainProvider\": \"Comics\",\n" +
                "   \"domainFields\": \"fgdfgdfgdfg\"" +
                "}";

        String formattedLog = LoggerUtil.buildLogMessage("logger json test", jsonMessage, "domainFields");
        assertTrue(formattedLog.contains("fgdfgdfgdfg"));
    }

    @Test
    public void testEmptyOuterDomain() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {

        String jsonMessage = "{  \n" +
                "   \"mediaGuid\":\"aaaaaaa-1010-bbbb-292929229\",\n" +
                "   \"fileUrl\":\"http://images.com/dir1/img1.jpg\",\n" +
                "   \"domain\": \"Lodging\",\n" +
                "   \"domainId\": \"1234\",\n" +
                "   \"domainProvider\": \"Comics\",\n" +
                "   \"domainFields\": \"\"" +
                "}";
        String formattedLog = LoggerUtil.buildLogMessage("logger json test", jsonMessage, "domainFields", "domainProvider");
        assertTrue(!formattedLog.contains("domainFields"));
        assertTrue(formattedLog.contains("Comics"));

    }

    @Test
    public void testNullOuterDomain() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {

        String jsonMessage = "{  \n" +
                "   \"mediaGuid\":\"aaaaaaa-1010-bbbb-292929229\",\n" +
                "   \"fileUrl\":\"http://images.com/dir1/img1.jpg\",\n" +
                "   \"domain\": \"Lodging\",\n" +
                "   \"domainId\": \"1234\",\n" +
                "   \"domainProvider\": \"Comics\",\n" +
                "   \"domainFields\": null" +
                "}";

        String formattedLog = LoggerUtil.buildLogMessage("logger json test", jsonMessage, "domainFields", "domainProvider");
        assertTrue(!formattedLog.contains("domainFields"));
        assertTrue(formattedLog.contains("Comics"));
    }
}
