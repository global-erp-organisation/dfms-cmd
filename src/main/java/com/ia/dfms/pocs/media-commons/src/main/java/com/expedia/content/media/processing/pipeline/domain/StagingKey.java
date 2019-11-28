package com.expedia.content.media.processing.pipeline.domain;

/**
 * As part of imageMessage, contain externalId, providerId, sourceId of image from multisource.
 */
public class StagingKey {
    private final String externalId;
    private final String providerId;
    private final String sourceId;

    public StagingKey(String externalId, String providerId, String sourceId) {
        this.externalId = externalId;
        this.providerId = providerId;
        this.sourceId = sourceId;
    }

    public String getExternalId() {
        return externalId;
    }

    public String getProviderId() {
        return providerId;
    }

    public String getSourceId() {
        return sourceId;
    }

    @Override
    public String toString() {
        return "StagingKey{" +
                "externalId='" + externalId + '\'' +
                ", providerId='" + providerId + '\'' +
                ", sourceId='" + sourceId + '\'' +
                '}';
    }
}
