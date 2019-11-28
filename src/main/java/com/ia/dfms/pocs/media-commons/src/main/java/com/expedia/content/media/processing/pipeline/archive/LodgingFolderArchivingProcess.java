package com.expedia.content.media.processing.pipeline.archive;

import com.expedia.content.media.processing.pipeline.exception.InvalidFileNameFormatException;
import com.expedia.content.media.processing.pipeline.util.FormattedLogger;
import com.expedia.content.media.processing.pipeline.util.FileSystemUtil;
import com.expedia.content.media.processing.pipeline.util.LodgingUtil;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;

import static com.expedia.content.media.processing.pipeline.archive.ArchivingUtil.deleteImageAfterArchive;



/**
 * Performs archiving of source images in a folder after the derivatives are created.
 */
public class LodgingFolderArchivingProcess implements ArchivingProcess {
    
    private static final FormattedLogger LOGGER = new FormattedLogger(LodgingFolderArchivingProcess.class);
    private static final String FILE_SEPARATOR = File.separator;
    
    private static final int EID_ARCHIVE_SWITCH_THRESHOLD = 7000000;

    private final String archivePath1;
    private final String archivePath2;
    
    public LodgingFolderArchivingProcess(String archivePath1, String archivePath2) {
        this.archivePath1 = archivePath1;
        this.archivePath2 = archivePath2;
    }
    
    /**
     * Archives the provided lodging image. Images are stored in either of the two archive paths provided. The
     * sub-folders the image is stored is determined by the EID at the beginning of the file name.
     * 
     * @param file URL of the image file to archive.
     * @param supplementalPath Path portion to add to the archiving destination. Since the path is determined
     *            from the EID in the file name this field is ignored.
     * @throws ArchiveImageException Thrown if any operations fail during the archiving process.
     * @throws InvalidFileNameFormatException Thrown if the name of the file doesn't start with an EID.
     */
    @Override
    public void archiveSourceImage(URL file, String supplementalPath) throws ArchiveImageException, NoSuchFileException {
        LOGGER.info("Archiving source Image={}", file.toString());
        try {
            String hotelId = LodgingUtil.extractHotelId(Paths.get(file.toURI()).getFileName().toString());
            if (hotelId != null) {
                File destinationFolder = new File(getArchivePath(Integer.parseInt(hotelId)));
                Files.createDirectories(destinationFolder.toPath());
                File destination = new File(destinationFolder.getPath() + FILE_SEPARATOR + Paths.get(file.toURI()).toFile().getName());
                File source = Paths.get(file.toURI()).toFile();
                LOGGER.info("Archiving source={} destination={}", source, destination);
                FileSystemUtil.copyFile(source, destination);
                LOGGER.info("Archiving SUCCESS Image={}", file.toString());
            } else {
                LOGGER.error("Archiving Error=extracting hotel id from filename");
                throw new InvalidFileNameFormatException("Failed to extract hotel id from filename - archiving source image " + file.toString()
                        + " failed.");
            }
        } catch (NoSuchFileException e) {
            throw new NoSuchFileException(file.toString(), "", "Archiving: Interrupted. Unable to find the source image: " + e);
        } catch (IOException | URISyntaxException e) {
            LOGGER.error("Archiving FAILURE Image={}", file.toString());
            throw new ArchiveImageException("Failed to archive source image " + file.toString(), e);
        }
        deleteImageAfterArchive(file);
    }
    
    /**
     * Generates the right archiving path based on the hotel ID, the root-level-paths in the configuration file, and the levels logic.
     * Note: Levels must be in decreasing order.
     * 
     * @param hotelId - id of the hotel.
     * @return archivePath - the correct archiving path.
     */
    private String getArchivePath(final int hotelId) {
        int[] folderIds = LodgingUtil.getFolderHierarchy(hotelId);
        String hotelIdPath = LodgingUtil.buildFolderPath(hotelId);

        if (folderIds[0] < EID_ARCHIVE_SWITCH_THRESHOLD) {
            return archivePath1 + hotelIdPath;
        } else {
            return archivePath2 + hotelIdPath;
        }
    }
}
