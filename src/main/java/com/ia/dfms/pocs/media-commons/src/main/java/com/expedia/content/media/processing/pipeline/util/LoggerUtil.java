package com.expedia.content.media.processing.pipeline.util;

import com.expedia.content.media.processing.pipeline.domain.ImageMessage;
import com.expedia.content.media.processing.pipeline.domain.MessageConstants;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Util class for Logger Messages.
 *
 * -----------------------------------------------------------------------------
 * Splunk Log best practices:
 * - Keys and Values should be in Key=Value pairs separated by commas or spaces.
 * - If a Value has a space, wrap it in quotes. This helps with indexing.
 * -----------------------------------------------------------------------------
 *
 */
// CHECKSTYLE:OFF
public final class LoggerUtil {

    // Default fields include: MediaId (the media guid), RequestId, ClientId, FileName, and the FileUrl
    private static final List<String> DEFAULT_FIELDS = new ArrayList<>(Arrays.asList(MessageConstants.MEDIA_ID, MessageConstants.REQUEST_ID,
            MessageConstants.CLIENT_ID, MessageConstants.FILE_NAME, MessageConstants.FILE_URL, MessageConstants.OUTER_DOMAIN_NAME));

    private LoggerUtil() {
        // no-op
    }

    /**
     * formats imageMessage exposing top level fieldsToExtract of an ImageMessage for the log.
     * @param message log message.
     * @param imageMessage imageMessage to log.
     * @param fieldsToExtract MessageConstant additional fields to extract from the ImageMessage.
     *               i.e. in the collector we may want to extract Metadata.
     *               Metadata is not a default Field to extract.
     * @MessageConstants are encouraged for fields.
     * @return formatted log message String.
     */
    public static String buildLogMessage(String message, ImageMessage imageMessage, Object... fieldsToExtract) {
        return parseMessageInFormat(message + " " + parseImageMessage(imageMessage, fieldsToExtract));
    }

    /**
     * formats imageMessage exposing top level fields of an ImageMessage for the log.
     * @param format with a message and formatted fields to fill with additionalValues.
     * @param formatValues values that are not in an ImageMessage.
     * @param imageMessage imageMessage to log.
     * @param fieldsToExtract MessageConstant additional fields to extract from the ImageMessage.
     *               i.e. in the collector we may want to extract Metadata.
     *               Metadata is not a default Field to extract.
     * @MessageConstants are encouraged for fields.
     * @return formatted log message String.
     */
    public static String buildLogMessage(String format, List<String> formatValues, ImageMessage imageMessage, Object... fieldsToExtract) {
        return formatLogMessage(format, formatValues.toArray()) + " " + parseImageMessage(imageMessage, fieldsToExtract);
    }

    /**
     * formats imageMessage exposing top level fields of an ImageMessage for the log.
     * @param t exception to log.
     * @param message log message.
     * @param imageMessage imageMessage being logged.
     * @param fields MessageConstant additional fields to extract from the ImageMessage.
     *               i.e. in the collector we may want to extract Metadata.
     *               Metadata is not a default Field to extract.
     * @MessageConstants are encouraged for fields.
     * @return formatted log message String.
     */
    public static String buildLogMessage(Throwable t, String message, ImageMessage imageMessage, Object... fields) {
        return parseFormat(message + " " + parseImageMessage(imageMessage, fields) + "Error=\"{}\"", getStackTraceString(t));
    }

    /**
     * formats imageMessage exposing top level fields of an ImageMessage for the log.
     * @param t exception to log.
     * @param format with a message and formatted fields to fill with additionalValues.
     * @param formatValues values that are not in an ImageMessage.
     * @param imageMessage imageMessage to log.
     * @param fields MessageConstant additional fields to extract from the ImageMessage.
     *               i.e. in the collector we may want to extract Metadata.
     *               Metadata is not a default Field to extract.
     * @MessageConstants are encouraged for fields.
     * @return formatted log message String.
     */
    public static String buildLogMessage(Throwable t, String format, List<String> formatValues, ImageMessage imageMessage, Object... fields) {
        return parseFormat(formatLogMessage(format, formatValues.toArray()) + " " + parseImageMessage(imageMessage, fields) + "Error=\"{}\"", getStackTraceString(t));
    }

