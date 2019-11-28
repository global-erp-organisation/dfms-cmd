package com.expedia.content.media.processing.pipeline.util;

import java.nio.ByteBuffer;
import java.util.Base64;
import java.util.UUID;

/**
 * Some utilities to deal with UUIDs
 *
 * @see UUID
 */
public final class GuidUtil {

    private GuidUtil() {
        /* static class */
    }

    /**
     * Converts an array of longs to an array of bytes
     *
     * @param longs array of longs to convert
     * @return Array of bytes
     */
    private static byte[] longToByteArray(long... longs) {
        final ByteBuffer buffer = ByteBuffer.allocate(Long.SIZE * longs.length / Byte.SIZE);
        for (final long l : longs) {
            buffer.putLong(l);
        }
        return buffer.array();
    }

    /**
     * Converts an array of bytes to an array of longs. It is the caller's responsibility to provide the right
     * number of bytes to create the right number of longs. Otherwise an exception will be thrown.
     *
     * @param bytes Array of bytes
     * @return Array of Longs
     */
    private static long[] byteArrayToLong(byte... bytes) {
        final ByteBuffer buffer = ByteBuffer.allocate(bytes.length * Byte.SIZE);
        buffer.put(bytes);
        final long[] result = new long[bytes.length * Byte.SIZE / Long.SIZE];
        buffer.flip();
        buffer.asLongBuffer().get(result);
        return result;
    }

    /**
     * Produces a shorter (22 chars) version of the GUID encoded in Base64 with some small transformations.
     *
     * <ol>
     *     <li>Removed the two trailing '==' at the end</li>
     *     <li>Changed '/' with '~'</li>
     *     <li>Changed '+' with '!'</li>
     * </ol>
     *
     * @param uuid UUID to encode
     * @return Encoded UUID shortened string version
     * @see UUID
     */
    public static String encodeToBase64(UUID uuid) {
        try {
            final byte[] allBits = longToByteArray(uuid.getMostSignificantBits(), uuid.getLeastSignificantBits());
            final String encodedLong = Base64.getEncoder().encodeToString(allBits);
            return encodedLong.substring(0, encodedLong.length() - 2).replace('/', '-').replace('+', '_');
        } catch (Exception e) {
            throw new RuntimeException("Unable to encode: " + uuid.toString(), e);
        }
    }

    /**
     * Decodes a previously encoded (with this class) UUID.
     *
     * @param encoded Encoded UUID
     * @return Decoded UUID
     * @see #encodeToBase64
     */
    public static UUID decodeFromBase64(String encoded) {
        try {
            final String replaced = encoded.replace('-', '/').replace('_', '+') + "==";
            final byte[] decode = Base64.getDecoder().decode(replaced);
            final long[] allBits = byteArrayToLong(decode);
            return new UUID(allBits[0], allBits[1]);
        } catch (Exception e) {
            throw new RuntimeException("Unable to decode: '" + encoded + "'", e);
        }
    }

}
