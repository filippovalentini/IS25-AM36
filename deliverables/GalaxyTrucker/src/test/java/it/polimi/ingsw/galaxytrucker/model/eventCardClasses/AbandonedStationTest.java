package it.polimi.ingsw.galaxytrucker.model.eventCardClasses;

import it.polimi.ingsw.galaxytrucker.model.componentClasses.CargoHold;
import it.polimi.ingsw.galaxytrucker.model.componentClasses.CargoSpecial;
import it.polimi.ingsw.galaxytrucker.model.componentClasses.Component;
import it.polimi.ingsw.galaxytrucker.model.enumerations.Color;
import it.polimi.ingsw.galaxytrucker.model.enumerations.Connector;
import it.polimi.ingsw.galaxytrucker.model.enumerations.Orientation;
import it.polimi.ingsw.galaxytrucker.model.enumerations.State;
import it.polimi.ingsw.galaxytrucker.model.exceptions.InvalidActionException;
import it.polimi.ingsw.galaxytrucker.model.exceptions.NoGoodsException;
import it.polimi.ingsw.galaxytrucker.model.gameClasses.GameState;
import it.polimi.ingsw.galaxytrucker.network.rmi.client.ClientRMI;
import it.polimi.ingsw.galaxytrucker.network.rmi.server.VirtualViewRMI;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AbandonedStationTest {
    private String player1;
    private String player2;
    private VirtualViewRMI cl1;
    private VirtualViewRMI cl2;
    private GameState gameState;
    private AbandonedStation abandonedStation;
    private AbandonedStation abandonedStationFreeCrew;
    List<Integer> x_cargo;
    List<Integer> y_cargo;
    List<Integer> goodsPosCargo;

    @BeforeEach
    void init() { // Initialize the game state and players
        gameState = new GameState(false, 2);
        player1 = "player1";
        player2 = "player2";
        try{
            cl1 = null;
            cl2 = null;
        }
        catch (Exception e){
            System.exit(-1);
        }
        gameState.addPlayer(cl1, player1, Color.RED); // Add first player
        gameState.addPlayer(cl2, player2, Color.BLUE); // Add second player
        gameState.setPosition(player1, 6); // Set position for first player
        gameState.setPosition(player2, 3); // Set position for second player
        gameState.addCrew(player1, 2, 3); // Add crew for first player
        gameState.addCrew(player2, 2, 3); // Add crew for second player
        gameState.updateTurns(); // Update turns for the game state

        CargoHold cargoHoldUniversal = new CargoHold(true, -1, List.of(Connector.UNIVERSAL, Connector.UNIVERSAL, Connector.UNIVERSAL, Connector.UNIVERSAL));
        gameState.assembleComponent(player1, cargoHoldUniversal, 2, 2); // Assemble a cargo hold for the first player
        gameState.checkShipBoards(); // Check the ship boards for the game state

        List<Color> stationGoods = new ArrayList<Color>(); // Initialize the goods for the abandoned station
        stationGoods.add(Color.YELLOW); // Add yellow goods
        stationGoods.add(Color.GREEN); // Add green goods
        abandonedStation = new AbandonedStation(stationGoods, 5, 1, -1); // Create an abandoned station with the goods, a cost of 5, and a required crew of 1
        //init abandonedStationFreeCrew
        List<Color> sG = new ArrayList<Color>(); // Initialize the goods for the abandoned station with free crew
        sG.add(Color.YELLOW); // Add yellow goods
        sG.add(Color.GREEN); // Add green goods
        abandonedStationFreeCrew = new AbandonedStation(sG, 0, 1, -1); // Create an abandoned station with the goods, no cost, and a required crew of 1
        x_cargo = new ArrayList<>(); // Initialize the x coordinates for cargo
        y_cargo = new ArrayList<>(); // Initialize the y coordinates for cargo
        goodsPosCargo = new ArrayList<>(); // Initialize the positions of goods in cargo
        x_cargo.add(2); // Add x coordinate for cargo
        y_cargo.add(2); // Add y coordinate for cargo
        goodsPosCargo.add(0); // Add position of goods in cargo
        x_cargo.add(2); // Add another x coordinate for cargo
        y_cargo.add(2); // Add another y coordinate for cargo
        goodsPosCargo.add(1); // Add another position of goods in cargo
    }

    @Test
    void testUseStation() { // Test using the abandoned station
        gameState.pickGivenCard(abandonedStation); // Pick the abandoned station card
        abandonedStation.setUsed(); // Set the station as used
        assertTrue(abandonedStation.isUsed()); // Check if the station is marked as used
    }

    @Test
    void testShouldNotUseStationAlreadyUsed() { // Test that the station cannot be used again if it has already been used
        gameState.pickGivenCard(abandonedStation); // Pick the abandoned station card
        abandonedStation.setUsed(); // Set the station as used
        assertThrows(InvalidActionException.class, () -> abandonedStation.setUsed()); // Attempt to set the station as used again, which should throw an exception
    }

    @Test
    void testLoadGoodsWithRequiredCrew() { // Test loading goods with the required crew
        assertTrue(gameState.getCrewCount(player1)>0); // Check that the player has crew available
        gameState.pickGivenCard(abandonedStationFreeCrew); // Pick the abandoned station card with free crew
        abandonedStationFreeCrew.loadGoods(gameState, player1, x_cargo, y_cargo); // Load goods into the cargo hold
        assertTrue(abandonedStationFreeCrew.isUsed()); // Check if the station is marked as used after loading goods
    }

    @Test
    void testShouldNotLoadGoodsMismatchingGoodsSize(){ // Test that loading goods fails if the size of goods does not match
        gameState.pickGivenCard(abandonedStationFreeCrew); // Pick the abandoned station card with free crew
        assertThrows(NoGoodsException.class, () -> abandonedStationFreeCrew.loadGoods(gameState, player1, new ArrayList<>(), new ArrayList<>())); // Attempt to load goods with empty lists, which should throw an exception
    }

    @Test
    void testShouldNotLoadGoodsCardAlreadyUsed(){ // Test that loading goods fails if the card has already been used
        gameState.pickGivenCard(abandonedStationFreeCrew); // Pick the abandoned station card with free crew
        abandonedStationFreeCrew.setUsed(); // Set the station as used
        assertThrows(InvalidActionException.class, () -> abandonedStationFreeCrew.loadGoods(gameState, player1, x_cargo, y_cargo)); // Attempt to load goods after the station has been used, which should throw an exception
    }
    @Test
    void testShouldNotLoadGoodsInvalidRequiredCrew() { // Test that loading goods fails if the player does not have enough crew
        gameState.pickGivenCard(abandonedStationFreeCrew); // Pick the abandoned station card with free crew
        assertThrows(InvalidActionException.class, () -> abandonedStation.loadGoods(gameState, player1, x_cargo, y_cargo)); // Attempt to load goods without enough crew, which should throw an exception
    }

}