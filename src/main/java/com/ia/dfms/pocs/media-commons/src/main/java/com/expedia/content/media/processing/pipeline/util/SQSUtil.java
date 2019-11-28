package com.expedia.content.media.processing.pipeline.util;

import java.util.function.Function;

import com.expedia.content.media.processing.pipeline.domain.ImageMessage;
import com.expedia.content.media.processing.pipeline.domain.ImageMessage.ImageMessageBuilder;

import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate;
import org.springframework.messaging.support.MessageBuilder;

/**
 * Utility class for SQS operations.
 */
public final class SQSUtil {

    private static final FormattedLogger LOGGER = new FormattedLogger(SQSUtil.class);

    private SQSUtil() {
        // non-op
    }

    /**
     * Publish a message to a given queue.
     * 
     * @param messagingTemplate QueueMessagingTemplate object use to publish the message.
     * @param queueName Destination queue name.
     * @param imageBuilder ImageMessageBuilder object use to build the message to send.
     * @param imageMessageFunction Generic function use to build the output imageMageMessage.
     */
    public static void sendMessageToQueue(QueueMessagingTemplate messagingTemplate, String queueName, ImageMessageBuilder imageBuilder,
                                          Function<ImageMessageBuilder, ImageMessage> imageMessageFunction) {
        final ImageMessage imageMessage = imageMessageFunction.apply(imageBuilder);
        sendMessageToQueue(messagingTemplate, queueName, imageMessage);
    }

    /**
     * Publish a message to a given queue.
     * 
     * @param messagingTemplate QueueMessagingTemplate object use to publish the message.
     * @param queueName Destination queue name.
     * @param imageMessage message to send.
     */
    public static void sendMessageToQueue(QueueMessagingTemplate messagingTemplate, String queueName, ImageMessage imageMessage) {
        try {
            messagingTemplate.send(queueName, MessageBuilder.withPayload(imageMessage.toJSONMessage()).build());
            LOGGER.info("Sending message to queue done JsonMessage={} queue={}", imageMessage.toJSONMessage(), queueName);
        } catch (Exception ex) {
            LOGGER.error(ex, "Error sending message. JsonMessage={} ErrorMessage={}", imageMessage.toJSONMessage(), ex.getMessage());
            throw ex;
        }
    }   
}
