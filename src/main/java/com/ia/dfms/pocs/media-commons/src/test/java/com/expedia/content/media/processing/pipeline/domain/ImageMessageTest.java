package com.expedia.content.media.processing.pipeline.domain;

import com.expedia.content.media.processing.pipeline.domain.ImageMessage.ImageMessageBuilder;
import com.expedia.content.media.processing.pipeline.exception.ImageMessageException;
import com.expedia.content.media.processing.pipeline.reporting.Activity;
import com.expedia.content.media.processing.pipeline.reporting.App;
import com.expedia.content.media.processing.pipeline.reporting.LogEntry;
import com.rabbitmq.tools.json.JSONWriter;
import org.junit.Test;

import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class ImageMessageTest {

    private final JSONWriter jsonWriter = new JSONWriter();

    @Test
    public void testJSONOutput() throws Exception {
        Map<String, Object> domainDataFields = new LinkedHashMap<>();
        domainDataFields.put("something", "good");
        domainDataFields.put("key", "value");
        OuterDomain domainData = new OuterDomain(Domain.LODGING, "123", "Comics", "VirtualTour", domainDataFields);
        
        List<Fingerprint> fingerprints = new ArrayList<>();
        fingerprints.add(new Fingerprint("PHash", "01010101010101001010101"));
        fingerprints.add(new Fingerprint("SHA1", "1234", "5678"));
        
        List<Derivative> derivatives = new ArrayList<>();
        derivatives.add(new Derivative("s3://bucket/folder/file1.jpg", "x", 3, 4, 34));
        derivatives.add(new Derivative("s3://bucket/folder/file2.jpg", "y", 5, 6, 56));
        derivatives.add(new Derivative("s3://bucket/folder/file3.jpg", "z", 7, 8, 78));

        List<LogEntry> logEntries = new ArrayList<>();
        logEntries.add(new LogEntry(App.MEDIA_SERVICE, Activity.RECEPTION, new Date()));
        logEntries.add(new LogEntry(App.MEDIA_SERVICE, Activity.MEDIA_MESSAGE_RECEIVED, new Date()));
        logEntries.add(new LogEntry(App.MEDIA_COLLECTOR_SERVICE, Activity.COLLECTOR_START, new Date()));
        logEntries.add(new LogEntry(App.MEDIA_COLLECTOR_SERVICE, Activity.COLLECTOR_DOWNLOAD, new Date()));
        logEntries.add(new LogEntry(App.MEDIA_COLLECTOR_SERVICE, Activity.COLLECTOR_CONVERT, new Date()));
        logEntries.add(new LogEntry(App.MEDIA_COLLECTOR_SERVICE, Activity.COLLECTOR_VALIDATION, new Date()));
        logEntries.add(new LogEntry(App.MEDIA_COLLECTOR_SERVICE, Activity.SOURCE_ARCHIVED, new Date()));
        logEntries.add(new LogEntry(App.MEDIA_DERIVATIVE_CREATOR_SERVICE, Activity.DCP_PICKUP, new Date()));
        logEntries.add(new LogEntry(App.MEDIA_DERIVATIVE_CREATOR_SERVICE, Activity.DERIVATIVE_CALCULATION, new Date()));
        logEntries.add(new LogEntry(App.MEDIA_DERIVATIVE_CREATOR_SERVICE, Activity.PREPROCESS, new Date()));
        logEntries.add(new LogEntry(App.MEDIA_DERIVATIVE_CREATOR_SERVICE, Activity.DERIVATIVES_CREATION, new Date()));
        logEntries.add(new LogEntry(App.MEDIA_DERIVATIVE_CREATOR_SERVICE, Activity.POSTPROCESS, new Date()));
        logEntries.add(new LogEntry(App.MEDIA_DATA_MANAGER_SERVICE, Activity.PREPROCESS, new Date()));
        logEntries.add(new LogEntry(App.MEDIA_DATA_MANAGER_SERVICE, Activity.DATA_STORAGE, new Date()));
        logEntries.add(new LogEntry(App.MEDIA_DATA_MANAGER_SERVICE, Activity.PUBLISH, new Date()));

        Map<String, String> details = new HashMap<>();
        details.put("Properties:exif:BitsPerSample", "8, 8, 8, 8");
        details.put("Properties:exif:DateTime", "2011:02:14 13:45:36");
        Metadata metadata = new Metadata(1, 2, 12, details);
        
        ImageMessage imageMessage =
                ImageMessage.builder()
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
                    .hidden(false)
                    .outerDomainData(domainData)
                    .fingerprints(fingerprints)
                    .metadata(metadata)
                    .derivatives(derivatives)
                    .logEntries(logEntries)
                    .failedReason("imageFailed")
                    .retryCount(3)
                    .build();
        String jsonSerializedMessage = imageMessage.toJSONMessage();
        assertTrue(jsonSerializedMessage.contains("\"fileUrl\":\"file:\\/my\\/files\\/are\\/awesome.jpg\""));
        assertTrue(jsonSerializedMessage.contains("\"requestId\":\"bbbbbb-1010-bbbb-292929229\""));
        assertTrue(jsonSerializedMessage.contains("\"mediaGuid\":\"aaaaaaa-1010-bbbb-292929229\""));
        assertTrue(jsonSerializedMessage.contains("\"userId\":\"you\""));
        assertTrue(jsonSerializedMessage.contains("\"active\":\"true\""));
        assertTrue(jsonSerializedMessage.contains("\"domainId\":\"123\""));
        assertTrue(jsonSerializedMessage.contains("\"domainFields\":"));
        assertTrue(jsonSerializedMessage.contains("\"domainProvider\":\"Comics\""));
        assertTrue(jsonSerializedMessage.contains("\"domainDerivativeCategory\":\"VirtualTour\""));
        assertTrue(jsonSerializedMessage.contains("\"something\":\"good\""));
        assertTrue(jsonSerializedMessage.contains("\"fingerprints\":"));
        assertTrue(jsonSerializedMessage.contains("\"algorithm\":\"PHash\""));
        assertTrue(jsonSerializedMessage.contains("\"values\":[\"01010101010101001010101\"]"));
        assertTrue(jsonSerializedMessage.contains("\"algorithm\":\"SHA1\""));
        assertTrue(jsonSerializedMessage.contains("\"values\":[\"1234\",\"5678\"]"));
        assertTrue(jsonSerializedMessage.contains("\"derivatives\":"));
        assertTrue(jsonSerializedMessage.contains("\"location\":\"s3:\\/\\/bucket\\/folder\\/file1.jpg\""));
        assertTrue(jsonSerializedMessage.contains("\"metadataDetails\":"));
        assertTrue(jsonSerializedMessage.contains("\"Properties:exif:BitsPerSample\":\"8, 8, 8, 8\""));
        assertTrue(jsonSerializedMessage.contains("\"comment\":\"test comment!\""));
        assertTrue(jsonSerializedMessage.contains("\"hidden\":\"false\""));
        assertTrue(jsonSerializedMessage.contains("\"logEntries\":["));
        assertTrue(jsonSerializedMessage.contains("\"failedReason\":\"imageFailed\""));
        assertTrue(jsonSerializedMessage.contains("\"retryCount\":3"));
    }
    @SuppressWarnings("all")
    @Test
    public void testParseStringDomainFields() throws Exception {
        String jsonMessage = "{  \n"
                + "  \"active\":\"true\",\n"
                + "  \"hidden\":null,\n"
                + "  \"clientId\":null,\n"
                + "  \"requestId\":null,\n"
                + "  \"mediaGuid\":null,\n"
                + "  \"rotation\":null,\n"
                + "  \"generateThumbnail\":\"true\",\n"
                + "  \"genOutput\":null,\n"
                + "  \"rejectedOutput\":null,\n"
                + "  \"sourceUrl\":null,\n"
                + "  \"callback\":null,\n"
                + "  \"outputFolder\":null,\n"
                + "  \"rejectedFolder\":null,\n"
                + "  \"providedName\":null,\n"
                + "  \"categoryId\":null,\n"
                + "  \"caption\":null,\n"
                + "  \"mediaProviderId\":null,\n"
                + "  \"comment\":null,\n"
                + "  \"failedReason\":null,\n"
                + "  \"retryCount\":0,\n"
                + "  \"expediaId\":0,\n"
                + "  \"domain\":\"Lodging\",\n"
                + "  \"domainId\":\"41098\",\n"
                + "  \"domainProvider\":\"EPC External User\",\n"
                + "  \"domainDerivativeCategory\":null,\n"
                + "  \"fileName\":\"41098_epc_2e84ed6f0c0e417b89a259dca269ad72RnJhzIFiw6ZyIGZvcnNpzIHDsHVteW5kIDI=_cleaned.jpg\",\n"
                + "  \"fileUrl\":\"s3://ewe-cs-media-test/e2e/images/8492274_13_0013ice.jpg\",\n"
                + "  \"width\":129,\n"
                + "  \"height\":200,\n"
                + "  \"fileSize\":0,\n"
                + "  \"userId\":\"media cloud router\",\n"
                + "  \"stagingKey\":{  \n"
                + "    \"externalId\":\"2\",\n"
                + "    \"providerId\":\"1\",\n"
                + "    \"sourceId\":\"2\"\n"
                + "  },\n"
                + "  \"fingerprints\":[  \n"
                + "    {  \n"
                + "      \"algorithm\":\"SHA1\",\n"
                + "      \"values\":[  \n"
                + "        \"1000101101011001100101101110110100001100000001101\"\n"
                + "      ]\n"
                + "    },\n"
                + "    {  \n"
                + "      \"algorithm\":\"1000101101011001100101101110110100001100000001101\",\n"
                + "      \"values\":[  \n"
                + "        \"1000101101011001100101101110110100001100000001101\"\n"
                + "      ]\n"
                + "    }\n"
                + "  ],\n"
                + "  \"metadataDetails\":{  \n"
                + "    \"BitsPerSample\":\"6\",\n"
                + "    \"Colorspace\":\"sRGB\"\n"
                + "  },\n"
                + "  \"domainFields\":\"{\\\"rooms\\\":[{\\\"roomHero\\\":\\\"false\\\",\\\"roomId\\\":\\\"11309080\\\"},"
                + "{\\\"roomHero\\\":\\\"false\\\",\\\"roomId\\\":\\\"5886507\\\"},{\\\"roomHero\\\":\\\"false\\\",\\\"roomId\\\":\\\"5886575\\\"},"
                + "{\\\"roomHero\\\":\\\"false\\\",\\\"roomId\\\":\\\"5886616\\\"}],\\\"subcategoryId\\\":\\\"23000\\\",\\\"propertyHero\\\":\\\"false\\\"}\",\n"
                + "  \"derivatives\":[  \n"
                + "    {  \n"
                + "      \"location\":\"test\",\n"
                + "      \"type\":\"t\",\n"
                + "      \"width\":22,\n"
                + "      \"height\":22,\n"
                + "      \"fileSize\":200\n"
                + "    },\n"
                + "    {  \n"
                + "      \"location\":\"test\",\n"
                + "      \"type\":\"t\",\n"
                + "      \"width\":22,\n"
                + "      \"height\":22,\n"
                + "      \"fileSize\":200\n"
                + "    }\n"
                + "  ],\n"
                + "  \"logEntries\":[  \n"
                + "    {  \n"
                + "      \"activityTime\":1475172729594,\n"
                + "      \"activity\":\"CollectorDownload\",\n"
                + "      \"appName\":\"cs-media-collector-service\"\n"
                + "    },\n"
                + "    {  \n"
                + "      \"activityTime\":1475172729594,\n"
                + "      \"activity\":\"DcpPickup\",\n"
                + "      \"appName\":\"cs-media-collector-service\"\n"
                + "    }\n"
                + "  ]\n"
                + "}\n"
                + "\n";
        ImageMessage imageMessage = ImageMessage.parseJsonMessage(jsonMessage);
        Map<String, Object> domainFields = imageMessage.getOuterDomainData().getDomainFields();
        assertTrue("23000".equals(domainFields.get("subcategoryId")));
        assertTrue("false".equals(domainFields.get("propertyHero")));
    }

    @Test
    public void testJSONOutputBuilder() throws Exception {
        ImageMessage imageMessage = ImageMessage.builder()
                .mediaGuid("aaaaaaa-1010-bbbb-292929229")
                .requestId("bbbbbb-1010-bbbb-292929229")
                .clientId("EPC")
                .userId("you")
                .rotation("90")
                .active(true)
                .fileUrl("file:/my/files/are/awesome.jpg")
                .fileName("original_file_name.png")
                .failedReason("imageProcessedFailed")
                .retryCount(3)
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
        String jsonSerializedMessage = imageMessage.toJSONMessage();
        assertTrue(jsonSerializedMessage.contains("\"fileUrl\":\"file:\\/my\\/files\\/are\\/awesome.jpg\""));
        assertTrue(jsonSerializedMessage.contains("\"mediaGuid\":\"aaaaaaa-1010-bbbb-292929229\""));
        assertTrue(jsonSerializedMessage.contains("\"userId\":\"you\""));
        assertTrue(jsonSerializedMessage.contains("\"active\":\"true\""));
        assertTrue(jsonSerializedMessage.contains("\"domainId\":\"123\""));
        assertTrue(jsonSerializedMessage.contains("\"domainFields\":"));
        assertTrue(jsonSerializedMessage.contains("\"domainProvider\":\"Comics\""));
        assertTrue(jsonSerializedMessage.contains("\"something\":\"good\""));
        assertTrue(jsonSerializedMessage.contains("\"fingerprints\":"));
        assertTrue(jsonSerializedMessage.contains("\"algorithm\":\"PHash\""));
        assertTrue(jsonSerializedMessage.contains("\"values\":[\"01010101010101001010101\"]"));
        assertTrue(jsonSerializedMessage.contains("\"algorithm\":\"SHA1\""));
        assertTrue(jsonSerializedMessage.contains("\"values\":[\"1234\",\"5678\"]"));
        assertTrue(jsonSerializedMessage.contains("\"derivatives\":"));
        assertTrue(jsonSerializedMessage.contains("\"location\":\"s3:\\/\\/bucket\\/folder\\/file1.jpg\""));
        assertTrue(jsonSerializedMessage.contains("\"metadataDetails\":"));
        assertTrue(jsonSerializedMessage.contains("\"Properties:exif:BitsPerSample\":\"8, 8, 8, 8\""));
        assertTrue(jsonSerializedMessage.contains("\"comment\":\"test comment!\""));
        assertTrue(jsonSerializedMessage.contains("\"hidden\":\"true\""));
        assertTrue(jsonSerializedMessage.contains("\"logEntries\":["));
        assertTrue(jsonSerializedMessage.contains("\"failedReason\":\"imageProcessedFailed\""));
        assertTrue(jsonSerializedMessage.contains("\"retryCount\":3"));


    }

    @Test
    public void testToString() throws Exception {
        Map<String, Object> domainDataFields = new LinkedHashMap<>();
        domainDataFields.put("something", "good");
        domainDataFields.put("key", "value");
        OuterDomain domainData = new OuterDomain(Domain.LODGING, "123", "Comics", null, domainDataFields);
        
        ImageMessage imageMessage =
                new ImageMessage.ImageMessageBuilder()
                        .fileUrl("file:/my/files/are/awesome.jpg")
                        .requestId("testRequestID#1")
                        .outputFolder("output").sourceUrl("archive")
                        .rejectedFolder("rejected")
                        .outerDomainData(domainData)
                        .mediaGuid("my-guid")
                        .fileName("my-filename")
                        .active(true)
                        .clientId("my-client-id")
                        .failedReason("my-failed-reason")
                        .retryCount(3)
                        .generateThumbnail(true)
                        .build();

        assertTrue(imageMessage.toString().contains("fileUrl='file:/my/files/are/awesome.jpg'"));
        assertTrue(imageMessage.toString().contains("requestId='testRequestID#1'"));
        assertTrue(imageMessage.toString().contains("mediaGuid='my-guid'"));
        assertTrue(imageMessage.toString().contains("fileName='my-filename'"));
        assertTrue(imageMessage.toString().contains("sourceUrl=\'archive\'"));
        assertTrue(imageMessage.toString().contains("OuterDomain{"));
        assertTrue(imageMessage.toString().contains("domain=\'Lodging\'"));
        assertTrue(imageMessage.toString().contains("domainId=\'123\'"));
        assertTrue(imageMessage.toString().contains("domainFields={"));
        assertTrue(imageMessage.toString().contains("something='good'"));
        assertTrue(imageMessage.toString().contains("provider=\'Comics\'"));
        assertTrue(imageMessage.toString().contains("key='value'"));
        assertTrue(imageMessage.toString().contains("active='true'"));
        assertTrue(imageMessage.toString().contains("clientId='my-client-id'"));
        assertTrue(imageMessage.toString().contains("failedReason='my-failed-reason'"));
        assertTrue(imageMessage.toString().contains("retryCount='3'"));
        assertTrue(imageMessage.toString().contains("generateThumbnail='true'"));
    }

    @Test
    public void testInstantiationProperImageSource() throws Exception {
        final URL imageUrl = ImageMessageTest.class.getResource("/examples/office.jpg");
        String jsonMessage = buildJSONString(imageUrl, Domain.LODGING, null, null, null);
        ImageMessage imageMessage = ImageMessage.parseJsonMessage(jsonMessage);
        assertNotNull(imageMessage.getFileUrl());
        assertNull(imageMessage.getOutputFolder());
        assertNull(imageMessage.getSourceUrl());
        assertNull(imageMessage.getRejectedFolder());
    }

    @Test
    public void testFromJsonToImageObject() throws Exception {
        String jsonMessage = "{  \n" +
                "   \"mediaGuid\":\"aaaaaaa-1010-bbbb-292929229\",\n" +
                "   \"fileUrl\":\"http://images.com/dir1/img1.jpg\",\n" +
                "   \"requestId\":\"myRequestId#1\",\n" +
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
                "   \"failedReason\":\"image process failed\",\n" +
                "   \"retryCount\":3,\n" +
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

        ImageMessage imageMessage = ImageMessage.parseJsonMessage(jsonMessage);
        assertEquals("aaaaaaa-1010-bbbb-292929229", imageMessage.getMediaGuid());
        assertEquals("test comment!", imageMessage.getComment());
        assertEquals("image process failed", imageMessage.getFailedReason());
        assertEquals("myRequestId#1", imageMessage.getRequestId());
        assertEquals(3 , imageMessage.getRetryCount().intValue());

        assertEquals(Boolean.TRUE, imageMessage.getHidden());
        assertTrue(imageMessage.isActive());
        assertFalse(imageMessage.isGenerateThumbnail());
        assertNotNull(imageMessage.getFileUrl());
        assertNotNull(imageMessage.getStagingKey());
        
        assertNotNull(imageMessage.getOuterDomainData());
        assertEquals("Lodging", imageMessage.getOuterDomainData().getDomain().getDomain());
        assertEquals("1234", imageMessage.getOuterDomainData().getDomainId());
        assertEquals("Comics", imageMessage.getOuterDomainData().getProvider());
        assertNotNull(imageMessage.getOuterDomainData().getDomainFieldValue("moreFields"));
        assertNull(imageMessage.getOuterDomainData().getDomainFieldValue("potato"));
        assertEquals(11111, imageMessage.getOuterDomainData().getDomainFieldValue("moreexpediaId"));
        
        assertNotNull(imageMessage.getFingerprints());
        assertEquals(2, imageMessage.getFingerprints().size());
        assertEquals(3, imageMessage.getRetryCount().intValue());
        assertNotNull(imageMessage.getFingerprints().get(0).getAlgorithm());
        assertNotNull(imageMessage.getFingerprints().get(0).getValues());
        assertEquals(1, imageMessage.getFingerprints().get(0).getValues().size());
        
        assertNotNull(imageMessage.getDerivatives());
        assertEquals(2, imageMessage.getDerivatives().size());
        assertNotNull(imageMessage.getDerivatives().get(0).getType());
        assertNotNull(imageMessage.getDerivatives().get(0).getFileSize());
        assertNotNull(imageMessage.getDerivatives().get(0).getWidth());
        assertNotNull(imageMessage.getDerivatives().get(0).getHeight());

        assertNotNull(imageMessage.getLogEntries());
        assertEquals(15, imageMessage.getLogEntries().size());
        assertNotNull(imageMessage.getLogEntries().get(0).getAppName());
        assertNotNull(imageMessage.getLogEntries().get(0).getActivity());
        assertNotNull(imageMessage.getLogEntries().get(0).getActivityTime());

        assertNotNull(imageMessage.getMetadata());
        assertNotNull(imageMessage.getMetadata().getFileSize());
        assertNotNull(imageMessage.getMetadata().getWidth());
        assertNotNull(imageMessage.getMetadata().getHeight());
        assertNotNull(imageMessage.getMetadata().getDetails());
        assertEquals(2, imageMessage.getMetadata().getDetails().size());
        assertTrue(imageMessage.getMetadata().getDetails().containsKey("Properties:exif:BitsPerSample"));
    }

    @Test
    public void testParseWithInvalidCallbackUrl() throws Exception {
        String invalidCallbackUrl = "http22://multi.source.callback/callback";
        String jsonMessage = "{  \n" +
                "   \"mediaProviderId\":\"1001\",\n" +
                "   \"fileUrl\":\"http://images.com/dir1/img1.jpg\",\n" +
                "   \"imageType\":\"Lodging\",\n" +
                "   \"stagingKey\":{  \n" +
                "      \"externalId\":\"222\",\n" +
                "      \"providerId\":\"300\",\n" +
                "      \"sourceId\":\"99\"\n" +
                "   },\n" +
                "   \"expediaId\":2001002,\n" +
                "   \"categoryId\":\"801\",\n" +
                "   \"callback\":\"" + invalidCallbackUrl + "\",\n" +
                "   \"caption\":\"caption\"\n" +
                "}";

        try {
            ImageMessage.parseJsonMessage(jsonMessage);
            fail("ImageMessageException is expected due to invalid callback url.");
        } catch (ImageMessageException ex) {
            assertEquals(ex.getMessage(), "callback: " + invalidCallbackUrl + " is malformed.");
        }
    }

    @Test
    public void testParseWithInvalidImageType() throws Exception {
        String invalidImageType = "Lodg";
        String jsonMessage = "{  \n" +
                "   \"mediaProviderId\":\"1001\",\n" +
                "   \"fileUrl\":\"http://images.com/dir1/img1.jpg\",\n" +
                "   \"domain\":\"" + invalidImageType + "\",\n" +
                "   \"stagingKey\":{  \n" +
                "      \"externalId\":\"222\",\n" +
                "      \"providerId\":\"300\",\n" +
                "      \"sourceId\":\"99\"\n" +
                "   },\n" +
                "   \"expediaId\":\"2001002\",\n" +
                "   \"categoryId\":\"801\",\n" +
                "   \"callback\":\"http://multi.source.callback/callback\",\n" +
                "   \"caption\":\"caption\"\n" +
                "}";
        try {
            ImageMessage.parseJsonMessage(jsonMessage);
            fail("Should throw an ImageMessageException");
        } catch (ImageMessageException ex) {
            assertEquals(MessageConstants.OUTER_DOMAIN_NAME + ": " + invalidImageType + " is not a recognized domain.", ex.getMessage());
            assertTrue(ex.getCause() instanceof InvalidDomainException);
        }
    }

    @Test
    public void testImageSourceDataMembersEmpty() throws Exception {
        final URL imageUrl = ImageMessageTest.class.getResource("/examples/office.jpg");
        String jsonMessage = buildJSONString(imageUrl, Domain.LODGING, "", "", "");
        ImageMessage imageMessage = ImageMessage.parseJsonMessage(jsonMessage);
        assertEquals(Domain.LODGING, imageMessage.getOuterDomainData().getDomain());
        assertEquals("", imageMessage.getOutputFolder());
        assertNull(imageMessage.getSourceUrl());
        assertNull(imageMessage.getRejectedFolder());
    }

    @Test
    public void testParseImageTypeWithNullValue() throws Exception {
        String jsonMessage = "{  \n" +
                "   \"mediaProviderId\":\"1001\",\n" +
                "   \"fileUrl\":null,\n" +
                "   \"domain\":null,\n" +
                "   \"stagingKey\":{  \n" +
                "      \"externalId\":\"222\",\n" +
                "      \"providerId\":\"300\",\n" +
                "      \"sourceId\":\"99\"\n" +
                "   },\n" +
                "   \"callback\":\"http://multi.source.callback/callback\"\n" +
                "}";

        ImageMessage imageMessage = ImageMessage.parseJsonMessage(jsonMessage);
        assertNull(imageMessage.getOuterDomainData());
        assertNull(imageMessage.getFileUrl());
    }

    @Test
    public void testParseImageTypeWithNoValue() throws Exception {
        String jsonMessage = "{  \n" +
                "   \"mediaProviderId\":\"1001\",\n" +
                "   \"fileUrl\":\"http://images.com/dir1/img1.jpg\",\n" +
                "   \"imageType\":,\n" +
                "   \"stagingKey\":{  \n" +
                "      \"externalId\":\"222\",\n" +
                "      \"providerId\":\"300\",\n" +
                "      \"sourceId\":\"99\"\n" +
                "   },\n" +
                "   \"callback\":\"http://multi.source.callback/callback\"\n" +
                "}";

        try {
            ImageMessage.parseJsonMessage(jsonMessage);
            fail("Should throw an ImageMessageException");
        } catch (ImageMessageException ex) {
            assertTrue(ex.getMessage().startsWith("Error parsing/converting Json message: "));
        }
    }

    @Test
    public void testBuilderTransferAll() {
        String jsonMessage = "{  \n" +
                "   \"mediaGuid\":\"aaaaaaa-1010-bbbb-292929229\",\n" +
                "   \"mediaProviderId\":\"1001\",\n" +
                "   \"fileUrl\":\"http://images.com/dir1/img1.jpg\",\n" +
                "   \"stagingKey\":{  \n" +
                "      \"externalId\":\"222\",\n" +
                "      \"providerId\":\"300\",\n" +
                "      \"sourceId\":\"99\"\n" +
                "   },\n" +
                "   \"callback\":\"http:\\/\\/multi.source.callback\\/callback\",\n" +
                "   \"comment\":\"test comment!\",\n" +
                "   \"hidden\":\"true\",\n" +
                "   \"caption\":\"caption\",\n" +
                "   \"domain\": \"Lodging\",\n" +
                "   \"domainId\": \"1234\",\n" +
                "   \"domainFields\": {\n" +
                "      \"expediaId\":2001002,\n" +
                "      \"categoryIds\":[\"801\",\"304\"],\n" +
                "      \"moreFields\": {\n" +
                "         \"moreexpediaId\":11111\n" +
                "        }\n" +
                "     }\n" +
                "}";
        ImageMessage imageMessage = ImageMessage.parseJsonMessage(jsonMessage);
        ImageMessage imageMessageClone = ImageMessage.builder().transferAll(imageMessage).build();
        assertEquals(imageMessage.getMediaGuid(), imageMessageClone.getMediaGuid());
        assertEquals(imageMessage.getFileUrl(), imageMessageClone.getFileUrl());
        assertEquals(imageMessage.getOuterDomainData().getDomain(), imageMessageClone.getOuterDomainData().getDomain());
        assertEquals(imageMessage.getStagingKey(), imageMessageClone.getStagingKey());
        assertEquals(imageMessage.getOuterDomainData(), imageMessageClone.getOuterDomainData());
        assertEquals(imageMessage.getComment(), imageMessageClone.getComment());
        assertEquals(imageMessage.getHidden(), imageMessageClone.getHidden());
    }

    private String buildJSONString(URL fileURL, Domain domain, String outputFolder, String sourceUrl, String rejectedFolder) {
        Map<String, String> mapMessage = new LinkedHashMap<>();
        mapMessage.put(MessageConstants.FILE_URL, fileURL.toString());
        mapMessage.put(MessageConstants.OUTER_DOMAIN_NAME, domain.getDomain());
        if (outputFolder != null) {
            mapMessage.put(MessageConstants.GEN_OUTPUT, outputFolder);
        }
        if (sourceUrl != null) {
            mapMessage.put(MessageConstants.SOURCE_URL, sourceUrl);
        }
        if (rejectedFolder != null) {
            mapMessage.put(MessageConstants.REJECTED_OUTPUT, rejectedFolder);
        }
        return jsonWriter.write(mapMessage);
    }

    @Test
    public void testNullThenAdd() {
        assertNotNull(ImageMessage.builder()
                .derivatives(null)
                .addDerivative(new Derivative("a", "b", 1, 2, 12))
                .build());
        assertNotNull(ImageMessage.builder()
                .fingerprints(null)
                .addFingerprint(new Fingerprint("a", "b"))
                .build());
    }

    @Test
    public void testParseImageTypeWithNullDomainFieldValue() throws Exception {
        String jsonMessage = "{" +
                "\"fileName\":\"9oZkgVs.jpg\"," +
                "\"domainProvider\":\"JumboTour\"," +
                "\"domain\":\"Lodging\"," +
                "\"generateThumbnail\":\"true\"," +
                "\"domainFields\":" +
                    "{\"rooms\":" +
                    "[{\"roomHero\":\"true\",\"roomId\":\"1673822\"}]" +
                    ",\"category\":\"71013\"," +
                    "\"propertyHero\":null}," +
                "\"active\":\"false\"," +
                "\"callback\":\"https://www.expedia.com/\"," +
                "\"fileUrl\":\"https://imgur.com/9oZkgVs.jpg\"," +
                "\"comment\":\"full message\"," +
                "\"userId\":\"EPC\"," +
                "\"domainId\":\"123\"}";

        ImageMessage imageMessage = ImageMessage.parseJsonMessage(jsonMessage);
        assertNull(imageMessage.getOuterDomainData().getDomainFields().get("propertyHero"));
        assertEquals(false, imageMessage.getHidden());
    }

    @Test
    public void testParseImageTypeWithNullStringDomainFields() throws Exception {
        String jsonMessage = "{" +
                "\"fileName\":\"9oZkgVs.jpg\"," +
                "\"domainProvider\":\"JumboTour\"," +
                "\"domain\":\"Lodging\"," +
                "\"generateThumbnail\":\"true\"," +
                "\"domainFields\":\"null\"," +
                "\"active\":\"false\"," +
                "\"callback\":\"https://www.expedia.com/\"," +
                "\"fileUrl\":\"https://imgur.com/9oZkgVs.jpg\"," +
                "\"comment\":\"full message\"," +
                "\"userId\":\"EPC\"," +
                "\"domainId\":\"123\"}";

        ImageMessage imageMessage = ImageMessage.parseJsonMessage(jsonMessage);
        assertNull(imageMessage.getOuterDomainData().getDomainFields());
    }

    @Test
    public void testParseImageTypeWithEmptyStringDomainFields() throws Exception {
        String jsonMessage = "{" +
                "\"fileName\":\"9oZkgVs.jpg\"," +
                "\"domainProvider\":\"JumboTour\"," +
                "\"domain\":\"Lodging\"," +
                "\"generateThumbnail\":\"true\"," +
                "\"domainFields\":\"\"," +
                "\"active\":\"false\"," +
                "\"callback\":\"https://www.expedia.com/\"," +
                "\"fileUrl\":\"https://imgur.com/9oZkgVs.jpg\"," +
                "\"comment\":\"full message\"," +
                "\"userId\":\"EPC\"," +
                "\"domainId\":\"123\"}";

        ImageMessage imageMessage = ImageMessage.parseJsonMessage(jsonMessage);
        assertNull(imageMessage.getOuterDomainData().getDomainFields());
    }

    @Test
    public void testParseJsonWithProvidedName() throws Exception {
        String jsonMessage = "{" +
                "\"fileName\":\"somethingOld.jpg\"," +
                "\"domainProvider\":\"JumboTour\"," +
                "\"domain\":\"Lodging\"," +
                "\"generateThumbnail\":\"true\"," +
                "\"domainFields\":" +
                "{\"rooms\":" +
                "[{\"roomHero\":\"true\",\"roomId\":\"1673822\"}]" +
                ",\"category\":\"71013\"," +
                "\"propertyHero\":null}," +
                "\"active\":\"false\"," +
                "\"callback\":\"https://www.expedia.com/\"," +
                "\"fileUrl\":\"https://imgur.com/9oZkgVs.jpg\"," +
                "\"comment\":\"full message\"," +
                "\"userId\":\"EPC\"," +
                "\"domainId\":\"123\"," +
                "\"providedName\":\"somethingNew.jpg\"}";

        ImageMessage imageMessage = ImageMessage.parseJsonMessage(jsonMessage);
        assertTrue("somethingNew.jpg".equals(imageMessage.getProvidedName()));
    }

    @Test
    public void testParseJsonWithoutProvidedName() throws Exception {
        String jsonMessage = "{" +
                "\"fileName\":\"somethingOld.jpg\"," +
                "\"domainProvider\":\"JumboTour\"," +
                "\"domain\":\"Lodging\"," +
                "\"generateThumbnail\":\"true\"," +
                "\"domainFields\":" +
                "{\"rooms\":" +
                "[{\"roomHero\":\"true\",\"roomId\":\"1673822\"}]" +
                ",\"category\":\"71013\"," +
                "\"propertyHero\":null}," +
                "\"active\":\"false\"," +
                "\"callback\":\"https://www.expedia.com/\"," +
                "\"fileUrl\":\"https://imgur.com/9oZkgVs.jpg\"," +
                "\"comment\":\"full message\"," +
                "\"userId\":\"EPC\"," +
                "\"domainId\":\"123\"}";

        ImageMessage imageMessage = ImageMessage.parseJsonMessage(jsonMessage);
        assertTrue(imageMessage.getProvidedName() == null);
    }
    
    @Test
    public void testParseJsonWithBoolean() throws Exception {
        String jsonMessage = "{" +
                "\"fileName\":\"somethingOld.jpg\"," +
                "\"domainProvider\":\"JumboTour\"," +
                "\"domain\":\"Lodging\"," +
                "\"generateThumbnail\":\"true\"," +
                "\"domainFields\":" +
                "{\"rooms\":" +
                "[{\"roomHero\":\"true\",\"roomId\":\"1673822\"}]" +
                ",\"category\":\"71013\"," +
                "\"propertyHero\":null}," +
                "\"active\":false," +
                "\"hidden\":true," +
                "\"generateThumbnail\":true," +
                "\"callback\":\"https://www.expedia.com/\"," +
                "\"fileUrl\":\"https://imgur.com/9oZkgVs.jpg\"," +
                "\"comment\":\"full message\"," +
                "\"userId\":\"EPC\"," +
                "\"domainId\":\"123\"," +
                "\"providedName\":\"somethingNew.jpg\"}";
        try {
            final ImageMessage imageMessage = ImageMessage.parseJsonMessage(jsonMessage);
            assertFalse(imageMessage.isActive());
            assertTrue(imageMessage.getHidden());
            assertTrue(imageMessage.isGenerateThumbnail());
        } catch (ClassCastException e) {
            fail("This should not throw a ClassCastException");
        }
    }
    
    @Test
    public void testCreateMessageBuilder() {
        final ImageMessage imageMessage = ImageMessage.builder().mediaGuid("aaaaaaa-1010-bbbb-292929229").requestId("bbbbbb-1010-bbbb-292929229").clientId("EPC")
                .userId("you").rotation("90").active(true).fileUrl("file:/my/files/are/awesome.jpg").fileName("original_file_name.png")
                .sourceUrl("s3://bucket/source/aaaaaaa-1010-bbbb-292929229.jpg").rejectedFolder("rejected").comment("test comment!").hidden(false)
                .failedReason("imageFailed").retryCount(3).build();        
        final ImageMessageBuilder builder = imageMessage.createBuilderFromMessage();
        final ImageMessage message = builder.build();
        assertTrue(imageMessage.toJSONMessage().equals(message.toJSONMessage()));
    }

}
