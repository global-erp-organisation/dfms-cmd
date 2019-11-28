package com.expedia.content.media.processing.pipeline.util;

import java.io.File;
import java.util.regex.Pattern;

/**
 * Utility class for lodging.
 */
public final class LodgingUtil {

    private static final int EID_ARCHIVE_LEVEL_1 = 1000000;
    private static final int EID_ARCHIVE_LEVEL_2 = 10000;
    private static final int EID_ARCHIVE_LEVEL_3 = 100;
    private static final int[] LEVELS = {EID_ARCHIVE_LEVEL_1, EID_ARCHIVE_LEVEL_2, EID_ARCHIVE_LEVEL_3};
    private static final String FILE_SEPARATOR = File.separator;

    private LodgingUtil() {
        /* Static class */
    }

    /**
     * Calculates the folders hierarchy for a hotel id.
     *
     * @param hotelId The hotel id
     * @return folder hierarchy - array of subFolders ids in which a hotel id should belong (rounded-up to nearest level each time).
     */
    public static int[] getFolderHierarchy(int hotelId) {
        int[] subFolderIds = new int[LEVELS.length];
        for (int i = 0; i < LEVELS.length; i++) {
            subFolderIds[i] = (int) (LEVELS[i] * Math.ceil((double) hotelId / LEVELS[i]));
        }
        return subFolderIds;
    }

    /**
     * Builds the folder path for a hotel id.
     *
     * @return path - string path for a hotel id.
     */
    public static String buildFolderPath(int hotelId) {
        StringBuilder path = new StringBuilder();
        int[] subFolderIds = getFolderHierarchy(hotelId);
        for (int subFolderId : subFolderIds) {
            path.append(FILE_SEPARATOR);
            path.append(Integer.toString(subFolderId));
        }

        path.append(FILE_SEPARATOR).append(hotelId).append(FILE_SEPARATOR);
        return path.toString();
    }

    /**
     * Extracts id from filename of format [id_any.any]
     *
     * @param fileName
     * @return id - hotel id, or null if filename format is wrong.
     */
    public static String extractHotelId(String fileName) {
        String format = "^[0-9]+_.*$";
        String hotelId = null;
        if (Pattern.matches(format, fileName)) {
            return fileName.split("_")[0];
        }
        return hotelId;
    }
}
