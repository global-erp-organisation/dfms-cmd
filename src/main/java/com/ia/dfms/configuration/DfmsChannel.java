package com.ia.dfms.configuration;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

public interface DfmsChannel {

    String OUTPUT = "dfms-out-exchange";

    @Output(OUTPUT)
    MessageChannel output();
}
