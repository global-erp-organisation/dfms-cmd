package com.expedia.content.media.processing.pipeline.retry;

import com.codahale.metrics.MetricRegistry;
import expedia.content.solutions.metrics.annotations.Meter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

/**
 * This test verifies that the {@code @RetryableMethod} annotation that triggers
 * the retry aspect happens _before_ the {@code @Meter, @Timer and @Counter} annotations
 * that trigger the metric aspects. In other words, make sure that the retry aspect is
 * applied first and then all other aspects.
 *
 * @see RetryableMethod
 * @see expedia.content.solutions.metrics.annotations.Meter
 * @see expedia.content.solutions.metrics.annotations.Timer
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/retry-test.xml")
public class RetryableMethodTest {

    public static final String TEST_METRIC_NAME = "TestObject.TestMeter";

    @Autowired
    private TestObject testObject;

    @Autowired
    private RetryTemplate retryTemplate;

    @Autowired
    private MetricRegistry metricRegistry;
    
    @Test
    public void testLoadContext() {
        assertNotNull(retryTemplate);
        assertNotNull(testObject);
    }

    @Test
    public void testVerifyRetryInnerAspect() {
        final com.codahale.metrics.Meter meter = metricRegistry.getMeters().get(TEST_METRIC_NAME);
        final com.codahale.metrics.Meter meterFailures = metricRegistry.getMeters().get(TEST_METRIC_NAME + ".failure");
        final com.codahale.metrics.Meter meterSuccess = metricRegistry.getMeters().get(TEST_METRIC_NAME + ".success");

        assertNotNull(meter);
        assertNotNull(meterFailures);
        assertNotNull(meterSuccess);
        assertEquals(0, meter.getCount());
        assertEquals(0, meterFailures.getCount());
        assertEquals(0, meterFailures.getCount());
        assertEquals(0, meterSuccess.getCount());

        try {
            testObject.retryableMethod(true);
            fail("This method never succeeds");
        } catch (RuntimeException e) {
            assertNotNull(e);
        }

        assertEquals(5, testObject.getInvocationCounter());
    }

}

@Component
class TestObject {
    private int invocationCounter;

    @RetryableMethod
    @Meter(name = "TestMeter")
    public void retryableMethod(boolean throwException) {
        invocationCounter++;
        if (throwException) {
            throw new RuntimeException("Test Exception");
        }
    }

    public int getInvocationCounter() {
        return invocationCounter;
    }
}
