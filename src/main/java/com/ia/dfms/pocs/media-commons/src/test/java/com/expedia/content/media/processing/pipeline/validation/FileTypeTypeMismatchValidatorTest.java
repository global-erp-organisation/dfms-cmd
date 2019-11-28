package com.expedia.content.media.processing.pipeline.validation;

import static org.junit.Assert.assertTrue;

import java.net.URL;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class FileTypeTypeMismatchValidatorTest {

    private static final URL REAL_JPEG_IMAGE = FileTypeTypeMismatchValidatorTest.class.getResource("/examples/real-jpeg.jpg");
    private static final URL FAKE_BMP_IMAGE = FileTypeTypeMismatchValidatorTest.class.getResource("/examples/fake-bmp.dib");
    private static final URL IMAGE_WITH_UNKNOW_EXTENSION = FileTypeTypeMismatchValidatorTest.class.getResource("/examples/unknow-extension.10");
    private ImageValidator validator = new FileTypeMismatchValidator();

    @Test
    public void testFileTypeMatch() throws Exception {
        assertTrue(getImageValidationStatus(REAL_JPEG_IMAGE));
    }

    @Test
    public void testFileTypeMismatch() throws Exception {
        assertTrue(getImageValidationStatus(FAKE_BMP_IMAGE));
    }

    @Test
    public void testUnknowExtension() throws Exception {
        assertTrue(getImageValidationStatus(IMAGE_WITH_UNKNOW_EXTENSION));
    }

    private Boolean getImageValidationStatus(URL imageUrl) throws Exception {
        return validator.validate(imageUrl, null);
    }
}
