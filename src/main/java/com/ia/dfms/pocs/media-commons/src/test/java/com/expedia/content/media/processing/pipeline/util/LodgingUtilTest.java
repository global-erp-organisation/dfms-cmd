package com.expedia.content.media.processing.pipeline.util;

import junit.framework.TestCase;

import java.util.Arrays;

public class LodgingUtilTest extends TestCase {

    public void testGetFolderHierarchy() throws Exception {
        int hotelId = 1;
        int[] expectedFolders = {1000000, 10000, 100};
        int[] folders = LodgingUtil.getFolderHierarchy(hotelId);
        assertTrue(Arrays.equals(expectedFolders, folders));

        hotelId = 245;
        expectedFolders = new int[]{1000000, 10000, 300};
        folders = LodgingUtil.getFolderHierarchy(hotelId);
        assertTrue(Arrays.equals(expectedFolders, folders));

        hotelId = 1501;
        expectedFolders = new int[]{1000000, 10000, 1600};
        folders = LodgingUtil.getFolderHierarchy(hotelId);
        assertTrue(Arrays.equals(expectedFolders, folders));

        hotelId = 76000;
        expectedFolders = new int[]{1000000, 80000, 76000};
        folders = LodgingUtil.getFolderHierarchy(hotelId);
        assertTrue(Arrays.equals(expectedFolders, folders));

        hotelId = 76340;
        expectedFolders = new int[]{1000000, 80000, 76400};
        folders = LodgingUtil.getFolderHierarchy(hotelId);
        assertTrue(Arrays.equals(expectedFolders, folders));

        hotelId = 1000003;
        expectedFolders = new int[]{2000000, 1010000, 1000100};
        folders = LodgingUtil.getFolderHierarchy(hotelId);
        assertTrue(Arrays.equals(expectedFolders, folders));
    }

    public void testBuildFolderPath() throws Exception {
        char charSeparator = System.getProperty("file.separator").charAt(0);

        int hotelId = 1;
        String expectedPath = "/1000000/10000/100/1/".replace('/', charSeparator);
        String path = LodgingUtil.buildFolderPath(hotelId);
        assertEquals(expectedPath, path);

        hotelId = 245;
        expectedPath = "/1000000/10000/300/245/".replace('/', charSeparator);
        path = LodgingUtil.buildFolderPath(hotelId);
        assertEquals(expectedPath, path);

        hotelId = 1501;
        expectedPath = "/1000000/10000/1600/1501/".replace('/', charSeparator);
        path = LodgingUtil.buildFolderPath(hotelId);
        assertEquals(expectedPath, path);

        hotelId = 76000;
        expectedPath = "/1000000/80000/76000/76000/".replace('/', charSeparator);
        path = LodgingUtil.buildFolderPath(hotelId);
        assertEquals(expectedPath, path);

        hotelId = 76340;
        expectedPath = "/1000000/80000/76400/76340/".replace('/', charSeparator);
        path = LodgingUtil.buildFolderPath(hotelId);
        assertEquals(expectedPath, path);

        hotelId = 1000003;
        expectedPath = "/2000000/1010000/1000100/1000003/".replace('/', charSeparator);
        path = LodgingUtil.buildFolderPath(hotelId);
        assertEquals(expectedPath, path);
    }

    public void testExtractHotelId() throws Exception {
        String fileName = "123_abc.jpeg";
        String hotelId = LodgingUtil.extractHotelId(fileName);
        assertEquals("123", hotelId);

        fileName = "123_xyz";
        hotelId = LodgingUtil.extractHotelId(fileName);
        assertEquals("123", hotelId);

        fileName = "a123_abc.jpeg";
        hotelId = LodgingUtil.extractHotelId(fileName);
        assertNull(hotelId);

        fileName = "123b_abc.jpeg";
        hotelId = LodgingUtil.extractHotelId(fileName);
        assertNull(hotelId);

        fileName = "123456.jpeg";
        hotelId = LodgingUtil.extractHotelId(fileName);
        assertNull(hotelId);
    }
}
