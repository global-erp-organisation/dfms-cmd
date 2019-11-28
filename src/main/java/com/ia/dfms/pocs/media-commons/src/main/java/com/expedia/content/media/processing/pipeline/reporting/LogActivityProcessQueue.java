package com.expedia.content.media.processing.pipeline.reporting;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Base class that implements writing activity logs in a separate process (thread)
 * this class builds the logActivity thread which writes to dead letter queue in case of exception.
 */
public class LogActivityProcessQueue implements LogActivityProcess {
    private final ExecutorService threadPool;
    private String deadLetterQueue;

    @Autowired
    private QueueMessagingTemplate messagingTemplate;

    public LogActivityProcessQueue(int logThreadPoolSize, String deadLetterQueue) {
        this.deadLetterQueue = deadLetterQueue;
        this.threadPool = Executors.newFixedThreadPool(logThreadPoolSize);
    }
    
    @Override
    public void log(final LogEntry logEntry, final Reporting reporting) {
        threadPool.execute(buildRunnable(logEntry, reporting));
    }
    
    protected Runnable buildRunnable(final LogEntry logEntry, final Reporting reporting) {
        return new LogActivityRunnableWIthDLQueue(logEntry, reporting, deadLetterQueue, messagingTemplate);
    }
    
}
