package com.expedia.content.media.processing.pipeline.util;

import com.expedia.content.media.processing.pipeline.domain.ImageMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

import static com.expedia.content.media.processing.pipeline.util.LoggerUtil.buildLogMessage;
import static com.expedia.content.media.processing.pipeline.util.LoggerUtil.formatLogMessage;

/**
 * Logger Wrapper for writing Splunk Formatted logs
 *
 * When logging exceptions, put them first, they will be logged in the following format:
 * - LogMessage Error=\"STACK_TRACE\"
 */
public class FormattedLogger {

    private Logger logger;

    public FormattedLogger(Class<?> clazz) {
        this.logger = LoggerFactory.getLogger(clazz);
    }

    /**
     * Returns the logger initiated by the constructor
     *
     * @return logger
     */
    public Logger getLogger() {
        return this.logger;
    }

    /**
     * Log a Message at the INFO level.
     *
     * @param message         message to log.
     * @param imageMessage    image message to extract fields from for the log.
     * @param fieldsToExtract optional additional fields to extract from the imageMessage.
     */
    public void info(String message, ImageMessage imageMessage, Object... fieldsToExtract) {
        logger.info(buildLogMessage(message, imageMessage, fieldsToExtract));
    }

    /**
     * Log a Message at the INFO level.
     *
     * @param format          with a message and formatted fields to fill with additionalValues.
     * @param formatValues    values to inject into the format.
     * @param imageMessage    image message to extract fields from for the log.
     * @param fieldsToExtract optional additional fields to extract from the imageMessage.
     */
    public void info(String format, List<Object> formatValues, ImageMessage imageMessage, Object... fieldsToExtract) {
        List<String> stringOfFormatValues = formatValues.stream().map(String::valueOf).collect(Collectors.toList());
        logger.info(buildLogMessage(format, stringOfFormatValues, imageMessage, fieldsToExtract));
    }

    /**
     * Log a Message at the INFO level.
     *
     * @param t               a throwable exception to log.
     * @param message         message to log.
     * @param imageMessage    image message to extract fields from for the log.
     * @param fieldsToExtract optional additional fields to extract from the imageMessage.
     */
    public void info(Throwable t, String message, ImageMessage imageMessage, Object... fieldsToExtract) {
        logger.info(buildLogMessage(t, message, imageMessage, fieldsToExtract));
    }

    /**
     * Log a Message at the INFO level.
     *
     * @param t               a throwable exception to log.
     * @param format          with a message and formatted fields to fill with additionalValues.
     * @param formatValues    values to inject into the format.
     * @param imageMessage    image message to extract fields from for the log.
     * @param fieldsToExtract optional additional fields to extract from the imageMessage.
     */
    public void info(Throwable t, String format, List<Object> formatValues, ImageMessage imageMessage, Object... fieldsToExtract) {
        List<String> stringOfFormatValues = formatValues.stream().map(String::valueOf).collect(Collectors.toList());
        logger.info(buildLogMessage(t, format, stringOfFormatValues, imageMessage, fieldsToExtract));
    }

    /**
     * Log a Message at the INFO level.
     *
     * @param message         message to log.
     * @param jsonMessage     JSON message to extract fields from for the log.
     * @param fieldsToExtract optional additional fields to extract from the JSONMessage.
     */
    public void info(String message, String jsonMessage, Object... fieldsToExtract) {
        logger.info(buildLogMessage(message, jsonMessage, fieldsToExtract));
    }

    /**
     * Log a Message at the INFO level.
     *
     * @param format          with a message and formatted fields to fill with additionalValues.
     * @param formatValues    values to inject into the format.
     * @param jsonMessage     JsonMessage being logged.
     * @param fieldsToExtract optional additional fields to extract from the JSONMessage.
     */
    public void info(String format, List<Object> formatValues, String jsonMessage, Object... fieldsToExtract) {
        List<String> stringOfFormatValues = formatValues.stream().map(String::valueOf).collect(Collectors.toList());
        logger.info(buildLogMessage(format, stringOfFormatValues, jsonMessage, fieldsToExtract));
    }

