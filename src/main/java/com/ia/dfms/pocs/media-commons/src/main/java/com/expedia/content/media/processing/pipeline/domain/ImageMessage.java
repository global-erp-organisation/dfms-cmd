package com.expedia.content.media.processing.pipeline.domain;

import com.expedia.content.media.processing.pipeline.domain.ImageMessage.ImageMessageBuilder;
import com.expedia.content.media.processing.pipeline.exception.ImageMessageException;
import com.expedia.content.media.processing.pipeline.reporting.Activity;
import com.expedia.content.media.processing.pipeline.reporting.App;
import com.expedia.content.media.processing.pipeline.reporting.LogEntry;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.tools.json.JSONWriter;

import org.apache.commons.lang3.BooleanUtils;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Represents an Image Messages received into the media processing pipeline. It contains data about the image
 * (url location, image type, etc.) but not the image itself.
 */
public class ImageMessage {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    
    private final String clientId;
    private final String userId;
    private final String requestId;
    private final String mediaGuid;
    private final String fileUrl;
    private final String fileName;
    private final String rotation;
    private final Boolean active;
    private final Boolean generateThumbnail;
    private final String sourceUrl;
    private final String outputFolder;
    private final String rejectedFolder;
    private final StagingKey stagingKey;
    private final URL callback;
    private final OuterDomain outerDomainData;
    private final Metadata metadata;
    private final List<Fingerprint> fingerprints;
    private final List<Derivative> derivatives;
    private final Boolean hidden;
    private final String providedName;
    // LCM specific fields
    private final Integer expediaId;
    private final String categoryId;
    private final String caption;
    private final String mediaProviderId;
    private final String comment;
    private final List<LogEntry> logEntries;
    private final String failedReason;
    private final Integer retryCount;


    public ImageMessage(ImageMessageBuilder imageMessageBuilder) {
        this.clientId = imageMessageBuilder.clientId;
        this.userId = imageMessageBuilder.userId;
        this.requestId = imageMessageBuilder.requestId;
        this.mediaGuid = imageMessageBuilder.mediaGuid;
        this.fileUrl = imageMessageBuilder.fileUrl;
        this.fileName = imageMessageBuilder.fileName;
        this.rotation = imageMessageBuilder.rotation;
        this.active = imageMessageBuilder.active;
        this.generateThumbnail = imageMessageBuilder.generateThumbnail;
        this.outputFolder = imageMessageBuilder.outputFolder;
        this.sourceUrl = (imageMessageBuilder.sourceUrl == null || imageMessageBuilder.sourceUrl.isEmpty()) ? null : imageMessageBuilder.sourceUrl;
        this.rejectedFolder =
                (imageMessageBuilder.rejectedFolder == null || imageMessageBuilder.rejectedFolder.isEmpty()) ? null : imageMessageBuilder.rejectedFolder;
        this.stagingKey = imageMessageBuilder.stagingKey;
        this.callback = imageMessageBuilder.callback;
        this.outerDomainData = imageMessageBuilder.outerDomainData;
        this.metadata = imageMessageBuilder.metadata;
        this.fingerprints = imageMessageBuilder.fingerprints;
        this.derivatives = imageMessageBuilder.derivatives;
        
        this.expediaId = imageMessageBuilder.expediaId;
        this.categoryId = imageMessageBuilder.categoryId;
        this.caption = imageMessageBuilder.caption;
        this.mediaProviderId = imageMessageBuilder.mediaProviderId;
        this.comment = imageMessageBuilder.comment;
        this.hidden = imageMessageBuilder.hidden;
        this.providedName = imageMessageBuilder.providedName;
        this.logEntries = imageMessageBuilder.logEntries;
        this.failedReason = imageMessageBuilder.failedReason;
        this.retryCount = imageMessageBuilder.retryCount;
    }
    