    /**
     * formats jsonMessage exposing top level fields of an ImageMessage for the log.
     * @param message log message.
     * @param jsonMessage JsonMessage being logged.
     * @param fields MessageConstant additional fields to extract from the JsonMessage.
     *               i.e. in the collector we may want to extract Metadata.
     *               Metadata is not a default Field to extract.
     * @MessageConstants are encouraged for fields.
     * @return formatted log message String.
     */
    public static String buildLogMessage(String message, String jsonMessage, Object... fields) {
        if (StringUtils.startsWith(jsonMessage, "{") && StringUtils.endsWith(jsonMessage, "}") && jsonMessage.contains(":") && !message.contains("{}")) {
            return parseMessageInFormat(message + " " + parseJsonMessage(jsonMessage, fields));
        } else {
            List<String> fieldsList = Stream.of(fields).map(String::valueOf).collect(Collectors.toList());
            fieldsList.add(0, jsonMessage);
            return formatLogMessage(message, fieldsList.toArray());
        }
    }

    /**
     * formats jsonMessage exposing top level fields of an ImageMessage for the log.
     * @param format with a message and formatted fields to fill with additionalValues.
     * @param formatValues values that are not in an ImageMessage.
     * @param jsonMessage JsonMessage being logged.
     * @param fields MessageConstant additional fields to extract from the JsonMessage.
     *               i.e. in the collector we may want to extract Metadata.
     *               Metadata is not a default Field to extract.
     * @MessageConstants are encouraged for fields.
     * @return formatted log message String.
     */
    public static String buildLogMessage(String format, List<String> formatValues, String jsonMessage, Object... fields) {
        if (StringUtils.startsWith(jsonMessage, "{") && StringUtils.endsWith(jsonMessage, "}") && jsonMessage.contains(":")) {
            return formatLogMessage(format, formatValues.toArray()) + " " + parseJsonMessage(jsonMessage, fields);
        } else {
            List<String> fieldsList = Stream.of(fields).map(String::valueOf).collect(Collectors.toList());
            fieldsList.add(0, jsonMessage);
            return formatLogMessage(format, formatValues, fieldsList.toArray());
        }
    }

    /**
     * formats jsonMessage exposing top level fields of an ImageMessage for the log.
     * @param t exception to log.
     * @param message log message.
     * @param jsonMessage JsonMessage being logged.
     * @param fields MessageConstant additional fields to extract from the JsonMessage.
     *               i.e. in the collector we may want to extract Metadata.
     *               Metadata is not a default Field to extract.
     * @MessageConstants are encouraged for fields.
     * @return formatted log message String.
     */
    public static String buildLogMessage(Throwable t, String message, String jsonMessage, Object... fields) {
        if (StringUtils.startsWith(jsonMessage, "{") && StringUtils.endsWith(jsonMessage, "}") && jsonMessage.contains(":") && !message.contains("{}")) {
            return parseFormat(message + " " + parseJsonMessage(jsonMessage, fields) + "Error=\"{}\"", getStackTraceString(t));
        } else {
            List<String> fieldsList = Stream.of(fields).map(String::valueOf).collect(Collectors.toList());
            fieldsList.add(0, jsonMessage);
            return formatLogMessage(t, message, fieldsList.toArray());
        }
    }

    /**
     * formats jsonMessage exposing top level fields of an ImageMessage for the log.
     * @param t exception to log.
     * @param format with a message and formatted fields to fill with additionalValues.
     * @param formatValues values that are not in an ImageMessage.
     * @param jsonMessage JsonMessage being logged.
     * @param fields MessageConstant additional fields to extract from the JsonMessage.
     *               i.e. in the collector we may want to extract Metadata.
     *               Metadata is not a default Field to extract.
     * @MessageConstants are encouraged for fields.
     * @return formatted log message String.
     */
    public static String buildLogMessage(Throwable t, String format, List<String> formatValues, String jsonMessage, Object... fields) {
        if (StringUtils.startsWith(jsonMessage, "{") && StringUtils.endsWith(jsonMessage, "}") && jsonMessage.contains(":")) {
            return parseFormat(formatLogMessage(format, formatValues.toArray()) + " " + parseJsonMessage(jsonMessage, fields) + "Error=\"{}\"", getStackTraceString(t));
        } else {
            List<String> fieldsList = Stream.of(fields).map(String::valueOf).collect(Collectors.toList());
            fieldsList.add(0, jsonMessage);
            return formatLogMessage(t, format, formatValues, fieldsList.toArray());
        }
    }

    /**
     * builds a formatted log string.
     * @param message Message to log.
     * @param values values to fill the fields.
     * @param fields Fields to log.
     * @MessageConstants are encouraged for fields.
     * @return formatted log message String.
     */
    public static String formatLogMessage(String message, List<String> values, Object... fields) {
        String[] stringsOfFields = Stream.of(fields).map(String::valueOf).toArray(String[]::new);
        if (stringsOfFields.length >= 1 && StringUtils.startsWith(stringsOfFields[0], "{") && StringUtils.endsWith(stringsOfFields[0], "}") && stringsOfFields[0].contains(":")) {
            return buildLogMessage(message, values, stringsOfFields[0], Arrays.copyOfRange(stringsOfFields, 1, stringsOfFields.length));
        }
        List<String> fieldsList = Stream.of(fields).map(String::valueOf).collect(Collectors.toList());
        StringBuilder sb = new StringBuilder();
        int range = Math.min(values.size(), fields.length);
        IntStream.range(0, range).forEach(i -> sb.append(StringUtils.capitalize(fieldsList.get(i)) + "=\"{}\" "));
        return parseFormat(message + " " + sb.toString(), values.toArray());
    }

