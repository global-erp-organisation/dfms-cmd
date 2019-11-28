package com.expedia.content.media.processing.pipeline.reporting;

/**
 * Supported activities for media reports.
 * Class stolen from:
 * https://ewegithub.sb.karmalab.net/ContentSystems/media-report-generator/blob/develop/src/main/java/com/expedia/content/media/reports/domain/Activity.
 * java
 * Added more activities from stolen version.
 */
public enum Activity {
    // Unknown activity, should not be used other than unit tests.
    UNKNOWN("Unknown"),
    // Activity for archiving the source media
    ARCHIVE("Archive"),
    // Activity for post processing source media. Legacy value: Archive
    POSTPROCESS("PostProcess"),
    // Activity for calculating derivatives against the source image
    DERIVATIVE_CALCULATION("DerivativeCalculation"),
    // Derivative creation based on calculation activity
    DERIVATIVES_CREATION("DerivativeCreation"),
    // Upload from outer system to MPP
    MEDIA_UPLOAD("MediaUpload"),
    // received media message
    MEDIA_MESSAGE_RECEIVED("MediaMessageReceived"),
    // Activity for publishing of media for public consumption
    PUBLISH("Publish"),
    // Reception of source media into MPP
    RECEPTION("Reception"),
    // Moving DCP-rejected source media to a rejected folder
    REJECT("Reject"),
    // Pick up of source media by the derivative creation process
    DCP_PICKUP("DcpPickup"),
    // Image validation + Copy image locally and strip all non-copyright metadata
    PREPROCESS("PreProcess"),
    // Collector starts processing after parsing ImageMessage
    COLLECTOR_START("CollectorStart"),
    // Collector download of source file
    COLLECTOR_DOWNLOAD("CollectorDownload"),
    // Collector duplicate image found
    COLLECTOR_DUP_MEDIA_FOUND("CollectorDupMediaFound"),
    // Collector validation of source file
    COLLECTOR_VALIDATION("CollectorValidation"),
    // Collector converts source file to jpeg
    COLLECTOR_CONVERT("CollectorConvert"),
    // Collector copy source file to archive folder
    SOURCE_ARCHIVED("SourceArchived"),
    // Database storage
    DATA_STORAGE("DataStorage"),
    // File not found
    FILE_NOT_FOUND("FileNotFound");
    
    private String name;
    
    Activity(String name) {
        this.name = name;
    }
    
    /**
     * Converts a String to an Activity enum type.
     *
     * @param name Activity name (case-sensitive)
     * @return The Activity enum value or null if not found.
     */
    public static Activity getActivityForName(String name) {
        return getActivityForName(name, false);
    }
    
    /**
     * Converts a String to an Activity enum type.
     *
     * @param name Activity name (case-sensitive)
     * @param fail If true, will throw a RuntimeException if the name is not found.
     * @return The Activity enum value. If fail is false, null will be returned for not found values.
     *         If fails is true, a RuntimeException will be thrown for not found values.
     */
    public static Activity getActivityForName(String name, boolean fail) {
        for (Activity activity : values()) {
            if (activity.getName().equals(name)) {
                return activity;
            }
        }
        if (fail) {
            throw new RuntimeException("Invalid Activity Name: " + name);
        } else {
            return null;
        }
    }
    
    public String getName() {
        return name;
    }
    
}
