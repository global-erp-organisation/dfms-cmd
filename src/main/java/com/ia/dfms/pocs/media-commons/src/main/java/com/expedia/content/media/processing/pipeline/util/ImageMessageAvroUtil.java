package com.expedia.content.media.processing.pipeline.util;

import com.expedia.content.media.processing.pipeline.avro.Derivative;
import com.expedia.content.media.processing.pipeline.avro.Fingerprint;
import com.expedia.content.media.processing.pipeline.avro.ImageMessageAvro;
import com.expedia.content.media.processing.pipeline.avro.LogEntry;
import com.expedia.content.media.processing.pipeline.domain.ImageMessage;
import com.rabbitmq.tools.json.JSONWriter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * build ImageMessageAvro object based on imageMessage
 * ImageMessageAvro is auto generated class by avro schema, the message is compressed.
 */
public final class ImageMessageAvroUtil {
    private ImageMessageAvroUtil() {
    }

    /**
     * generate ImageMessageAvro from imageMessage.
     * Avro only support int not integer object, so for some null property from imageMessage
     * like 'retryCount', set it to -1 that means it is null imageMessage.
     * @param imageMessage
     * @return
     */
    public static ImageMessageAvro generateAvroMsg(ImageMessage imageMessage) {
        ImageMessageAvro imageMessageAvro = new ImageMessageAvro();

        String domainFields = null;
        if (imageMessage.getOuterDomainData() != null && imageMessage.getOuterDomainData().getDomainFields() != null) {
            domainFields = new JSONWriter().write(imageMessage.getOuterDomainData().getDomainFields());
        }
        imageMessageAvro.setClientId(imageMessage.getClientId());
        imageMessageAvro.setUserId(imageMessage.getUserId());
        imageMessageAvro.setRequestId(imageMessage.getRequestId());
        imageMessageAvro.setMediaGuid(imageMessage.getMediaGuid());
        imageMessageAvro.setFileUrl(imageMessage.getFileUrl());
        imageMessageAvro.setFileName(imageMessage.getFileName());
        imageMessageAvro.setRotation(imageMessage.getRotation());
        imageMessageAvro.setActive(imageMessage.isActive() != null ? imageMessage.isActive().toString() : null);
        imageMessageAvro.setGenerateThumbnail(imageMessage.isGenerateThumbnail() != null ? imageMessage.isGenerateThumbnail().toString() : null);
        imageMessageAvro.setSourceUrl(imageMessage.getSourceUrl());
        imageMessageAvro.setOutputFolder(imageMessage.getOutputFolder());
        imageMessageAvro.setRejectedFolder(imageMessage.getRejectedFolder());
        imageMessageAvro.setCallback(imageMessage.getCallback() != null ? imageMessage.getCallback().toString() : null);
        imageMessageAvro.setHidden(imageMessage.getHidden() != null ? imageMessage.getHidden().toString() : null);

        imageMessageAvro.setProvidedName(imageMessage.getProvidedName());
        imageMessageAvro.setCategoryId(imageMessage.getCategoryId());
        imageMessageAvro.setCaption(imageMessage.getCaption());
        imageMessageAvro.setMediaProviderId(imageMessage.getMediaProviderId());
        imageMessageAvro.setComment(imageMessage.getComment());
        imageMessageAvro.setFailedReason(imageMessage.getFailedReason());

        int retryCount = imageMessage.getRetryCount() == null ? -1 : imageMessage.getRetryCount();
        imageMessageAvro.setRetryCount(retryCount);
        int expediaId = imageMessage.getExpediaId() == null ? -1 : imageMessage.getExpediaId();
        imageMessageAvro.setExpediaId(expediaId);
        imageMessageAvro.setDomainFields(domainFields);
        if (imageMessage.getOuterDomainData() != null) {
            imageMessageAvro.setDomain(imageMessage.getOuterDomainData().getDomain().getDomain());
            imageMessageAvro.setDomainId(imageMessage.getOuterDomainData().getDomainId());
            imageMessageAvro.setDomainProvider(imageMessage.getOuterDomainData().getProvider());
            imageMessageAvro.setDomainDerivativeCategory(imageMessage.getOuterDomainData().getDerivativeCategory());
        }

        if (imageMessage.getMetadata() != null) {
            if (imageMessage.getMetadata().getWidth() != null) {
                imageMessageAvro.setWidth(imageMessage.getMetadata().getWidth());
            } else {
                imageMessageAvro.setWidth(-1);
            }
            if (imageMessage.getMetadata().getHeight() != null) {
                imageMessageAvro.setHeight(imageMessage.getMetadata().getHeight());

            } else {
                imageMessageAvro.setHeight(-1);
            }
            if (imageMessage.getMetadata().getFileSize() != null) {
                imageMessageAvro.setFileSize(imageMessage.getMetadata().getFileSize());

            } else {
                imageMessageAvro.setFileSize(-1);
            }
            imageMessageAvro.setMetadataDetails((Map) imageMessage.getMetadata().getDetails());

        }

        if (imageMessage.getStagingKey() != null) {
            com.expedia.content.media.processing.pipeline.avro.StagingKey stagingKey =
                    new com.expedia.content.media.processing.pipeline.avro.StagingKey(imageMessage.getStagingKey().getExternalId(),
                            imageMessage.getStagingKey().getProviderId(), imageMessage.getStagingKey().getSourceId());
            imageMessageAvro.setStagingKey(stagingKey);
        }
        imageMessageAvro.setFingerprints(getFingerprints(imageMessage));
        imageMessageAvro.setLogEntries(getLogEntries(imageMessage));
        imageMessageAvro.setDerivatives(getDerivatives(imageMessage));
        return imageMessageAvro;
    }

