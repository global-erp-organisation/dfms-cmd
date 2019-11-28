package com.expedia.content.media.processing.pipeline.reporting.sql;

/**
 * Represents the data retrieved from LCM of a Media.
 */
@Deprecated
public final class Media {

    private String mediaID;
    private int skuGroupCatalogItemID;
    private String mediaFormatID;
    private String contentProviderID;
    private String contentProviderMediaName;
    private String statusCode;

    public Media(String mediaID, int skuGroupCatalogItemID, String mediaFormatID,
                 String contentProviderID, String contentProviderMediaName,
                 String statusCode) {
        this.mediaID = mediaID;
        this.skuGroupCatalogItemID = skuGroupCatalogItemID;
        this.mediaFormatID = mediaFormatID;
        this.contentProviderID = contentProviderID;
        this.contentProviderMediaName = contentProviderMediaName;
        this.statusCode = statusCode;
    }

    public String getMediaID() {
        return mediaID;
    }

    public void setMediaID(String mediaID) {
        this.mediaID = mediaID;
    }

    public int getSkuGroupCatalogItemID() {
        return skuGroupCatalogItemID;
    }

    public void setSkuGroupCatalogItemID(int skuGroupCatalogItemID) {
        this.skuGroupCatalogItemID = skuGroupCatalogItemID;
    }

    public String getMediaFormatID() {
        return mediaFormatID;
    }

    public void setMediaFormatID(String mediaFormatID) {
        this.mediaFormatID = mediaFormatID;
    }

    public String getContentProviderID() {
        return contentProviderID;
    }

    public void setContentProviderID(String contentProviderID) {
        this.contentProviderID = contentProviderID;
    }

    public String getContentProviderMediaName() {
        return contentProviderMediaName;
    }

    public void setContentProviderMediaName(String contentProviderMediaName) {
        this.contentProviderMediaName = contentProviderMediaName;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }
}