    /**
     * Parses a JSON message to an {@code ImageMessage} object.
     * 
     * @param jsonMessage JSON message to parse.
     * @return An ImageMessage instance with the content of the JSON message.
     * @throws ImageMessageException Thrown if parsing the message fails.
     */
    @SuppressWarnings("unchecked")
    public static ImageMessage parseJsonMessage(String jsonMessage) throws ImageMessageException {
        Map<String, Object> mapMessage = buildMapFromJson(jsonMessage);
        
        URL callbackUrl = retrieveURL(mapMessage, MessageConstants.CALLBACK);
        Integer expediaId = retrieveInteger(mapMessage, MessageConstants.EXPEDIA_ID);
        final Object active = mapMessage.get(MessageConstants.ACTIVE);
        final Object hidden = mapMessage.get(MessageConstants.HIDDEN);
        final Object generateThumbnail = mapMessage.get(MessageConstants.GENERATE_THUMBNAIL);
        /* @formatter:off */
        ImageMessageBuilder imageMessageBuilder =
                new ImageMessageBuilder()
                        .clientId((String) mapMessage.get(MessageConstants.CLIENT_ID))
                        .userId((String) mapMessage.get(MessageConstants.USER_ID))
                        .requestId((String) mapMessage.get(MessageConstants.REQUEST_ID))
                        .fileName((String) mapMessage.get(MessageConstants.FILE_NAME))
                        .rotation((String) mapMessage.get(MessageConstants.ROTATION))
                        .active(BooleanUtils.toBoolean(active == null ? Boolean.TRUE.toString() : active.toString()))
                        .generateThumbnail(BooleanUtils.toBoolean(generateThumbnail == null ? Boolean.FALSE.toString() : generateThumbnail.toString()))
                        .mediaGuid((String) mapMessage.get(MessageConstants.MEDIA_ID))
                        .fileUrl((String) mapMessage.get(MessageConstants.FILE_URL))
                        .outputFolder((String) mapMessage.get(MessageConstants.GEN_OUTPUT))
                        .sourceUrl((String) mapMessage.get(MessageConstants.SOURCE_URL))
                        .rejectedFolder((String) mapMessage.get(MessageConstants.REJECTED_OUTPUT))
                        .comment((String) mapMessage.get(MessageConstants.COMMENT))
                        .stagingKey(buildStagingKey(mapMessage))
                        .expediaId(expediaId)
                        .categoryId((String) mapMessage.get(MessageConstants.CATEGORY_ID))
                        .caption((String) mapMessage.get(MessageConstants.CAPTION))
                        .mediaProviderId((String) mapMessage.get(MessageConstants.MEDIA_PROVIDER_ID))
                        .callback(callbackUrl)
                        .outerDomainData(retrieveOuterDomainDomain(mapMessage))
                        .metadata(retrieveMetadata(mapMessage))
                        .fingerprints(retrieveFingerprints(mapMessage))
                        .derivatives(retrieveDerivatives(mapMessage))
                        .hidden(BooleanUtils.toBoolean(hidden == null ? Boolean.FALSE.toString() : hidden.toString()))
                        .providedName(mapMessage.get(MessageConstants.PROVIDED_NAME) == null ? null : (String) mapMessage.get(MessageConstants.PROVIDED_NAME))
                        .logEntries(retrieveLogEntries(mapMessage))
                        .retryCount((Integer) mapMessage.get(MessageConstants.RETRY_COUNT))
                        .failedReason((String) mapMessage.get(MessageConstants.FAILED_REASON));
        /* @formatter:on */
        return imageMessageBuilder.build();
    }
    
    /**
     * Create an ImageMessage builder from the current imageMessage.
     * 
     * @return An ImageMessageBuilder build from the current imageMessage.
     */
    public  ImageMessageBuilder createBuilderFromMessage() {
        final ImageMessageBuilder imageMessageBuilder = new ImageMessage.ImageMessageBuilder();
        return imageMessageBuilder.transferAll(this);
    }

    
    @SuppressWarnings("rawtypes")
    private static Map buildMapFromJson(String jsonMessage) throws ImageMessageException {
        try {
            return OBJECT_MAPPER.readValue(jsonMessage, Map.class);
        } catch (IOException ex) {
            String errorMsg = MessageFormat.format("Error parsing/converting Json message: {0}", jsonMessage);
            throw new ImageMessageException(errorMsg, ex);
        }
    }
    
    @SuppressWarnings("rawtypes")
    private static URL retrieveURL(Map mapMessage, String fieldName) {
        String fieldValue = (String) mapMessage.get(fieldName);
        if (fieldValue == null) {
            return null;
        }
        try {
            return new URL(fieldValue);
        } catch (MalformedURLException ex) {
            String errorMsg = MessageFormat.format("{0}: {1} is malformed.", fieldName, fieldValue);
            throw new ImageMessageException(errorMsg, ex);
        }
    }
    
