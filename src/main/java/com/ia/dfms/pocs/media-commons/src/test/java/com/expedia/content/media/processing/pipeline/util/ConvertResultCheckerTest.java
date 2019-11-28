package com.expedia.content.media.processing.pipeline.util;

import static com.expedia.content.media.processing.pipeline.util.TestingUtil.setFieldValue;
import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;

import java.lang.reflect.Field;

import org.im4java.process.ProcessStarter;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.mockito.Mockito;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import com.expedia.content.media.processing.pipeline.validation.CorruptionValidator;

public class ConvertResultCheckerTest {

    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();

    @BeforeClass
    public static void classSetUp() {
        final String path = System.getenv("PATH").replace('\\', '/');
        ProcessStarter.setGlobalSearchPath(path);
    }

    @Test
    public void testCorruptedImageWithKnownError() throws Exception {
        Resource testImage = new ClassPathResource("/examples/corrupted.jpg");
        ConvertResultChecker checker = new ConvertResultChecker();
        CorruptionValidator validator = new CorruptionValidator(tempFolder.newFolder().toString(), checker);
        assertFalse(validator.validate(testImage.getURL(), null));
    }

    @Test
    public void testCorruptedImageWithUnknownError() throws Exception {
        Resource testImage = new ClassPathResource("/examples/corrupted.jpg");
        ConvertResultChecker checker = new ConvertResultChecker();
        final Field field = checker.getClass().getDeclaredField("knownErrors");
        field.setAccessible(true);
        final String[] value = {".+ Corrupt JPEG data: .+ extraneous bytes before marker .+"};
        field.set(checker, value);
        CorruptionValidator validator = new CorruptionValidator(tempFolder.newFolder().toString(), checker);
        assertFalse(validator.validate(testImage.getURL(), null));
    }

    @Test
    public void testKnownWarningImage() throws Exception {
        Resource testImage = new ClassPathResource("/examples/warningSOS.jpg");
        ConvertResultChecker checker = new ConvertResultChecker();
        CorruptionValidator validator = new CorruptionValidator(tempFolder.newFolder().toString(), checker);
        assertTrue(validator.validate(testImage.getURL(), null));
    }

    @Test
    public void testCleanImage() throws Exception {
        Resource testImage = new ClassPathResource("/examples/office.jpg");
        ConvertResultChecker checker = new ConvertResultChecker();
        CorruptionValidator validator = new CorruptionValidator(tempFolder.newFolder().toString(), checker);
        assertTrue(validator.validate(testImage.getURL(), null));
    }

    @Test
    public void testCorruptedImageWithKnownErrorTwoSOFMarkers() throws Exception {
        Resource testImage = new ClassPathResource("/examples/corrupted_twoSOF.jpg");
        ConvertResultChecker checker = new ConvertResultChecker();
        Poker poker = mock(Poker.class);
        setFieldValue(checker, "poker", poker);
        CorruptionValidator validator = new CorruptionValidator(tempFolder.newFolder().toString(), checker);
        assertFalse(validator.validate(testImage.getURL(), null));
        Mockito.verify(poker, times(0)).poke(eq("New ImageMagick Error Message"), eq(null), Mockito.anyString(), Mockito.any(Exception.class));
    }

    @Test
    public void testCorruptedImageWithKnownErrorTwoSOIMarkers() throws Exception {
        Resource testImage = new ClassPathResource("/examples/corrupted_twoSOI.jpg");
        ConvertResultChecker checker = new ConvertResultChecker();
        Poker poker = mock(Poker.class);
        setFieldValue(checker, "poker", poker);
        CorruptionValidator validator = new CorruptionValidator(tempFolder.newFolder().toString(), checker);
        assertFalse(validator.validate(testImage.getURL(), null));
        Mockito.verify(poker, times(0)).poke(eq("New ImageMagick Error Message"), eq(null), Mockito.anyString(), Mockito.any(Exception.class));
    }
}
