package com.expedia.content.media.processing.pipeline.reporting;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Base class that implements writing activity logs in a separate process (thread)
 */
public class ThreadLogActivityProcess implements LogActivityProcess {
    private final ExecutorService threadPool;
    
    public ThreadLogActivityProcess(int logThreadPoolSize) {
        this.threadPool = Executors.newFixedThreadPool(logThreadPoolSize);
    }
    
    @Override
    public void log(final LogEntry logEntry, final Reporting reporting) {
        threadPool.execute(buildRunnable(logEntry, reporting));
    }
    
    protected Runnable buildRunnable(final LogEntry logEntry, final Reporting reporting) {
        return new LogActivityRunnable(logEntry, reporting);
    }
    
}
