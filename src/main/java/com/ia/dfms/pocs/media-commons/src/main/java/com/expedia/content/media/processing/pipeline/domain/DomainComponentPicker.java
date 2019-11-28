package com.expedia.content.media.processing.pipeline.domain;

import com.expedia.content.media.processing.pipeline.util.FormattedLogger;

import java.util.Arrays;
import java.util.Map;

/**
 * Selects the an domain type dependent component according to an image type {@link com.expedia.content.media.processing.pipeline.domain.Domain}.
 */
public class DomainComponentPicker<T> {
    
    private static final FormattedLogger LOGGER = new FormattedLogger(DomainComponentPicker.class);
    
    private final Map<String, T> domainTypeComponentMap;
    
    public DomainComponentPicker(final Map<String, T> domainTypeComponentMap) {
        this.domainTypeComponentMap = domainTypeComponentMap;
    }
    
    /**
     * Returns a component instance corresponding to the domain.
     * Note: assumes there are no null keys in {@code derivativesManagerMap} map.
     * 
     * @param domain Domain that the component is needed.
     * @return A component instance corresponding to the image type.
     * @throws InvalidDomainException Thrown if the component is not available for the passed type.
     */
    public T getDomainTypeComponent(Domain domain) throws InvalidDomainException {
        T component = domainTypeComponentMap.get(domain.getDomain());
        if (component != null) {
            return component;
        } else {
            LOGGER.error("Failed to find a proper component", Arrays.asList(domain.getDomain()), "domain");
            throw new InvalidDomainException("ERROR - Failed to find a proper component for type[" + domain.getDomain() + "]");
        }
    }
}