    @SuppressWarnings("rawtypes")
    private static Integer retrieveInteger(Map mapMessage, String fieldName) {
        Object fieldValue = mapMessage.get(fieldName);
        if (fieldValue == null) {
            return null;
        }
        try {
            return Integer.parseInt(fieldValue.toString());
        } catch (NumberFormatException ex) {
            String errorMsg = MessageFormat.format("{0}: {1} is not a valid number.", fieldName, fieldValue);
            throw new ImageMessageException(errorMsg, ex);
        }
    }
    
    @SuppressWarnings("unchecked")
    private static OuterDomain retrieveOuterDomainDomain(final Map<String, Object> mapMessage) {
        final String domainName = (String) mapMessage.get(MessageConstants.OUTER_DOMAIN_NAME);
        if (domainName == null) {
            return null;
        }
        Domain domain = null;
        try {
            domain = getDomain(domainName);
        } catch (InvalidDomainException ex) {
            String errorMsg = MessageFormat.format("{0}: {1} is not a recognized domain.", MessageConstants.OUTER_DOMAIN_NAME, domainName);
            throw new ImageMessageException(errorMsg, ex);
        }
        Map<String, Object> domainField = buildDomainField(mapMessage);
        return new OuterDomain(domain, (String) mapMessage.get(MessageConstants.OUTER_DOMAIN_ID),
                (String) mapMessage.get(MessageConstants.OUTER_DOMAIN_PROVIDER),
                (String) mapMessage.get(MessageConstants.OUTER_DOMAIN_DERIVATIVE_CATEGORY), domainField
        );
    }

    /**
     * if message is from Avro kafka topic, the domainFields is string, or else it is a map.
     * @param mapMessage
     * @return
     */
    private static Map<String, Object> buildDomainField(final Map<String, Object> mapMessage) {
        Map<String, Object> domainField = null;

        if (mapMessage.get(MessageConstants.OUTER_DOMAIN_FIELDS) instanceof Map) {
            domainField = (Map<String, Object>) mapMessage.get(MessageConstants.OUTER_DOMAIN_FIELDS);
        } else if (mapMessage.get(MessageConstants.OUTER_DOMAIN_FIELDS) instanceof String) {
            domainField = buildMapForDomainField((String) mapMessage.get(MessageConstants.OUTER_DOMAIN_FIELDS));
        }
        return domainField;
    }

    private static Map buildMapForDomainField(String jsonMessage) {
        try {
            return OBJECT_MAPPER.readValue(jsonMessage, Map.class);
        } catch (IOException ex) {
            String errorMsg = MessageFormat.format("Error parsing/converting Json message: {0}", jsonMessage);
        }
        //in case invalid string return null
        return null;
    }
    
    /**
     * Parses the metadata information from the incoming image message.
     *
     * @param mapMessage Message to parse.
     * @return The message's metadata.
     */
    @SuppressWarnings("unchecked")
    private static Metadata retrieveMetadata(final Map<String, Object> mapMessage) {
        return new Metadata((Integer) mapMessage.get(MessageConstants.IMAGE_WIDTH), (Integer) mapMessage.get(MessageConstants.IMAGE_HEIGHT),
                            (Integer) mapMessage.get(MessageConstants.FILE_SIZE), (Map<String, String>) mapMessage.get(MessageConstants.METADATA_DETAILS));
    }
    
    /**
     * Parses the fingerprint information from the incoming image message.
     *
     * @param mapMessage Message to parse.
     * @return The message's fingerprints.
     */
    @SuppressWarnings("unchecked")
    private static List<Fingerprint> retrieveFingerprints(final Map<String, Object> mapMessage) {
        final List<Map<String, Object>> messageFingerprints = (List<Map<String, Object>>) mapMessage.get(MessageConstants.FINGERPRINTS);
        if (messageFingerprints != null) {
            return messageFingerprints
                    .stream()
                    .map(fingerprintMap -> new Fingerprint((String) fingerprintMap.get(MessageConstants.FINGERPRINT_ALGORITHM),
                                                           (List<String>) fingerprintMap.get(MessageConstants.FINGERPRINT_VALUES)))
                    .collect(Collectors.toList());
        }
        return null;
    }
    
