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
    void init(){ // Initialize the Slavers event cards and game state
        slavers1 = new Slavers(5,3, 3,1,0); // Slavers with 5 credits, 3 crew, 3 goods, 1 ship, and 0 slavers
        slavers2 = new Slavers(5,5, 3,1,0); // Slavers with 5 credits, 5 crew, 3 goods, 1 ship, and 0 slavers
        gameState = new GameState(false, 2); // Create a new game state for 2 players
        player1="filippo";
        player2="thomas";
        try{
            cl1 = null;
            cl2 = null;
        }
        catch (Exception e){
            System.exit(-1);
        }
        gameState.addPlayer(cl1,player1, Color.RED); // Add first player with red color
        gameState.addPlayer(cl2,player2, Color.GREEN);  // Add second player with green color
        Component cab1 = new Cabin(301, new ArrayList<>(Arrays.asList(Connector.UNIVERSAL, Connector.UNIVERSAL, Connector.UNIVERSAL, Connector.UNIVERSAL))); // Create a cabin component for player 1
        Component cab2 = new Cabin(302, new ArrayList<>(Arrays.asList(Connector.UNIVERSAL, Connector.UNIVERSAL, Connector.UNIVERSAL, Connector.UNIVERSAL))); // Create another cabin component for player 1
        Component c1 = new Cannon(false, 201, new ArrayList<>(Arrays.asList(Connector.UNIVERSAL, Connector.UNIVERSAL, Connector.UNIVERSAL, Connector.UNIVERSAL))); // Create a cannon component for player 1
        Component c2 = new Cannon(false, 202, new ArrayList<>(Arrays.asList(Connector.UNIVERSAL, Connector.UNIVERSAL, Connector.UNIVERSAL, Connector.UNIVERSAL))); // Create another cannon component for player 1
        Component c3 = new Cannon(false, 203, new ArrayList<>(Arrays.asList(Connector.UNIVERSAL, Connector.UNIVERSAL, Connector.UNIVERSAL, Connector.UNIVERSAL))); // Create a third cannon component for player 1
        Component c4 = new Cannon(false, 204, new ArrayList<>(Arrays.asList(Connector.UNIVERSAL, Connector.UNIVERSAL, Connector.UNIVERSAL, Connector.UNIVERSAL))); // Create a fourth cannon component for player 1
        gameState.assembleComponent(player1, c1, 2, 2); // Assemble the first cannon component at position (2, 2) for player 1
        gameState.assembleComponent(player1, c2, 2, 4); // Assemble the second cannon component at position (2, 4) for player 1
        gameState.assembleComponent(player1, c3, 2, 1); // Assemble the third cannon component at position (2, 1) for player 1
        gameState.assembleComponent(player1, c4, 2, 0); // Assemble the fourth cannon component at position (2, 0) for player 1
        gameState.assembleComponent(player1, cab1, 3, 4); // Assemble the first cabin component at position (3, 4) for player 1
        gameState.assembleComponent(player1, cab2, 3, 2); // Assemble the second cabin component at position (3, 2) for player 1
        gameState.setPosition(player1, 6); // Set the position of player 1 to 6
        gameState.setPosition(player2, 3); // Set the position of player 2 to 3
        gameState.addCrew(player1, 2, 3); // Add crew member at position (2, 3) for player 1
        gameState.addCrew(player1, 3, 4); // Add crew member at position (3, 4) for player 1
        gameState.addCrew(player1, 3, 2); // Add crew member at position (3, 2) for player 1
        gameState.addCrew(player2, 2, 3); // Add crew member at position (2, 3) for player 2
    }

    @Test
    void testFirstPlayerDefeatsSlavers() { // Test when the first player defeats the slavers
        gameState.setGameDeck(new Deck(new ArrayList<>(Arrays.asList(slavers1)))); // Set the game deck with slavers1
        assertEquals(State.CARD_PICKING, gameState.getGameState()); // Check that the game state is CARD_PICKING
        gameState.pickNextCard(player1); // First player picks the next card
        assertEquals(State.CARD_SOLVING, gameState.getGameState()); // Check that the game state is now CARD_SOLVING
        gameState.defeat(player1, 0, true); // First player defeats the slavers with 0 used batteries
        assertEquals(State.CARD_PICKING, gameState.getGameState()); // Check that the game state is now CARD_PICKING
        assertEquals(5, gameState.getPlayersPlay().get(player1).getCredits()); // Check that the first player has 5 credits
        assertEquals(5, gameState.getPlayersPos().get(player1).getCell()); // Check that the first player is at cell 5
    }

    @Test
    void testBothPlayersDefeated() { // Test when both players defeat the slavers
        gameState.setGameDeck(new Deck(new ArrayList<>(Arrays.asList(slavers2)))); // Set the game deck with slavers2
        assertEquals(State.CARD_PICKING, gameState.getGameState()); // Check that the game state is CARD_PICKING
        gameState.pickNextCard(player1); // First player picks the next card
        slavers2.defeat(gameState, player1, 0, true); // First player defeats the slavers with 0 used batteries
        assertEquals(State.CARD_SOLVING, gameState.getGameState()); // Check that the game state is now CARD_SOLVING
        assertEquals(player1, gameState.getTurnPlayer()); // Check that the turn player is still the first player
        slavers2.landing(gameState, player1, new ArrayList<>(Arrays.asList(3,3)), new ArrayList<>(Arrays.asList(4,2)), new ArrayList<>(Arrays.asList(1,2))); // First player lands on the slavers
        assertEquals(player2, gameState.getTurnPlayer()); // Check that the turn player is now the second player
        assertEquals(3, gameState.getCrewCount(player1)); // Check that the first player has 3 crew members
        assertThrows(NoCrewException.class, () -> slavers2.defeat(gameState, player2, 0, true)); // Second player tries to defeat the slavers but has no crew
        assertEquals(State.CARD_PICKING, gameState.getGameState()); // Check that the game state is now CARD_PICKING
        assertEquals(player1, gameState.getTurnPlayer()); // Check that the turn player is still the first player
        assertEquals(null, gameState.getPlayersPos().get(player2)); // Check that the second player has no position
    }
}