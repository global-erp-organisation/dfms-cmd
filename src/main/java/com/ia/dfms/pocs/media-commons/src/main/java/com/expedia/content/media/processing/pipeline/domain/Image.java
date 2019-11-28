package com.expedia.content.media.processing.pipeline.domain;

import com.expedia.content.media.processing.pipeline.exception.ImageInfoException;
import com.expedia.content.media.processing.pipeline.util.FormattedLogger;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.Validate;
import org.im4java.core.ExiftoolCmd;
import org.im4java.core.IM4JavaException;
import org.im4java.core.Info;
import org.im4java.core.InfoException;
import org.im4java.core.Operation;
import org.im4java.process.OutputConsumer;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.FileSystemNotFoundException;
import java.nio.file.Paths;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

/**
 * Holds the image file URL, file name, and the dimensions of the image (retrieved through ImageMagick's identify command).
 */
public class Image {
    private static final FormattedLogger LOGGER = new FormattedLogger(Image.class);
    //if meta data property length is bigger than 1024, skip it
    private static final int PROPERTY_LIMIT = 1024;
    /* @formatter:off */
    private static final List<String> BASIC_PROPERTY_NAMES = Lists.newArrayList(
          "Image",
          "Filesize",
          "Mime type",
          "Geometry",
          "Colorspace",
          "Depth",
          "Quality",
          "Gamma",
          "Interlace"
    );
    /* @formatter:on */
    private static final String PROPERTY_COLORSPACE = "Colorspace";
    private static final String PROPERTY_MIMETYPE = "Mime type";

    private final String path;
    private final Info imageInfo;
    private final int height;
    private final int width;

    public Image(final URL url) throws URISyntaxException {
        Validate.notNull(url, "URL can't be null");
        try {
            File imageFile = Paths.get(url.toURI()).toFile();
            if (!imageFile.exists()) {
                throw new FileSystemNotFoundException("File not found: " + imageFile.getAbsolutePath());
            }
            this.path = imageFile.getAbsolutePath();
            this.imageInfo = new Info(this.path);
            this.width = imageInfo.getImageWidth();
            this.height = imageInfo.getImageHeight();
        } catch (InfoException e) {
            throw new ImageInfoException(url, "Can't retrieve image information from file: " + url, e);
        }
    }

    public String getPath() {
        return this.path;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public String getColorSpace() {
        return imageInfo.getProperty(PROPERTY_COLORSPACE);
    }

    public String getMimeType() {
        return imageInfo.getProperty(PROPERTY_MIMETYPE);
    }

    public Map<String, String> getMetadata() {
        Map<String, String> metadata = new TreeMap<>();
        Enumeration<String> propertyNames = imageInfo.getPropertyNames();
        while (propertyNames.hasMoreElements()) {
            String propertyName = propertyNames.nextElement();
            if (BASIC_PROPERTY_NAMES.contains(propertyName)) {
                metadata.put(propertyName, imageInfo.getProperty(propertyName));
            }
        }
        initMetaByExifCmd(metadata);
        return metadata;
    }

    /**
     * get the meta data by cmd 'exif -s image.jpg'
     * @param metaMap
     */
    private void initMetaByExifCmd(Map<String, String> metaMap) {
        File imageFile = new File(path);
        String outPut = executeCommand(imageFile);
        String[] line = outPut.split("\n");
        if (line != null && line.length > 0) {
            for (int i = 0; i < line.length; i++) {
                if (line[i].contains(":")) {
                    String[] subLine = line[i].split(":");
                    if (subLine[1].trim().getBytes().length < PROPERTY_LIMIT) {
                        metaMap.put(subLine[0].trim(), subLine[1].trim());
                    }
                }
            }
        }
    }

    private static String executeCommand(File imageFile) {
        final ExiftoolCmd exifTool = new ExiftoolCmd();
        final ConsoleConsumer exifConsumer = new ConsoleConsumer();
        exifTool.setOutputConsumer(exifConsumer);

        final Operation readTags = new Operation();
        readTags.addRawArgs("-s");
        readTags.addRawArgs("-ignoreMinorErrors");
        readTags.addImage(imageFile.getAbsolutePath());
        try {
            exifTool.run(readTags);
            if (exifTool.getErrorText().size() > 0) {
                LOGGER.warn("Exiftool Errors Error={}", Joiner.on("\n").join(exifTool.getErrorText()));
            }
            return exifConsumer.getOutput();
        } catch (IOException | InterruptedException | IM4JavaException e) {
            LOGGER.warn(e, "Unable to extract tags Image={}", imageFile.getAbsolutePath());
        }
        return "";
    }

    /**
     * String representation of the verbose image data.
     */
    @Override
    public String toString() {
        Map<String, String> metadata = getMetadata();
        return metadata.keySet().stream().filter(key -> BASIC_PROPERTY_NAMES.contains(key)).map(key -> key + ":" + metadata.get(key))
                .collect(Collectors.joining(System.lineSeparator(), "ImageInfo {", "}"));
    }
}

class ConsoleConsumer implements OutputConsumer {
    private String output = "";

    @Override
    public void consumeOutput(InputStream inputStream) throws IOException {
        List list = IOUtils.readLines(inputStream);
        output += Joiner.on("\n").join(list);
    }

    public String getOutput() {
        return output;
    }
}
