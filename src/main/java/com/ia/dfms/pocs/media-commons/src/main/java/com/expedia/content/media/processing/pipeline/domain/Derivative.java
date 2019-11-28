package com.expedia.content.media.processing.pipeline.domain;

/**
 * Derivative file data.
 *
 * {@code Location} is the s3 location of the derivative.
 * {@code Type} is the type name of the derivative (i.e. In lodging there is s, t, z, w etc.)
 * {@code Width} and {@code Height} are the lengths of the image sides in pixels.
 * {@code FileSize} is the size in bytes of the derivative.
 */
public class Derivative {

    private final String location;
    private final String type;
    private final Integer width;
    private final Integer height;
    private final Integer fileSize;
   
    public Derivative(String location, String type, Integer width, Integer height, Integer fileSize) {
        this.location = location;
        this.type = type;
        this.width = width;
        this.height = height;
        this.fileSize = fileSize;
    }

    public String getLocation() {
        return location;
    }

    public String getType() {
        return type;
    }

    public Integer getWidth() {
        return width;
    }

    public Integer getHeight() {
        return height;
    }

    public Integer getFileSize() {
        return fileSize;
    }

    @Override
    public String toString() {
        return "Derivative{" +
                "location='" + location + '\'' +
                ", type='" + type + '\'' +
                ", width=" + width +
                ", height=" + height +
                ", fileSize=" + fileSize +
                '}';
    }
}
