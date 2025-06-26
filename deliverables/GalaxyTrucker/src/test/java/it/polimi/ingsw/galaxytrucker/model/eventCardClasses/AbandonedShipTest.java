package it.polimi.ingsw.galaxytrucker.model.eventCardClasses;

import it.polimi.ingsw.galaxytrucker.model.enumerations.Color;
import it.polimi.ingsw.galaxytrucker.model.enumerations.State;
import it.polimi.ingsw.galaxytrucker.model.exceptions.InvalidActionException;
import it.polimi.ingsw.galaxytrucker.model.exceptions.NoCrewException;
import it.polimi.ingsw.galaxytrucker.model.gameClasses.GameState;
import it.polimi.ingsw.galaxytrucker.network.rmi.client.ClientRMI;
import it.polimi.ingsw.galaxytrucker.network.rmi.server.VirtualViewRMI;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AbandonedShipTest {
    private AbandonedShip abandonedShipMoreCrew;
    private AbandonedShip abandonedShipLessCrew;
    private String player1;
    private String player2;
    private VirtualViewRMI cl1;
    private VirtualViewRMI cl2;
    private GameState gameState;

    @BeforeEach
    void init(){ // Initialize the game state and players
        gameState = new GameState(false,2);
        player1 = "truck3r";
        player2 = "4lien";
        try{
            cl1 = null;
            cl2 = null;
        }
        catch (Exception e){
            System.exit(-1);
        }
        gameState.addPlayer(cl1, player1, Color.RED); // Add first player
        gameState.addPlayer(cl2, player2, Color.BLUE); // Add second player
        gameState.setPosition(player1, 6); // Set position for player 1
        gameState.setPosition(player2, 3); // Set position for player 2
        gameState.addCrew(player1, 2, 3); // Add crew for player 1
        gameState.addCrew(player2, 2, 3); // Add crew for player 2
        gameState.checkShipBoards(); // Check ship boards for both players

        abandonedShipMoreCrew = new AbandonedShip(3, 3, 1, 0); // Create an AbandonedShip card with more crew required
        abandonedShipLessCrew = new AbandonedShip(1, 3, 1, 0); // Create an AbandonedShip card with less crew required
    }

    @Test
    void testUseShip(){ // Test if the ship can be used
        gameState.pickGivenCard(abandonedShipLessCrew); // Pick the abandoned ship card
        abandonedShipMoreCrew.setUsed(); // Set the ship as used
        assertTrue(abandonedShipMoreCrew.isUsed()); // Check if the ship is marked as used
    }

    @Test
    void testShouldNotUseShipAlreadyUsed(){ // Test if the ship cannot be used again after being marked as used
        gameState.pickGivenCard(abandonedShipMoreCrew); // Pick the abandoned ship card
        abandonedShipMoreCrew.setUsed(); // Set the ship as used
        assertThrows(InvalidActionException.class, () -> abandonedShipMoreCrew.setUsed()); // Attempt to set the ship as used again should throw an exception
    }

    @Test
    void testLandingWithRequiredCrew(){ // Test landing with the required crew
        gameState.pickGivenCard(abandonedShipLessCrew); // Pick the abandoned ship card
        List<Integer> xCabin = new ArrayList<>(); // List to hold x-coordinates of cabins
        List<Integer> yCabin = new ArrayList<>(); // List to hold y-coordinates of cabins
        List<Integer> eachCabinCrew = new ArrayList<>(); // List to hold the number of crew in each cabin
        xCabin.add(2); // Add x-coordinate of the cabin
        yCabin.add(3); // Add y-coordinate of the cabin
        eachCabinCrew.add(1); // Add the number of crew in the cabin
        abandonedShipLessCrew.landing(gameState, player1,xCabin,yCabin,eachCabinCrew); // Attempt to land the ship with the required crew
        assertTrue(abandonedShipLessCrew.isUsed()); // Check if the ship is marked as used after landing
    }

    @Test
    void testShouldNotLandingCardAlreadyUsed(){ // Test that landing on a ship that has already been used throws an exception
        gameState.pickGivenCard(abandonedShipLessCrew); // Pick the abandoned ship card
        List<Integer> xCabin = new ArrayList<>(); // List to hold x-coordinates of cabins
        List<Integer> yCabin = new ArrayList<>(); // List to hold y-coordinates of cabins
        List<Integer> eachCabinCrew = new ArrayList<>(); // List to hold the number of crew in each cabin
        xCabin.add(2); // Add x-coordinate of the cabin
        yCabin.add(3); // Add y-coordinate of the cabin
        eachCabinCrew.add(2); // Add the number of crew in the cabin
        abandonedShipLessCrew.setUsed(); // Mark the ship as used
        assertThrows(InvalidActionException.class, () -> abandonedShipLessCrew.landing(gameState, player1, xCabin, yCabin, eachCabinCrew)); // Attempt to land the ship should throw an exception since it has already been used
    }

    @Test
    void testShouldNotLandingInvalidRequiredCrew(){ //not enough crew, player can not land in the station
        gameState.pickGivenCard(abandonedShipMoreCrew); // Pick the abandoned ship card with more crew required
        List<Integer> xCabin = new ArrayList<>(); // List to hold x-coordinates of cabins
        List<Integer> yCabin = new ArrayList<>(); // List to hold y-coordinates of cabins
        List<Integer> eachCabinCrew = new ArrayList<>(); // List to hold the number of crew in each cabin
        xCabin.add(2); // Add x-coordinate of the cabin
        yCabin.add(3); // Add y-coordinate of the cabin
        eachCabinCrew.add(2); // Add the number of crew in the cabin
        assertThrows(NoCrewException.class,() -> abandonedShipMoreCrew.landing(gameState, player1,xCabin,yCabin,eachCabinCrew)); // Attempt to land the ship with insufficient crew should throw an exception
    }

    @Test
    void testSkipFirstPlayer(){ // Test skipping the first player
        gameState.pickGivenCard(abandonedShipLessCrew); // Pick the abandoned ship card
        abandonedShipLessCrew.skip(gameState, player1); // Skip the first player
        assertEquals(player2, gameState.getTurnPlayer()); //next player should be in turn
    }

    @Test
    void testSkipLastPlayer(){ // Test skipping the last player
        gameState.pickGivenCard(abandonedShipLessCrew); // Pick the abandoned ship card
        abandonedShipLessCrew.skip(gameState, player1); // Skip the first player
        abandonedShipLessCrew.skip(gameState, player2); // Skip the second player
        assertEquals(State.CARD_PICKING, gameState.getGameState()); //the phase should be CARD_PICKING
        assertEquals(player1, gameState.getTurnPlayer()); //player one should be in turn
    }
}