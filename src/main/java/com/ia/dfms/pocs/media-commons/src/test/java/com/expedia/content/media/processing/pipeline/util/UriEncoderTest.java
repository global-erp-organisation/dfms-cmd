package com.expedia.content.media.processing.pipeline.util;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Several test for URI encoding/decoding
 */
public class UriEncoderTest {

    @Test
    public void testFilenameUrlWithEncoding() {
        assertEncodingDecoding("http://somehost/path/fileName.jpg");
        assertEncodingDecoding(
                "http://somehost/path/fileName _~!@#$%^*()-+={}[]\\|;:\"<>,.jpg",
                "http://somehost/path/fileName%20_%7E%21%40%23%24%25%5E*%28%29-%2B%3D%7B%7D%5B%5D%5C%7C%3B%3A%22%3C%3E%2C.jpg");
        assertEncodingDecoding("http://somehost/path/callback");
        assertEncodingDecoding("http://somehost/path/call back", "http://somehost/path/call%20back");
    }

    @Test
    public void testQueryUrlWithoutEncoding() {
        assertEncodingDecoding("http://somehost//path/command");
        assertEncodingDecoding("http://somehost/path/command?");
        assertEncodingDecoding("http://somehost//path/command?var");
        assertEncodingDecoding("http://somehost//path/command?var1&var2");
        assertEncodingDecoding("http://somehost//path/command?var0&var1=value1&var2");
    }

    @Test
    public void testQueryUrlWithEncoding() {
        assertEncodingDecoding("http://somehost//path/command");
        assertEncodingDecoding("http://somehost/path/command?");
        assertEncodingDecoding("http://somehost//path/command?var one", "http://somehost//path/command?var%20one");
        assertEncodingDecoding("http://somehost//path/command?var one&var two", "http://somehost//path/command?var%20one&var%20two");
        assertEncodingDecoding(
                "http://somehost//path/command?var zero&var one=value one&var two=value two",
                "http://somehost//path/command?var%20zero&var%20one=value%20one&var%20two=value%20two");
    }

    @Test
    public void testWeirdValues() {
        // The additional question marks after the first become part of the first variable
        assertEncodingDecoding("http://somehost/command???var0&&&var2=val2", "http://somehost/command?%3F%3Fvar0&&&var2=val2");
        // Subsequent question marks will be part of the variable name and encoded
        assertEncodingDecoding("http://somehost/command???var0&&&var?2=val?2", "http://somehost/command?%3F%3Fvar0&&&var%3F2=val%3F2");
    }

    private static void assertEncodingDecoding(String sameUrl) {
        assertEncodingDecoding(sameUrl, sameUrl);
    }

    private static void assertEncodingDecoding(String fromUrl, String toUrl) {
        String encodedUrl = UriEncoder.encodeUrl(fromUrl);
        assertEquals(toUrl, encodedUrl);
        String decodedUrl = UriEncoder.decodeUrl(encodedUrl);
        assertEquals(fromUrl, decodedUrl);
    }

}
