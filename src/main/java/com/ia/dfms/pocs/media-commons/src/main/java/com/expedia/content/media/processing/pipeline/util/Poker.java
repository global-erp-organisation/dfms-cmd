package com.expedia.content.media.processing.pipeline.util;

import com.expedia.content.media.processing.pipeline.domain.ImageMessage;
import expedia.content.solutions.poke.client.Poke;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * This class is an intermediate to the poke library to make it easy to mock.
 */
@Component
public class Poker {

    @Value("${EXPEDIA_ENVIRONMENT}")
    private String environment;

    /**
     * Full call to poke.
     *
     * @param emailSubject emailSubject to send the poke to
     * @param hipchatRoom hip chat room to send the notification to
     * @param message Message of the poke
     * @param exception Exception of the poke
     */
    public void poke(String emailSubject, String hipchatRoom, String message, Exception exception) {
        final Poke.PokeBuilder pokeBuilder = Poke.build();
        if (emailSubject != null) {
            pokeBuilder.email(environment + ": " + emailSubject);
        }
        if (hipchatRoom != null) {
            pokeBuilder.hipchat(hipchatRoom);
        }
        if (message == null) {
            pokeBuilder.poke(exception);
        } else {
            pokeBuilder.poke(message, exception);
        }
    }

    /**
     * Simplified version of the poke for emailSubject and exception only
     *
     * @param emailSubject emailSubject to send the poke to
     * @param exception Exception of the poke
     */
    public void poke(String emailSubject, Exception exception) {
        poke(emailSubject, null, null, exception);
    }

    /**
     * Poke on an ImageMessage object that needs poking. The full message text will be included.
     *
     * @param emailSubject email subject of the poke.
     * @param imageMessage ImageMessage of the poke.
     * @param exception Exception being poked on.
     */
    public void pokeOnImageMessage(String emailSubject, ImageMessage imageMessage, Exception exception) {
        poke(emailSubject, null, imageMessage.toString(), exception);
    }

}
