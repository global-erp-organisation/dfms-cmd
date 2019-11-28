package com.expedia.content.media.processing.pipeline.archive;

import java.net.URL;
import java.nio.file.NoSuchFileException;

/**
 * Archiving of source images after the derivatives are created.
 * Links to {@literal Image imageData} and {@literal URL fileName}.
 */
public interface ArchivingProcess {
    
    /**
     * Archives the provided source image following the archiving logic, or for other purposes (e.g.: handling rejected media).
     * 
     * @param file URL of the image file to archive.
     * @param supplementalPath Path portion to add to the archiving destination.
     * 
     * @throws ArchiveImageException Thrown if any operations fail during the archiving process.
     */
    void archiveSourceImage(URL file, String supplementalPath) throws ArchiveImageException, NoSuchFileException;
}
