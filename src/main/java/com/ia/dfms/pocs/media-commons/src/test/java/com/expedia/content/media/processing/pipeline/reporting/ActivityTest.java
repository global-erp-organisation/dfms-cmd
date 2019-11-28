package com.expedia.content.media.processing.pipeline.reporting;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import org.junit.Test;

public class ActivityTest {
    
    @Test(expected = RuntimeException.class)
    public void testInvalidWithException() {
        Activity.getActivityForName("potato", true);
        fail("Should throw RuntimeException");
    }
    
    @Test
    public void testInvalidWithoutException() {
        Activity testActivity = Activity.getActivityForName("potato");
        assertNull(testActivity);
    }
    
    @Test
    public void testValid() {
        Activity testActivity = Activity.getActivityForName("Unknown");
        assertEquals(Activity.UNKNOWN, testActivity);
    }
    
}