    /**
     * Log a Message at the INFO level.
     *
     * @param t               a throwable exception to log.
     * @param message         message to log.
     * @param jsonMessage     JSON message to extract fields from for the log.
     * @param fieldsToExtract optional additional fields to extract from the JSONMessage.
     */
    public void info(Throwable t, String message, String jsonMessage, Object... fieldsToExtract) {
        logger.info(buildLogMessage(t, message, jsonMessage, fieldsToExtract));
    }

    /**
     * Log a Message at the INFO level.
     *
     * @param t               a throwable exception to log.
     * @param format          with a message and formatted fields to fill with additionalValues.
     * @param formatValues    values to inject into the format.
     * @param jsonMessage     JsonMessage being logged.
     * @param fieldsToExtract optional additional fields to extract from the JSONMessage.
     */
    public void info(Throwable t, String format, List<Object> formatValues, String jsonMessage, Object... fieldsToExtract) {
        List<String> stringOfFormatValues = formatValues.stream().map(String::valueOf).collect(Collectors.toList());
        logger.info(buildLogMessage(t, format, stringOfFormatValues, jsonMessage, fieldsToExtract));
    }

    /**
     * Log a Message at the INFO level.
     *
     * @param message message to log.
     * @param values  values to log.
     * @param fields  fields or keys to log.
     */
    public void info(String message, List<Object> values, Object... fields) {
        logger.info(formatLogMessage(message, values, fields));
    }

    /**
     * Log a Message at the INFO level.
     *
     * @param t       a throwable exception to log.
     * @param message message to log.
     * @param values  values to log.
     * @param fields  fields or keys to log.
     */
    public void info(Throwable t, String message, List<Object> values, Object... fields) {
        logger.info(formatLogMessage(t, message, values, fields));
    }

    /**
     * Log a Message at the INFO level.
     * A format is encourage to be in the following form:
     * "\"message goes here\" Field1=\"{}\" \"Field With A Space\"=\"{}\" Field2=\"{}\" ... FieldN=\"{}\""
     *
     * @param format A String format using {} to template arguments.
     * @param args   arguments to put in the format.
     */
    public void info(String format, Object... args) {
        logger.info(LoggerUtil.formatLogMessage(format, args));
    }

    /**
     * Log a Message at the INFO level.
     * A format is encourage to be in the following form:
     * "\"message goes here\" Field1=\"{}\" \"Field With A Space\"=\"{}\" Field2=\"{}\" ... FieldN=\"{}\""
     *
     * @param t      a throwable exception to log.
     * @param format A String format using {} to template arguments.
     * @param args   arguments to put in the format.
     */
    public void info(Throwable t, String format, Object... args) {
        logger.info(formatLogMessage(t, format, args));
    }

    /**
     * Log a Message at the WARN level.
     *
     * @param message         message to log.
     * @param imageMessage    image message to extract fields from for the log.
     * @param fieldsToExtract optional additional fields to extract from the imageMessage.
     */
    public void warn(String message, ImageMessage imageMessage, Object... fieldsToExtract) {
        logger.warn(buildLogMessage(message, imageMessage, fieldsToExtract));
    }

    /**
     * Log a Message at the WARN level.
     *
     * @param format          with a message and formatted fields to fill with additionalValues.
     * @param formatValues    values to inject into the format.
     * @param imageMessage    image message to extract fields from for the log.
     * @param fieldsToExtract optional additional fields to extract from the imageMessage.
     */
    public void warn(String format, List<Object> formatValues, ImageMessage imageMessage, Object... fieldsToExtract) {
        List<String> stringOfFormatValues = formatValues.stream().map(String::valueOf).collect(Collectors.toList());
        logger.warn(buildLogMessage(format, stringOfFormatValues, imageMessage, fieldsToExtract));
    }

    /**
     * Log a Message at the WARN level.
     *
     * @param t               a throwable exception to log.
     * @param message         message to log.
     * @param imageMessage    image message to extract fields from for the log.
     * @param fieldsToExtract optional additional fields to extract from the imageMessage.
     */
    public void warn(Throwable t, String message, ImageMessage imageMessage, Object... fieldsToExtract) {
        logger.warn(buildLogMessage(t, message, imageMessage, fieldsToExtract));
    }

