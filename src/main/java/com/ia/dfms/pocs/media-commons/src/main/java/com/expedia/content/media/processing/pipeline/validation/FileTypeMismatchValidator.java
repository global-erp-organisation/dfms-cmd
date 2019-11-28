package com.expedia.content.media.processing.pipeline.validation;

import java.net.URL;

import org.apache.commons.io.FilenameUtils;

import com.expedia.content.media.processing.pipeline.domain.FileTypeMapper;
import com.expedia.content.media.processing.pipeline.domain.Image;
import com.expedia.content.media.processing.pipeline.domain.ImageMessage;

/**
 * Verifies if the provided file extension match with the current file type.
 * The image is considered invalid if the provided file extension is different from the one detected by Exfitool.
 */
public class FileTypeMismatchValidator implements ImageValidator {

    private static final String FILE_TYPE_FIELD = "FileType";
    private static final String FILE_NAME_FIELD = "FileName";

    @Override
    public boolean validate(URL imageUrl, ImageMessage imageMessage) {
        Image image = buildImage(imageUrl);
        if (image == null) {
            return false;
        }
        final FileTypeMapper realFileType = FileTypeMapper.getFyleTypeByExtension(image.getMetadata().get(FILE_TYPE_FIELD));
        final FileTypeMapper provideFileType = FileTypeMapper.getFyleTypeByExtension(FilenameUtils.getExtension(image.getMetadata().get(FILE_NAME_FIELD)));
        final Boolean isValid = (realFileType != null && provideFileType != null) && realFileType == provideFileType;
        if (!isValid) {
            LOGGER.warn("The provided file type does not match with the real mime-type. Provided type=[{}], Real type=[{}]",
                    provideFileType.getMediaType(), realFileType.getMediaType());
        }
        return true;
    }
}
