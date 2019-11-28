package com.expedia.content.media.processing.pipeline.validation;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import org.im4java.process.ProcessStarter;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import com.expedia.content.media.processing.pipeline.util.ConvertResultChecker;
import com.expedia.content.media.processing.pipeline.util.Poker;

public class CorruptionValidatorTest {
    
    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();
    
    @BeforeClass
    public static void classSetUp() {
        final String path = System.getenv("PATH").replace('\\', '/');
        ProcessStarter.setGlobalSearchPath(path);
    }

    @Test
    public void testCorruptedImage() throws Exception {
        Resource testImage = new ClassPathResource("/examples/corrupted.jpg");
        ConvertResultChecker mockChecker = mock(ConvertResultChecker.class);
        when(mockChecker.hasCommandResultErrors(any())).thenReturn(true);
        CorruptionValidator validator = new CorruptionValidator(tempFolder.newFolder().toString(), mockChecker);
        assertFalse(validator.validate(testImage.getURL(), null));
    }

    @Test
    public void testCleanImage() throws Exception {
        Resource testImage = new ClassPathResource("/examples/office.jpg");
        ConvertResultChecker mockChecker = mock(ConvertResultChecker.class);
        when(mockChecker.hasCommandResultErrors(any())).thenReturn(false);
        CorruptionValidator validator = new CorruptionValidator(tempFolder.newFolder().toString(), mockChecker);
        assertTrue(validator.validate(testImage.getURL(), null));
    }

    @Test
    public void testBogusMarker() throws Exception {
        Resource testImage = new ClassPathResource("/examples/bogus_marker.jpg");
        Poker mockPoker = mock(Poker.class);
        ConvertResultChecker convertResultChecker = new ConvertResultChecker(mockPoker);
        CorruptionValidator validator = new CorruptionValidator(tempFolder.newFolder().toString(), convertResultChecker);
        assertFalse(validator.validate(testImage.getURL(), null));
        verifyZeroInteractions(mockPoker);
    }

    /* The image is viewable but IM reports cHRM warnings */
    @Test
    public void testInvalidcHRMpoint() throws Exception {
        Resource testImage = new ClassPathResource("/examples/invalid.png");
        Poker mockPoker = mock(Poker.class);
        ConvertResultChecker convertResultChecker = new ConvertResultChecker(mockPoker);
        CorruptionValidator validator = new CorruptionValidator(tempFolder.newFolder().toString(), convertResultChecker);
        assertTrue(validator.validate(testImage.getURL(), null));
        verifyZeroInteractions(mockPoker);
    }

    @Test
    public void testInvalidAdobeColor2() throws Exception {
        Resource testImage = new ClassPathResource("/examples/unknown_adobe_color.jpg");
        Poker mockPoker = mock(Poker.class);
        ConvertResultChecker convertResultChecker = new ConvertResultChecker(mockPoker);
        CorruptionValidator validator = new CorruptionValidator(tempFolder.newFolder().toString(), convertResultChecker);
        assertTrue(validator.validate(testImage.getURL(), null));
        verifyZeroInteractions(mockPoker);
    }

}
