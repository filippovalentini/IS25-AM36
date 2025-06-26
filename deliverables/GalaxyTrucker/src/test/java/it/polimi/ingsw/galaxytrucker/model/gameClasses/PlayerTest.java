package it.polimi.ingsw.galaxytrucker.model.gameClasses;

import it.polimi.ingsw.galaxytrucker.model.enumerations.Color;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {

    private Player player;

    /**
     * Initializes a {@code Player} instance before each test.
     */
    @BeforeEach
    void init(){
        player = new Player("playerone", Color.BLUE, true);
    }

    /**
     * Tests the {@code updateCredits} method.
     * Verifies that the player's credits are updated correctly.
     */
    @Test
    void testUpdateCredits(){
        int credits = 5;
        player.updateCredits(credits);
        assertEquals(credits, player.getCredits());
    }

    /**
     * Tests the {@code quitGame} method.
     * Verifies that the player is marked as having abandoned the game.
     */
    @Test
    void testShouldQuitGame(){
        player.quitGame();
        assertTrue(player.hasAbandoned());
    }
}
