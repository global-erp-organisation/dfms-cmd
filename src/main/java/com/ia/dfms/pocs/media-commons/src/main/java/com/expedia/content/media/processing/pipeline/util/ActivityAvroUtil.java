package com.expedia.content.media.processing.pipeline.util;

import com.expedia.content.media.processing.pipeline.avro.ActivityLogAvro;
import com.expedia.content.media.processing.pipeline.domain.ImageMessage;
import com.expedia.content.media.processing.pipeline.reporting.Activity;
import com.expedia.content.media.processing.pipeline.reporting.App;

import java.sql.Timestamp;
import java.util.Date;

/**
 * generate AcitivityAvro class that sent to kafka Activity log topic
 */
public final class ActivityAvroUtil {

    private ActivityAvroUtil() {
    }

    /**
     * create ActivityLogAvro from ImageMessage and Activity.
     *
     * @param imageMessage ImageMessage
     * @param activity Media Activity
     * @param app Media Processing Pipeline Apps
     * @return ActivityLogAvro which is avro object.
     */
    public static ActivityLogAvro generateActivity(ImageMessage imageMessage, Activity activity, App app) {
        final ActivityLogAvro activityLogAvro = new ActivityLogAvro();
        activityLogAvro.setDomainId(imageMessage.getOuterDomainData().getDomainId());
        activityLogAvro.setMediaGuid(imageMessage.getMediaGuid());
        activityLogAvro.setDomain(imageMessage.getOuterDomainData().getDomain().getDomain());
        activityLogAvro.setFileName(imageMessage.getFileName());
        activityLogAvro.setAppName(app.getName());
        activityLogAvro.setActivityType(activity.getName());
        final Date date = new Date();
        final Timestamp sqlTimestamp = new java.sql.Timestamp(date.getTime());
        activityLogAvro.setActivityTime(sqlTimestamp.toString());
        return activityLogAvro;
    }
}


