package com.expedia.content.media.processing.pipeline.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * Provides some helper methods for encoding/decoding URLs from String
 *
 * The difficulty here is that enconding needs to take place on the right segments of the URL. So for a regular URL:
 *
 * <p>
 * {@code
 *     http://<no_encoding_necessary>/<encoding_necessary>
 * }
 * </p>
 *
 * When encoding is taking place two cases arise:
 * <ol>
 *     <li>File URL: {@code http://somehost/fileName.jpg}</li>
 *     Here the fileName part needs to be URL encoded
 *     <li>Query URL: {@code http://somehost/command?var0&var1=val1&var2=val2}</li>
 *     Here the command, variables and values need to be encoded individually
 * </ol>
 *
 * @see URLEncoder
 * @see URLDecoder
 */
public final class UriEncoder {
    public static final String URL_ENCODING = "UTF-8";

    private UriEncoder() {
        /* Static utility class */
    }

    /**
     * Escapes the characters in a URL.
     *
     * This method supports two kinds of URLs:
     * <ol>
     *     <li>Filename URL: Of the form: http://somehost/fileName.jpg</li>
     *     <li>Query URL: Of the form: http://somehost/query?var0&var1=val1&var2=val2</li>
     * </ol>
     *
     * @param toEncode Unencoded URL
     * @return Encoded URL where the proper elements are escaped
     */
    public static String encodeUrl(String toEncode) {
        int pathDivider = toEncode.lastIndexOf('/') + 1;
        String fileName = toEncode.substring(pathDivider);
        String rootUrl = toEncode.substring(0, pathDivider);

        int commandDivider = fileName.indexOf('?');
        if (commandDivider < 0) {
            return encodeFileName(rootUrl, fileName);
        }

        String command = fileName.substring(0, commandDivider);
        String query = fileName.substring(commandDivider + 1);
        String[] params = query.split("&");
        if (params.length > 0) {
            return encodeQuery(rootUrl, command, params);
        }

        return encodeFileName(rootUrl, fileName);
    }

    /**
     * Encodes a query URL, where the command is separated with '?', parameters with '&' and each key value pair with '='
     *
     * @param rootUrl Root of the Url up until the command.
     * @param command Query command
     * @param params Array of key=value pairs in each of the elements of the array
     *
     * @return A correctly escaped URL where the query command, the parameters (both name and value) are escaped individually.
     */
    public static String encodeQuery(String rootUrl, String command, String[] params) {
        StringBuilder result = new StringBuilder(rootUrl);
        result.append(encode(command));
        result.append("?");
        boolean isFirst = true;
        for (String param : params) {
            if (isFirst) {
                isFirst = false;
            } else {
                result.append("&");
            }
            int valueDivider = param.indexOf("=");
            if (valueDivider < 0) {
                result.append(encode(param));
                continue;
            }
            result.append(encode(param.substring(0, valueDivider)));
            result.append("=");
            result.append(encode(param.substring(valueDivider + 1)));
        }
        return result.toString();
    }

    /**
     * <p>Encodes the fileName part of the raw URL. For example, encoding (space) as %20, and so on.
     * The filename is considered to begin after the last '/' so if the url contains a query it will be
     * encoded as it if were a file name. For example, in the below link:</p>
     * <p>http://somehost/someCommand?someVariable=someValue&someOtherVariable=someOtherValue</p>
     * <p>The filename would be: someCommand?someVariable=someValue&someOtherVariable=someOtherValue</p>
     * <p>So this means that the URL to be encoded is not meant to be a query or the filename will be
     * misinterpreted.</p>
     *
     * @param rootUrl root URL up to the filename including the trailing / to encode
     * @param fileName fileName to encode
     *
     * @return Encoded URL string
     */
    public static String encodeFileName(String rootUrl, String fileName) {
        return rootUrl + encode(fileName);
    }

    /**
     * Decodes the URL string encoded with the encode method.
     *
     * @param encodedUrl encoded URL
     *
     * @return Decoded URL as a string
     */
    public static String decodeUrl(String encodedUrl) {
        int pathDivider = encodedUrl.lastIndexOf('/') + 1;
        String fileName = encodedUrl.substring(pathDivider);
        String rootUrl = encodedUrl.substring(0, pathDivider);
        try {
            return rootUrl + URLDecoder.decode(fileName, URL_ENCODING);
        } catch (UnsupportedEncodingException e) {
            return encodedUrl;
        }
    }

    /**
     * Generic URL encoding with the additional caveat that the + sign, which is a
     * valid url character get escaped as well.
     *
     * @param value Value to encode
     * @return URL Encoded value
     *
     * @throws RuntimeException This should never happen but if the encoding is not valid this
     * exception will be thrown.
     */
    private static String encode(String value) {
        try {
            return URLEncoder.encode(value, URL_ENCODING).replaceAll("\\+", "%20");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Unable to encode value: " + value, e);
        }
    }

}