    private static List<LogEntry> getLogEntries(ImageMessage imageMessage) {
        if (!imageMessage.getLogEntries().isEmpty()) {
            List<LogEntry> logEntryAvroList = new ArrayList<>();
            for (com.expedia.content.media.processing.pipeline.reporting.LogEntry logEntry : imageMessage.getLogEntries()) {
                LogEntry avroLogEntry = new LogEntry();
                avroLogEntry.setActivityTime(logEntry.getActivityTime().getTime());
                avroLogEntry.setAppName(logEntry.getAppName());
                avroLogEntry.setActivity(com.expedia.content.media.processing.pipeline.avro.Activity.valueOf(logEntry.getActivity().getName()));
                logEntryAvroList.add(avroLogEntry);
            }
            return logEntryAvroList;

        } else {
            return null;
        }

    }

    private static List<Derivative> getDerivatives(ImageMessage imageMessage) {
        if (!imageMessage.getLogEntries().isEmpty()) {
            List<Derivative> derivativeList = new ArrayList<>();
            for (com.expedia.content.media.processing.pipeline.domain.Derivative derivative : imageMessage.getDerivatives()) {
                Derivative derivativeAvro = new Derivative();
                derivativeAvro.setFileSize(derivative.getFileSize());
                derivativeAvro.setHeight(derivative.getHeight());
                derivativeAvro.setLocation(derivative.getLocation());
                derivativeAvro.setType(derivative.getType());
                derivativeAvro.setWidth(derivative.getWidth());
                derivativeList.add(derivativeAvro);
            }
            return derivativeList;

        } else {
            return null;
        }
    }

    private static List<Fingerprint> getFingerprints(ImageMessage imageMessage) {
        if (!imageMessage.getFingerprints().isEmpty()) {
            List<Fingerprint> fingerprintAvroList = new ArrayList<>();
            for (com.expedia.content.media.processing.pipeline.domain.Fingerprint fingerprint : imageMessage.getFingerprints()) {
                Fingerprint avroFinger = new Fingerprint();
                avroFinger.setAlgorithm(fingerprint.getAlgorithm());
                List<java.lang.CharSequence> fingerAvros = new ArrayList<>();
                for (String fingerValue : fingerprint.getValues()) {
                    fingerAvros.add(fingerValue);
                }
                avroFinger.setValues(fingerAvros);
                fingerprintAvroList.add(avroFinger);
            }
            return fingerprintAvroList;
        } else {
            return null;
        }
    }
}
