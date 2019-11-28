package com.expedia.content.media.processing.pipeline.util;

import org.junit.Test;

import static junit.framework.Assert.assertTrue;
import static junit.framework.TestCase.assertEquals;

public class OSDetectorTest {
    @Test
    public void testWindowsOrLinux() {
        assertTrue(OSDetector.detectOS() == OSDetector.OS.WINDOWS ||
                   OSDetector.detectOS() == OSDetector.OS.UNIX ||
                   OSDetector.detectOS() == OSDetector.OS.MAC);
    }

    @Test
    public void testWindows() {
        assertEquals(OSDetector.OS.WINDOWS, OSDetector.detectOS("Some WiNcrap".toLowerCase()));
    }

    @Test
    public void testLinux() {
        assertEquals(OSDetector.OS.UNIX, OSDetector.detectOS("Some Linux distro".toLowerCase()));
    }

    @Test
    public void testMac() {
        assertEquals(OSDetector.OS.MAC, OSDetector.detectOS("Mactastic O'Job".toLowerCase()));
    }

    @Test
    public void testUnknown() {
        assertEquals(OSDetector.OS.UNKNOWN, OSDetector.detectOS("SuperOs".toLowerCase()));
    }
}
