package com.expedia.content.media.processing.pipeline.domain;

import java.util.Locale;
import java.util.stream.Stream;

/**
 * Helps to map file type and available extensions.
 */
public enum FileTypeMapper {
    BMP("image/bmp", "BMP3,BMP,DIB"),
    JPEG("image/jpeg", "JPG,JPEG,JPE,JFIF"),
    PNG("image/png", "PNG"),
    TIFF("image/tiff", "TIF,TIFF"),
    GIF("image/gif", "GIF"),
    UNKNOWN("unknown", "unknown");

    private final String mediaType;
    private final String extensionList;

    private FileTypeMapper(String mediaType, String extensionList) {
        this.mediaType = mediaType;
        this.extensionList = extensionList;
    }

    public String getExtensionList() {
        return extensionList;
    }

    public String getMediaType() {
        return mediaType;
    }
    
    /**
     * Find a specific mapping according to the given extension.
     * 
     * @param extension provided extension.
     * @return
     */
    public static FileTypeMapper getFyleTypeByExtension(String extension) {
          return Stream.of(FileTypeMapper.values()).filter(f -> f.getExtensionList().contains(extension.toUpperCase(Locale.getDefault()))).findFirst()
                .orElse(UNKNOWN);
    }

    /**
     * Find a specific mapping according to the given media type.
     * 
     * @param mediaType given media type.
     * @return
     */
    public static FileTypeMapper getFileTypeByType(String mediaType) {
         return Stream.of(FileTypeMapper.values()).filter(f -> f.getMediaType().equals(mediaType)).findFirst()
                .orElse(UNKNOWN);
    }
}
