package it.polimi.ingsw.galaxytrucker.model.eventCardClasses;

import it.polimi.ingsw.galaxytrucker.model.componentClasses.*;
import it.polimi.ingsw.galaxytrucker.model.enumerations.Color;
import it.polimi.ingsw.galaxytrucker.model.enumerations.Connector;
import it.polimi.ingsw.galaxytrucker.model.enumerations.Orientation;
import it.polimi.ingsw.galaxytrucker.model.enumerations.State;
import it.polimi.ingsw.galaxytrucker.model.gameClasses.Deck;
import it.polimi.ingsw.galaxytrucker.model.gameClasses.GameState;
import it.polimi.ingsw.galaxytrucker.model.shotClasses.Meteor;
import it.polimi.ingsw.galaxytrucker.network.rmi.client.ClientRMI;
import it.polimi.ingsw.galaxytrucker.network.rmi.server.VirtualViewRMI;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MeteorsSwarmTest { // MeteorsSwarmTest
    private MeteorsSwarm meteorsSwarm;
    private String player1;
    private String player2;
    private VirtualViewRMI cl1;
    private VirtualViewRMI cl2;
    private GameState gameState;


    @BeforeEach
    void init() { // Initialize the MeteorsSwarm and GameState for testing
        player1 = "thomas";
        player2 = "nico";
        Component struct1 = new Structural(101, new ArrayList<>(Arrays.asList(Connector.UNIVERSAL, Connector.UNIVERSAL, Connector.UNIVERSAL, Connector.UNIVERSAL))); // Structural component with universal connectors
        Component struct2 = new Structural(101, new ArrayList<>(Arrays.asList(Connector.UNIVERSAL, Connector.UNIVERSAL, Connector.UNIVERSAL, Connector.UNIVERSAL))); // Another structural component
        Component struct3 = new Structural(101, new ArrayList<>(Arrays.asList(Connector.UNIVERSAL, Connector.UNIVERSAL, Connector.UNIVERSAL, Connector.UNIVERSAL))); // Yet another structural component
        Component struct4 = new Structural(101, new ArrayList<>(Arrays.asList(Connector.UNIVERSAL, Connector.UNIVERSAL, Connector.UNIVERSAL, Connector.UNIVERSAL))); // Fourth structural component
        Component struct5 = new Structural(101, new ArrayList<>(Arrays.asList(Connector.UNIVERSAL, Connector.UNIVERSAL, Connector.UNIVERSAL, Connector.UNIVERSAL))); // Fifth structural component
        Component battery1 = new Battery(false, 201, new ArrayList<>(Arrays.asList(Connector.UNIVERSAL, Connector.UNIVERSAL, Connector.UNIVERSAL, Connector.UNIVERSAL))); // Battery component with universal connectors
        Component battery2 = new Battery(false, 201, new ArrayList<>(Arrays.asList(Connector.UNIVERSAL, Connector.UNIVERSAL, Connector.UNIVERSAL, Connector.UNIVERSAL))); // Another battery component
        Component shield = new Shield(901, new ArrayList<>(Arrays.asList(Connector.UNIVERSAL, Connector.UNIVERSAL, Connector.UNIVERSAL, Connector.UNIVERSAL))); // Shield component with universal connectors
        Component doubleCannon = new Cannon(true, 401, new ArrayList<>(Arrays.asList(Connector.UNIVERSAL, Connector.UNIVERSAL, Connector.UNIVERSAL, Connector.UNIVERSAL))); // Double cannon component with universal connectors

        try{
            cl1 = null;
            cl2 = null;
        }
        catch (Exception e){
            System.exit(-1);
        }
        gameState = new GameState(false, 2); // Create a new GameState for two players
        gameState.addPlayer(cl1, player1, Color.RED); // Add first player with red color
        gameState.addPlayer(cl2, player2, Color.BLUE); // Add second player with blue color
        gameState.assembleComponent(player1, struct1, 2,1); // Assemble structural components for player 1
        gameState.assembleComponent(player1, struct2, 2,2); // Assemble another structural component for player 1
        gameState.assembleComponent(player1, struct3, 2,4); // Assemble yet another structural component for player 1
        gameState.assembleComponent(player2, struct4, 2,2); // Assemble structural components for player 2
        gameState.assembleComponent(player2, struct5, 2,4); // Assemble another structural component for player 2
        gameState.assembleComponent(player1, battery1, 3,3);   // Assemble battery component for player 1
        gameState.assembleComponent(player2, battery2, 3,3); // Assemble battery component for player 2
        gameState.assembleComponent(player1, shield, 1,3); // Assemble shield component for player 1
        gameState.assembleComponent(player2, doubleCannon, 1,3); // Assemble double cannon component for player 2
        gameState.setPosition(player1, 1); // Set position for player 1
        gameState.setPosition(player2, 0); // Set position for player 2
        gameState.addCrew(player1, 2, 3); // Add crew member for player 1
        gameState.addBatteries(player1, 3, 3); // Add batteries for player 1
        gameState.addCrew(player2, 2, 3); // Add crew member for player 2
        gameState.addBatteries(player2, 3, 3); // Add batteries for player 2
        Meteor mLarge = new Meteor(true, Orientation.SOUTH); // Create a large meteor coming from the south
        Meteor mNotLarge1 = new Meteor(false, Orientation.EAST); // Create a small meteor coming from the east
        Meteor mNotLarge2 = new Meteor(false, Orientation.NORTH); // Create another small meteor coming from the north
        List<Meteor> meteorsList = new ArrayList<>(); // Create a list to hold the meteors
        meteorsList.add(mLarge); // Add the large meteor to the list
        meteorsList.add(mNotLarge1); // Add the first small meteor to the list
        meteorsList.add(mNotLarge2); // Add the second small meteor to the list
        meteorsSwarm = new MeteorsSwarm(meteorsList, 1828); // Create a MeteorsSwarm event card with the list of meteors and an ID
    }

    //The first player is hit by a large meteor that destroys a structural components. It also activates a shield
    //in order to defend the ship from an attack, losing a battery. The second player tries to activate a cannon
    //to destroy a small meteor coming from north, but cannons can't be used to destroy small meteors so it doesn't
    //lose batteries.
    @Test
    void testHitShip() { // Test hitting the ship with a meteor
        gameState.setGameDeck(new Deck(new ArrayList<>(Arrays.asList(meteorsSwarm)))); // Set the game deck with the MeteorsSwarm event card
        assertEquals(State.CARD_PICKING, gameState.getGameState()); // Check that the game state is CARD_PICKING
        gameState.pickNextCard(player1); // Player 1 picks the next card
        assertEquals(State.CARD_SOLVING, gameState.getGameState()); // Check that the game state is now CARD_SOLVING

        gameState.hit(player1, 5, false, false); // Player 1 is hit by a meteor at position 5, not large, not from south
        gameState.hit(player1, 5, false, false); // Player 1 is hit again by a meteor at position 5, not large, not from south
        gameState.hit(player1, 7, true, false); // Player 1 is hit by a large meteor at position 7, from south

        assertEquals(player2, gameState.getTurnPlayer()); // Check that it's now player 2's turn
        assertFalse(gameState.getPlayersPlay().get(player1).getShipBoard().getAssembledComponent(2, 1).isNotEmpty()); // Check that the structural component at (2, 1) for player 1 is destroyed
        assertTrue(gameState.getPlayersPlay().get(player1).getShipBoard().getAssembledComponent(1, 3).isNotEmpty()); // Check that the shield component at (1, 3) for player 1 is still intact

        gameState.hit(player2, 4, false, false); // Player 2 tries to hit a small meteor at position 4, not large, not from south
        gameState.hit(player2, 5, false, false); // Player 2 tries to hit another small meteor at position 5, not large, not from south
        gameState.hit(player2, 7, false, true); // Player 2 tries to hit a large meteor at position 7, from south, but fails because cannons can't be used against small meteors

        assertTrue(gameState.getPlayersPlay().get(player2).getShipBoard().getAssembledComponent(2, 2).isNotEmpty()); // Check that the structural component at (2, 2) for player 2 is still intact
        assertTrue(gameState.getPlayersPlay().get(player1).getShipBoard().getAssembledComponent(1, 3).isNotEmpty()); // Check that the shield component at (1, 3) for player 1 is still intact

        assertEquals(State.CARD_PICKING, gameState.getGameState()); // Check that the game state is now CARD_PICKING after resolving the card
        assertEquals(2, gameState.getNumberBatteries(player1)); // Check that player 1 has 2 batteries left after using one for the shield
        assertEquals(3, gameState.getNumberBatteries(player2)); // Check that player 2 has 3 batteries left after trying to use a cannon
    }

    //The first player is hit by a meteor form south that splits its ship in two pieces. After the second player
    //has solved the card, the game enters the ship repair phase, where player one destroys one components
    //in order to validate its ship.
    @Test
    void testShipRepair(){ // Test the ship repair phase after being hit by a meteor
        gameState.setGameDeck(new Deck(new ArrayList<>(Arrays.asList(meteorsSwarm)))); // Set the game deck with the MeteorsSwarm event card
        gameState.pickNextCard(player1); // Player 1 picks the next card

        gameState.hit(player1, 6, false, false); // Player 1 is hit by a meteor at position 6, not large, not from south
        gameState.hit(player1, 5, false, false); // Player 1 is hit again by a meteor at position 5, not large, not from south
        gameState.hit(player1, 4, false, false); // Player 1 is hit again by a meteor at position 4, not large, not from south

        gameState.hit(player2, 7, false, false); // Player 2 is hit by a meteor at position 7, not large, not from south
        gameState.hit(player2, 5, false, false); // Player 2 is hit again by a meteor at position 5, not large, not from south
        gameState.hit(player2, 4, false, false); // Player 2 is hit again by a meteor at position 4, not large, not from south

        assertEquals(State.SHIP_REPAIR, gameState.getGameState()); // Check that the game state is now SHIP_REPAIR after resolving the card
        assertEquals(3, gameState.getNumberBatteries(player1)); // Check that player 1 has 3 batteries left
        assertEquals(0, gameState.getNumberBatteries(player2)); // Check that player 2 has 0 batteries left after being hit by meteors

        gameState.destroyComponent(player1, 2, 1); // Player 1 destroys a component at (2, 1) to validate their ship

        assertEquals(State.CARD_PICKING, gameState.getGameState()); // Check that the game state is now CARD_PICKING after the ship repair phase
    }

}

