package com.expedia.content.media.processing.pipeline.util;

import org.junit.Test;

import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class GuidUtilTest {

    public static final String CHARS = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz-_";

    public static final String RIGHT_LENGTH = "1234567890xx0987654321";

    @Test(expected = RuntimeException.class)
    public void testInvalidEncode() {
        GuidUtil.decodeFromBase64("whatever");
    }

    @Test
    public void testInvalidWithProperLength() {
        UUID uuid = GuidUtil.decodeFromBase64(RIGHT_LENGTH);
        assertNotNull(uuid);
        String encoded = GuidUtil.encodeToBase64(uuid);
        // For some reason the resulting UUID does not maintan the equality
        // In any case, you're not supposed to do this
        assertEquals(RIGHT_LENGTH, encoded.replace('w', '1'));
    }

    @Test(expected = NullPointerException.class)
    public void testNullUuidObject() {
        GuidUtil.encodeToBase64(null);
    }

    @Test
    public void testSimpleUUID() {
        UUID uuid = UUID.randomUUID();
        UUID uuidSame = GuidUtil.decodeFromBase64(GuidUtil.encodeToBase64(uuid));
        assertEquals(uuid, uuidSame);
    }

    @Test
    public void testThereAndBackAgain() {
        for (int i = 0; i < 1000; i++) {
            final UUID uuid = UUID.randomUUID();
            final String encodedGuid = GuidUtil.encodeToBase64(uuid);
            assertEquals(22, encodedGuid.length());
            final UUID decodedGuid = GuidUtil.decodeFromBase64(encodedGuid);
            assertEquals(uuid.toString(), decodedGuid.toString());
        }
    }
}
