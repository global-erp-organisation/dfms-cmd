package com.expedia.content.media.processing.pipeline.archive;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.expedia.content.media.processing.pipeline.exception.InvalidFileNameFormatException;

import java.io.File;
import java.io.IOException;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public class LodgingFolderArchivingProcessTest {
    
    private static final String FILE_WILL_WRONG_FORMAT = "asasa_willbearchived.jpg";
    private static final String FILE_WILL_BE_ARCHIVED = "232_willbearchived.jpg";
    private static final String FILE_WILL_BE_ARCHIVED_2 = "18292_willbearchived.jpg";
    private static final String FILE_WILL_BE_ARCHIVED_3 = "19963_willbearchived.jpg";
    private static final String FILE_WILL_BE_ARCHIVED_4 = "299993_willbearchived.jpg";
    private static final String FILE_WILL_BE_ARCHIVED_5 = "6041599_willbearchived.jpg";
    
    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();
    
    @Rule
    public TemporaryFolder archivePath1 = new TemporaryFolder();
    
    @Rule
    public TemporaryFolder archivePath2 = new TemporaryFolder();
    
    @Test
    public void testImagesArchived() throws IOException, ArchiveImageException {
        ArchivingProcess archiver = new LodgingFolderArchivingProcess(archivePath1.getRoot().getPath(), archivePath2.getRoot().getPath());
        File testFile = tempFolder.newFile(FILE_WILL_BE_ARCHIVED);
        archiver.archiveSourceImage(testFile.toURI().toURL(), null);
        assertTrue(new File(archivePath1.getRoot().getPath() + "/1000000/10000/300/232/" + FILE_WILL_BE_ARCHIVED).exists());
        assertFalse(testFile.exists());
        testFile = tempFolder.newFile(FILE_WILL_BE_ARCHIVED_2);
        archiver.archiveSourceImage(testFile.toURI().toURL(), "whatever");
        assertTrue(new File(archivePath1.getRoot().getPath() + "/1000000/20000/18300/18292/" + FILE_WILL_BE_ARCHIVED_2).exists());
        assertFalse(testFile.exists());
        testFile = tempFolder.newFile(FILE_WILL_BE_ARCHIVED_3);
        archiver.archiveSourceImage(testFile.toURI().toURL(), null);
        assertTrue(new File(archivePath1.getRoot().getPath() + "/1000000/20000/20000/19963/" + FILE_WILL_BE_ARCHIVED_3).exists());
        assertFalse(testFile.exists());
        testFile = tempFolder.newFile(FILE_WILL_BE_ARCHIVED_4);
        archiver.archiveSourceImage(testFile.toURI().toURL(), null);
        assertTrue(new File(archivePath1.getRoot().getPath() + "/1000000/300000/300000/299993/" + FILE_WILL_BE_ARCHIVED_4).exists());
        assertFalse(testFile.exists());
        testFile = tempFolder.newFile(FILE_WILL_BE_ARCHIVED_5);
        archiver.archiveSourceImage(testFile.toURI().toURL(), null);
        assertTrue(new File(archivePath2.getRoot().getPath() + "/7000000/6050000/6041600/6041599/" + FILE_WILL_BE_ARCHIVED_5).exists());
        assertFalse(testFile.exists());
        
    }
    
    @Test(expected = InvalidFileNameFormatException.class)
    public void testImageArchivingError() throws IOException, ArchiveImageException {
        ArchivingProcess archiver = new LodgingFolderArchivingProcess(archivePath1.getRoot().getPath(), archivePath2.getRoot().getPath());
        File testFile = tempFolder.newFile(FILE_WILL_WRONG_FORMAT);
        archiver.archiveSourceImage(testFile.toURI().toURL(), null);
    }
}