    /**
     * Parses the derivatives information from the incoming image message.
     *
     * @param mapMessage Message to parse.
     * @return The message's derivatives.
     */
    @SuppressWarnings("unchecked")
    private static List<Derivative> retrieveDerivatives(final Map<String, Object> mapMessage) {
        final List<Map<String, Object>> messageDerivatives = (List<Map<String, Object>>) mapMessage.get(MessageConstants.DERIVATIVES);
        if (messageDerivatives != null) {
            /* @formatter:off */
            return messageDerivatives
                    .stream()
                    .map(derivativesMap -> new Derivative(
                            (String) derivativesMap.get(MessageConstants.DERIVATIVE_LOCATION), 
                            (String) derivativesMap.get(MessageConstants.DERIVATIVE_TYPE),
                            (Integer) derivativesMap.get(MessageConstants.IMAGE_WIDTH),
                            (Integer) derivativesMap.get(MessageConstants.IMAGE_HEIGHT),
                            (Integer) derivativesMap.get(MessageConstants.FILE_SIZE)))
                    .collect(Collectors.toList());
            /* @formatter:on */
        }
        return null;
    }

    /**
     * Parses the logEntries information form the incoming image message.
     *
     * @param mapMessage Message to parse.
     * @return The message's logEntries list.
     */
    @SuppressWarnings("unchecked")
    private static List<LogEntry> retrieveLogEntries(final Map<String, Object> mapMessage) {
        final List<Map<String, Object>> messageLogEntries = (List<Map<String, Object>>) mapMessage.get(MessageConstants.LOG_ENTRIES);
        if (messageLogEntries != null) {
            /* @formatter:off */
            return messageLogEntries
                    .stream()
                    .map(logEntryMap -> new LogEntry(
                            App.getAppByName((String) logEntryMap.get(MessageConstants.LOG_ENTRY_APP)),
                            Activity.getActivityForName((String) logEntryMap.get(MessageConstants.LOG_ENTRY_ACTIVITY)),
                            new Date((Long) logEntryMap.get(MessageConstants.LOG_ENTRY_DATE))))
                    .collect(Collectors.toList());
            /* @formatter:on */
        }
        return null;
    }
    
    /**
     * Builds the JSON message of this image message.
     *
     * @return The ImageMessage as a JSON message.
     */
    public String toJSONMessage() {
        Map<String, Object> mapMessage = new LinkedHashMap<>();
        mapMessage.put(MessageConstants.CLIENT_ID, (clientId == null) ? null : clientId);
        mapMessage.put(MessageConstants.USER_ID, (userId == null) ? null : userId);
        mapMessage.put(MessageConstants.REQUEST_ID, (requestId == null) ? null : requestId);
        mapMessage.put(MessageConstants.MEDIA_ID, (mediaGuid == null) ? null : mediaGuid);
        mapMessage.put(MessageConstants.FILE_URL, (fileUrl == null) ? null : fileUrl);
        mapMessage.put(MessageConstants.FILE_NAME, (fileName == null) ? null : fileName);
        mapMessage.put(MessageConstants.ROTATION, (rotation == null) ? null : rotation);
        mapMessage.put(MessageConstants.ACTIVE, (active == null) ? null : active.toString());
        mapMessage.put(MessageConstants.GENERATE_THUMBNAIL, (generateThumbnail == null) ? null : generateThumbnail.toString());
        mapMessage.put(MessageConstants.GEN_OUTPUT, (outputFolder == null) ? null : outputFolder);
        mapMessage.put(MessageConstants.SOURCE_URL, (sourceUrl == null) ? null : sourceUrl);
        mapMessage.put(MessageConstants.REJECTED_OUTPUT, (rejectedFolder == null) ? null : rejectedFolder);
        mapMessage.put(MessageConstants.CALLBACK, (callback == null) ? null : callback.toString());
        mapMessage.put(MessageConstants.COMMENT, (comment == null) ? null : comment);
        mapMessage.put(MessageConstants.HIDDEN, (hidden == null) ? null : hidden.toString());
        mapMessage.put(MessageConstants.PROVIDED_NAME, (providedName == null) ? null : providedName);
        mapMessage.put(MessageConstants.FAILED_REASON, (failedReason == null) ? null : failedReason);
        mapMessage.put(MessageConstants.RETRY_COUNT, (retryCount == null) ? null : retryCount);


        if (stagingKey != null) {
            mapMessage.put(MessageConstants.STAGING_KEY, stagingKey);
        }
        putOuterDomainToMessage(mapMessage);
        putFingerprintsToMessage(mapMessage);
        putDerivativesToMessage(mapMessage);
        putMetadataToMessage(mapMessage);
        putLogEntriesToMessage(mapMessage);
        
        // LCM specific fields
        if (expediaId != null) {
            mapMessage.put(MessageConstants.EXPEDIA_ID, expediaId);
        }
        if (categoryId != null) {
            mapMessage.put(MessageConstants.CATEGORY_ID, categoryId);
        }
        if (caption != null) {
            mapMessage.put(MessageConstants.CAPTION, caption);
        }
        if (mediaProviderId != null) {
            mapMessage.put(MessageConstants.MEDIA_PROVIDER_ID, mediaProviderId);
        }
        return new JSONWriter().write(mapMessage);
    }
    