    /**
     * Log a Message at the WARN level.
     *
     * @param t               a throwable exception to log.
     * @param format          with a message and formatted fields to fill with additionalValues.
     * @param formatValues    values to inject into the format.
     * @param imageMessage    image message to extract fields from for the log.
     * @param fieldsToExtract optional additional fields to extract from the imageMessage.
     */
    public void warn(Throwable t, String format, List<Object> formatValues, ImageMessage imageMessage, Object... fieldsToExtract) {
        List<String> stringOfFormatValues = formatValues.stream().map(String::valueOf).collect(Collectors.toList());
        logger.warn(buildLogMessage(t, format, stringOfFormatValues, imageMessage, fieldsToExtract));
    }

    /**
     * Log a Message at the WARN level.
     *
     * @param message         message to log.
     * @param jsonMessage     JSON message to extract fields from for the log.
     * @param fieldsToExtract optional additional fields to extract from the JSONMessage.
     */
    public void warn(String message, String jsonMessage, Object... fieldsToExtract) {
        logger.warn(buildLogMessage(message, jsonMessage, fieldsToExtract));
    }

    /**
     * Log a Message at the WARN level.
     *
     * @param format          with a message and formatted fields to fill with additionalValues.
     * @param formatValues    values to inject into the format.
     * @param jsonMessage     JsonMessage being logged.
     * @param fieldsToExtract optional additional fields to extract from the JSONMessage.
     */
    public void warn(String format, List<Object> formatValues, String jsonMessage, Object... fieldsToExtract) {
        List<String> stringOfFormatValues = formatValues.stream().map(String::valueOf).collect(Collectors.toList());
        logger.warn(buildLogMessage(format, stringOfFormatValues, jsonMessage, fieldsToExtract));
    }

    /**
     * Log a Message at the WARN level.
     *
     * @param t               a throwable exception to log.
     * @param message         message to log.
     * @param jsonMessage     JSON message to extract fields from for the log.
     * @param fieldsToExtract optional additional fields to extract from the JSONMessage.
     */
    public void warn(Throwable t, String message, String jsonMessage, Object... fieldsToExtract) {
        logger.warn(buildLogMessage(t, message, jsonMessage, fieldsToExtract));
    }

    /**
     * Log a Message at the WARN level.
     *
     * @param t               a throwable exception to log.
     * @param format          with a message and formatted fields to fill with additionalValues.
     * @param formatValues    values to inject into the format.
     * @param jsonMessage     JsonMessage being logged.
     * @param fieldsToExtract optional additional fields to extract from the JSONMessage.
     */
    public void warn(Throwable t, String format, List<Object> formatValues, String jsonMessage, Object... fieldsToExtract) {
        List<String> stringOfFormatValues = formatValues.stream().map(String::valueOf).collect(Collectors.toList());
        logger.warn(buildLogMessage(t, format, stringOfFormatValues, jsonMessage, fieldsToExtract));
    }

    /**
     * Log a Message at the WARN level.
     *
     * @param message message to log.
     * @param values  values to log.
     * @param fields  fields or keys to log.
     */
    public void warn(String message, List<Object> values, Object... fields) {
        logger.warn(formatLogMessage(message, values, fields));
    }

    /**
     * Log a Message at the WARN level.
     *
     * @param t       a throwable exception to log.
     * @param message message to log.
     * @param values  values to log.
     * @param fields  fields or keys to log.
     */
    public void warn(Throwable t, String message, List<Object> values, Object... fields) {
        logger.warn(formatLogMessage(t, message, values, fields));
    }

    /**
     * Log a Message at the WARN level.
     * A format is encourage to be in the following form:
     * "\"message goes here\" Field1=\"{}\" \"Field With A Space\"=\"{}\" Field2=\"{}\" ... FieldN=\"{}\""
     *
     * @param format A String format using {} to template arguments.
     * @param args   arguments to put in the format.
     */
    public void warn(String format, Object... args) {
        logger.warn(LoggerUtil.formatLogMessage(format, args));
    }

