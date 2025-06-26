package it.polimi.ingsw.galaxytrucker.model.eventCardClasses;

import it.polimi.ingsw.galaxytrucker.model.componentClasses.Battery;
import it.polimi.ingsw.galaxytrucker.model.enumerations.Color;
import it.polimi.ingsw.galaxytrucker.model.enumerations.Connector;
import it.polimi.ingsw.galaxytrucker.model.enumerations.State;
import it.polimi.ingsw.galaxytrucker.model.exceptions.InvalidActionException;
import it.polimi.ingsw.galaxytrucker.model.exceptions.NoBatteriesException;
import it.polimi.ingsw.galaxytrucker.model.gameClasses.GameState;
import it.polimi.ingsw.galaxytrucker.network.rmi.client.ClientRMI;
import it.polimi.ingsw.galaxytrucker.network.rmi.server.VirtualViewRMI;
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
    private VirtualViewRMI cl1;
    private VirtualViewRMI cl2;
    List<Color> prizeGoods;

    @BeforeEach
    void init(){ // Initialize the smugglers event card and game state
        prizeGoods = new ArrayList<>(); // Initialize the prize goods for smugglers
        prizeGoods.add(Color.YELLOW); // Add different colors of goods to the prize list
        prizeGoods.add(Color.GREEN);
        prizeGoods.add(Color.BLUE);
        List<Connector> sides = new ArrayList<>(); // Initialize the sides
        sides.add(Connector.SMOOTH); // Add different types of connectors to the sides list
        sides.add(Connector.UNIVERSAL);
        sides.add(Connector.SINGLE);
        sides.add(Connector.DOUBLE);
        smugglers = new Smugglers(prizeGoods, 2,4, 1, 0); // Create a new instance of Smugglers with the prize goods and other parameters
        gameState = new GameState(false, 2); // Create a new game state with 2 players
        nickname = "a";
        nickname2 = "b";
        try{
            cl1 = null;
            cl2 = null;
        }
        catch (Exception e){
            System.exit(-1);
        }
        gameState.addPlayer(cl1, nickname, Color.BLUE); // Add the first player to the game state with a nickname and color
        gameState.addPlayer(cl2, nickname2, Color.RED); // Add the second player to the game state with a different nickname and color
        gameState.setPosition(nickname, 0); // Set the position of the first player to 0
        gameState.setPosition(nickname2, 1); // Set the position of the second player to 1
        Battery battery = new Battery(true,4, sides); // Create a new battery with the specified parameters

        gameState.assembleComponent(nickname, battery, 1,3); // Assemble the battery component for the first player at position (1,3)
        gameState.setGameState(State.CARD_SOLVING); // Set the game state to CARD_SOLVING
    }

    @Test
    void testShouldNotAttackIfDefeated() { // Test that the smugglers cannot be defeated again if they have already been defeated
        int usedBatteries = 4; // Number of batteries used to defeat smugglers
        boolean looseDays = true; // Whether the player loses days or not
        smugglers.setDefeated(); // Set the smugglers as defeated
        assertThrows(InvalidActionException.class, () -> smugglers.defeat(gameState, nickname, usedBatteries, looseDays)); // Assert that an InvalidActionException is thrown when trying to defeat smugglers again
    }

    @Test
    void testShouldNotAttackIfNotEnoughBatteries() { // Test that the smugglers cannot be defeated if there are not enough batteries
        int usedBatteries = 5; // Number of batteries used to defeat smugglers
        gameState.getNumberBatteries(nickname); // Get the number of batteries for the first player
        boolean looseDays = true; // Whether the player loses days or not
        assertThrows(NoBatteriesException.class, () -> smugglers.defeat(gameState, nickname, usedBatteries, looseDays)); // Assert that a NoBatteriesException is thrown when trying to defeat smugglers with insufficient batteries
    }
}