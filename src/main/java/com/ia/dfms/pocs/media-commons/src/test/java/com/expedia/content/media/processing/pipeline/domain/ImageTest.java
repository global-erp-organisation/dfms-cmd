package com.expedia.content.media.processing.pipeline.domain;

import com.expedia.content.media.processing.pipeline.exception.ImageInfoException;
import com.expedia.content.media.processing.pipeline.util.FormattedLogger;
import com.expedia.content.media.processing.pipeline.util.FileSystemUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.FileSystemNotFoundException;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.im4java.core.Info;
import org.im4java.core.InfoException;
import org.joda.time.DateTime;
import org.joda.time.Period;
import org.joda.time.format.PeriodFormat;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.junit.Assert.assertFalse;

public class ImageTest {
    
    private static final FormattedLogger LOGGER = new FormattedLogger(ImageTest.class);

    @Test
    public void testImage_constructor() throws URISyntaxException {
        final URL url = ImageTest.class.getResource("/examples/office.jpg");
        final Image image = new Image(url);
        assertEquals(720, image.getWidth());
        assertEquals(540, image.getHeight());
        assertEquals("sRGB", image.getColorSpace());
        assertTrue(image.getPath().endsWith("office.jpg"));
    }

    @Test
    public void testImageMetaData() throws URISyntaxException {
        final URL url = ImageTest.class.getResource("/examples/office.jpg");
        final Image image = new Image(url);
        Map<String, String> meta = image.getMetadata();
        assertEquals("720", meta.get("ImageWidth"));
        assertEquals("540", meta.get("ImageHeight"));
    }

    /**
     * meta_xml.jpg contain xml format meta data
     * and getMetadata does not contain xml
     * @throws URISyntaxException
     */
    @Test
    public void testImageMetaDataXml() throws URISyntaxException {
        final URL url = ImageTest.class.getResource("/examples/meta_xml.jpg");
        final Image image = new Image(url);
        Map<String, String> meta = image.getMetadata();
        for (Map.Entry<String, String> entry : meta.entrySet()) {
            String key = entry.getKey();
            assertTrue(!key.contains("xml"));
        }
    }

    @Test
    public void testImageMetaWithBigProperty() throws URISyntaxException {
        final URL url = ImageTest.class.getResource("/examples/2850d317-b52d-43a4-8db5-52eb41283d76.jpg");
        final Image image = new Image(url);
        Map<String, String> meta = image.getMetadata();
        for (Map.Entry<String, String> entry : meta.entrySet()) {
            String key = entry.getKey();
            assertTrue(!key.contains("Document Ancestors"));
        }
    }

    @Test(expected = ImageInfoException.class)
    @Ignore("New identify is much more lenient with corrupted images. Find another corrupted image")
    public void testImage_corrupted() throws URISyntaxException {
        final URL url = ImageTest.class.getResource("/examples/corrupt_jpeg_data.jpg");
        new Image(url);
    }

    @Test(expected = ImageInfoException.class)
    public void test0kbImage() throws URISyntaxException {
        final URL url = ImageTest.class.getResource("/examples/ZeroKbImage.jpg");
        new Image(url);
    }

    @Test(expected = FileSystemNotFoundException.class)
    public void testFileNotFoundException() throws Exception {
        new Image(new URL("file:/this/file/doesnt/exist.jpg"));
    }

    @Test(expected = InfoException.class)
    public void test0kbInfo() throws InfoException, URISyntaxException {
        final URL url = ImageTest.class.getResource("/examples/ZeroKbImage.jpg");
        String imagePath = Paths.get(url.toURI()).toFile().getAbsolutePath();
        new Info(imagePath);
    }

    @Test
    public void testCMYKImageColorSpace() throws URISyntaxException {
        final URL url = ImageTest.class.getResource("/examples/80_100_image_cmyk_1.jpg");
        Image image = new Image(url);

        assertEquals("CMYK", image.getColorSpace());
        assertEquals(100, image.getHeight());
        assertEquals(80, image.getWidth());
    }

    @Test
    public void testImageInfoBasicProperties() throws Exception {
        Resource testImage = new ClassPathResource("/examples/80_100_image_cmyk_1.jpg");
        Image image = new Image(testImage.getURL());
        String imageInfo = image.toString();
        assertTrue(imageInfo.startsWith("ImageInfo {"));
        assertTrue(imageInfo.endsWith("}"));
        assertTrue(imageInfo.contains("80_100_image_cmyk_1.jpg"));
        assertTrue(imageInfo.contains("Filesize:10.3KB"));
        assertTrue(imageInfo.contains("Mime type:image/jpeg"));
        assertTrue(imageInfo.contains("Geometry:80x100+0+0"));
        assertTrue(imageInfo.contains("Colorspace:CMYK"));
        assertTrue(imageInfo.contains("Depth:8-bit"));
        assertTrue(imageInfo.contains("Quality:92"));
        assertTrue(imageInfo.contains("Gamma:0.454545"));
        assertTrue(imageInfo.contains("Interlace:None"));
        assertFalse(imageInfo.contains("Number pixels:"));
        assertFalse(imageInfo.contains("Tainted:"));
    }

    @Ignore
    @Test
    public void test_truncated_file() throws IOException, NoSuchAlgorithmException, InterruptedException {
        
        final String filename = "/examples/lodgingpics/5999825/5999825_10.jpg";
        final File outFile = File.createTempFile("mpp-junit-", ".jpg");
        LOGGER.info("Temporary output image Image={}", outFile);
        
        Thread t1 = new Thread() {
            public void run() {
                try (final InputStream inputStream = getClass().getResourceAsStream(filename);
                        final FileOutputStream outputStream = new FileOutputStream(outFile)) {
                    
                    DateTime start = new DateTime();
                    
                    int b;
                    while ((b = inputStream.read()) >= 0) {
                        outputStream.write(b);
                    }
                    
                    final Period elapsedPeriod = new Period(start, new DateTime());
                    LOGGER.info("File copy took {}", PeriodFormat.getDefault().print(elapsedPeriod));
                } catch (IOException e) {
                    LOGGER.error(e, "Can't copy the source file");
                    fail(e.getMessage());
                }
            }
        };
        t1.start();
        
        TimeUnit.MILLISECONDS.sleep(100);
        final FileSystemUtil.ImageAnalysisResult imageAnalysisResult = FileSystemUtil.analyzeImage(outFile.toPath());
        LOGGER.info("Is file image={} truncated={} ?", imageAnalysisResult.isImage(), imageAnalysisResult.isTruncated());
        assertTrue(imageAnalysisResult.isImage());
        assertTrue(imageAnalysisResult.isTruncated());
    }
}
