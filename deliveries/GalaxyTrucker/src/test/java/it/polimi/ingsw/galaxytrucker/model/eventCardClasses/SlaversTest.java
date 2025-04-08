package it.polimi.ingsw.galaxytrucker.model.eventCardClasses;

import it.polimi.ingsw.galaxytrucker.model.componentClasses.Cabin;
import it.polimi.ingsw.galaxytrucker.model.componentClasses.Cannon;
import it.polimi.ingsw.galaxytrucker.model.componentClasses.Component;
import it.polimi.ingsw.galaxytrucker.model.componentClasses.Engine;
import it.polimi.ingsw.galaxytrucker.model.enumerations.Color;
import it.polimi.ingsw.galaxytrucker.model.enumerations.Connector;
import it.polimi.ingsw.galaxytrucker.model.enumerations.State;
import it.polimi.ingsw.galaxytrucker.model.exceptions.NoCrewException;
import it.polimi.ingsw.galaxytrucker.model.gameClasses.Deck;
import it.polimi.ingsw.galaxytrucker.model.gameClasses.GameState;
import it.polimi.ingsw.galaxytrucker.model.gameClasses.LevelTwoPosition;
import it.polimi.ingsw.galaxytrucker.network.rmi.client.ClientRMI;
import it.polimi.ingsw.galaxytrucker.network.rmi.server.VirtualViewRMI;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class SlaversTest {
    private EventCard slavers1;
    private EventCard slavers2;
    private GameState gameState;
    String player1;
    String player2;
    private VirtualViewRMI cl1;
    private VirtualViewRMI cl2;

    @BeforeEach
    void init(){
        slavers1 = new Slavers(5,3, 3,1,0);
        slavers2 = new Slavers(5,5, 3,1,0);
        gameState = new GameState(false, 2);
        player1="filippo";
        player2="thomas";
        try{
            cl1 = new ClientRMI();
            cl2 = new ClientRMI();
        }
        catch (Exception e){
            System.exit(-1);
        }
        gameState.addPlayer(cl1,player1, Color.RED);
        gameState.addPlayer(cl2,player2, Color.GREEN);
        Component cab1 = new Cabin(301, new ArrayList<>(Arrays.asList(Connector.UNIVERSAL, Connector.UNIVERSAL, Connector.UNIVERSAL, Connector.UNIVERSAL)));
        Component cab2 = new Cabin(302, new ArrayList<>(Arrays.asList(Connector.UNIVERSAL, Connector.UNIVERSAL, Connector.UNIVERSAL, Connector.UNIVERSAL)));
        Component c1 = new Cannon(false, 201, new ArrayList<>(Arrays.asList(Connector.UNIVERSAL, Connector.UNIVERSAL, Connector.UNIVERSAL, Connector.UNIVERSAL)));
        Component c2 = new Cannon(false, 202, new ArrayList<>(Arrays.asList(Connector.UNIVERSAL, Connector.UNIVERSAL, Connector.UNIVERSAL, Connector.UNIVERSAL)));
        Component c3 = new Cannon(false, 203, new ArrayList<>(Arrays.asList(Connector.UNIVERSAL, Connector.UNIVERSAL, Connector.UNIVERSAL, Connector.UNIVERSAL)));
        Component c4 = new Cannon(false, 204, new ArrayList<>(Arrays.asList(Connector.UNIVERSAL, Connector.UNIVERSAL, Connector.UNIVERSAL, Connector.UNIVERSAL)));
        gameState.assembleComponent(player1, c1, 2, 2);
        gameState.assembleComponent(player1, c2, 2, 4);
        gameState.assembleComponent(player1, c3, 2, 1);
        gameState.assembleComponent(player1, c4, 2, 0);
        gameState.assembleComponent(player1, cab1, 1, 4);
        gameState.assembleComponent(player1, cab2, 1, 2);
        gameState.setPosition(player1, 6);
        gameState.setPosition(player2, 3);
    }

    @Test
    void testFirstPlayerDefeatsSlavers() {
        gameState.setGameDeck(new Deck(new ArrayList<>(Arrays.asList(slavers1))));
        assertEquals(State.CARD_PICKING, gameState.getGameState());
        gameState.pickNextCard(player1);
        assertEquals(State.CARD_SOLVING, gameState.getGameState());
        slavers1.defeat(gameState, player1, 0, true);
        assertEquals(State.CARD_PICKING, gameState.getGameState());
        assertEquals(5, gameState.getPlayersPlay().get(player1).getCredits());
        assertEquals(5, gameState.getPlayersPos().get(player1).getCell());
    }

    @Test
    void testBothPlayersDefeated() {
        gameState.setGameDeck(new Deck(new ArrayList<>(Arrays.asList(slavers2))));
        assertEquals(State.CARD_PICKING, gameState.getGameState());
        gameState.pickNextCard(player1);
        slavers2.defeat(gameState, player1, 0, true);
        assertEquals(State.CARD_SOLVING, gameState.getGameState());
        assertEquals(player1, gameState.getTurnPlayer());
        slavers2.landing(gameState, player1, new ArrayList<>(Arrays.asList(1,1)), new ArrayList<>(Arrays.asList(4,2)), new ArrayList<>(Arrays.asList(1,2)));
        assertEquals(player2, gameState.getTurnPlayer());
        assertEquals(3, gameState.getCrewCount(player1));
        slavers2.defeat(gameState, player2, 0, true);
        assertThrows(NoCrewException.class, () -> slavers2.landing(gameState, player2, new ArrayList<>(Arrays.asList(1,1)), new ArrayList<>(Arrays.asList(4,2)), new ArrayList<>(Arrays.asList(1,2))));
    }
}