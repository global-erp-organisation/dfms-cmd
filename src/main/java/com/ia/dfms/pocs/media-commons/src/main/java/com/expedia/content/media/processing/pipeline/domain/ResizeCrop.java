package com.expedia.content.media.processing.pipeline.domain;

import com.expedia.content.media.processing.pipeline.exception.DerivativeManagerInvalidResizeMethodException;
import com.expedia.content.media.processing.pipeline.util.FormattedLogger;

/**
 * Object to hold the results of a resize and crop calculation.
 */
public class ResizeCrop {
    private static final FormattedLogger LOGGER = new FormattedLogger(ResizeCrop.class);

    private CropInstruction cropInstruction;
    private int height;
    private int width;

    public ResizeCrop(final int mediaHeight, final int mediaWidth, final DerivativeType derivativeType) {
        if (mediaHeight >= derivativeType.getHeight() || mediaWidth >= derivativeType.getWidth()
                || DerivativeType.isMediaWithinThreshold(mediaHeight, mediaWidth, derivativeType)) {
            switch (derivativeType.getResizeMethod()) {
                case FIXED:
                    adjustForFixed(mediaHeight, mediaWidth, derivativeType.getHeight(), derivativeType.getWidth());
                    break;
                case HEIGHT:
                    adjustWidth(mediaHeight, mediaWidth, derivativeType.getHeight());
                    break;
                case VARIABLE:
                    adjustForShortestSide(mediaHeight, mediaWidth, mediaHeight, mediaWidth, derivativeType.getHeight(), derivativeType.getWidth());
                    break;
                case WIDTH:
                    adjustHeight(mediaHeight, mediaWidth, derivativeType.getWidth());
                    break;
                default:
                    // Should never happen. This is a safety net as the resize methods are validated when loading the configuration.
                    final String errorMessage = "Resize method " + derivativeType.getResizeMethod().getName() + " is unrecognized";
                    LOGGER.error(errorMessage);
                    throw new DerivativeManagerInvalidResizeMethodException(errorMessage);
            }
        } else {
            this.height = mediaHeight;
            this.width = mediaWidth;
        }
        setCropInstructions(derivativeType.getResizeMethod(), derivativeType.getHeight(), derivativeType.getWidth());
    }

    public CropInstruction getCropInstruction() {
        return cropInstruction;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    /**
     * Adjusts the height and width for fixed derivatives.
     *
     * @param mediaHeight The height of the original image.
     * @param mediaWitdh The width of the original image.
     * @param derivativeHeight
     * @param derivativeWidth
     */
    private void adjustForFixed(final int mediaHeight, final int mediaWitdh, final int derivativeHeight, final int derivativeWidth) {
        if (mediaHeight == mediaWitdh) {
            adjustForShortestSide(derivativeHeight, derivativeWidth, mediaHeight, mediaWitdh, derivativeHeight, derivativeWidth);
        } else {
            if (mediaHeight > mediaWitdh) {
                adjustHeight(mediaHeight, mediaWitdh, derivativeWidth);
                if (this.height < derivativeHeight) {
                    adjustWidth(mediaHeight, mediaWitdh, derivativeHeight);
                }
            } else {
                adjustWidth(mediaHeight, mediaWitdh, derivativeHeight);
                if (this.width < derivativeWidth) {
                    adjustHeight(mediaHeight, mediaWitdh, derivativeWidth);
                }
            }
        }
    }

    /**
     * Adjusts the derivative to create against the short side of the derivative. The check size are either from the derivative or
     * from the media. In the case of fixed derivatives the check is done with derivative sizes, in the case of variable derivatives
     * the check is done with media sizes.
     *
     * @param checkHeight Height to verify which is shortest.
     * @param checkWidth Width to verify which is shortest.
     * @param mediaHeight Height of the media.
     * @param mediaWidth Width of the media.
     * @param derivativeHeight Height of the derivative.
     * @param derivativeWidth Width of the derivative.
     */
    private void adjustForShortestSide(int checkHeight, int checkWidth, int mediaHeight, int mediaWidth, int derivativeHeight, int derivativeWidth) {
        if (checkHeight > checkWidth) {
            adjustWidth(mediaHeight, mediaWidth, derivativeHeight);
        } else {
            adjustHeight(mediaHeight, mediaWidth, derivativeWidth);
        }
    }

    /**
     * Adjusts the height of the derivative and sets the width to the derivative's width. Does
     * a cross product of the derivative and the media to find the new height.
     *
     * @param mediaHeight Height of the media to resize.
     * @param mediaWitdh Width of the media to resize.
     * @param derivativeWidth Width of the derivative to resize to.
     */
    private void adjustHeight(int mediaHeight, int mediaWitdh, int derivativeWidth) {
        this.height = (int) ((derivativeWidth / (float) mediaWitdh) * mediaHeight);
        this.width = derivativeWidth;
    }

    /**
     * Adjusts the width of the derivative and sets the height to the derivative's height. Does
     * a cross product of the derivative and the media to find the new width.
     *
     * @param mediaHeight Height of the media to resize.
     * @param mediaWitdh Width of the media to resize.
     * @param derivativeHeight Height of the derivative to resize to.
     */
    private void adjustWidth(int mediaHeight, int mediaWitdh, int derivativeHeight) {
        this.height = derivativeHeight;
        this.width = (int) ((derivativeHeight / (float) mediaHeight) * mediaWitdh);
    }

    /**
     * Creates the cropping instructions for the media against the derivative. The cropping
     * instruction is only created if the passed ResizeMethod allows cropping. When it's
     * not available the cropping is left as null.
     *
     * @param resizeMethod Indicates if the image can be cropped.
     * @param derivativeHeight Final height to give the new derivative.
     * @param derivativeWidth Final width to give the new derivative
     */
    private void setCropInstructions(ResizeMethod resizeMethod, int derivativeHeight, int derivativeWidth) {
        if (resizeMethod.isCropable()) {
            final CropInstruction newCropInstruction = new CropInstruction(derivativeHeight, derivativeWidth);
            this.cropInstruction = newCropInstruction;
        }
    }
}
