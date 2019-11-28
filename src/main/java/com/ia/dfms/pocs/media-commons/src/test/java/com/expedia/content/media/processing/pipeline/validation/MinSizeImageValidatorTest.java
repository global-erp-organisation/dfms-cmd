package com.expedia.content.media.processing.pipeline.validation;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import com.expedia.content.media.processing.pipeline.domain.ImageMessage;
import com.expedia.content.media.processing.pipeline.domain.OuterDomain;

public class MinSizeImageValidatorTest {

    @Test
    public void testValid() throws IOException {
        Resource testImage = new ClassPathResource("/examples/office.jpg");
        MinSizeImageValidator validator = new MinSizeImageValidator(10);
        assertTrue(validator.validate(testImage.getURL(), null));
    }

    @Test
    public void testValidHeightOnly() throws IOException {
        Resource testImage = new ClassPathResource("/examples/80_100_image_cmyk_1.jpg");
        MinSizeImageValidator validator = new MinSizeImageValidator(90);
        assertTrue(validator.validate(testImage.getURL(), null));
    }

    @Test
    public void testValidWidthOnly() throws IOException {
        Resource testImage = new ClassPathResource("/examples/office.jpg");
        MinSizeImageValidator validator = new MinSizeImageValidator(700);
        assertTrue(validator.validate(testImage.getURL(), null));
    }

    @Test
    public void testInvalid() throws IOException {
        Resource testImage = new ClassPathResource("/examples/office.jpg");
        MinSizeImageValidator validator = new MinSizeImageValidator(7000);
        assertFalse(validator.validate(testImage.getURL(), null));
    }

    @Test
    public void testProviderWhitelist() throws IOException {
        Resource testImage = new ClassPathResource("/examples/office.jpg");
        MinSizeImageValidator validator = new MinSizeImageValidator(7000);
        List<String> providerWhileList = new ArrayList<>();
        providerWhileList.add("SCORE");
        validator.setProviderWhileList(providerWhileList);
        OuterDomain.OuterDomainBuilder outerDomainBuilder = new OuterDomain.OuterDomainBuilder();
        OuterDomain outerDomain = outerDomainBuilder.mediaProvider("SCORE").build();
        ImageMessage.ImageMessageBuilder imageMessageBuilder = new ImageMessage.ImageMessageBuilder();
        imageMessageBuilder.outerDomainData(outerDomain);
        ImageMessage imageMessage = imageMessageBuilder.build();
        assertTrue(validator.validate(testImage.getURL(), imageMessage));
    }

    @Test
    public void testImageMessageWithOutProviderId() throws IOException {
        Resource testImage = new ClassPathResource("/examples/office.jpg");
        MinSizeImageValidator validator = new MinSizeImageValidator(7000);
        List<String> providerWhileList = new ArrayList<>();
        providerWhileList.add("SCORE");
        validator.setProviderWhileList(providerWhileList);
        OuterDomain.OuterDomainBuilder outerDomainBuilder = new OuterDomain.OuterDomainBuilder();
        OuterDomain outerDomain = outerDomainBuilder.build();
        ImageMessage.ImageMessageBuilder imageMessageBuilder = new ImageMessage.ImageMessageBuilder();
        imageMessageBuilder.outerDomainData(outerDomain);
        ImageMessage imageMessage = imageMessageBuilder.build();
        assertFalse(validator.validate(testImage.getURL(), imageMessage));
    }

    @Test
    public void testClientIdWhitelist() throws IOException {
        Resource testImage = new ClassPathResource("/examples/office.jpg");
        MinSizeImageValidator validator = new MinSizeImageValidator(7000);
        String clientId = "Media Cloud Router";
        validator.setClientId(clientId);
        ImageMessage.ImageMessageBuilder imageMessageBuilder = new ImageMessage.ImageMessageBuilder();
        imageMessageBuilder.clientId(clientId);
        ImageMessage imageMessage = imageMessageBuilder.build();
        assertTrue(validator.validate(testImage.getURL(), imageMessage));
    }

    @Test
    public void testClientIdNotWhitelist() throws IOException {
        Resource testImage = new ClassPathResource("/examples/office.jpg");
        MinSizeImageValidator validator = new MinSizeImageValidator(7000);
        String clientId = "Media Cloud Router";
        validator.setClientId(clientId);
        ImageMessage.ImageMessageBuilder imageMessageBuilder = new ImageMessage.ImageMessageBuilder();
        imageMessageBuilder.clientId("potato");
        ImageMessage imageMessage = imageMessageBuilder.build();
        assertFalse(validator.validate(testImage.getURL(), imageMessage));
    }

    @Test
    public void testProviderWhitelistAndClientId() throws IOException {
        Resource testImage = new ClassPathResource("/examples/office.jpg");
        MinSizeImageValidator validator = new MinSizeImageValidator(7000);
        List<String> providerWhileList = new ArrayList<>();
        providerWhileList.add("ICE");
        String clientId = "Media Cloud Router";
        validator.setClientId(clientId);
        validator.setProviderWhileList(providerWhileList);
        OuterDomain.OuterDomainBuilder outerDomainBuilder = new OuterDomain.OuterDomainBuilder();
        OuterDomain outerDomain = outerDomainBuilder.mediaProvider("ICE").build();
        ImageMessage.ImageMessageBuilder imageMessageBuilder = new ImageMessage.ImageMessageBuilder();
        imageMessageBuilder.clientId(clientId);
        imageMessageBuilder.outerDomainData(outerDomain);
        ImageMessage imageMessage = imageMessageBuilder.build();
        assertTrue(validator.validate(testImage.getURL(), imageMessage));
    }
}
