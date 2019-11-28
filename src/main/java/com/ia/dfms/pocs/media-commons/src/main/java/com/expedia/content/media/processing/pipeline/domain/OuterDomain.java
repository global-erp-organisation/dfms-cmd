package com.expedia.content.media.processing.pipeline.domain;

import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Contains non media domain data. Includes a loose map of maps for domain information. This
 * allows new domains to be on-boarded without having to change the image message domain message.
 * 
 * @see com.expedia.content.media.processing.pipeline.domain.ImageMessage
 */
public class OuterDomain {

    private final Domain domain;
    private final String domainId;
    private final String derivativeCategory;
    private final String provider;
    private final Map<String, Object> domainFields;

    /**
     * Builds a key/value map of maps to hold domain data unrelated to media.
     * 
     * @param domain Domain a media is to be attached to. Used to build out objects when data is needed.
     * @param domainId The id of the domain item a media is to be attached to.
     * @param provider The domain specific provider of the media.
     * @param derivativeCategory Indicator to use an alternative derivative set than the default domain set.
     * @param domainFields Key/value map of maps containing outer domain data.
     */
    public OuterDomain(Domain domain, String domainId, String provider, String derivativeCategory, Map<String, Object> domainFields) {
        this.domain = domain;
        this.domainId = domainId;
        this.provider = provider;
        this.derivativeCategory = derivativeCategory;
        if (domainFields != null) {
            this.domainFields = Collections.unmodifiableMap(domainFields);
        } else {
            this.domainFields = null;
        }
    }

    /**
     * Constructor for the builder
     *
     * @param outerDomainBuilder
     */
    public OuterDomain(OuterDomainBuilder outerDomainBuilder) {
        this(outerDomainBuilder.domain, outerDomainBuilder.domainId, outerDomainBuilder.provider, outerDomainBuilder.derivativeCategory,
             outerDomainBuilder.domainFields);
    }

    public Domain getDomain() {
        return domain;
    }

    public String getDomainId() {
        return domainId;
    }

    public String getProvider() {
        return provider;
    }

    public String getDerivativeCategory() {
        return derivativeCategory;
    }

    public Map<String, Object> getDomainFields() {
        return domainFields;
    }

    /**
     * Searches the domain fields for the value corresponding to the passed name. Searches
     * the fields recursively if some values turn out to be maps as well.
     * 
     * @param fieldName Name of the field searched.
     * @return The value of the field. Returns {@code null} if not found.
     */
    public Object getDomainFieldValue(String fieldName) {
        return domainFields == null ? null : findDomainFieldValue(fieldName, domainFields);
    }

    @Deprecated
    public OuterDomain putDomainFieldValue(String fieldName, Object fieldValue) {
        Map<String, Object> newDomainFields = new LinkedHashMap<>(domainFields);
        newDomainFields.put(fieldName, fieldValue);
        return new OuterDomain(domain, domainId, provider, derivativeCategory, newDomainFields);
    }

    @SuppressWarnings("unchecked")
    private Object findDomainFieldValue(String fieldName, Map<String, Object> fields) {
        for (Object key : fields.keySet()) {
            Object value = fields.get(key);
            if (key.equals(fieldName)) {
                return value;
            } else if (value instanceof Map) {
                return findDomainFieldValue(fieldName, (Map<String, Object>) value);
            }
        }
        return null;
    }

    @Override
    public String toString() {
        StringBuilder toStringBuilder = new StringBuilder();
        toStringBuilder.append("OuterDomain{");
        toStringBuilder.append("domain='" + domain.getDomain() + '\'');
        toStringBuilder.append(", domainId='" + domainId + '\'');
        toStringBuilder.append(", provider='" + provider + '\'');
        toStringBuilder.append(", derivativeCategory='" + derivativeCategory + '\'');
        if (!CollectionUtils.isEmpty(domainFields)) {
            toStringBuilder
                    .append(domainFields.entrySet().stream()
                            .map(entry -> entry.getValue() == null ? entry.getKey() + "='null\'"
                                                                   : entry.getKey() + "='" + entry.getValue().toString() + "\'")
                            .collect(Collectors.joining(",", ", domainFields={", "}")));
        }
        toStringBuilder.append("}");
        return toStringBuilder.toString();
    }

    public static OuterDomainBuilder builder() {
        return new OuterDomainBuilder();
    }

    public static class OuterDomainBuilder {
        private Domain domain;
        private String domainId;
        private String provider;
        private String derivativeCategory;
        private Map<String, Object> domainFields = new LinkedHashMap<>();

        public OuterDomainBuilder domain(Domain outerDomain) {
            this.domain = outerDomain;
            return this;
        }

        public OuterDomainBuilder domainId(String outerDomainId) {
            this.domainId = outerDomainId;
            return this;
        }

        public OuterDomainBuilder mediaProvider(String mediaProvider) {
            this.provider = mediaProvider;
            return this;
        }

        public OuterDomainBuilder fields(Map<String, Object> fields) {
            this.domainFields = new LinkedHashMap<>(Optional.ofNullable(fields).orElse(Collections.EMPTY_MAP));
            return this;
        }

        public OuterDomainBuilder addField(String fieldName, Object fieldValue) {
            this.domainFields.put(fieldName, fieldValue);
            return this;
        }

        public OuterDomainBuilder derivativeCategory(String outerDerivativeCategory) {
            this.derivativeCategory = outerDerivativeCategory;
            return this;
        }

        public OuterDomainBuilder from(OuterDomain outerDomain) {
            this.domain = outerDomain.domain;
            this.domainId = outerDomain.domainId;
            this.provider = outerDomain.provider;
            this.derivativeCategory = outerDomain.derivativeCategory;
            this.domainFields = new LinkedHashMap<>(Optional.ofNullable(outerDomain.domainFields).orElse(Collections.EMPTY_MAP));
            return this;
        }

        public OuterDomain build() {
            return new OuterDomain(this);
        }
    }
}
