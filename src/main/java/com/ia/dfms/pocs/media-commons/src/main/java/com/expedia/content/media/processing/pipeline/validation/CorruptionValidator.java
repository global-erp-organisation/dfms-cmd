package com.expedia.content.media.processing.pipeline.validation;

import java.io.File;
import java.net.URL;
import java.nio.file.Paths;

import org.apache.commons.io.FilenameUtils;
import org.im4java.core.ConvertCmd;
import org.im4java.core.IMOperation;

import com.expedia.content.media.processing.pipeline.domain.ImageMessage;
import com.expedia.content.media.processing.pipeline.domain.MessageConstants;
import com.expedia.content.media.processing.pipeline.util.ConvertResultChecker;
import com.expedia.content.media.processing.pipeline.util.FormattedLogger;
import com.expedia.content.media.processing.pipeline.util.TemporaryWorkFolder;

/**
 * Verifies if an image is corrupted or not. Uses the ImageMagik convert command to asses if the image is corrupted or not.
 * The image is considered invalid if there are any errors returned from the convert command call.
 */
public class CorruptionValidator implements ImageValidator {

    private static final FormattedLogger LOGGER = new FormattedLogger(CorruptionValidator.class);
    private final String tempWorkFolderPath;
    private final ConvertResultChecker resultChecker;

    public CorruptionValidator(String tempWorkFolderPath, ConvertResultChecker resultChecker) {
        this.tempWorkFolderPath = tempWorkFolderPath;
        this.resultChecker = resultChecker;
    }

    @Override
    public boolean validate(URL imageUrl, ImageMessage imageMessage) {
        ConvertCmd convert = new ConvertCmd();
        try (TemporaryWorkFolder workFolder = new TemporaryWorkFolder(Paths.get(tempWorkFolderPath))) {
            String testImagePath = imageUrl.getPath();
            File taintWorkFile = new File(workFolder.getWorkPath().toString(), FilenameUtils.getName(testImagePath));
            IMOperation op = new IMOperation();
            op.limit("memory");
            op.addRawArgs("32MB");
            op.taint();
            op.addImage(testImagePath);
            op.addImage(taintWorkFile.toString());
            convert.run(op);
            return !resultChecker.hasCommandResultErrors(convert.getErrorText());
        } catch (Exception e) {
            LOGGER.warn(e, "Failed to validate image corruption", imageMessage, MessageConstants.CLIENT_ID, MessageConstants.REQUEST_ID,
                    MessageConstants.MEDIA_ID, MessageConstants.FILE_URL);
            return false;
        }
    }


}
