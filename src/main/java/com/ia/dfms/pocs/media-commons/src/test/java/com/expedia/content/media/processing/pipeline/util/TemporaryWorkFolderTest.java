package com.expedia.content.media.processing.pipeline.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public class TemporaryWorkFolderTest {

    private static final String PATH_SEPARATOR = System.getProperty("file.separator");

    private static final URL VALID_IMAGE = TemporaryWorkFolderTest.class.getResource("/examples/office.jpg");
    private static final URL VALID_IMAGE2 = TemporaryWorkFolderTest.class.getResource("/examples/las-vegas.jpg");

    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();

    private File workRoot;

    @Before
    public void checkRootCreated() throws IOException {
        workRoot = tempFolder.newFolder("work");
        if (!workRoot.exists()) {
            assertTrue(workRoot.mkdir());
        }
        assertEquals(0, workRoot.list().length);
    }

    @After
    public void makeSureRootDeleted() {
        assertEquals(0, workRoot.list().length);
        assertTrue(workRoot.delete());
    }

    @Test
    public void createDestroyFolder() {
        try (TemporaryWorkFolder workfolder = new TemporaryWorkFolder(Paths.get(workRoot.getAbsolutePath()))) {
            assertEquals(1, workRoot.list().length);
            assertTrue(workfolder.getWorkPath().toFile().exists());
            assertEquals(0, workfolder.getWorkPath().toFile().list().length);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void createCopyFileAndAutoCleanup() {
        try (TemporaryWorkFolder workfolder = new TemporaryWorkFolder(Paths.get(workRoot.getAbsolutePath()))) {
            System.out.println(workfolder.getWorkPath().toString());
            Path copied = workfolder.copyInto(Paths.get(VALID_IMAGE.toURI()));
            assertNotNull(copied);
            assertTrue(Files.exists(copied));
            assertEquals(1, workfolder.getWorkPath().toFile().list().length);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void createNestedFoldersAndAutoCleanup() {
        try (TemporaryWorkFolder workfolder = new TemporaryWorkFolder(Paths.get(workRoot.getAbsolutePath()))) {
            System.out.println(workfolder.getWorkPath().toString());
            workfolder.copyInto(Paths.get(VALID_IMAGE.toURI()));
            workfolder.copyInto(Paths.get(VALID_IMAGE2.toURI()));
            File newFolder = new File(workfolder.getWorkPath().toAbsolutePath() + PATH_SEPARATOR + "test1");
            assertFalse(newFolder.exists());
            assertTrue(newFolder.mkdir());
            assertEquals(3, workfolder.getWorkPath().toFile().list().length);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testNewFilename() {
        try (TemporaryWorkFolder workfolder = new TemporaryWorkFolder(Paths.get(workRoot.getAbsolutePath()))) {
            System.out.println(workfolder.getWorkPath().toString());
            workfolder.getWorkPath().toFile();
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

}
