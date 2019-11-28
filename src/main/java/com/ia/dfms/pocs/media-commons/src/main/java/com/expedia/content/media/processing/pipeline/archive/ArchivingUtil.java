package com.expedia.content.media.processing.pipeline.archive;

import com.expedia.content.media.processing.pipeline.util.FormattedLogger;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Archiving utility methods. 
 */
public final class ArchivingUtil {
    
    private static final FormattedLogger LOGGER = new FormattedLogger(ArchivingUtil.class);
    private static final String DELETE_MESSAGE = "Failed to delete source image ";
    
    private ArchivingUtil() {
    }
    
    /**
     * Deletes a file that was archived.
     * 
     * @param file File to delete
     * @throws ArchiveImageException Throw if deleting the file fails.
     */
    public static void deleteImageAfterArchive(URL file) throws ArchiveImageException {
        LOGGER.info("Deleting repoImage={}", file.toString());
        boolean deleted;
        try {
            Path filePath = Paths.get(file.toURI());
            deleted = Files.deleteIfExists(filePath);
            if (!deleted || filePath.toFile().exists()) {
                throw new ArchiveImageException(DELETE_MESSAGE + file.toString());
            }
        } catch (IOException | URISyntaxException e) {
            throw new ArchiveImageException(DELETE_MESSAGE + file.toString(), e);
        }
    }
    
}
