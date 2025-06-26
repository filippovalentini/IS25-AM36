package it.polimi.ingsw.galaxytrucker.model.eventCardClasses;

import it.polimi.ingsw.galaxytrucker.model.componentClasses.CargoHold;
import it.polimi.ingsw.galaxytrucker.model.componentClasses.Component;
import it.polimi.ingsw.galaxytrucker.model.enumerations.Color;
import it.polimi.ingsw.galaxytrucker.model.enumerations.Connector;
import it.polimi.ingsw.galaxytrucker.model.enumerations.State;
import it.polimi.ingsw.galaxytrucker.model.exceptions.InvalidActionException;
import it.polimi.ingsw.galaxytrucker.model.gameClasses.Deck;
import it.polimi.ingsw.galaxytrucker.model.gameClasses.GameState;
import it.polimi.ingsw.galaxytrucker.network.rmi.client.ClientRMI;
import it.polimi.ingsw.galaxytrucker.network.rmi.server.VirtualViewRMI;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PlanetsTest {
    private Planets planets;
    private GameState gameState;
    String nickname;
    String nickname2;
    private VirtualViewRMI cl1;
    private VirtualViewRMI cl2;
    List<Color> planetOneGoods;
    List<Color> planetTwoGoods;
    List<List<Color>> allPlanetsGoods;

    @BeforeEach
    void init() throws RemoteException { // Initialize the planets event card and game state
        gameState = new GameState(false, 2); // Create a new game state for 2 players
        nickname="filippo";
        nickname2="thomas";
        cl1 = null;
        cl2 = null;
        gameState.addPlayer(cl1,nickname,Color.RED); // Add first player with nickname and color
        gameState.addPlayer(cl2,nickname2,Color.BLUE); // Add second player with nickname and color

        Component cargo1 = new CargoHold(false, 408, new ArrayList<>(Arrays.asList(Connector.UNIVERSAL, Connector.UNIVERSAL, Connector.UNIVERSAL, Connector.UNIVERSAL))); // Create cargo hold components for the players' ships
        Component cargo2 = new CargoHold(false, 408, new ArrayList<>(Arrays.asList(Connector.UNIVERSAL, Connector.UNIVERSAL, Connector.UNIVERSAL, Connector.UNIVERSAL))); // Create another cargo hold component
        Component cargo3 = new CargoHold(false, 408, new ArrayList<>(Arrays.asList(Connector.UNIVERSAL, Connector.UNIVERSAL, Connector.UNIVERSAL, Connector.UNIVERSAL))); // Create a third cargo hold component
        Component cargo4 = new CargoHold(false, 408, new ArrayList<>(Arrays.asList(Connector.UNIVERSAL, Connector.UNIVERSAL, Connector.UNIVERSAL, Connector.UNIVERSAL))); // Create a fourth cargo hold component
        gameState.assembleComponent(nickname, cargo1, 2, 2); // Assemble cargo hold components on the first player's ship
        gameState.assembleComponent(nickname, cargo2, 2, 4); // Assemble another cargo hold component
        gameState.assembleComponent(nickname, cargo3, 1, 3); // Assemble a third cargo hold component
        gameState.assembleComponent(nickname, cargo4, 3, 3); // Assemble a fourth cargo hold component
        gameState.setPosition(nickname, 6); // Set the position of the first player
        gameState.setPosition(nickname2, 3); // Set the position of the second player
        gameState.addCrew(nickname, 2, 3); // Add crew to the first player's ship
        gameState.addCrew(nickname2, 2, 3); // Add crew to the second player's ship

        planetOneGoods = new ArrayList<>(); // Initialize goods for the first planet
        planetOneGoods.add(Color.YELLOW); // Add yellow goods to the first planet
        planetOneGoods.add(Color.GREEN); // Add green goods to the first planet
        planetOneGoods.add(Color.BLUE); // Add blue goods to the first planet
        planetOneGoods.add(Color.BLUE); // Add another blue good to the first planet
        planetTwoGoods = new ArrayList<>(); // Initialize goods for the second planet
        planetTwoGoods.add(Color.YELLOW); // Add yellow goods to the second planet
        planetTwoGoods.add(Color.YELLOW); // Add another yellow good to the second planet
        allPlanetsGoods = new ArrayList<>(); // Create a list to hold all planets' goods
        allPlanetsGoods.add(planetOneGoods); // Add the first planet's goods to the list
        allPlanetsGoods.add(planetTwoGoods); // Add the second planet's goods to the list
        planets = new Planets(allPlanetsGoods, 3, 409); // Create a new Planets event card with the goods and other parameters

        gameState.setGameDeck(new Deck(new ArrayList<>(Arrays.asList(planets)))); // Set the game deck with the Planets event card
        gameState.pickNextCard(nickname); // Pick the next card for the first player
    }


    @Test
    void testPlanetLanding() { // Test landing on a planet
       int numberPlanet = 0; // Specify the planet number to land on
       assertDoesNotThrow(() -> gameState.planetLanding(nickname, numberPlanet)); // Assert that landing on the planet does not throw an exception
       assertEquals(2, gameState.getPlayersPos().get(nickname).getCell()); // Assert that the player's position is updated correctly after landing on the planet
    }

    @Test
    void testShouldNotLandIfPlanetNull() { // Test landing on a null planet
        int numberPlanet = 0; // Specify the planet number to land on
        List<Integer> x = new ArrayList<>(Arrays.asList(0,0,0,0)); // Create a list of x coordinates for loading goods
        List<Integer> y = new ArrayList<>(Arrays.asList(0,0,0,0)); // Create a list of y coordinates for loading goods
        gameState.planetLanding(nickname, numberPlanet); // Land on the planet
        gameState.loadGoods(nickname, x, y); // Load goods onto the ship
        assertThrows(InvalidActionException.class, () -> gameState.planetLanding(nickname2, numberPlanet)); // Assert that landing on the planet with another player throws an exception
    }
   @Test
    void testSwitchGoods(){ // Test switching goods on the ship after landing on a planet
        int numberPlanet = 0; // Specify the planet number to land on
        List<Integer> x = new ArrayList<>(Arrays.asList(2,2,1,0)); // Create a list of x coordinates for loading goods
        List<Integer> y = new ArrayList<>(Arrays.asList(2,4,3,0)); // Create a list of y coordinates for loading goods
        gameState.planetLanding(nickname, numberPlanet); // Land on the planet
        gameState.loadGoods(nickname, x, y); // Load goods onto the ship
        assertEquals(Color.YELLOW,gameState.getPlayersPlay().get(nickname).getShipBoard().getAssembledComponent(2,2).getGoods().get(0)); // Assert that the first loaded good is yellow
        assertEquals(Color.GREEN,gameState.getPlayersPlay().get(nickname).getShipBoard().getAssembledComponent(2,4).getGoods().get(0)); // Assert that the second loaded good is green
        assertEquals(Color.BLUE,gameState.getPlayersPlay().get(nickname).getShipBoard().getAssembledComponent(1,3).getGoods().get(0)); // Assert that the third loaded good is blue
        assertEquals(0,gameState.getPlayersPlay().get(nickname).getShipBoard().getAssembledComponent(3,3).getGoods().size()); // Assert that the fourth loaded good is empty

   }
}