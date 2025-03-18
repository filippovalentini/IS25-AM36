package it.polimi.ingsw.galaxytrucker.model.eventCardClasses;

import it.polimi.ingsw.galaxytrucker.model.gameClasses.GameState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SlaversTest {
    private Slavers slavers;
    private GameState gameState;
    String nickname;
    String nickname2;

    @BeforeEach
    void init(){
        slavers = new Slavers(5,6, 3,1,0);

    }

    @Test
    void testDefeat() {
        int usedBatteries = 6;
        boolean looseDays = true;

        assertDoesNotThrow(() -> slavers.defeat(gameState, nickname, usedBatteries, looseDays));
    }

    @Test
    void testShouldNotAttackIfDefeated() {
        int usedBatteries = 6;
        boolean looseDays = true;
        slavers.defeat(gameState, nickname, usedBatteries, looseDays);

        assertDoesNotThrow(() -> slavers.defeat(gameState, nickname, usedBatteries, looseDays));
    }
}