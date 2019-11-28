package com.expedia.content.media.processing.pipeline.util;

import com.expedia.content.media.processing.pipeline.exception.FileNotReadyException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

/**
 * <p>This represents a temporary folder for intermediate operations.</p>
 *
 * <p>This folder will self delete when it's closed. It can be used safely withing a try () catch construct and it will
 * clean up after itself.</p>
 *
 */
public class TemporaryWorkFolder implements AutoCloseable {

    public static final String TEMP_WORK_FOLDER_PREFIX = "work-folder-";

    private final Path tempWorkPath;

    public TemporaryWorkFolder(Path tempRootPath) {
        if (tempRootPath.isAbsolute() && Files.isDirectory(tempRootPath) && Files.isWritable(tempRootPath)) {
            this.tempWorkPath = createTempWorkFolder(tempRootPath);
        } else {
            throw new IllegalArgumentException("Invalid root path: " + tempRootPath.toString());
        }
    }

    public Path getWorkPath() {
        return tempWorkPath;
    }

    public Path copyInto(Path source) throws IOException {
        return Files.copy(source, Paths.get(tempWorkPath.toString(), source.getFileName().toString()), StandardCopyOption.REPLACE_EXISTING);
    }

    @Override
    public void close() throws Exception {
        if (!deleteRecursive(tempWorkPath)) {
            throw new IOException("Could not fully delete work folder: " + tempWorkPath.toAbsolutePath());
        }
    }

    private static Path createTempWorkFolder(Path rootPath) {
        try {
            return Files.createTempDirectory(rootPath, TEMP_WORK_FOLDER_PREFIX);
        } catch (IOException ioe) {
            throw new FileNotReadyException("Creating work folder failed", ioe);
        }
    }

    private static boolean deleteRecursive(Path toDelete) {
        boolean result = true;
        for (File item : toDelete.toFile().listFiles()) {
            if (item.isDirectory()) {
                result &= deleteRecursive(item.toPath());
            } else {
                result &= item.delete();
            }
        }
        return result && toDelete.toFile().delete();
    }
}
