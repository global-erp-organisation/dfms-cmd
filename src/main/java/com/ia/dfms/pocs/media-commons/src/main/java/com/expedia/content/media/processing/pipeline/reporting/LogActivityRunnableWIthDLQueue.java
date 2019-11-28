package com.expedia.content.media.processing.pipeline.reporting;

import com.expedia.content.media.processing.pipeline.util.FormattedLogger;
import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate;
import org.springframework.messaging.support.MessageBuilder;

/**
 * Logs class that inserts media process log to DB.
 * in case of the exception,the log entry will be written to dead letter queue.
 */
public class LogActivityRunnableWIthDLQueue implements Runnable {

    private static final FormattedLogger LOGGER = new FormattedLogger(LogActivityRunnableWIthDLQueue.class);

    private final LogEntry logEntry;
    private final Reporting reporting;
    private final String deadLetterQueue;
    private QueueMessagingTemplate messagingTemplate;

    public LogActivityRunnableWIthDLQueue(final LogEntry logEntry, final Reporting reporting, final String dlQueue, final QueueMessagingTemplate messagingTemplate) {
        this.logEntry = logEntry;
        this.reporting = reporting;
        this.deadLetterQueue = dlQueue;
        this.messagingTemplate = messagingTemplate;
    }

    @Override
    public void run() {
        try {
            LOGGER.info("++> {}", logEntry);
            reporting.writeLogEntry(logEntry);
        } catch (Exception e) {
            final String domainName = (logEntry.getDomain() == null) ? null : logEntry.getDomain().getDomain();
            final String activityName = (logEntry.getActivity() == null) ? null : logEntry.getActivity().getName();
            messagingTemplate.send(deadLetterQueue, MessageBuilder.withPayload(logEntry.toJSONMessage()).build());
            LOGGER.error(e, "Failed to log activity Activity={} Domain={} DomainId={} FileName={}", activityName,
                    domainName, logEntry.getDomainId(), logEntry.getFileName());
        }
    }
}