    /**
     * Log a Message at the WARN level.
     * A format is encourage to be in the following form:
     * "\"message goes here\" Field1=\"{}\" \"Field With A Space\"=\"{}\" Field2=\"{}\" ... FieldN=\"{}\""
     *
     * @param t      a throwable exception to log.
     * @param format A String format using {} to template arguments.
     * @param args   arguments to put in the format.
     */
    public void warn(Throwable t, String format, Object... args) {
        logger.warn(formatLogMessage(t, format, args));
    }

    /**
     * Log a Message at the DEBUG level.
     *
     * @param message         message to log.
     * @param imageMessage    image message to extract fields from for the log.
     * @param fieldsToExtract optional additional fields to extract from the imageMessage.
     */
    public void debug(String message, ImageMessage imageMessage, Object... fieldsToExtract) {
        logger.debug(buildLogMessage(message, imageMessage, fieldsToExtract));
    }

    /**
     * Log a Message at the DEBUG level.
     *
     * @param format          with a message and formatted fields to fill with additionalValues.
     * @param formatValues    values to inject into the format.
     * @param imageMessage    image message to extract fields from for the log.
     * @param fieldsToExtract optional additional fields to extract from the imageMessage.
     */
    public void debug(String format, List<Object> formatValues, ImageMessage imageMessage, Object... fieldsToExtract) {
        List<String> stringOfFormatValues = formatValues.stream().map(String::valueOf).collect(Collectors.toList());
        logger.debug(buildLogMessage(format, stringOfFormatValues, imageMessage, fieldsToExtract));
    }

    /**
     * Log a Message at the DEBUG level.
     *
     * @param t               a throwable exception to log.
     * @param message         message to log.
     * @param imageMessage    image message to extract fields from for the log.
     * @param fieldsToExtract optional additional fields to extract from the imageMessage.
     */
    public void debug(Throwable t, String message, ImageMessage imageMessage, Object... fieldsToExtract) {
        logger.debug(buildLogMessage(t, message, imageMessage, fieldsToExtract));
    }

    /**
     * Log a Message at the DEBUG level.
     *
     * @param t               a throwable exception to log.
     * @param format          with a message and formatted fields to fill with additionalValues.
     * @param formatValues    values to inject into the format.
     * @param imageMessage    image message to extract fields from for the log.
     * @param fieldsToExtract optional additional fields to extract from the imageMessage.
     */
    public void debug(Throwable t, String format, List<Object> formatValues, ImageMessage imageMessage, Object... fieldsToExtract) {
        List<String> stringOfFormatValues = formatValues.stream().map(String::valueOf).collect(Collectors.toList());
        logger.debug(buildLogMessage(t, format, stringOfFormatValues, imageMessage, fieldsToExtract));
    }

    /**
     * Log a Message at the DEBUG level.
     *
     * @param message         message to log.
     * @param jsonMessage     JSON message to extract fields from for the log.
     * @param fieldsToExtract optional additional fields to extract from the JSONMessage.
     */
    public void debug(String message, String jsonMessage, Object... fieldsToExtract) {
        logger.debug(buildLogMessage(message, jsonMessage, fieldsToExtract));
    }

    /**
     * Log a Message at the DEBUG level.
     *
     * @param format          with a message and formatted fields to fill with additionalValues.
     * @param formatValues    values to inject into the format.
     * @param jsonMessage     JsonMessage being logged.
     * @param fieldsToExtract optional additional fields to extract from the JSONMessage.
     */
    public void debug(String format, List<Object> formatValues, String jsonMessage, Object... fieldsToExtract) {
        List<String> stringOfFormatValues = formatValues.stream().map(String::valueOf).collect(Collectors.toList());
        logger.debug(buildLogMessage(format, stringOfFormatValues, jsonMessage, fieldsToExtract));
    }

    /**
     * Log a Message at the DEBUG level.
     *
     * @param t               a throwable exception to log.
     * @param message         message to log.
     * @param jsonMessage     JSON message to extract fields from for the log.
     * @param fieldsToExtract optional additional fields to extract from the JSONMessage.
     */
    public void debug(Throwable t, String message, String jsonMessage, Object... fieldsToExtract) {
        logger.debug(buildLogMessage(t, message, jsonMessage, fieldsToExtract));
    }

