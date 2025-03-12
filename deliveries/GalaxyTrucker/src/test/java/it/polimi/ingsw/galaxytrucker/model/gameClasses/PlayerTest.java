package it.polimi.ingsw.galaxytrucker.model.gameClasses;

import it.polimi.ingsw.galaxytrucker.model.enumerations.Color;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {

    @Test
    void testUpdateCredits(){
        int credits = 5;
        Player player = new Player("playerone", Color.BLUE, true);
        player.updateCredits(credits);
        assertEquals(credits, player.getCredits());
    }
}