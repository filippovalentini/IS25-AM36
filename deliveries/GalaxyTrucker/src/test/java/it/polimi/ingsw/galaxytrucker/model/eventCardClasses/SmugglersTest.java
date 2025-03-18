package it.polimi.ingsw.galaxytrucker.model.eventCardClasses;

import it.polimi.ingsw.galaxytrucker.model.enumerations.Color;
import it.polimi.ingsw.galaxytrucker.model.gameClasses.GameState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SmugglersTest {
    private Smugglers smugglers;
    private GameState gameState;
    String nickname;
    String nickname2;

    @BeforeEach
    void init(){
        List<Color> prizeGoods = new ArrayList<>();
        prizeGoods.add(Color.YELLOW);
        prizeGoods.add(Color.GREEN);
        prizeGoods.add(Color.BLUE);
        smugglers = new Smugglers(prizeGoods, 2,4, 1, 0);
    }

    @Test
    void testDefeat() {
        int usedBatteries = 4;
        boolean looseDays = true;

        assertDoesNotThrow(() -> smugglers.defeat(gameState, nickname, usedBatteries, looseDays));
        assertTrue(smugglers.isDefeated());
    }

    @Test
    void testShouldNotAttackIfDefeated() {
        int usedBatteries = 4;
        boolean looseDays = true;
        smugglers.defeat(gameState, nickname, usedBatteries, looseDays);

        assertDoesNotThrow(() -> smugglers.defeat(gameState, nickname, usedBatteries, looseDays));
    }
}