    /**
     * builds a formatted log string.
     * @param t exception to log.
     * @param message Message to log.
     * @param values values to fill the fields.
     * @param fields Fields to log.
     * @MessageConstants are encouraged for fields.
     * @return formatted log message String.
     */
    public static String formatLogMessage(Throwable t, String message, List<String> values, Object... fields) {
        List<String> fieldsList = Stream.of(fields).map(String::valueOf).collect(Collectors.toList());
        StringBuilder sb = new StringBuilder();
        int range = Math.min(values.size(), fields.length);
        IntStream.range(0, range).forEach(i -> sb.append(StringUtils.capitalize(fieldsList.get(i)) + "=\"{}\" "));
        values = new ArrayList<>(values);
        values.add(getStackTraceString(t));
        return parseFormat(message + " " + sb.toString() + "Error=\"{}\"", values.toArray());
    }

    /**
     * builds a formatted log string.
     * @param format format to parse.
     * @param args arguments to populate the format with.
     * @return formatted log message String.
     */
    public static String formatLogMessage(String format, Object... args) {
        String[] stringsOfArgs = Stream.of(args).map(String::valueOf).toArray(String[]::new);
        if (stringsOfArgs.length >= 1 && StringUtils.startsWith(stringsOfArgs[0], "{")
                && StringUtils.endsWith(stringsOfArgs[0], "}") && stringsOfArgs[0].contains(":") && !format.contains("{}")) {
            return (stringsOfArgs.length == 1) ? buildLogMessage(format, stringsOfArgs[0]) :
                    buildLogMessage(format, stringsOfArgs[0], Arrays.copyOfRange(stringsOfArgs, 1, stringsOfArgs.length));
        }
        return parseFormat(format, stringsOfArgs);
    }

    /**
     * builds a formatted log string.
     * @param t exception to log.
     * @param format format to parse.
     * @param args arguments to populate the format with.
     * @return formatted log message String.
     */
    public static String formatLogMessage(Throwable t, String format, Object... args) {
        List<String> stringsOfArgs = Stream.of(args).map(String::valueOf).collect(Collectors.toList());
        stringsOfArgs.add(getStackTraceString(t));
        return parseFormat(format + " Error=\"{}\"", stringsOfArgs.toArray());
    }

    /**
     * parses a format with fields converting ("something={} somethingElse={}", val1, val2) to "something=val1 somethingElse=val2".
     * @param format A string curly bracket ({}) formatting.
     * @param args Arguments to place in the string.
     * @return a formatted string.
     */
    public static String parseFormat(String format, Object... args) {
        format = parseMessageInFormat(format);
        List<String> wrappedArgs = Stream.of(args).map(arg -> {
          if (!StringUtils.contains(String.valueOf(arg), " ") && !StringUtils.contains(String.valueOf(arg), "\"")) {
              return String.valueOf(arg);
          } else if (StringUtils.contains(String.valueOf(arg), "\"")) {
              return "[" + String.valueOf(arg) + "]";
          } else {
              return "\"" + String.valueOf(arg) + "\"";
          }
        }).collect(Collectors.toList());
        format = StringUtils.replace(format, "=\"{}\"", "=%s");
        format = StringUtils.replace(format, "={}", "=%s");
        format = StringUtils.replace(format, "{}", "%s");
        if ((StringUtils.countMatches(format, "%s") - args.length) > 0) {
            format = replaceLast(format, "%s", "{}", (StringUtils.countMatches(format, "%s") - args.length));
        }
        return String.format(format, wrappedArgs.toArray());
    }




    /**
     * Replaces the last N Strings that match the regex.
     * @param text String to replace characters in.
     * @param regex String to match.
     * @param replacement String to replace match with.
     * @param times Times to replace last string.
     * @return replaced String.
     */
    private static String replaceLast(String text, String regex, String replacement, Integer times) {
        for (int i = 0; i < times; i++) {
            text = text.replaceFirst("(?s)(.*)" + regex, "$1" + replacement);
        }
        return text;
    }

