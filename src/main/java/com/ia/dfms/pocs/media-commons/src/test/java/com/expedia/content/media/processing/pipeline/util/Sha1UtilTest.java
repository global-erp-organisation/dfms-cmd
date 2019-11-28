package com.expedia.content.media.processing.pipeline.util;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class Sha1UtilTest {
    @Test
    public void testHashValueAsHex() {
        String hash = Sha1Util.calculateSha1("Hello, world!");
        assertEquals("943a702d06f34599aee1f8da8ef9f7296031d699", hash);
        String hash2 = Sha1Util.calculateSha1("Hello, world!", Sha1Util.HashFormat.HEX);
        assertEquals("943a702d06f34599aee1f8da8ef9f7296031d699", hash2);
    }

    @Test
    public void testHashValueAsBase64() {
        String hash = Sha1Util.calculateSha1("Hello, world!", Sha1Util.HashFormat.BASE64);
        assertEquals("lDpwLQbzRZmu4fjajvn3KWAx1pk=", hash);
        String hash2 = Sha1Util.calculateSha1("Hello, world!", Sha1Util.HashFormat.BASE64);
        assertEquals("lDpwLQbzRZmu4fjajvn3KWAx1pk=", hash2);
    }

    @Test
    public void testHashShortened() {
        String shortenHex = Sha1Util.shorten("0123456789", Sha1Util.HashFormat.HEX, 5);
        assertEquals("87ace", shortenHex);
        String shortenHexDefault = Sha1Util.shorten("0123456789", 5);
        assertEquals("87ace", shortenHexDefault);
        String shortenBase64 = Sha1Util.shorten("0123456789", Sha1Util.HashFormat.BASE64, 5);
        assertEquals("h6zsF", shortenBase64);
    }

}
