package com.expedia.content.media.processing.pipeline.domain;

/**
 * Domain types. List of supported domain types.
 */
public enum Domain {
    LODGING("Lodging"),
    CARS("Cars"),
    CONTENT_REPO("ContentRepo");

    private String domain;

    private Domain(final String domain) {
        this.domain = domain;
    }

    public String getDomain() {
        return this.domain;
    }

    /**
     * Finds the Domain for the passed name.
     * 
     * @param name Name search is done for.
     * @return The Domain. null if the code is not found.
     */
    public static Domain findDomain(String name) {
        return findDomain(name, false);
    }

    /**
     * Finds the Domain for the passed name.
     * 
     * @param name Name search is done for.
     * @param ignoreCase Allows to ignore the name casing.
     * @return The Domain. null if the code is not found.
     */
    public static Domain findDomain(String name, boolean ignoreCase) {
        for (Domain type : values()) {
            if (ignoreCase ? type.getDomain().equalsIgnoreCase(name) : type.getDomain().equals(name)) {
                return type;
            }
        }
        return null;
    }


}
