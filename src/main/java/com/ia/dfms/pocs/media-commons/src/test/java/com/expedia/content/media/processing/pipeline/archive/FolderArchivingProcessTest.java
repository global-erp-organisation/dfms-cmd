package com.expedia.content.media.processing.pipeline.archive;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public class FolderArchivingProcessTest {
    
    private static final String TEST_FILE = "file.jpg";
    
    @Rule
    public TemporaryFolder archiveFolder = new TemporaryFolder();
    
    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();
    
    @Test
    public void testImageMovedToFolder() throws ArchiveImageException, IOException {
        ArchivingProcess archiver = new FolderArchivingProcess(archiveFolder.getRoot().getPath());
        File testFile = tempFolder.newFile(TEST_FILE);
        archiver.archiveSourceImage(testFile.toURI().toURL(), null);
        assertTrue(new File(archiveFolder.getRoot().getPath() + "/" + TEST_FILE).exists());
        assertFalse(testFile.exists());
    }
    
    @Test
    public void testImageMovedToFolderWithSupplementalPath() throws ArchiveImageException, IOException {
        ArchivingProcess archiver = new FolderArchivingProcess(archiveFolder.getRoot().getPath());
        File testFile = tempFolder.newFile(TEST_FILE);
        String supplementalPath = "path" + File.separator + "path";
        archiver.archiveSourceImage(testFile.toURI().toURL(), supplementalPath);
        assertTrue(new File(archiveFolder.getRoot().getPath() + File.separator + supplementalPath + File.separator + TEST_FILE).exists());
        assertFalse(testFile.exists());
    }
    
}
