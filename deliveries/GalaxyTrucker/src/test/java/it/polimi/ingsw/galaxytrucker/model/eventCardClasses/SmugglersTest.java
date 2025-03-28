package it.polimi.ingsw.galaxytrucker.model.eventCardClasses;

import it.polimi.ingsw.galaxytrucker.model.enumerations.Color;
import it.polimi.ingsw.galaxytrucker.model.enumerations.State;
import it.polimi.ingsw.galaxytrucker.model.exceptions.InvalidActionException;
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
    List<Color> prizeGoods;

    @BeforeEach
    void init(){
        prizeGoods = new ArrayList<>();
        prizeGoods.add(Color.YELLOW);
        prizeGoods.add(Color.GREEN);
        prizeGoods.add(Color.BLUE);
        smugglers = new Smugglers(prizeGoods, 2,4, 1, 0);
        gameState = new GameState(false, 2);
        nickname = "a";
        nickname2 = "b";
        gameState.addPlayer(nickname, Color.BLUE);
        gameState.addPlayer(nickname2, Color.RED);
        gameState.setPosition(nickname, 0);
        gameState.setPosition(nickname2, 1);
        gameState.
        gameState.setGameState(State.CARD_SOLVING);
    }

    @Test
    void testDefeat() {
        Smugglers weakSmugglers = new Smugglers(prizeGoods, 2,0, 1, 0);
        int usedBatteries = 0;
        boolean looseDays = true;
        weakSmugglers.defeat(gameState, nickname, usedBatteries, looseDays);
        assertTrue(weakSmugglers.isDefeated());
    }

    @Test
    void testShouldNotAttackIfDefeated() {
        int usedBatteries = 4;
        boolean looseDays = true;
        smugglers.setDefeated();
        assertThrows(InvalidActionException.class, () -> smugglers.defeat(gameState, nickname, usedBatteries, looseDays));
    }
    @Test
    void testShouldNotAttackIfNotEnoughBatteries() {
        int usedBatteries = 5;
        gameState.getNumberBatteries(nick);
        boolean looseDays = true;
        assertThrows(InvalidActionException.class, () -> smugglers.defeat(gameState, nickname, usedBatteries, looseDays));
    }
}