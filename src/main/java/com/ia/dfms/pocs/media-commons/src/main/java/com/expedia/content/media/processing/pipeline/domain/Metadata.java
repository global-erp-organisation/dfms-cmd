package com.expedia.content.media.processing.pipeline.domain;

import com.google.common.collect.ImmutableMap;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Metadata and "physical" properties of an image. 
 */
public class Metadata {

    private final Integer width;
    private final Integer height;
    private final Integer fileSize;
    private final Map<String, String> details;

    public Metadata(Integer width, Integer height, Integer fileSize, Map<String, String> details) {
        this.width = width;
        this.height = height;
        this.fileSize = fileSize;
        if (details != null && !details.isEmpty()) {
            this.details = ImmutableMap.copyOf(details);
        } else {
            this.details = null;
        }
    }

    public Metadata(MetadataBuilder metadataBuilder) {
        this(
                metadataBuilder.width,
                metadataBuilder.height,
                metadataBuilder.fileSize,
                metadataBuilder.details
        );
    }

    public Integer getWidth() {
        return width;
    }

    public Integer getHeight() {
        return height;
    }

    public Integer getFileSize() {
        return fileSize;
    }

    public Map<String, String> getDetails() {
        return details;
    }

    @Override
    public String toString() {
        StringBuilder toStringBuilder = new StringBuilder();
        toStringBuilder.append("Metadata{");
        toStringBuilder.append("height='" + height + '\'');
        toStringBuilder.append(", width='" + width + '\'');
        toStringBuilder.append(", fileSize='" + fileSize + '\'');
        if (!CollectionUtils.isEmpty(details)) {
            toStringBuilder.append(details.entrySet().stream().map(entry -> entry.getKey() + "='" + entry.getValue().toString() + "\'")
                    .collect(Collectors.joining(",", ", details={", "}")));
        }
        toStringBuilder.append("}");
        return toStringBuilder.toString();
    }

    public static MetadataBuilder builder() {
        return new MetadataBuilder();
    }

    public static class MetadataBuilder {
        private Integer width;
        private Integer height;
        private Integer fileSize;
        private Map<String, String> details = new LinkedHashMap<>();

        public MetadataBuilder width(int imageWidth) {
            this.width = imageWidth;
            return this;
        }

        public MetadataBuilder height(int imageHeight) {
            this.height = imageHeight;
            return this;
        }

        public MetadataBuilder fileSize(int imageFileSize) {
            this.fileSize = imageFileSize;
            return this;
        }

        public MetadataBuilder details(Map<String, String> metaDetails) {
            this.details = new LinkedHashMap<>(Optional.ofNullable(metaDetails).orElse(Collections.EMPTY_MAP));
            return this;
        }

        public MetadataBuilder addDetail(String detailName, String detailValue) {
            this.details.put(detailName, detailValue);
            return this;
        }

        public MetadataBuilder from(Metadata metadata) {
            this.width = metadata.width;
            this.height = metadata.height;
            this.fileSize = metadata.fileSize;
            this.details = new LinkedHashMap<>(Optional.ofNullable(metadata.details).orElse(Collections.EMPTY_MAP));
            return this;
        }

        public Metadata build() {
            return new Metadata(this);
        }
     }
}
