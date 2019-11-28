package com.expedia.content.media.processing.pipeline.validation;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;
import static org.mockito.Mockito.mock;

import java.net.MalformedURLException;
import java.net.URL;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.expedia.content.media.processing.pipeline.domain.Image;

@RunWith(MockitoJUnitRunner.class)
public class OnlyAsciiFileNamesImageValidatorTest {

    @Mock
    private final Image mockImage = mock(Image.class);

    private final OnlyAsciiFileNamesImageValidator validator = new OnlyAsciiFileNamesImageValidator();

    @Test(expected = NullPointerException.class)
    public void testNPE() {
        validator.validate(null, null);
    }

    @Test
    public void testValidPath() throws MalformedURLException {
        URL url = new URL("file://this/is/a/valid/path.jpg");
        assertTrue(validator.validate(url, null));
    }

    @Test
    public void testInvalidPathExtendedAscii() throws MalformedURLException {
        URL url = new URL("file://this/is/an/extended/ascii/path-çà.jpg");
        assertFalse(validator.validate(url, null));
    }

    @Test
    public void testInvalidPathUnicode() throws MalformedURLException {
        URL url = new URL("file://this/is/unicode/path-主要照片.jpg");
        assertFalse(validator.validate(url, null));
    }

    @Test
    public void testInvalidAsciiCharacters() throws MalformedURLException {
        URL url = new URL("file://this/is/invalid/ascii/path-&.jpg");
        assertTrue(validator.validate(url, null));

        url = new URL("file://this/is/invalid/ascii/path-^.jpg");
        assertTrue(validator.validate(url, null));

        url = new URL("file://this/is/invalid/ascii/path- -.jpg");
        assertTrue(validator.validate(url, null));
    }
}
