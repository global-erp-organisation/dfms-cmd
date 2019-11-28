package com.expedia.content.media.processing.pipeline.domain;

/**
 * Cropping instructions for images. Includes the height and width defining
 * the section to crop.
 */
public class CropInstruction {
    private final int height;
    private final int width;

    public CropInstruction(final int height, final int width) {
        this.height = height;
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

}