    private void putMetadataToMessage(Map<String, Object> mapMessage) {
        if (metadata != null) {
            mapMessage.put(MessageConstants.FILE_SIZE, metadata.getFileSize());
            mapMessage.put(MessageConstants.IMAGE_WIDTH, metadata.getWidth());
            mapMessage.put(MessageConstants.IMAGE_HEIGHT, metadata.getHeight());
            if (metadata.getDetails() != null) {
                mapMessage.put(MessageConstants.METADATA_DETAILS, metadata.getDetails());
            }
        }
    }
    
    private void putDerivativesToMessage(Map<String, Object> mapMessage) {
        if (derivatives != null && !derivatives.isEmpty()) {
            List<Map<String, Object>> jsonDerivatives = new ArrayList<>();
            for (Derivative derivative : derivatives) {
                Map<String, Object> derivativesMap = new HashMap<>();
                derivativesMap.put(MessageConstants.DERIVATIVE_LOCATION, derivative.getLocation());
                derivativesMap.put(MessageConstants.DERIVATIVE_TYPE, derivative.getType());
                derivativesMap.put(MessageConstants.FILE_SIZE, derivative.getFileSize());
                derivativesMap.put(MessageConstants.IMAGE_WIDTH, derivative.getWidth());
                derivativesMap.put(MessageConstants.IMAGE_HEIGHT, derivative.getHeight());
                jsonDerivatives.add(derivativesMap);
            }
            mapMessage.put(MessageConstants.DERIVATIVES, jsonDerivatives);
        }
    }
    
    private void putFingerprintsToMessage(Map<String, Object> mapMessage) {
        if (fingerprints != null && !fingerprints.isEmpty()) {
            List<Map<String, Object>> jsonFingerprints = new ArrayList<>();
            for (Fingerprint fingerprint : fingerprints) {
                Map<String, Object> fingerprintMap = new HashMap<>();
                fingerprintMap.put(MessageConstants.FINGERPRINT_ALGORITHM, fingerprint.getAlgorithm());
                fingerprintMap.put(MessageConstants.FINGERPRINT_VALUES, fingerprint.getValues());
                jsonFingerprints.add(fingerprintMap);
            }
            mapMessage.put(MessageConstants.FINGERPRINTS, jsonFingerprints);
        }
    }
    
    private void putOuterDomainToMessage(Map<String, Object> mapMessage) {
        if (outerDomainData != null) {
            mapMessage.put(MessageConstants.OUTER_DOMAIN_NAME, outerDomainData.getDomain().getDomain());
            mapMessage.put(MessageConstants.OUTER_DOMAIN_ID, outerDomainData.getDomainId());
            mapMessage.put(MessageConstants.OUTER_DOMAIN_PROVIDER, outerDomainData.getProvider());
            mapMessage.put(MessageConstants.OUTER_DOMAIN_DERIVATIVE_CATEGORY, outerDomainData.getDerivativeCategory());
            mapMessage.put(MessageConstants.OUTER_DOMAIN_FIELDS, outerDomainData.getDomainFields());
        }
    }

    private void putLogEntriesToMessage(Map<String, Object> mapMessage) {
        if (logEntries != null && !logEntries.isEmpty()) {
            List<Map<String, Object>> jsonEvents = new ArrayList<>();
            logEntries.stream().forEach(logEntry -> {
                Map<String, Object> logEntriesMap = new HashMap<>();
                logEntriesMap.put(MessageConstants.LOG_ENTRY_APP, logEntry.getAppName());
                logEntriesMap.put(MessageConstants.LOG_ENTRY_ACTIVITY, logEntry.getActivity().getName());
                logEntriesMap.put(MessageConstants.LOG_ENTRY_DATE, logEntry.getActivityTimeMillis());
                jsonEvents.add(logEntriesMap);
            });
            mapMessage.put(MessageConstants.LOG_ENTRIES, jsonEvents);
        }
    }
    
    public String getClientId() {
        return clientId;
    }
    
    public String getRequestId() {
        return requestId;
    }
    
    public URL getCallback() {
        return callback;
    }
    
    public String getMediaGuid() {
        return mediaGuid;
    }
    
