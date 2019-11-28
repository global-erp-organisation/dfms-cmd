package com.expedia.content.media.processing.pipeline.domain;

/**
 * Container for the configured base attributes of a single derivative. A list of these is built at
 * the initialization of the derivatives manager.
 */
public class DerivativeType {
    private String typeName;
    private String extension;
    private int height;
    private int width;
    private ResizeMethod resizeMethod;
    private int threshold;
    private int border;
    private String outputPath;
    private boolean progressive;
    private boolean generateLodgingPath;

    public DerivativeType() {
        //empty
    }

    public boolean isGenerateLodgingPath() {
        return generateLodgingPath;
    }

    public void setGenerateLodgingPath(boolean generateLodgingPath) {
        this.generateLodgingPath = generateLodgingPath;
    }

    public boolean isProgressive() {
        return progressive;
    }

    public void setProgressive(boolean progressive) {
        this.progressive = progressive;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public ResizeMethod getResizeMethod() {
        return resizeMethod;
    }

    public void setResizeMethod(ResizeMethod resizeMethod) {
        this.resizeMethod = resizeMethod;
    }

    public int getThreshold() {
        return threshold;
    }

    public void setThreshold(int threshold) {
        this.threshold = threshold;
    }

    public int getBorder() {
        return border;
    }

    public void setBorder(int border) {
        this.border = border;
    }

    public String getOutputPath() {
        return outputPath;
    }

    public void setOutputPath(String outputPath) {
        this.outputPath = outputPath;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    /**
     * Verifies if the media size is within the threshold of the passed derivative type.
     *
     * @param mediaHeight The height of the media.
     * @param mediaWidth The width of the media.
     * @param derivativeType The type to verify against.
     * @return <code>true</code> if the media is within the threshold and the derivative type has a
     *         threshold; <code>false</code> if it is otherwise.
     */
    public static boolean isMediaWithinThreshold(int mediaHeight, int mediaWidth, DerivativeType derivativeType) {
        return derivativeType.getThreshold() > 0 && (mediaHeight >= derivativeType.getThreshold() || mediaWidth >= derivativeType.getThreshold());
    }

}
