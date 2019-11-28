package com.expedia.content.media.processing.pipeline.domain;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

@SuppressWarnings("CPD-START")
public class ResizeCropTest {
    
    @Test
    public void testSquareMediaToSquareDerivative() {
        final int mediaHeight = 100;
        final int mediaWitdh = 100;
        final DerivativeType derivativeType = new DerivativeType();
        derivativeType.setHeight(50);
        derivativeType.setWidth(50);
        derivativeType.setResizeMethod(ResizeMethod.FIXED);
        final ResizeCrop resizeCropper = new ResizeCrop(mediaHeight, mediaWitdh, derivativeType);
        assertEquals(derivativeType.getHeight(), resizeCropper.getHeight());
        assertEquals(derivativeType.getWidth(), resizeCropper.getWidth());
        assertNotNull(resizeCropper.getCropInstruction());
        assertEquals(derivativeType.getHeight(), resizeCropper.getCropInstruction().getHeight());
        assertEquals(derivativeType.getWidth(), resizeCropper.getCropInstruction().getWidth());
    }
    
    @Test
    public void testSquareMediaToWideDerivative() {
        final int mediaHeight = 1000;
        final int mediaWitdh = 1000;
        final DerivativeType derivativeType = new DerivativeType();
        derivativeType.setHeight(250);
        derivativeType.setWidth(500);
        derivativeType.setResizeMethod(ResizeMethod.FIXED);
        final ResizeCrop resizeCropper = new ResizeCrop(mediaHeight, mediaWitdh, derivativeType);
        assertEquals(500, resizeCropper.getHeight());
        assertEquals(derivativeType.getWidth(), resizeCropper.getWidth());
        assertNotNull(resizeCropper.getCropInstruction());
        assertEquals(derivativeType.getHeight(), resizeCropper.getCropInstruction().getHeight());
        assertEquals(derivativeType.getWidth(), resizeCropper.getCropInstruction().getWidth());
    }
    
    @Test
    public void testSquareMediaToTallDerivative() {
        final int mediaHeight = 1000;
        final int mediaWitdh = 1000;
        final DerivativeType derivativeType = new DerivativeType();
        derivativeType.setHeight(500);
        derivativeType.setWidth(250);
        derivativeType.setResizeMethod(ResizeMethod.FIXED);
        final ResizeCrop resizeCropper = new ResizeCrop(mediaHeight, mediaWitdh, derivativeType);
        assertEquals(derivativeType.getHeight(), resizeCropper.getHeight());
        assertEquals(500, resizeCropper.getWidth());
        assertNotNull(resizeCropper.getCropInstruction());
        assertEquals(derivativeType.getHeight(), resizeCropper.getCropInstruction().getHeight());
        assertEquals(derivativeType.getWidth(), resizeCropper.getCropInstruction().getWidth());
    }
    
    @Test
    public void testWideMediaToSquareDerivative() {
        final int mediaHeight = 1000;
        final int mediaWitdh = 1500;
        final DerivativeType derivativeType = new DerivativeType();
        derivativeType.setHeight(500);
        derivativeType.setWidth(500);
        derivativeType.setResizeMethod(ResizeMethod.FIXED);
        final ResizeCrop resizeCropper = new ResizeCrop(mediaHeight, mediaWitdh, derivativeType);
        assertEquals(derivativeType.getHeight(), resizeCropper.getHeight());
        assertEquals(750, resizeCropper.getWidth());
        assertNotNull(resizeCropper.getCropInstruction());
        assertEquals(derivativeType.getHeight(), resizeCropper.getCropInstruction().getHeight());
        assertEquals(derivativeType.getWidth(), resizeCropper.getCropInstruction().getWidth());
    }
    
    @Test
    public void testWideMediaToWideDerivative() {
        final int mediaHeight = 1000;
        final int mediaWitdh = 1500;
        final DerivativeType derivativeType = new DerivativeType();
        derivativeType.setHeight(250);
        derivativeType.setWidth(500);
        derivativeType.setResizeMethod(ResizeMethod.FIXED);
        final ResizeCrop resizeCropper = new ResizeCrop(mediaHeight, mediaWitdh, derivativeType);
        assertEquals(333, resizeCropper.getHeight());
        assertEquals(derivativeType.getWidth(), resizeCropper.getWidth());
        assertNotNull(resizeCropper.getCropInstruction());
        assertEquals(derivativeType.getHeight(), resizeCropper.getCropInstruction().getHeight());
        assertEquals(derivativeType.getWidth(), resizeCropper.getCropInstruction().getWidth());
    }
    
    @Test
    public void testWideMediaToTallDerivative() {
        final int mediaHeight = 1000;
        final int mediaWitdh = 1500;
        final DerivativeType derivativeType = new DerivativeType();
        derivativeType.setHeight(500);
        derivativeType.setWidth(250);
        derivativeType.setResizeMethod(ResizeMethod.FIXED);
        final ResizeCrop resizeCropper = new ResizeCrop(mediaHeight, mediaWitdh, derivativeType);
        assertEquals(derivativeType.getHeight(), resizeCropper.getHeight());
        assertEquals(750, resizeCropper.getWidth());
        assertNotNull(resizeCropper.getCropInstruction());
        assertEquals(derivativeType.getHeight(), resizeCropper.getCropInstruction().getHeight());
        assertEquals(derivativeType.getWidth(), resizeCropper.getCropInstruction().getWidth());
    }
    
