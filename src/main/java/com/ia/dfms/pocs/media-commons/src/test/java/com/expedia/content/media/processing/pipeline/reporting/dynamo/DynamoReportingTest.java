package com.expedia.content.media.processing.pipeline.reporting.dynamo;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.expedia.content.media.processing.pipeline.domain.Domain;
import com.expedia.content.media.processing.pipeline.reporting.Activity;
import com.expedia.content.media.processing.pipeline.reporting.LogEntry;

import java.util.Date;

import org.junit.Test;

public class DynamoReportingTest {
    
    @Test
    public void testWriteLogEntry() {
        
        String appName = "dynamo-reporting-test";
        DynamoDBMapper dynamoMapper = mock(DynamoDBMapper.class);
        
        LogEntry logEntry = new LogEntry("s3://folder/myawesomefile.jpg", "b536314c-36a5-4adf-a80a-dd0b56d664a5", Activity.UNKNOWN, new Date(),
                Domain.LODGING, "123", null);
        DynamoReporting reporting = new DynamoReporting(dynamoMapper, appName);
        reporting.writeLogEntry(logEntry);
        
        verify(dynamoMapper).save(eq(logEntry));
    }
}
