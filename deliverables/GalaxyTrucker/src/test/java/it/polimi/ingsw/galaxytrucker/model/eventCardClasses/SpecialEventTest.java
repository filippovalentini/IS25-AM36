package it.polimi.ingsw.galaxytrucker.model.eventCardClasses;

import it.polimi.ingsw.galaxytrucker.model.componentClasses.*;
import it.polimi.ingsw.galaxytrucker.model.enumerations.Color;
import it.polimi.ingsw.galaxytrucker.model.enumerations.Connector;
import it.polimi.ingsw.galaxytrucker.model.enumerations.SpecialEventType;
import it.polimi.ingsw.galaxytrucker.model.enumerations.State;
import it.polimi.ingsw.galaxytrucker.model.gameClasses.GameState;
import it.polimi.ingsw.galaxytrucker.network.rmi.client.ClientRMI;
import it.polimi.ingsw.galaxytrucker.network.rmi.server.VirtualViewRMI;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SpecialEventTest {
    private SpecialEvent stardustEvent;
    SpecialEvent epidemicEvent;
    private GameState gameState;
    private String player1;
    private String player2;
    private VirtualViewRMI cl1;
    private VirtualViewRMI cl2;

    @BeforeEach
    void init() {
        gameState = new GameState(false, 2);
        player1 = "a";
        player2 = "b";
        try{
            cl1 = null;
            cl2 = null;
        }
        catch (Exception e){
            System.exit(-1);
        }
        gameState.addPlayer(cl1, player1, Color.RED);
        gameState.addPlayer(cl2, player2, Color.YELLOW);
        Component struct = new Structural(101, new ArrayList<>(Arrays.asList(Connector.UNIVERSAL, Connector.UNIVERSAL, Connector.UNIVERSAL, Connector.UNIVERSAL)));
        gameState.assembleComponent(player1, struct, 1,3);
        gameState.assembleComponent(player1, struct, 1,2);
        gameState.assembleComponent(player1, struct, 1,4);
        gameState.assembleComponent(player1, struct, 1,1);
        gameState.assembleComponent(player1, struct, 1,5);
        gameState.setPosition(player1, 0);
        gameState.setPosition(player2, 6);
        gameState.addCrew(player1, 2, 3);
        gameState.addCrew(player2, 2, 3);

        gameState.setGameState(State.CARD_SOLVING);
        stardustEvent = new SpecialEvent(SpecialEventType.STARDUST, 0);
        epidemicEvent = new SpecialEvent(SpecialEventType.EPIDEMIC, 0);
    }

    @Test
    void testSpecialEventStardust() {
        stardustEvent.specialEffect(gameState);
        assertEquals(10, gameState.getPlayersPos().get(player1).getCell());
        assertEquals(2, gameState.getPlayersPos().get(player2).getCell());
    }

    @Test
    void testSpecialEventEpidemic(){
        epidemicEvent.specialEffect(gameState);
        assertEquals(2,gameState.getPlayersPlay().get(player1).getNumberCrew());
    }
}