    /**
     * Finds the Message in a format and surrounds it with quotes if there are none.
     * @param format input format.
     * @return the format with the message surrounded in quotes.
     */
    public static String parseMessageInFormat(String format) {
        String[] elements = StringUtils.split(format, " ");
        Integer nonKeyValues = (int) Stream.of(elements).filter(element -> !StringUtils.contains(element, "=")).count();
        if (nonKeyValues == 0) {
            return format;
        }
        if (StringUtils.contains(format, "=") && !StringUtils.contains(format, " ")) {
            return format;
        }
        if (!StringUtils.contains(format, "=")) {
            return "\"" + format + "\"";
        }
        String message = StringUtils.substring(format, 0, StringUtils.indexOf(format, "="));
        message = StringUtils.substring(format, 0, StringUtils.lastIndexOf(message, " "));
        if (StringUtils.endsWith(message, "\"") && StringUtils.startsWith(message, "\"")) {
            return format;
        }
        return StringUtils.replace(format, message, "\"" + message + "\"");
    }

    /**
     * parses an ImageMessage to log top level fields.
     * @param imageMessage imageMessage to parse.
     * @param fields optional fields to log.
     * @return returns formatted string of imageMessage fields.
     */
    private static String parseImageMessage(ImageMessage imageMessage, Object... fields) {
        if (imageMessage == null) {
            return "ImageMessage=[] ";
        }
        return parseJsonMessage(imageMessage.toJSONMessage(), fields);
    }

    /**
     * parses an JSONMessage to log top level fields.
     * @param jsonMessage jsonMessage to parse.
     * @param fields optional fields to log.
     * @return returns formatted string of imageMessage fields.
     */
    private static String parseJsonMessage(String jsonMessage, Object... fields) {
        if (jsonMessage == null) {
            return "ImageMessage=[] ";
        }
        List<String> optionalFieldsList = Stream.of(fields).map(String::valueOf).collect(Collectors.toList());
        optionalFieldsList.addAll(0, DEFAULT_FIELDS);
        List<String> fieldsList = optionalFieldsList.stream().distinct().collect(Collectors.toList());
        ObjectMapper mapper = new ObjectMapper();
        try {
            Map<String, Object> map = mapper.readValue(jsonMessage, new TypeReference<Map<String, Object>>() { });
            List<String> values = new ArrayList<>();
            StringBuilder sb = new StringBuilder();
            for (String field : fieldsList) {
                String value = String.valueOf(map.get(field));
                if (Strings.isNullOrEmpty(value) || "null".equals(value)) {
                    final Object mapFields = map.get(MessageConstants.OUTER_DOMAIN_FIELDS);
                    Map<String, Object> domainFields = mapFields instanceof Map ? (Map) map.get(MessageConstants.OUTER_DOMAIN_FIELDS) : null;
                    if (domainFields != null) {
                        value = String.valueOf(domainFields.get(field));
                    }
                }
                if (!Strings.isNullOrEmpty(value) && !"null".equals(value)) {
                    sb.append(StringUtils.capitalize(field) + "={} ");
                    values.add(value);
                }
            }
            return (!Strings.isNullOrEmpty(sb.toString())) ? parseFormat(sb.toString(), values.toArray()) : "ImageMessage=[" + jsonMessage + "] ";
        } catch (IOException e) {
            return "ImageMessage=[" + jsonMessage + "] ";
        }
    }

    /**
     * Returns the StackTrace String.
     * @param e exception to print the stacktrace of.
     * @return a stacktrace string.
     */
    public static String getStackTraceString(Throwable e) {
        return getStackTraceString(e, "");
    }

    /**
     * Returns the StackTrace String.
     * @param e exception to print the stacktrace of.
     * @param indent how much to indent each line of the stacktrace.
     * @return a stacktrace string.
     */
    private static String getStackTraceString(Throwable e, String indent) {
        StringBuilder sb = new StringBuilder();
        sb.append(e.toString());
        sb.append("\n");

        StackTraceElement[] stack = e.getStackTrace();
        if (stack != null) {
            for (StackTraceElement stackTraceElement : stack) {
                sb.append(indent);
                sb.append("\tat ");
                sb.append(stackTraceElement.toString());
                sb.append("\n");
            }
        }

        Throwable[] suppressedExceptions = e.getSuppressed();
        // Print suppressed exceptions indented one level deeper.
        if (suppressedExceptions != null) {
            for (Throwable throwable : suppressedExceptions) {
                sb.append(indent);
                sb.append("\tSuppressed: ");
                sb.append(getStackTraceString(throwable, indent + "\t"));
            }
        }

        Throwable cause = e.getCause();
        if (cause != null) {
            sb.append(indent);
            sb.append("Caused by: ");
            sb.append(getStackTraceString(cause, indent));
        }

        return sb.toString();
    }
}
