package com.expedia.content.media.processing.pipeline.reporting;

/**
 * Media Processing Pipeline Apps
 */
public enum App {
    MEDIA_SERVICE("cs-media-service"),
    MEDIA_COLLECTOR_SERVICE("cs-media-collector-service"),
    MEDIA_DERIVATIVE_CREATOR_SERVICE("cs-media-derivative-creator-service"),
    MEDIA_DATA_MANAGER_SERVICE("cs-media-data-manager-service");

    private String name;
    App(String name) {
        this.name = name;
    }

    /**
     * Converts a String to an App enum type.
     *
     * @param name App name (case-sensitive)
     * @return The App enum value or null if not found.
     */
    public static App getAppByName(String name) {
        return getAppByName(name, false);
    }

    /**
     * Converts a String to an App enum type.
     *
     * @param name App name (case-sensitive)
     * @param fail If true, will throw a RuntimeException if the name is not found.
     * @return The App enum value. If fail is false, null will be returned for not found values.
     *         If fails is true, a RuntimeException will be thrown for not found values.
     */
    public static App getAppByName(String name, boolean fail) {
        for (App app : values()) {
            if (app.getName().equals(name)) {
                return app;
            }
        }
        if (fail) {
            throw new RuntimeException("Invalid Activity Name: " + name);
        } else {
            return null;
        }
    }

    public String getName() {
        return this.name;
    }
}
