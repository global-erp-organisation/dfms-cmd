package com.expedia.content.media.processing.pipeline.archive;

import static org.junit.Assert.assertFalse;

import java.io.File;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public class ArchivingUtilTest {
    
    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();
    
    @Test
    public void testDeleteSuccess() throws Exception {
        File fileToDelete = tempFolder.newFile();
        ArchivingUtil.deleteImageAfterArchive(fileToDelete.toURI().toURL());
        assertFalse(fileToDelete.exists());
    }
    
}
