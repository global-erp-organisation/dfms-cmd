package com.expedia.content.media.processing.pipeline.archive;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public class RejectedFolderArchivingProcessTest {
    
    private static final String FILE_WILL_BE_REJECTED = "to_be_rejected.jpg";
    
    @Rule
    public TemporaryFolder rejectedFolder = new TemporaryFolder();
    
    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();
    
    @Test
    public void testImageMovedToRejectedFolder() throws IOException, ArchiveImageException {
        RejectedFolderArchivingProcess archiver = new RejectedFolderArchivingProcess(rejectedFolder.getRoot().getPath());
        File testFile = tempFolder.newFile(FILE_WILL_BE_REJECTED);
        archiver.archiveSourceImage(testFile.toURI().toURL(), null);
        assertTrue(new File(rejectedFolder.getRoot().getPath() + "/" + FILE_WILL_BE_REJECTED).exists());
        assertFalse(testFile.exists());
    }
}