    @Test
    public void testTallMediaToSquareDerivative() {
        final int mediaHeight = 1500;
        final int mediaWitdh = 1000;
        final DerivativeType derivativeType = new DerivativeType();
        derivativeType.setHeight(500);
        derivativeType.setWidth(500);
        derivativeType.setResizeMethod(ResizeMethod.FIXED);
        final ResizeCrop resizeCropper = new ResizeCrop(mediaHeight, mediaWitdh, derivativeType);
        assertEquals(750, resizeCropper.getHeight());
        assertEquals(derivativeType.getWidth(), resizeCropper.getWidth());
        assertNotNull(resizeCropper.getCropInstruction());
        assertEquals(derivativeType.getHeight(), resizeCropper.getCropInstruction().getHeight());
        assertEquals(derivativeType.getWidth(), resizeCropper.getCropInstruction().getWidth());
    }
    
    @Test
    public void testTallMediaToWideDerivative() {
        final int mediaHeight = 1500;
        final int mediaWitdh = 1000;
        final DerivativeType derivativeType = new DerivativeType();
        derivativeType.setHeight(250);
        derivativeType.setWidth(500);
        derivativeType.setResizeMethod(ResizeMethod.FIXED);
        final ResizeCrop resizeCropper = new ResizeCrop(mediaHeight, mediaWitdh, derivativeType);
        assertEquals(750, resizeCropper.getHeight());
        assertEquals(derivativeType.getWidth(), resizeCropper.getWidth());
        assertNotNull(resizeCropper.getCropInstruction());
        assertEquals(derivativeType.getHeight(), resizeCropper.getCropInstruction().getHeight());
        assertEquals(derivativeType.getWidth(), resizeCropper.getCropInstruction().getWidth());
    }
    
    @Test
    public void testTallMediaToTallDerivative() {
        final int mediaHeight = 1500;
        final int mediaWitdh = 1000;
        final DerivativeType derivativeType = new DerivativeType();
        derivativeType.setHeight(500);
        derivativeType.setWidth(250);
        derivativeType.setResizeMethod(ResizeMethod.FIXED);
        final ResizeCrop resizeCropper = new ResizeCrop(mediaHeight, mediaWitdh, derivativeType);
        assertEquals(derivativeType.getHeight(), resizeCropper.getHeight());
        assertEquals(333, resizeCropper.getWidth());
        assertNotNull(resizeCropper.getCropInstruction());
        assertEquals(derivativeType.getHeight(), resizeCropper.getCropInstruction().getHeight());
        assertEquals(derivativeType.getWidth(), resizeCropper.getCropInstruction().getWidth());
    }
    
    @Test
    public void testHeightResizeMethod() {
        final int mediaHeight = 1000;
        final int mediaWitdh = 1000;
        final DerivativeType derivativeType = new DerivativeType();
        derivativeType.setHeight(100);
        derivativeType.setWidth(200);
        derivativeType.setResizeMethod(ResizeMethod.HEIGHT);
        final ResizeCrop resizeCropper = new ResizeCrop(mediaHeight, mediaWitdh, derivativeType);
        assertEquals(derivativeType.getHeight(), resizeCropper.getHeight());
        assertEquals(100, resizeCropper.getWidth());
        assertNull(resizeCropper.getCropInstruction());
    }
    
    @Test
    public void testWidthResizeMethod() {
        final int mediaHeight = 1000;
        final int mediaWitdh = 1000;
        final DerivativeType derivativeType = new DerivativeType();
        derivativeType.setHeight(100);
        derivativeType.setWidth(200);
        derivativeType.setResizeMethod(ResizeMethod.WIDTH);
        final ResizeCrop resizeCropper = new ResizeCrop(mediaHeight, mediaWitdh, derivativeType);
        assertEquals(200, resizeCropper.getHeight());
        assertEquals(derivativeType.getWidth(), resizeCropper.getWidth());
        assertNull(resizeCropper.getCropInstruction());
    }
    
    @Test
    public void testVariableHeightResizeMethod() {
        final int mediaHeight = 2000;
        final int mediaWitdh = 1000;
        final DerivativeType derivativeType = new DerivativeType();
        derivativeType.setHeight(100);
        derivativeType.setWidth(200);
        derivativeType.setResizeMethod(ResizeMethod.VARIABLE);
        final ResizeCrop resizeCropper = new ResizeCrop(mediaHeight, mediaWitdh, derivativeType);
        assertEquals(derivativeType.getHeight(), resizeCropper.getHeight());
        assertEquals(50, resizeCropper.getWidth());
        assertNull(resizeCropper.getCropInstruction());
    }
    
    @Test
    public void testVariableWidthResizeMethod() {
        final int mediaHeight = 1000;
        final int mediaWitdh = 2000;
        final DerivativeType derivativeType = new DerivativeType();
        derivativeType.setHeight(200);
        derivativeType.setWidth(100);
        derivativeType.setResizeMethod(ResizeMethod.VARIABLE);
        final ResizeCrop resizeCropper = new ResizeCrop(mediaHeight, mediaWitdh, derivativeType);
        assertEquals(50, resizeCropper.getHeight());
        assertEquals(derivativeType.getWidth(), resizeCropper.getWidth());
        assertNull(resizeCropper.getCropInstruction());
    }
    
}
