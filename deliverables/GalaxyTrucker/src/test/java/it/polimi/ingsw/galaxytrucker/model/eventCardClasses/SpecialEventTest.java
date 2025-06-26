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
    void init() { // Initialize the game state and players
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
        gameState.addPlayer(cl1, player1, Color.RED); // Add players to the game state
        gameState.addPlayer(cl2, player2, Color.YELLOW); // Add players to the game state
        Component struct = new Structural(101, new ArrayList<>(Arrays.asList(Connector.UNIVERSAL, Connector.UNIVERSAL, Connector.UNIVERSAL, Connector.UNIVERSAL))); // Create a structural component
        gameState.assembleComponent(player1, struct, 1,3); // Assemble components for player1
        gameState.assembleComponent(player1, struct, 1,2); // Assemble components for player1
        gameState.assembleComponent(player1, struct, 1,4); // Assemble components for player1
        gameState.assembleComponent(player1, struct, 1,1); // Assemble components for player1
        gameState.assembleComponent(player1, struct, 1,5); // Assemble components for player1
        gameState.setPosition(player1, 0); // Set initial position for player1
        gameState.setPosition(player2, 6); // Set initial position for player2
        gameState.addCrew(player1, 2, 3); // Add crew members for player1
        gameState.addCrew(player2, 2, 3); // Add crew members for player2

        gameState.setGameState(State.CARD_SOLVING); // Set the game state to CARD_SOLVING
        stardustEvent = new SpecialEvent(SpecialEventType.STARDUST, 0); // Initialize the Stardust event
        epidemicEvent = new SpecialEvent(SpecialEventType.EPIDEMIC, 0); // Initialize the Epidemic event
    }

    @Test
    void testSpecialEventStardust() { // Test the Stardust event
        stardustEvent.specialEffect(gameState); // Apply the special effect of the Stardust event
        assertEquals(10, gameState.getPlayersPos().get(player1).getCell()); // Check if player1's position is updated correctly
        assertEquals(2, gameState.getPlayersPos().get(player2).getCell()); // Check if player2's position is updated correctly
    }

    @Test
    void testSpecialEventEpidemic(){ // Test the Epidemic event
        epidemicEvent.specialEffect(gameState); // Apply the special effect of the Epidemic event
        assertEquals(2,gameState.getPlayersPlay().get(player1).getNumberCrew()); // Check if player1's crew count is updated correctly
    }
}