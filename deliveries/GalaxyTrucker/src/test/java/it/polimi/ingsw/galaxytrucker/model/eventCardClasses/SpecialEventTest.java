package it.polimi.ingsw.galaxytrucker.model.eventCardClasses;

import it.polimi.ingsw.galaxytrucker.model.enumerations.Color;
import it.polimi.ingsw.galaxytrucker.model.enumerations.SpecialEventType;
import it.polimi.ingsw.galaxytrucker.model.enumerations.State;
import it.polimi.ingsw.galaxytrucker.model.gameClasses.GameState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SpecialEventTest {
    private SpecialEvent stardustEvent;
    SpecialEvent epidemicEvent;
    private GameState gameState;
    private String player1;
    private String player2;

    @BeforeEach
    void init() {
        gameState = new GameState(false, 2);
        player1 = "a";
        player2 = "b";
        gameState.addPlayer(player1, Color.RED);
        gameState.addPlayer(player2, Color.YELLOW);
        gameState.setPosition(player1, 0);
        gameState.setPosition(player2, 6);
        gameState.setGameState(State.CARD_SOLVING);
        stardustEvent = new SpecialEvent(SpecialEventType.STARDUST, 0);
        epidemicEvent = new SpecialEvent(SpecialEventType.EPIDEMIC, 0);
    }

    @Test
    void testSpecialEventStardust() {
        stardustEvent.specialEffect(gameState);
        assertEquals(2, gameState.getPlayersPos().get(player2).getCell());
    }

    @Test
    void testSpecialEventEpidemic(){
        epidemicEvent.specialEffect(gameState);
        //each player should lose one crew member
        assertEquals(1,gameState.getPlayersPlay().get(player1).getNumberCrew());
        assertEquals(1,gameState.getPlayersPlay().get(player2).getNumberCrew());
    }
}