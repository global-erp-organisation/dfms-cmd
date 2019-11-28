package com.expedia.content.media.processing.pipeline.util;

import com.expedia.content.media.processing.pipeline.exception.ImageValidationException;
import com.google.common.base.Joiner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Convert command result checker. Used to determine if the command result has benign errors or not. Pokes
 * when an error isn't recognized.
 */
@Component
public class ConvertResultChecker {

    private static final FormattedLogger LOGGER = new FormattedLogger(ConvertResultChecker.class);
    private static final String IM_ERROR_POKE_TITLE = "New ImageMagick Error Message";

    @Autowired
    private Poker poker;

    /*
     * TODO Once new Media DB is available change the checker to use the DB instead of these hard coded values. In the mean time
     * these are here instead of a configuration so as to not have to configure the collector and DCP with their own lists.
     */
    private String[] safeErrors = {".+ Invalid SOS parameters for sequential JPEG .+",
                                   ".+ (w|W)rong data type .+ for .+ tag ignored\\. .TIFF.+Directory.+",
                                   ".+ Incompatible type for .+ tag ignored\\. .TIFFFetchNormalTag.+",
                                   ".+ (u|U)nknown field with tag .+ encountered\\. .TIFF.+Directory.+",
                                   ".+ length and filesize do not match .+ReadBMPImage.+",
                                   ".+cHRM.+warning.+",
                                   ".+Unknown Adobe color transform code 2.+"};
    private String[] knownErrors = {".+ Premature end of JPEG file .+",
                                    ".+ Corrupt JPEG data: .+",
                                    ".+Unsupported marker type.+JPEGErrorHandler.+",
                                    ".+Bogus marker length.+",
                                    ".+convert: Invalid JPEG file structure: two SOF markers.+",
                                    ".+convert: Invalid JPEG file structure: two SOI markers.+"};

    public ConvertResultChecker() {

    }

    public ConvertResultChecker(Poker poker) {
        this.poker = poker;
    }

    /**
     * Scans the content the error text collection returned by the convert command.
     *
     * @param errorTexts List of error texts form the executed convert command.
     * @return {@code true} if the command has errors; {@code false} otherwise.
     */
    public boolean hasCommandResultErrors(List<String> errorTexts) {
        if (errorTexts.size() < 1) {
            return false;
        } else {
            boolean hasNonSafeErrors = false;
            List<String> unknownErrors = new ArrayList<>();
            for (String errorText : errorTexts) {
                if (!errorText.matches(Joiner.on('|').join((safeErrors)))) {
                    hasNonSafeErrors = true;
                }
                if (!errorText.matches(Joiner.on('|').join((knownErrors))) && !errorText.matches(Joiner.on('|').join((safeErrors)))) {
                    unknownErrors.add(errorText);
                }
            }
            if (unknownErrors.size() > 0) {
                String unknownErrorsMessage = Joiner.on('\n').join(unknownErrors);
                poker.poke(IM_ERROR_POKE_TITLE, new ImageValidationException(unknownErrorsMessage));
                LOGGER.warn(IM_ERROR_POKE_TITLE + "=" + unknownErrorsMessage);
            }
            return hasNonSafeErrors;
        }
    }
}
