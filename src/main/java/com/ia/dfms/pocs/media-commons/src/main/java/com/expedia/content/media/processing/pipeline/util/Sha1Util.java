package com.expedia.content.media.processing.pipeline.util;

import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Calculates hashes using the SHA1 algorithm.
 */
public final class Sha1Util {
    private static final String SHA1_ALGORITHM = "SHA1";

    private Sha1Util() {
        /* Utility class */
    }

    /**
     * Calculates the SHA1 for a string value.
     *
     * @param value Value for which to calculate the Sha1
     * @return FingerprintValue object containing the Sha1 hash value for the media.
     */
    public static String calculateSha1(String value) {
        return calculateSha1(value, HashFormat.HEX);
    }

    /**
     * Calculates the SHA1 for a string value.
     *
     * @param value Value for which to calculate the Sha1
     * @param format HEX (default) or BASE64
     * @return FingerprintValue object containing the Sha1 hash value for the media.
     */
    public static String calculateSha1(String value, HashFormat format) {
        MessageDigest messageDigest = getSha1Digest();
        messageDigest.update(value.getBytes());
        if (format == HashFormat.BASE64) {
            return DatatypeConverter.printBase64Binary(messageDigest.digest());
        }
        return DatatypeConverter.printHexBinary(messageDigest.digest()).toLowerCase();
    }

    /**
     * Computes the SHA1 of the String value and then allows the selection of the length of the result.
     *
     * @param value Value on which to calculate the SHA1
     * @param length Returned length of the SHA1
     *
     * @return Shortened
     */
    public static String shorten(String value, HashFormat format, int length) {
        return Sha1Util.calculateSha1(value, format).substring(0, length);
    }

    /**
     * Computes the SHA1 (hex) of the String value and then allows the selection of the length of the result.
     *
     * @param value Value on which to calculate the SHA1
     * @param length Returned length of the SHA1
     *
     * @return Shortened
     */
    public static String shorten(String value, int length) {
        return Sha1Util.calculateSha1(value, HashFormat.HEX).substring(0, length);
    }

    private static MessageDigest getSha1Digest() {
        try {
            return MessageDigest.getInstance(SHA1_ALGORITHM);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Unsupported algorithm: " + SHA1_ALGORITHM);
        }
    }

    /**
     * Different representation of Hashes
     */
    public enum HashFormat {
        /**
         * Hexadecimal representation: [0-9] + [a-f]
         */
        HEX,
        /**
         * Base64 representation: [0-9] + [a-z] + [A-Z] + '/' + '+' + '=' (padding)
         */
        BASE64
    }
}
