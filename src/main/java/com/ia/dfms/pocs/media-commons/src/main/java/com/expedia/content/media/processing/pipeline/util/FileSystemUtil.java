package com.expedia.content.media.processing.pipeline.util;

import javax.imageio.IIOException;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.awt.image.BufferedImage;
import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.security.NoSuchAlgorithmException;
import java.util.Iterator;

/**
 * Contains file system utilities used for handling files internally.
 */
public final class FileSystemUtil {
    
    private static final int[] JPEG_EOF_MARKER = {-1, -39};
    
    private FileSystemUtil() {
        /* Static class */
    }
    
    /**
     * Convert a URL to a File object if possible.
     *
     * @param uri URL that points to a file like: {@code file://}
     * @return File object represented by the URL
     * @throws java.net.MalformedURLException if the the URL is malformed.
     * @throws java.lang.NullPointerException if the URL is null.
     * @throws java.lang.IllegalArgumentException if the URL argument cannot be read as a File path.
     */
    public static File uriToFile(String uri) throws MalformedURLException {
        File file;
        URL url = new URL(uri);
        try {
            file = new File(url.toURI());
        } catch (URISyntaxException e) {
            file = new File(url.getPath());
        }
        return file;
    }
    
    /**
     * Copies the source file to the destination file and then it compares the size of the destination file to make sure
     * the same number of bytes was copied.
     *
     * @param source Source file to copy
     * @param destination Destination file
     * @return Number of bytes copied or 0 if the destination is full (in case of windows file shares)
     * @throws IOException if the copy fails. More details in the exception message
     * @throws java.lang.RuntimeException If the size check on the destination fails. Meaning the number of bytes on source and destination is not the
     *             same.
     */
    public static long copyFile(File source, File destination) throws IOException {
        Files.copy(source.toPath(), destination.toPath(), StandardCopyOption.REPLACE_EXISTING);
        long sourceSize = Files.size(source.toPath());
        long destSize = Files.size(destination.toPath());
        if (sourceSize != destSize) {
            throw new RuntimeException(String.format("Copy from '%s' to '%s' did not complete successfully. Expected: %d bytes; Copied: %d bytes copied.",
                    source.getPath(), destination.getPath(), sourceSize, destSize));
        }
        return destSize;
    }
    
    /**
     * Analyse a file to check if it is an image and it's not corrupted.
     *
     * @param file to be checked
     * @return ImageAnalysisResult where one could check if the file isImage and isTruncated
     * @throws NoSuchAlgorithmException
     * @throws IOException
     * @see <a href="http://stackoverflow.com/questions/8039444/how-to-detect-corrupted-images-png-jpg-in-java">Source</a>
     */
    public static ImageAnalysisResult analyzeImage(final Path file)
            throws NoSuchAlgorithmException, IOException {
            
        final ImageAnalysisResult result = new ImageAnalysisResult();
        
        try (InputStream digestInputStream = Files.newInputStream(file)) {
            
            final ImageInputStream imageInputStream = ImageIO.createImageInputStream(digestInputStream);
            final Iterator<ImageReader> imageReaders = ImageIO.getImageReaders(imageInputStream);
            
            if (!imageReaders.hasNext()) {
                result.setImage(false);
                return result;
            }
            final ImageReader imageReader = imageReaders.next();
            imageReader.setInput(imageInputStream);
            final BufferedImage image = imageReader.read(0);
            if (image == null) {
                return result;
            }
            image.flush();
            if ("JPEG".equals(imageReader.getFormatName())) {
                imageInputStream.seek(imageInputStream.getStreamPosition() - 2);
                final byte[] lastTwoBytes = new byte[2];
                imageInputStream.read(lastTwoBytes);
                if (lastTwoBytes[0] != JPEG_EOF_MARKER[0] && lastTwoBytes[1] != JPEG_EOF_MARKER[1]) {
                    result.setTruncated(true);
                } else {
                    result.setTruncated(false);
                }
            }
            result.setImage(true);
        } catch (final IndexOutOfBoundsException e) {
            result.setTruncated(true);
        } catch (final IIOException e) {
            if (e.getCause() instanceof EOFException) {
                result.setTruncated(true);
            }
        }
        return result;
    }
    
    /**
     * Response object for an image analysis.
     */
    public static class ImageAnalysisResult {
        private boolean image;
        private boolean truncated;
        
        public boolean isImage() {
            return image;
        }
        
        public void setImage(final boolean image) {
            this.image = image;
        }
        
        public boolean isTruncated() {
            return truncated;
        }
        
        public void setTruncated(final boolean truncated) {
            this.truncated = truncated;
        }
    }
    
    /**
     * Convert a file size in Kb.
     * 
     * @param fileSize size of the file.
     * @return size in Kb.
     */
    public static int getFileSizeKb(int fileSize) {
        final double dfileSize = (double) fileSize / 1024;
        final BigDecimal bigDecimal = new BigDecimal(new Double(dfileSize).toString()).setScale(0, BigDecimal.ROUND_HALF_UP);
        return bigDecimal.intValue();
    }
    
}
