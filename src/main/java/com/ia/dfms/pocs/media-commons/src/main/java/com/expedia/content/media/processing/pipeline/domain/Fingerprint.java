package com.expedia.content.media.processing.pipeline.domain;

import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Image fingerprint containing the name of the algorithm used to generate values and the list of generated values.
 */
public class Fingerprint {
    
    private final String algorithm;
    private final List<String> values;
    
    public Fingerprint(String algorithm, String... values) {
        this(algorithm, Lists.newArrayList(values));
    }
    
    public Fingerprint(String algorithm, List<String> values) {
        this.algorithm = algorithm;
        this.values = new ArrayList<>();
        for (String extraValue : values) {
            this.values.add(extraValue);
        }
    }
    
    public String getAlgorithm() {
        return algorithm;
    }
    
    /**
     * Returns a copy of fingerprint values.
     * 
     * @return a read-only list with the items in the fingerprint values.
     */
    public List<String> getValues() {
        return Collections.unmodifiableList(values);
    }

    @Override
    public String toString() {
        return "Fingerprint{" + "algorithm=" + algorithm + ", values=" + values + '}';
    }
}
