package com.expedia.content.media.processing.pipeline.reporting.dynamo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.expedia.content.media.processing.pipeline.reporting.LogEntry;
import com.expedia.content.media.processing.pipeline.reporting.Reporting;

/**
 * Dynamo implementation of the Reporting interface.
 */
public class DynamoReporting implements Reporting {
    
    private final String appName;
    private final DynamoDBMapper dynamoMapper;
    
    @Autowired
    public DynamoReporting(final DynamoDBMapper dynamoMapper, @Value("${appname}") final String appName) {
        this.appName = appName;
        this.dynamoMapper = dynamoMapper;
    }
    
    @Override
    public void writeLogEntry(final LogEntry logEntry) {
        logEntry.setAppName(appName);
        dynamoMapper.save(logEntry);
    }
}
