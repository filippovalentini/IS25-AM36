package it.polimi.ingsw.galaxytrucker.model.eventCardClasses;

import it.polimi.ingsw.galaxytrucker.model.componentClasses.Battery;
import it.polimi.ingsw.galaxytrucker.model.componentClasses.Cannon;
import it.polimi.ingsw.galaxytrucker.model.componentClasses.Component;
import it.polimi.ingsw.galaxytrucker.model.enumerations.Color;
import it.polimi.ingsw.galaxytrucker.model.enumerations.Connector;
import it.polimi.ingsw.galaxytrucker.model.enumerations.Orientation;
import it.polimi.ingsw.galaxytrucker.model.enumerations.State;
import it.polimi.ingsw.galaxytrucker.model.exceptions.InvalidActionException;
import it.polimi.ingsw.galaxytrucker.model.exceptions.NoCrewException;
import it.polimi.ingsw.galaxytrucker.model.gameClasses.GameState;
import it.polimi.ingsw.galaxytrucker.model.shotClasses.CannonShot;
import it.polimi.ingsw.galaxytrucker.network.rmi.client.ClientRMI;
import it.polimi.ingsw.galaxytrucker.network.rmi.server.VirtualViewRMI;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PiratesTest {
    private Pirates pirates;
    private GameState gameState;
    String nickname;
    String nickname2;
    private VirtualViewRMI cl1;
    private VirtualViewRMI cl2;
    List<CannonShot> cannonShots;



    @BeforeEach
    void init(){ // Initialize the Pirates event card with its parameters
        gameState = new GameState(false, 2);
        nickname = "player1";
        nickname2 = "player2";
        try{
            cl1 = null;
            cl2 = null;
        }
        catch (Exception e){
            System.exit(-1);
        }
        gameState.addPlayer(cl1, nickname, Color.RED); // Add the first player to the game state
        gameState.addPlayer(cl2, nickname2, Color.BLUE); // Add the second player to the game state
        Component battery = new Battery(false, 1000, new ArrayList<>(Arrays.asList(Connector.UNIVERSAL, Connector.UNIVERSAL, Connector.UNIVERSAL, Connector.UNIVERSAL)));
        Component cannon = new Cannon(true, 2000, new ArrayList<>(Arrays.asList(Connector.SMOOTH, Connector.UNIVERSAL, Connector.UNIVERSAL, Connector.UNIVERSAL)));
        gameState.assembleComponent(nickname, battery, 2, 4); // Assemble a battery component for the first player
        gameState.assembleComponent(nickname, cannon, 1, 3); // Assemble a cannon component for the first player
        gameState.setPosition(nickname, 0); // Set the position of the first player to 0
        gameState.setPosition(nickname2, 1); // Set the position of the second player to 1
        gameState.addBatteries(nickname, 2, 4); // Add batteries to the first player
        gameState.addCrew(nickname, 2, 3); // Add crew members to the first player
        gameState.addCrew(nickname2, 2, 3); // Add crew members to the second player

        CannonShot cShotLarge = new CannonShot(true, Orientation.NORTH); // Create a large cannon shot
        CannonShot cShotNotLarge1 = new CannonShot(false, Orientation.NORTH); // Create a non-large cannon shot
        CannonShot cShotNotLarge2 = new CannonShot(false, Orientation.NORTH); // Create another non-large cannon shot
        cannonShots = new ArrayList<>(); // Initialize the list of cannon shots
        cannonShots.add(cShotLarge); // Add the large cannon shot to the list
        cannonShots.add(cShotNotLarge1); // Add the first non-large cannon shot to the list
        cannonShots.add(cShotNotLarge2); // Add the second non-large cannon shot to the list
        pirates = new Pirates(4, 1, cannonShots, 1, 0); // Create a Pirates event card with specific parameters
        gameState.setGameState(State.CARD_SOLVING); // Set the game state to CARD_SOLVING
    }

    @Test
    void testDefeat() { // Test the defeat method of the Pirates event card
        int usedBatteries = 0; // Number of batteries used
        boolean looseDays = true; // Whether to lose days or not
        assertDoesNotThrow(() -> pirates.defeat(gameState, nickname, usedBatteries, looseDays)); // Assert that no exception is thrown when defeating the pirates
    }

    @Test
    void testShouldNotDefeatNotEnoughStrength() { // Test that the defeat method throws an exception if the pirates are not strong enough
        Pirates strongPirates = new Pirates(4, 1000, cannonShots, 1, 0); // Create a strong Pirates event card with high strength
        int usedBatteries = 0; // Number of batteries used
        boolean looseDays = true; // Whether to lose days or not
        assertDoesNotThrow(() -> strongPirates.defeat(gameState, nickname, usedBatteries, looseDays)); // Assert that no exception is thrown when defeating the strong pirates
    }

    @Test
    void testHitShip() { // Test the hitShip method of the Pirates event card
        int diceResult = 7; // Result of the dice roll
        int usedBatteries = 0; // Number of batteries used
        boolean looseDays = true; // Whether to lose days or not
        boolean activateShield = false; // Whether to activate the shield or not
        boolean activateCannon = false; // Whether to activate the cannon or not
        pirates.defeat(gameState, nickname, usedBatteries, looseDays); // Defeat the pirates before hitting the ship
        pirates.hitShip(gameState, nickname, diceResult, activateShield, activateCannon); // Hit the ship with the specified parameters
        assertEquals(0, gameState.getPlayersPlay().get(nickname).getShipBoard().getAssembledComponent(1, 3).getImageID()); // Assert that the cannon component at position (1, 3) has been hit and its image ID is set to 0
        assertDoesNotThrow(() -> pirates.hitShip(gameState, nickname, diceResult, activateShield, activateCannon)); // Assert that no exception is thrown when hitting the ship again
    }

    @Test
    void testDefeatedPirates(){ // Test that the pirates cannot be hit if they have already been defeated
        int usedBatteries = 1; // Number of batteries used
        boolean loseDays = true; // Whether to lose days or not
        pirates.defeat(gameState, nickname, usedBatteries, loseDays); // Defeat the pirates
        assertThrows(InvalidActionException.class, () -> pirates.hitShip(gameState, nickname, 3, false, false)); // Assert that an exception is thrown when trying to hit the ship after defeating the pirates
        assertEquals(23, gameState.getPlayersPos().get(nickname).getCell()); // Assert that the position of the first player remains unchanged
    }

    @Test
    void testHitShipQuitCond() { // Test the hitShip method when the player has no crew members left
        int diceResult = 4; // Result of the dice roll
        int usedBatteries = 0; // Number of batteries used
        boolean looseDays = true; // Whether to lose days or not
        boolean activateShield = false; // Whether to activate the shield or not
        boolean activateCannon = false; // Whether to activate the cannon or not
        List<Integer> y= new ArrayList<>(); // List of y-coordinates for the crew members
        List<Integer> x= new ArrayList<>(); // List of x-coordinates for the crew members
        List<Integer> e= new ArrayList<>(); // List of energy levels for the crew members
        x.add(2); // Add x-coordinate for the crew member
        y.add(3); // Add y-coordinate for the crew member
        e.add(2); // Add energy level for the crew member
        pirates.defeat(gameState, nickname, usedBatteries, looseDays); // Defeat the pirates before hitting the ship
        gameState.removeCrewMembers(nickname,x,y,e,2); // Remove the crew member from the first player
        pirates.hitShip(gameState, nickname, diceResult, activateShield, activateCannon); // Hit the ship with the specified parameters
        pirates.hitShip(gameState, nickname, diceResult, activateShield, activateCannon); // Hit the ship again with the same parameters
        assertThrows(NoCrewException.class, () -> pirates.hitShip(gameState, nickname, diceResult, activateShield, activateCannon)); // Assert that an exception is thrown when trying to hit the ship after removing the crew member
        assertFalse(gameState.getPlayersPos().containsKey(nickname)); // Assert that the first player has been removed from the game state
        assertEquals(State.CARD_PICKING, gameState.getGameState()); // Assert that the game state is set to CARD_PICKING after the player has been removed
    }
}
