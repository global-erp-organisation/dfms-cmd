package com.expedia.content.media.processing.pipeline.archive;

import com.expedia.content.media.processing.pipeline.util.FormattedLogger;
import com.expedia.content.media.processing.pipeline.util.FileSystemUtil;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

import static com.expedia.content.media.processing.pipeline.archive.ArchivingUtil.deleteImageAfterArchive;

/**
 * Performs archiving of image files to a folder.
 */
public class FolderArchivingProcess implements ArchivingProcess {
    
    private static final FormattedLogger LOGGER = new FormattedLogger(FolderArchivingProcess.class);
    private static final String ARCHIVE_PURPOSE = "archive";
    
    private final String purpose;
    private final String rootPath;
    
    public FolderArchivingProcess(String rootPath) {
        this.rootPath = rootPath;
        this.purpose = getPurpose();
    }
    
    /**
     * Allows to override the purpose of this class in the logs.
     * 
     * @return The class purpose. By default it is "archive".
     */
    protected String getPurpose() {
        return ARCHIVE_PURPOSE;
    }
    
    @Override
    public void archiveSourceImage(URL file, String supplementalPath) throws ArchiveImageException {
        LOGGER.info("Moving image purpose={} image={}", this.purpose, file.toString());
        File destinationFolder = new File(rootPath + (StringUtils.isBlank(supplementalPath) ? "" : File.separatorChar + supplementalPath));
        try {
            Files.createDirectories(destinationFolder.toPath());
            File destination = new File(destinationFolder.getPath() + File.separatorChar + Paths.get(file.toURI()).toFile().getName());
            File source = Paths.get(file.toURI()).toFile();
            LOGGER.info("Moving image purpose={} source={} destination={}", this.purpose, source, destination);
            FileSystemUtil.copyFile(source, destination);
            LOGGER.info("Moving image purpose={} source={} destination={} status=SUCCESS", this.purpose, source, destination);
        } catch (IOException | URISyntaxException e) {
            LOGGER.error("Moving image purpose={} source={} status=FAILURE", file.toString());
            throw new ArchiveImageException("Failed to move source image to " + this.purpose + " folder: " + file.toString(), e);
        }
        deleteImageAfterArchive(file);
    }
    
}
