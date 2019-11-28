package com.expedia.content.media.processing.pipeline.util;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PathUtilsTest {

    @Test(expected = NullPointerException.class)
    public void testExtractExtensionNull() {
        PathUtils.extractExtension(null);
    }

    @Test
    public void testExtractExtensionLotsOfDots() {
        final String ext = PathUtils.extractExtension("s3://bucket.name//this/path.has/lots.of/dots/filename.ext");
        assertEquals(".ext", ext);
    }

    @Test
    public void testExtractNoExtension() {
        final String ext = PathUtils.extractExtension("s3://bucket.name//this/path.has/lots.of/dots/filename");
        assertEquals("", ext);
    }

    @Test(expected = NullPointerException.class)
    public void testExtractFilenameNull() {
        PathUtils.extractFilename(null);
    }

    @Test
    public void testExtractFilename() {
        final String filename = PathUtils.extractFilename("s3://bucket-name//this/path/is/very/deep/filename.ext");
        assertEquals("filename.ext", filename);
    }

    @Test
    public void testExtractOnlyFilename() {
        final String filename = PathUtils.extractFilename("filename.ext");
        assertEquals("filename.ext", filename);
    }
}
