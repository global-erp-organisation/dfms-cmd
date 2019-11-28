package com.expedia.content.media.processing.pipeline.util;

import com.expedia.content.media.processing.pipeline.avro.ActivityLogAvro;
import com.expedia.content.media.processing.pipeline.domain.ImageMessage;
import com.expedia.content.media.processing.pipeline.reporting.Activity;
import com.expedia.content.media.processing.pipeline.reporting.App;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ActivityAvroUtilTest {

    @Test
    public void testActivityAvroPropertyForRouterJson() throws Exception {
        String fullJson = "\n"
                + "{  \n"
                + "  \"active\":\"true\",\n"
                + "  \"domain\":\"Lodging\",\n"
                + "   \"mediaGuid\":\"e52c5164-e0e8-4101-8627-38efa8bdb1ab\",\n"
                + "  \"domainId\":\"41098\",\n"
                + "  \"domainProvider\":\"EPC External User\",\n"
                + "  \"fileName\":\"41098_epc_2e84ed6.jpg\",\n"
                + "  \"fileUrl\":\"s3://ewe-cs-media-test/e2e/images/8492274_13_0013ice.jpg\",\n"
                + "  \"userId\":\"media-cloud-router\"\n"
                + "}";
        ImageMessage imageMessage = ImageMessage.parseJsonMessage(fullJson);
        ActivityLogAvro activityLogAvro = ActivityAvroUtil.generateActivity(imageMessage, Activity.RECEPTION, App.MEDIA_SERVICE);
        assertEquals(activityLogAvro.getDomain(), "Lodging");
        assertEquals(activityLogAvro.getDomainId(), "41098");
        assertEquals(activityLogAvro.getMediaGuid(), "e52c5164-e0e8-4101-8627-38efa8bdb1ab");
        assertEquals(activityLogAvro.getFileName(), "41098_epc_2e84ed6.jpg");
        assertEquals(activityLogAvro.getActivityType(), Activity.RECEPTION.getName());
        assertEquals(activityLogAvro.getAppName(), App.MEDIA_SERVICE.getName());
    }
}