    /**
     * Log a Message at the DEBUG level.
     *
     * @param t               a throwable exception to log.
     * @param format          with a message and formatted fields to fill with additionalValues.
     * @param formatValues    values to inject into the format.
     * @param jsonMessage     JsonMessage being logged.
     * @param fieldsToExtract optional additional fields to extract from the JSONMessage.
     */
    public void debug(Throwable t, String format, List<Object> formatValues, String jsonMessage, Object... fieldsToExtract) {
        List<String> stringOfFormatValues = formatValues.stream().map(String::valueOf).collect(Collectors.toList());
        logger.debug(buildLogMessage(t, format, stringOfFormatValues, jsonMessage, fieldsToExtract));
    }

    /**
     * Log a Message at the DEBUG level.
     *
     * @param message message to log.
     * @param values  values to log.
     * @param fields  fields or keys to log.
     */
    public void debug(String message, List<Object> values, Object... fields) {
        logger.debug(formatLogMessage(message, values, fields));
    }

    /**
     * Log a Message at the DEBUG level.
     *
     * @param t       a throwable exception to log.
     * @param message message to log.
     * @param values  values to log.
     * @param fields  fields or keys to log.
     */
    public void debug(Throwable t, String message, List<Object> values, Object... fields) {
        logger.debug(formatLogMessage(t, message, values, fields));
    }

    /**
     * Log a Message at the DEBUG level.
     * A format is encourage to be in the following form:
     * "\"message goes here\" Field1=\"{}\" \"Field With A Space\"=\"{}\" Field2=\"{}\" ... FieldN=\"{}\""
     *
     * @param format A String format using {} to template arguments.
     * @param args   arguments to put in the format.
     */
    public void debug(String format, Object... args) {
        logger.debug(LoggerUtil.formatLogMessage(format, args));
    }

    /**
     * Log a Message at the DEBUG level.
     * A format is encourage to be in the following form:
     * "\"message goes here\" Field1=\"{}\" \"Field With A Space\"=\"{}\" Field2=\"{}\" ... FieldN=\"{}\""
     *
     * @param t      a throwable exception to log.
     * @param format A String format using {} to template arguments.
     * @param args   arguments to put in the format.
     */
    public void debug(Throwable t, String format, Object... args) {
        logger.debug(formatLogMessage(t, format, args));
    }

    /**
     * Log a Message at the ERROR level.
     *
     * @param message         message to log.
     * @param imageMessage    image message to extract fields from for the log.
     * @param fieldsToExtract optional additional fields to extract from the imageMessage.
     */
    public void error(String message, ImageMessage imageMessage, Object... fieldsToExtract) {
        logger.error(buildLogMessage(message, imageMessage, fieldsToExtract));
    }

    /**
     * Log a Message at the ERROR level.
     *
     * @param format          with a message and formatted fields to fill with additionalValues.
     * @param formatValues    values to inject into the format.
     * @param imageMessage    image message to extract fields from for the log.
     * @param fieldsToExtract optional additional fields to extract from the imageMessage.
     */
    public void error(String format, List<Object> formatValues, ImageMessage imageMessage, Object... fieldsToExtract) {
        List<String> stringOfFormatValues = formatValues.stream().map(String::valueOf).collect(Collectors.toList());
        logger.error(buildLogMessage(format, stringOfFormatValues, imageMessage, fieldsToExtract));
    }

    /**
     * Log a Message at the ERROR level.
     *
     * @param t               a throwable exception to log.
     * @param message         message to log.
     * @param imageMessage    image message to extract fields from for the log.
     * @param fieldsToExtract optional additional fields to extract from the imageMessage.
     */
    public void error(Throwable t, String message, ImageMessage imageMessage, Object... fieldsToExtract) {
        logger.error(buildLogMessage(t, message, imageMessage, fieldsToExtract));
    }