    public String getFileUrl() {
        return fileUrl;
    }
    
    public String getOutputFolder() {
        return outputFolder;
    }
    
    public String getSourceUrl() {
        return sourceUrl;
    }
    
    public String getRejectedFolder() {
        return rejectedFolder;
    }
    
    public URL getCallBack() {
        return callback;
    }
    
    public StagingKey getStagingKey() {
        return stagingKey;
    }
    
    public String getUserId() {
        return userId;
    }
    
    public String getFileName() {
        return fileName;
    }

    public String getFailedReason() {
        return failedReason;
    }

    public Integer getRetryCount() {
        return retryCount;
    }


    public String getRotation() {
        return rotation;
    }
    
    public Boolean isActive() {
        return active;
    }
    
    public Boolean isGenerateThumbnail() {
        return generateThumbnail;
    }
    
    public OuterDomain getOuterDomainData() {
        return outerDomainData;
    }
    
    public Metadata getMetadata() {
        return metadata;
    }
    
    public List<Fingerprint> getFingerprints() {
        return fingerprints;
    }
    
    public List<Derivative> getDerivatives() {
        return derivatives;
    }

    public String getComment() {
        return comment;
    }
    
    public Boolean getHidden() {
        return hidden;
    }

    public String getProvidedName() {
        return providedName;
    }

    public List<LogEntry> getLogEntries() {
        return logEntries;
    }

    @Deprecated
    public Integer getExpediaId() {
        return expediaId;
    }
    
    @Deprecated
    public String getCategoryId() {
        return categoryId;
    }
    
    @Deprecated
    public String getCaption() {
        return caption;
    }
    
    @Deprecated
    public String getMediaProviderId() {
        return mediaProviderId;
    }
    
    /**
     * Helper method for parseMessage to find the image source within the allowed enums.
     *
     * @param domainName String used to find the ImageType.
     * @throws InvalidDomainException Thrown if no supported domain is found.
     */
    private static Domain getDomain(String domainName) throws InvalidDomainException {
        Domain imageTypeMatch = Domain.findDomain(domainName);
        if (imageTypeMatch == null) {
            throw new InvalidDomainException("ERROR - Domain " + domainName + " not recognized.");
        }
        return imageTypeMatch;
    }
    
    /**
     * get the stagingKey from message Map
     *
     * @param mapMessage Map containing the Json message in key/value pairs.
     * @return StagingKey Returns an StagingKey object using the Json message, null if there is no staging key information in the Json message.
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    private static StagingKey buildStagingKey(Map mapMessage) {
        Map<String, String> mapValue = (Map<String, String>) mapMessage.get(MessageConstants.STAGING_KEY);
        if (mapValue != null) {
            return new StagingKey(mapValue.get("externalId"), mapValue.get("providerId"), mapValue.get("sourceId"));
        }
        return null;
    }

    /**
     * Adds a LogEntry to the logEntries list.
     * @param logEntry a logEntry to add to the logEntries list.
     */
    public void addLogEntry(LogEntry logEntry) {
        this.logEntries.add(logEntry);
    }
    
    @Override
    public String toString() {
        StringBuilder toStringBuilder = new StringBuilder();
        /* @formatter:off */
        toStringBuilder.append("ImageMessage{\nfileUrl='").append(fileUrl).append("'\n")
                       .append(", requestId='").append(requestId).append("'\n")
                       .append(", fileName='").append(fileName).append("'\n")
                       .append(", mediaGuid='").append(mediaGuid).append("'\n")
                       .append(", sourceUrl='").append(sourceUrl).append("'\n")
                       .append(", outputFolder='").append(outputFolder).append("'\n")
                       .append(", rejectedFolder='").append(rejectedFolder).append("'\n")
                       .append(", stagingKey=").append(stagingKey).append("\n")
                       .append(", expediaId=").append(expediaId).append("\n")
                       .append(", categoryId=").append(categoryId).append("\n")
                       .append(", caption='").append(caption).append("'\n")
                       .append(", mediaProviderId='").append(mediaProviderId).append("'\n")
                       .append(", callback='").append(callback).append("'\n")
                       .append(", comment='").append(comment).append("'\n")
                       .append(", hidden='").append(hidden).append("'\n")
                       .append(", retryCount='").append(retryCount).append("'\n")
                       .append(", providedName='").append(providedName).append("'\n")
                       .append(", active='").append(active).append("'\n")
                       .append(", clientId='").append(clientId).append("'\n")
                       .append(", failedReason='").append(failedReason).append("'\n")
                       .append(", generateThumbnail='").append(generateThumbnail).append("'\n");
        /* @formatter:on */
        if (outerDomainData != null) {
            toStringBuilder.append(", " + outerDomainData.toString()).append("\n");
        }
        if (metadata != null) {
            toStringBuilder.append(", " + metadata.toString()).append("\n");
        }
        if (!CollectionUtils.isEmpty(fingerprints)) {
            toStringBuilder.append(fingerprints.stream().map(Fingerprint::toString)
                    .collect(Collectors.joining(", ", ", fingerprints={", "}"))).append("\n");
        }
        if (!CollectionUtils.isEmpty(derivatives)) {
            toStringBuilder.append(derivatives.stream().map(Derivative::toString)
                    .collect(Collectors.joining(", ", ", derivatives={", "}"))).append("\n");
        }
        if (!CollectionUtils.isEmpty(logEntries)) {
            toStringBuilder.append(logEntries.stream().map(LogEntry::toStringForImageMessage)
                    .collect(Collectors.joining(", ", ", logEntries={", "}"))).append("\n");
        }
        toStringBuilder.append("}");
        return toStringBuilder.toString();
    }

