package com.expedia.content.media.processing.pipeline.validation;

import java.net.URL;
import java.nio.charset.Charset;

import com.expedia.content.media.processing.pipeline.domain.ImageMessage;

/**
 * Validate that images only contain Ascii characters
 */
public class OnlyAsciiFileNamesImageValidator implements ImageValidator {

    @Override public boolean validate(URL imageUrl, ImageMessage imageMessage) {
        return new String(imageUrl.getPath().getBytes(), Charset.forName("US-ASCII")).equals(imageUrl.getPath());
    }
}
