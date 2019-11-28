package com.expedia.content.media.processing.pipeline.util;

/**
 * Static class containing several path manipulation utilites for URLs and Files
 */
public final class PathUtils {

    /**
     * Separator for paths in unix and urls. This constant is not platform dependent.
     */
    public static final String PATH_SEPARATOR = "/";

    private PathUtils() {
        /* static class */
    }

    /**
     * Gets the extension of a file. That is the remainder of the fileName after the last dot ('.')
     *
     * @param fullPath Full file name including the extension.
     * @return String with just the extension returned including the dot.
     */
    public static String extractExtension(String fullPath) {
        final int pathIndexEnd = fullPath.lastIndexOf(PATH_SEPARATOR);
        final int extensionIndexBegin = fullPath.lastIndexOf('.');
        if (pathIndexEnd < extensionIndexBegin) {
            return fullPath.substring(extensionIndexBegin);
        } else {
            return "";
        }
    }

    /**
     * Returns the filename (with extension) of the provided path, after the last separator.
     *
     * @param fullPath Full path to the resource, either a File path (unix) or an Url.
     * @return Just the filename portion of the path including the extension.
     */
    public static String extractFilename(String fullPath) {
        final int endOfPath = fullPath.lastIndexOf(PATH_SEPARATOR);
        return fullPath.substring(endOfPath + 1);
    }
}
