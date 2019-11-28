package com.expedia.content.media.processing.pipeline.util;

import com.expedia.content.media.processing.pipeline.util.FileSystemUtil.ImageAnalysisResult;
import org.joda.time.DateTime;
import org.joda.time.Period;
import org.joda.time.format.PeriodFormat;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class FileSystemUtilTest {

    private static final FormattedLogger LOGGER = new FormattedLogger(FileSystemUtilTest.class);

    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();

    @Test
    public void testDeleteUrlFileUnix() throws Exception {
        File file = tempFolder.newFile("test.jpg");

        String uri = "file:///" + file.getAbsolutePath().replace('\\', '/');

        assertTrue(FileSystemUtil.uriToFile(uri).delete());
        assertFalse(FileSystemUtil.uriToFile(uri + "u").delete());
    }

    @Ignore
    @Test
    public void testDeleteFileUnix() throws Exception {
        File file = tempFolder.newFile("test.png");

        String uri = "file:/" + file.getAbsolutePath().replace('\\', '/');

        assertTrue(FileSystemUtil.uriToFile(uri).delete());
        assertFalse(FileSystemUtil.uriToFile(uri + "u").delete());
    }

    @Ignore
    @Test
    public void testDeleteSpacesEncoded() throws Exception {
        File newFolder = tempFolder.newFolder("Folder with spaces");
        File fileSpacesRaw = File.createTempFile("img", ".jpg", newFolder);

        String uri = "file:/" + fileSpacesRaw.getAbsolutePath().replace('\\', '/');
        assertTrue(FileSystemUtil.uriToFile(uri).delete());

        File fileSpacesEncoded = File.createTempFile("img", ".jpeg", newFolder);
        uri = "file:///" + fileSpacesEncoded.getAbsolutePath().replace('\\', '/').replace(" ", "%20");
        assertTrue(FileSystemUtil.uriToFile(uri).delete());
    }

    @Ignore
    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArgumentException() throws Exception {
        File file = tempFolder.newFile("test.png");
        String uri = "file://" + file.getAbsolutePath().replace('\\', '/');
        FileSystemUtil.uriToFile(uri);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testHttpUrlCannotBeConvertedToAFile() throws Exception {
        String uri = "https://whatever.com/path/to/some/file.jpg";
        FileSystemUtil.uriToFile(uri);
    }

    @Test(expected = MalformedURLException.class)
    public void testMalformedUrl() throws Exception {
        File file = tempFolder.newFile("test.png");
        String uri = "fil:///" + file.getAbsolutePath().replace('\\', '/');
        FileSystemUtil.uriToFile(uri);
    }

    @Test
    public void testAnalysisForCorruptedFile() throws Exception {
        URL filename = getClass().getResource("/examples/corrupted.jpg");
        ImageAnalysisResult result = FileSystemUtil.analyzeImage(Paths.get(filename.toURI()));
        assertTrue(result.isImage());
        assertTrue(result.isTruncated());
    }

    @Test
    public void testFileAnalysis() throws IOException, NoSuchAlgorithmException, InterruptedException {

        final String filename = "/examples/office.jpg";
        final File outFile = File.createTempFile("mpp-junit-", ".jpg");
        LOGGER.info("Temporary output image={}", outFile);

        Thread t1 = new Thread() {
            public void run() {
                try (
                        final InputStream inputStream = getClass().getResourceAsStream(filename);
                        final FileOutputStream outputStream = new FileOutputStream(outFile)) {

                    DateTime start = new DateTime();

                    int b;
                    while ((b = inputStream.read()) >= 0) {
                        outputStream.write(b);
                    }

                    final Period elapsedPeriod = new Period(start, new DateTime());
                    LOGGER.info("File copy took={}", PeriodFormat.getDefault().print(elapsedPeriod));
                } catch (IOException e) {
                    LOGGER.error("Can't copy the source file={}", e.getMessage());
                    fail(e.getMessage());
                }
            }
        };

        Thread t2 = new Thread() {
            @Override
            public void run() {
                try {
                    while (FileSystemUtil.analyzeImage(outFile.toPath()).isTruncated()) {
                        LOGGER.info("Outfile is not ready yet...");
                        TimeUnit.MILLISECONDS.sleep(1000);
                    }
                } catch (NoSuchAlgorithmException | IOException | InterruptedException e) {
                    LOGGER.error("Can't check the output file={}", e.getMessage());
                    fail(e.getMessage());
                }
            }
        };

        t1.start();
        t2.start();

        t2.join(TimeUnit.SECONDS.toMillis(10));
    }
}
