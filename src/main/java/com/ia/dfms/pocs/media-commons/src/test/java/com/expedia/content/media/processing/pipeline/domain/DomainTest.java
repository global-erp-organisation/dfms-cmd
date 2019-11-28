package com.expedia.content.media.processing.pipeline.domain;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

public class DomainTest {

    @Test
    public void testFindDomain() throws Exception {
        String invalidDomain1 = "INVALID";
        Domain domain = Domain.findDomain(invalidDomain1);
        assertNull(domain);

        String invalidDomain2 = "lodging";
        domain = Domain.findDomain(invalidDomain2);
        assertNull(domain);

        String validDomain = "Lodging";
        domain = Domain.findDomain(validDomain);
        assertNotNull(domain);
    }
    
    @Test
    public void testFindDomainIgnoreCase() throws Exception {
        String invalidDomain = "INVALID";
        Domain domain = Domain.findDomain(invalidDomain, true);
        assertNull(domain);

        String validDomain1 = "Lodging";
        domain = Domain.findDomain(validDomain1, true);
        assertNotNull(domain);

        String validDomain2 = "lOdGiNg";
        domain = Domain.findDomain(validDomain2, true);
        assertNotNull(domain);
    }

    @Test
    public void testFindCarsDomain() {
        Domain domain = Domain.findDomain("Cars");
        assertEquals(Domain.CARS, domain);
    }

    @Test
    public void testFindContentRepoDomain() {
        Domain domain = Domain.findDomain("ContentRepo");
        assertEquals(Domain.CONTENT_REPO, domain);
    }

    @Test
    public void testFindLodgingDomain() {
        Domain domain = Domain.findDomain("Lodging");
        assertEquals(Domain.LODGING, domain);
    }
}