    public static ImageMessageBuilder builder() {
        return new ImageMessageBuilder();
    }
    
    /**
     * build class to help initialize imageMessage.
     */
    public static class ImageMessageBuilder {
        private String clientId;
        private String userId;
        private String requestId;
        private String mediaGuid;
        private String fileUrl;
        private String fileName;
        private String rotation;
        private Boolean active;
        private Boolean generateThumbnail;
        private String outputFolder;
        private String sourceUrl;
        private String rejectedFolder;
        private StagingKey stagingKey;
        private URL callback;
        private String comment;
        private OuterDomain outerDomainData;
        private Metadata metadata;
        private List<Fingerprint> fingerprints = new ArrayList<>();
        private List<Derivative> derivatives = new ArrayList<>();
        private Boolean hidden;
        private String providedName;
        private List<LogEntry> logEntries = new ArrayList<>();
        // LCM specific fields
        private Integer expediaId;
        private String categoryId;
        private String caption;
        private String mediaProviderId;
        private String failedReason;
        private Integer retryCount;


        @SuppressWarnings("unchecked")
        public ImageMessageBuilder transferAll(ImageMessage message) {
            this.clientId = message.getClientId();
            this.userId = message.getUserId();
            this.requestId = message.getRequestId();
            this.mediaGuid = message.getMediaGuid();
            this.fileUrl = message.getFileUrl();
            this.fileName = message.getFileName();
            this.rotation = message.getRotation();
            this.active = message.isActive();
            this.generateThumbnail = message.isGenerateThumbnail();
            this.outputFolder = message.getOutputFolder();
            this.sourceUrl = message.getSourceUrl();
            this.rejectedFolder = message.getRejectedFolder();
            this.stagingKey = message.getStagingKey();
            this.callback = message.getCallBack();
            this.comment = message.getComment();
            this.outerDomainData = message.getOuterDomainData();
            this.metadata = message.getMetadata();
            this.fingerprints = new ArrayList<>(Optional.ofNullable(message.getFingerprints()).orElse(Collections.EMPTY_LIST));
            this.derivatives = new ArrayList<>(Optional.ofNullable(message.getDerivatives()).orElse(Collections.EMPTY_LIST));
            
            this.expediaId = message.getExpediaId();
            this.categoryId = message.getCategoryId();
            this.caption = message.getCaption();
            this.mediaProviderId = message.getMediaProviderId();
            this.hidden = message.hidden;
            this.providedName = message.providedName;
            this.logEntries = new ArrayList<>(Optional.ofNullable(message.getLogEntries()).orElse(Collections.EMPTY_LIST));
            this.failedReason = message.getFailedReason();
            this.retryCount = message.getRetryCount();
            return this;
        }
        
        public ImageMessageBuilder clientId(String builderClientId) {
            this.clientId = builderClientId;
            return this;
        }
        
        public ImageMessageBuilder userId(String builderUserId) {
            this.userId = builderUserId;
            return this;
        }
        
        public ImageMessageBuilder requestId(String builderRequestId) {
            this.requestId = builderRequestId;
            return this;
        }
        
        public ImageMessageBuilder mediaGuid(String builderMediaId) {
            this.mediaGuid = builderMediaId;
            return this;
        }
        
