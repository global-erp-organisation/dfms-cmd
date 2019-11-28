package com.expedia.content.media.processing.pipeline.validation;

import java.net.URL;
import java.util.List;

import com.expedia.content.media.processing.pipeline.domain.Image;
import com.expedia.content.media.processing.pipeline.domain.ImageMessage;

/**
 * ImageValidator that verifies if the width or height is greater than the minimal accepted size.
 * If either side is equal or greater than the minimal size the image is considered valid.
 */
public class MinSizeImageValidator implements ImageValidator {

    private final int minSize;

    private List<String> providerWhileList;
    private String clientId;

    public MinSizeImageValidator(int minSize) {
        this.minSize = minSize;
    }

    public void setProviderWhileList(List<String> providerWhileList) {
        this.providerWhileList = providerWhileList;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    @Override
    public boolean validate(URL imageUrl, ImageMessage imageMessage) {
        Image image = buildImage(imageUrl);
        if (image == null) {
            return false;
        }
        if (isProviderWhiteListed(imageMessage) || isClientIdWhiteListed(imageMessage)) {
            return true;
        }
        if (image.getHeight() >= minSize || image.getWidth() >= minSize) {
            return true;
        }
        return false;
    }

    /**
     * if the provider is in the whitelist the verification is ignored
     * 
     * @param imageMessage
     * @return
     */
    private boolean isProviderWhiteListed(ImageMessage imageMessage) {
        return providerWhileList != null && imageMessage.getOuterDomainData() != null
                && providerWhileList.contains(imageMessage.getOuterDomainData().getProvider());
    }

    /**
     * if the clientId is in the whitelist the verification is ignored
     * 
     * @param imageMessage
     * @return
     */
    private boolean isClientIdWhiteListed(ImageMessage imageMessage) {
        return clientId != null && imageMessage.getClientId() != null && imageMessage.getClientId().equals(clientId);
    }

}
