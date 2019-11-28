package com.expedia.content.media.processing.pipeline.domain;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class DerivativeTypeTest {
    
    @Test
    public void testFalseOnZeroThreshold() {
        final int mediaHeight = 1000;
        final int mediaWitdh = 1000;
        final DerivativeType derivativeType = new DerivativeType();
        derivativeType.setHeight(500);
        derivativeType.setWidth(250);
        
        assertFalse(DerivativeType.isMediaWithinThreshold(mediaHeight, mediaWitdh, derivativeType));
    }
    
    @Test
    public void testFalseOutsideThreshold() {
        final int mediaHeight = 100;
        final int mediaWitdh = 100;
        final DerivativeType derivativeType = new DerivativeType();
        derivativeType.setHeight(500);
        derivativeType.setWidth(250);
        derivativeType.setThreshold(200);
        
        assertFalse(DerivativeType.isMediaWithinThreshold(mediaHeight, mediaWitdh, derivativeType));
    }
    
    @Test
    public void testTrueHeightInsideThreshold() {
        final int mediaHeight = 210;
        final int mediaWitdh = 100;
        final DerivativeType derivativeType = new DerivativeType();
        derivativeType.setHeight(500);
        derivativeType.setWidth(250);
        derivativeType.setThreshold(200);
        
        assertTrue(DerivativeType.isMediaWithinThreshold(mediaHeight, mediaWitdh, derivativeType));
    }
    
    @Test
    public void testTrueWidthInsideThreshold() {
        final int mediaHeight = 100;
        final int mediaWitdh = 210;
        final DerivativeType derivativeType = new DerivativeType();
        derivativeType.setHeight(500);
        derivativeType.setWidth(250);
        derivativeType.setThreshold(200);
        
        assertTrue(DerivativeType.isMediaWithinThreshold(mediaHeight, mediaWitdh, derivativeType));
    }
    
    @Test
    public void testTrueBothInsideThreshold() {
        final int mediaHeight = 210;
        final int mediaWitdh = 210;
        final DerivativeType derivativeType = new DerivativeType();
        derivativeType.setHeight(500);
        derivativeType.setWidth(250);
        derivativeType.setThreshold(200);
        
        assertTrue(DerivativeType.isMediaWithinThreshold(mediaHeight, mediaWitdh, derivativeType));
    }
    
}
