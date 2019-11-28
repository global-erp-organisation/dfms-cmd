package com.expedia.content.media.processing.pipeline.util;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.net.URL;

import static org.junit.Assert.assertTrue;

public class FileImageCopyTest {

    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();

    @Test
    public void testDeleteUrlFileUnix() throws Exception {
        final URL imageUrl = FileImageCopyTest.class.getResource("/examples/office.jpg");
        File file = new File(imageUrl.getFile());
        System.out.println(file.length());
        File destinationFile = tempFolder.newFile("test2.jpg");
        FileImageCopy fileImageCopy = new FileImageCopy();
        fileImageCopy.copyImage(file, destinationFile.getAbsolutePath());
        assertTrue(file.length() == destinationFile.length());
    }
}
