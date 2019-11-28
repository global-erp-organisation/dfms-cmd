package com.expedia.content.media.processing.pipeline.util;

import com.expedia.content.media.processing.pipeline.domain.ImageMessage;
import com.expedia.content.media.processing.pipeline.domain.ImageMessage.ImageMessageBuilder;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate;
import org.springframework.messaging.Message;

@RunWith(MockitoJUnitRunner.class)
public class SQSUtilTest {

    @Mock
    QueueMessagingTemplate messagingTemplate; 
    @Mock
    ImageMessageBuilder imageMessageBuilder;    
    ImageMessage imageMessage;

    @Before
    public void setup() {
        imageMessage = ImageMessage.builder().mediaGuid("aaaaaaa-1010-bbbb-292929229").requestId("bbbbbb-1010-bbbb-292929229").clientId("EPC")
                .userId("you").rotation("90").active(true).fileUrl("file:/my/files/are/awesome.jpg").fileName("original_file_name.png")
                .sourceUrl("s3://bucket/source/aaaaaaa-1010-bbbb-292929229.jpg").rejectedFolder("rejected").comment("test comment!").hidden(false)
                .failedReason("imageFailed").retryCount(3).build();        
    }

    @Test
    public void testSenMessageToQueueWithFunction() {
        Mockito.when(imageMessageBuilder.build()).thenReturn(imageMessage);
        Mockito.when(imageMessageBuilder.retryCount(Mockito.anyInt())).thenReturn(imageMessageBuilder);
        SQSUtil.sendMessageToQueue(messagingTemplate, "test-queue", imageMessageBuilder, (builder) -> {
            builder.retryCount(0);
            return builder.build();
        });      
        final ArgumentCaptor<Message> outputMessage = ArgumentCaptor.forClass(Message.class);       
        Mockito.verify(messagingTemplate, Mockito.times(1)).send(Mockito.eq("test-queue"), outputMessage.capture());
        Mockito.verify(imageMessageBuilder, Mockito.times(1)).retryCount(Mockito.anyInt());
        Mockito.verify(imageMessageBuilder, Mockito.times(1)).build();
    }
    
    
    @Test
    public void testSenMessageToQueueWithoutFunction() {
        SQSUtil.sendMessageToQueue(messagingTemplate, "test-queue", imageMessage);      
        final ArgumentCaptor<Message> outputMessage = ArgumentCaptor.forClass(Message.class);       
        Mockito.verify(messagingTemplate, Mockito.times(1)).send(Mockito.eq("test-queue"), outputMessage.capture());
        Mockito.verify(imageMessageBuilder, Mockito.never()).retryCount(Mockito.anyInt());
        Mockito.verify(imageMessageBuilder, Mockito.never()).build();
    }

}
