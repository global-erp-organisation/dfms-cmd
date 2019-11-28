package com.expedia.content.media.processing.pipeline.validation;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;

import org.im4java.process.ProcessStarter;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

public class OnlyJPEGImageValidatorTest {

    private OnlyJPEGImageValidator validator = new OnlyJPEGImageValidator();

    @BeforeClass
    public static void classSetUp() {
        final String path = System.getenv("PATH").replace('\\', '/');
        ProcessStarter.setGlobalSearchPath(path);
    }

    @Test
    public void testFakeJPGImage() throws Exception {
        // This is really a PNG image with a .jpg extension
        Resource testImage = new ClassPathResource("/examples/toyota_webpng.jpg");
        assertFalse(validator.validate(testImage.getURL(), null));
    }

    @Test
    public void testRealJPGImage() throws Exception {
        Resource testImage = new ClassPathResource("/examples/las-vegas.jpg");
        assertTrue(validator.validate(testImage.getURL(), null));
    }

}
