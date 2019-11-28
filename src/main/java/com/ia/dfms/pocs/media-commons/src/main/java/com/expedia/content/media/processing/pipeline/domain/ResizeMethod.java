package com.expedia.content.media.processing.pipeline.domain;

/**
 * Supported resize methods.
 */
public enum ResizeMethod {
    FIXED("Fixed", true),
    HEIGHT("Height", false),
    VARIABLE("Variable", false),
    WIDTH("Width", false);

    private final String name;
    private final boolean crop;

    ResizeMethod(final String name, final boolean crop) {
        this.name = name;
        this.crop = crop;
    }

    public String getName() {
        return this.name;
    }

    public boolean isCropable() {
        return this.crop;
    }

    /**
     * Finds the ResizeMethod for the passed name.
     *
     * @param name Name search is done for.
     * @return The ResizeMethod. null if the code is not found.
     */
    public static ResizeMethod findResizeMethod(String name) {
        for (final ResizeMethod method : values()) {
            if (method.getName().equals(name)) {
                return method;
            }
        }
        return null;
    }
}
