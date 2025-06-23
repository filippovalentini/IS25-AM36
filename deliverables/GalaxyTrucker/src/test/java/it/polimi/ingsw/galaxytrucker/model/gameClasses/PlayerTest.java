package it.polimi.ingsw.galaxytrucker.model.gameClasses;

import it.polimi.ingsw.galaxytrucker.model.enumerations.Color;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {

    private Player player;

    @BeforeEach
    void init(){
        player = new Player("playerone", Color.BLUE, true);
    }

    @Test
    void testUpdateCredits(){
        int credits = 5;
        player.updateCredits(credits);
        assertEquals(credits, player.getCredits());
    }

    @Test
    void testShouldQuitGame(){
        player.quitGame();
        assertTrue(player.hasAbandoned());
    }
}