        public ImageMessageBuilder fileUrl(String builderImageUrl) {
            this.fileUrl = builderImageUrl;
            return this;
        }
        
        public ImageMessageBuilder fileName(String builderFileName) {
            this.fileName = builderFileName;
            return this;
        }

        public ImageMessageBuilder failedReason(String builderFailedReason) {
            this.failedReason = builderFailedReason;
            return this;
        }

        public ImageMessageBuilder retryCount(Integer builderRetryCount) {
            this.retryCount = builderRetryCount;
            return this;
        }

        public ImageMessageBuilder rotation(String builderRotation) {
            this.rotation = builderRotation;
            return this;
        }
        
        public ImageMessageBuilder active(Boolean builderActive) {
            this.active = builderActive;
            return this;
        }
        
        public ImageMessageBuilder generateThumbnail(Boolean builderGenerateThumbnail) {
            this.generateThumbnail = builderGenerateThumbnail;
            return this;
        }
        
        public ImageMessageBuilder outputFolder(String builderOutputFolder) {
            this.outputFolder = builderOutputFolder;
            return this;
        }
        
        public ImageMessageBuilder sourceUrl(String builderSourceUrl) {
            this.sourceUrl = builderSourceUrl;
            return this;
        }
        
        public ImageMessageBuilder rejectedFolder(String builderRejectedFolder) {
            this.rejectedFolder = builderRejectedFolder;
            return this;
        }
        
        public ImageMessageBuilder stagingKey(StagingKey builderStagingKey) {
            this.stagingKey = builderStagingKey;
            return this;
        }
        
        public ImageMessageBuilder callback(URL builderCallback) {
            this.callback = builderCallback;
            return this;
        }

        public ImageMessageBuilder comment(String builderComment) {
            this.comment = builderComment;
            return this;
        }
        
        public ImageMessageBuilder outerDomainData(OuterDomain builderOuterDomainData) {
            this.outerDomainData = builderOuterDomainData;
            return this;
        }
        
        public ImageMessageBuilder metadata(Metadata builderMetadata) {
            this.metadata = builderMetadata;
            return this;
        }
        
        @SuppressWarnings("unchecked")
        public ImageMessageBuilder fingerprints(List<Fingerprint> builderFingerprints) {
            this.fingerprints = new ArrayList<>(Optional.ofNullable(builderFingerprints).orElse(Collections.EMPTY_LIST));
            return this;
        }

        public ImageMessageBuilder addFingerprint(Fingerprint fingerprint) {
            this.fingerprints.add(fingerprint);
            return this;
        }
        
        @SuppressWarnings("unchecked")
        public ImageMessageBuilder derivatives(List<Derivative> builderDerivatives) {
            this.derivatives = new ArrayList<>(Optional.ofNullable(builderDerivatives).orElse(Collections.EMPTY_LIST));
            return this;
        }

        public ImageMessageBuilder addDerivative(Derivative derivative) {
            this.derivatives.add(derivative);
            return this;
        }
        
        public ImageMessageBuilder hidden(Boolean builderHidden) {
            this.hidden = builderHidden;
            return this;
        }

        public ImageMessageBuilder providedName(String providedFileName) {
            this.providedName = providedFileName;
            return this;
        }

        public ImageMessageBuilder addLogEntry(LogEntry log) {
            this.logEntries.add(log);
            return this;
        }

        @SuppressWarnings("unchecked")
        public ImageMessageBuilder logEntries(List<LogEntry> builderEvents) {
            this.logEntries = new ArrayList<>(Optional.ofNullable(builderEvents).orElse(Collections.EMPTY_LIST));
            return this;
        }

        @Deprecated
        public ImageMessageBuilder expediaId(Integer builderExpediaId) {
            this.expediaId = builderExpediaId;
            return this;
        }
        
        @Deprecated
        public ImageMessageBuilder categoryId(String builderCategoryId) {
            this.categoryId = builderCategoryId;
            return this;
        }
        
        @Deprecated
        public ImageMessageBuilder caption(String builderCaption) {
            this.caption = builderCaption;
            return this;
        }
        
        @Deprecated
        public ImageMessageBuilder mediaProviderId(String builderMediaProviderId) {
            this.mediaProviderId = builderMediaProviderId;
            return this;
        }
        
        /**
         * final builder method that will call constructor of ImageMessage
         *
         * @return ImageMessage
         */
        public ImageMessage build() {
            return new ImageMessage(this);
        }
    }
}
