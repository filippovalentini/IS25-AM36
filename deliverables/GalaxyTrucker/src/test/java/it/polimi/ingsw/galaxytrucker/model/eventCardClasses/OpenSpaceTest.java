package it.polimi.ingsw.galaxytrucker.model.eventCardClasses;

import it.polimi.ingsw.galaxytrucker.model.componentClasses.Battery;
import it.polimi.ingsw.galaxytrucker.model.componentClasses.Component;
import it.polimi.ingsw.galaxytrucker.model.componentClasses.Engine;
import it.polimi.ingsw.galaxytrucker.model.enumerations.Color;
import it.polimi.ingsw.galaxytrucker.model.enumerations.Connector;
import it.polimi.ingsw.galaxytrucker.model.enumerations.State;
import it.polimi.ingsw.galaxytrucker.model.exceptions.NoStrengthException;
import it.polimi.ingsw.galaxytrucker.model.gameClasses.GameState;
import it.polimi.ingsw.galaxytrucker.model.gameClasses.LevelTwoPosition;
import it.polimi.ingsw.galaxytrucker.model.gameClasses.Position;
import it.polimi.ingsw.galaxytrucker.network.rmi.client.ClientRMI;
import it.polimi.ingsw.galaxytrucker.network.rmi.server.VirtualViewRMI;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class OpenSpaceTest {
    private OpenSpace os;
    private GameState gameState;
    private String player1;
    private String player2;
    private VirtualViewRMI cl1;
    private VirtualViewRMI cl2;
    private Engine engine2;
    private List<Connector> sides;
    private Position position;
    @BeforeEach
    void init() { // Initialize OpenSpace event card
        os = new OpenSpace(2);
        gameState = new GameState(false, 2);
        player1="filippo";
        player2="thomas";
        try{
            cl1 = null;
            cl2 = null;
        }
        catch (Exception e){
            System.exit(-1);
        }
        gameState.addPlayer(cl1, player1, Color.RED); // Add first player
        gameState.addPlayer(cl2, player2, Color.GREEN); // Add second player
        gameState.setPosition(player1, 6); // Set position of first player
        gameState.setPosition(player2, 3); // Set position of second player
        gameState.updateTurns(); // Update turns to set the current player
        sides = new ArrayList<>(); // Initialize sides for the engine
        sides.add(Connector.UNIVERSAL); // Add connectors to the engine
        sides.add(Connector.UNIVERSAL); // Add connectors to the engine
        sides.add(Connector.SMOOTH); // Add connectors to the engine
        sides.add(Connector.UNIVERSAL); // Add connectors to the engine
        engine2= new Engine(false,24244, sides); // Create a new engine component
        gameState.assembleComponent(player1, engine2,1,3); // Assemble the engine component for player1
        position= new LevelTwoPosition(gameState.getPlayersPos().get(player1).getCell()); // Create a position for player1
    }

    @Test
    void testFly(){ // Test the fly method of OpenSpace event card
        os.fly(gameState,player1,0); // Attempt to fly with player1
        assertEquals(player2, gameState.getTurnPlayer(), "The leader should be player2 (thomas)"); // Check if the turn has changed to player2
        assertEquals(position.getCell()+1, gameState.getPlayersPos().get(player1).getCell(), "The leader should be the lap"); // Check if player1 has moved one cell forward
        gameState.destroyComponent(player1,1,3); // Destroy the engine component of player1
    }
    @Test
    void testFly_secondAttemptAfterDestruction() { // Test flying after the engine component has been destroyed
        gameState.destroyComponent(player1, 1, 3); // Destroy the engine component of player1
        gameState.setGameState(State.CARD_PICKING); // Set the game state to CARD_PICKING
        assertThrows(NoStrengthException.class, () -> os.fly(gameState, player1, 0)); // Attempt to fly again, expecting a NoStrengthException
        assertFalse(gameState.getPlayersPos().containsKey(player1)); // Check that player1 is no longer in the game state
    }
}