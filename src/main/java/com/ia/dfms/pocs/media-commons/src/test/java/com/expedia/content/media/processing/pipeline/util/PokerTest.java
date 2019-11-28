package com.expedia.content.media.processing.pipeline.util;

import com.expedia.content.media.processing.pipeline.domain.ImageMessage;
import org.junit.Test;

public class PokerTest {

    private final Poker poker = new Poker();

    @Test
    public void testPokeShort() {
        poker.poke("Poke test", new Exception("Poke test"));
    }

    @Test
    public void testPokeLong() {
        poker.poke("Poke test", "Hipchat poke", "Poke message", new Exception("Poke message"));
    }

    @Test
    public void testPokeImageMessage() {
        ImageMessage imageMessage = ImageMessage.builder().mediaGuid("some-guid-1234").build();
        poker.pokeOnImageMessage("Poke test", imageMessage, new Exception("Poke exception"));
    }

}
