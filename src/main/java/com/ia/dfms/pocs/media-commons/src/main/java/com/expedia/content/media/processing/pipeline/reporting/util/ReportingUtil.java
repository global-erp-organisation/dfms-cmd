package com.expedia.content.media.processing.pipeline.reporting.util;

import org.apache.commons.lang3.StringUtils;

import java.util.regex.Pattern;

/**
 * parse expedia id from filename
 */
public final class ReportingUtil {

    private ReportingUtil() {
    }

    /**
     * Extracts the substring before the underscore "_" which corresponds to the property/Catalog Item Id.
     * Assumes file name has an underscore.
     *
     * @param fileName
     * @return EID (Catalog Item Id) as a int
     */
    public static Integer extractExpediaID(final String fileName) {
        String format = "^[0-9]+_[\\w,\\s-]+\\.[A-Za-z]+$";
        if (Pattern.matches(format, fileName)) {
            String id = fileName.split("_")[0];
            if (StringUtils.isNumeric(id)) {
                return Integer.parseInt(id);
            }
        }
        return null;
    }
}
