package com.expedia.content.media.processing.pipeline.domain;

public final class MessageConstants {
    public static final String CLIENT_ID = "clientId";
    public static final String USER_ID = "userId";
    public static final String REQUEST_ID = "requestId";
    public static final String MEDIA_ID = "mediaGuid";
    public static final String FILE_URL = "fileUrl";
    public static final String FILE_NAME = "fileName";
    public static final String ROTATION = "rotation";
    public static final String ACTIVE = "active";
    public static final String GENERATE_THUMBNAIL = "generateThumbnail";
    public static final String SOURCE_URL = "sourceUrl";
    public static final String GEN_OUTPUT = "genOutput";
    public static final String REJECTED_OUTPUT = "rejectedOutput";
    public static final String STAGING_KEY = "stagingKey";
    public static final String CALLBACK = "callback";
    public static final String OUTER_DOMAIN_NAME = "domain";
    public static final String OUTER_DOMAIN_ID = "domainId";
    public static final String OUTER_DOMAIN_PROVIDER = "domainProvider";
    public static final String OUTER_DOMAIN_FIELDS = "domainFields";
    public static final String OUTER_DOMAIN_DERIVATIVE_CATEGORY = "domainDerivativeCategory";
    public static final String FILE_SIZE = "fileSize";
    public static final String IMAGE_WIDTH = "width";
    public static final String IMAGE_HEIGHT = "height";
    public static final String METADATA_DETAILS = "metadataDetails";
    public static final String FINGERPRINTS = "fingerprints";
    public static final String FINGERPRINT_ALGORITHM = "algorithm";
    public static final String FINGERPRINT_VALUES = "values";
    public static final String DERIVATIVES = "derivatives";
    public static final String DERIVATIVE_TYPE = "type";
    public static final String DERIVATIVE_LOCATION = "location";
    public static final String HIDDEN = "hidden";
    public static final String PROVIDED_NAME = "providedName";
    public static final String LOG_ENTRIES = "logEntries";
    public static final String LOG_ENTRY_APP = "appName";
    public static final String LOG_ENTRY_ACTIVITY = "activity";
    public static final String LOG_ENTRY_DATE = "activityTime";
    public static final String FAILED_REASON = "failedReason";
    public static final String RETRY_COUNT = "retryCount";



    ////LCM specific fields
    @Deprecated
    public static final String EXPEDIA_ID = "expediaId";
    @Deprecated
    public static final String CATEGORY_ID = "categoryId";
    @Deprecated
    public static final String CAPTION = "caption";
    @Deprecated
    public static final String MEDIA_PROVIDER_ID = "mediaProviderId";
    public static final String COMMENT = "comment";

    private MessageConstants() {
    }
}