    /**
     * Log a Message at the ERROR level.
     *
     * @param t               a throwable exception to log.
     * @param format          with a message and formatted fields to fill with additionalValues.
     * @param formatValues    values to inject into the format.
     * @param imageMessage    image message to extract fields from for the log.
     * @param fieldsToExtract optional additional fields to extract from the imageMessage.
     */
    public void error(Throwable t, String format, List<Object> formatValues, ImageMessage imageMessage, Object... fieldsToExtract) {
        List<String> stringOfFormatValues = formatValues.stream().map(String::valueOf).collect(Collectors.toList());
        logger.error(buildLogMessage(t, format, stringOfFormatValues, imageMessage, fieldsToExtract));
    }

    /**
     * Log a Message at the ERROR level.
     *
     * @param message         message to log.
     * @param jsonMessage     JSON message to extract fields from for the log.
     * @param fieldsToExtract optional additional fields to extract from the JSONMessage.
     */
    public void error(String message, String jsonMessage, Object... fieldsToExtract) {
        logger.error(buildLogMessage(message, jsonMessage, fieldsToExtract));
    }

    /**
     * Log a Message at the ERROR level.
     *
     * @param format          with a message and formatted fields to fill with additionalValues.
     * @param formatValues    values to inject into the format.
     * @param jsonMessage     JsonMessage being logged.
     * @param fieldsToExtract optional additional fields to extract from the JSONMessage.
     */
    public void error(String format, List<Object> formatValues, String jsonMessage, Object... fieldsToExtract) {
        List<String> stringOfFormatValues = formatValues.stream().map(String::valueOf).collect(Collectors.toList());
        logger.error(buildLogMessage(format, stringOfFormatValues, jsonMessage, fieldsToExtract));
    }

    /**
     * Log a Message at the ERROR level.
     *
     * @param t               a throwable exception to log.
     * @param message         message to log.
     * @param jsonMessage     JSON message to extract fields from for the log.
     * @param fieldsToExtract optional additional fields to extract from the JSONMessage.
     */
    public void error(Throwable t, String message, String jsonMessage, Object... fieldsToExtract) {
        logger.error(buildLogMessage(t, message, jsonMessage, fieldsToExtract));
    }

    /**
     * Log a Message at the ERROR level.
     *
     * @param t               a throwable exception to log.
     * @param format          with a message and formatted fields to fill with additionalValues.
     * @param formatValues    values to inject into the format.
     * @param jsonMessage     JsonMessage being logged.
     * @param fieldsToExtract optional additional fields to extract from the JSONMessage.
     */
    public void error(Throwable t, String format, List<Object> formatValues, String jsonMessage, Object... fieldsToExtract) {
        List<String> stringOfFormatValues = formatValues.stream().map(String::valueOf).collect(Collectors.toList());
        logger.error(buildLogMessage(t, format, stringOfFormatValues, jsonMessage, fieldsToExtract));
    }

    /**
     * Log a Message at the ERROR level.
     *
     * @param message message to log.
     * @param values  values to log.
     * @param fields  fields or keys to log.
     */
    public void error(String message, List<Object> values, Object... fields) {
        logger.error(formatLogMessage(message, values, fields));
    }

    /**
     * Log a Message at the ERROR level.
     *
     * @param t       a throwable exception to log.
     * @param message message to log.
     * @param values  values to log.
     * @param fields  fields or keys to log.
     */
    public void error(Throwable t, String message, List<Object> values, Object... fields) {
        logger.error(formatLogMessage(t, message, values, fields));
    }

    /**
     * Log a Message at the ERROR level.
     * A format is encourage to be in the following form:
     * "\"message goes here\" Field1=\"{}\" \"Field With A Space\"=\"{}\" Field2=\"{}\" ... FieldN=\"{}\""
     *
     * @param format A String format using {} to template arguments.
     * @param args   arguments to put in the format.
     */
    public void error(String format, Object... args) {
        logger.error(LoggerUtil.formatLogMessage(format, args));
    }

    /**
     * Log a Message at the ERROR level.
     * A format is encourage to be in the following form:
     * "\"message goes here\" Field1=\"{}\" \"Field With A Space\"=\"{}\" Field2=\"{}\" ... FieldN=\"{}\""
     *
     * @param t      a throwable exception to log.
     * @param format A String format using {} to template arguments.
     * @param args   arguments to put in the format.
     */
    public void error(Throwable t, String format, Object... args) {
        logger.error(formatLogMessage(t, format, args));
    }
}