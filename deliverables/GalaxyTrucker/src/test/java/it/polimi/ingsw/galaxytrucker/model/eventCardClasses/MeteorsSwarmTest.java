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

class MeteorsSwarmTest {
    private MeteorsSwarm meteorsSwarm;
    private String player1;
    private String player2;
    private VirtualViewRMI cl1;
    private VirtualViewRMI cl2;
    private GameState gameState;


    @BeforeEach
    void init() {
        player1 = "thomas";
        player2 = "nico";
        Component struct1 = new Structural(101, new ArrayList<>(Arrays.asList(Connector.UNIVERSAL, Connector.UNIVERSAL, Connector.UNIVERSAL, Connector.UNIVERSAL)));
        Component struct2 = new Structural(101, new ArrayList<>(Arrays.asList(Connector.UNIVERSAL, Connector.UNIVERSAL, Connector.UNIVERSAL, Connector.UNIVERSAL)));
        Component struct3 = new Structural(101, new ArrayList<>(Arrays.asList(Connector.UNIVERSAL, Connector.UNIVERSAL, Connector.UNIVERSAL, Connector.UNIVERSAL)));
        Component struct4 = new Structural(101, new ArrayList<>(Arrays.asList(Connector.UNIVERSAL, Connector.UNIVERSAL, Connector.UNIVERSAL, Connector.UNIVERSAL)));
        Component struct5 = new Structural(101, new ArrayList<>(Arrays.asList(Connector.UNIVERSAL, Connector.UNIVERSAL, Connector.UNIVERSAL, Connector.UNIVERSAL)));
        Component battery1 = new Battery(false, 201, new ArrayList<>(Arrays.asList(Connector.UNIVERSAL, Connector.UNIVERSAL, Connector.UNIVERSAL, Connector.UNIVERSAL)));
        Component battery2 = new Battery(false, 201, new ArrayList<>(Arrays.asList(Connector.UNIVERSAL, Connector.UNIVERSAL, Connector.UNIVERSAL, Connector.UNIVERSAL)));
        Component shield = new Shield(901, new ArrayList<>(Arrays.asList(Connector.UNIVERSAL, Connector.UNIVERSAL, Connector.UNIVERSAL, Connector.UNIVERSAL)));
        Component doubleCannon = new Cannon(true, 401, new ArrayList<>(Arrays.asList(Connector.UNIVERSAL, Connector.UNIVERSAL, Connector.UNIVERSAL, Connector.UNIVERSAL)));

        try{
            cl1 = null;
            cl2 = null;
        }
        catch (Exception e){
            System.exit(-1);
        }
        gameState = new GameState(false, 2);
        gameState.addPlayer(cl1, player1, Color.RED);
        gameState.addPlayer(cl2, player2, Color.BLUE);
        gameState.assembleComponent(player1, struct1, 2,1);
        gameState.assembleComponent(player1, struct2, 2,2);
        gameState.assembleComponent(player1, struct3, 2,4);
        gameState.assembleComponent(player2, struct4, 2,2);
        gameState.assembleComponent(player2, struct5, 2,4);
        gameState.assembleComponent(player1, battery1, 3,3);
        gameState.assembleComponent(player2, battery2, 3,3);
        gameState.assembleComponent(player1, shield, 1,3);
        gameState.assembleComponent(player2, doubleCannon, 1,3);
        gameState.setPosition(player1, 1);
        gameState.setPosition(player2, 0);
        gameState.addCrew(player1, 2, 3);
        gameState.addBatteries(player1, 3, 3);
        gameState.addCrew(player2, 2, 3);
        gameState.addBatteries(player2, 3, 3);
        Meteor mLarge = new Meteor(true, Orientation.SOUTH);
        Meteor mNotLarge1 = new Meteor(false, Orientation.EAST);
        Meteor mNotLarge2 = new Meteor(false, Orientation.NORTH);
        List<Meteor> meteorsList = new ArrayList<>();
        meteorsList.add(mLarge);
        meteorsList.add(mNotLarge1);
        meteorsList.add(mNotLarge2);
        meteorsSwarm = new MeteorsSwarm(meteorsList, 1828);
    }

    //The first player is hit by a large meteor that destroys a structural components. It also activates a shield
    //in order to defend the ship from an attack, losing a battery. The second player tries to activate a cannon
    //to destroy a small meteor coming from north, but cannons can't be used to destroy small meteors so it doesn't
    //lose batteries.
    @Test
    void testHitShip() {
        gameState.setGameDeck(new Deck(new ArrayList<>(Arrays.asList(meteorsSwarm))));
        assertEquals(State.CARD_PICKING, gameState.getGameState());
        gameState.pickNextCard(player1);
        assertEquals(State.CARD_SOLVING, gameState.getGameState());

        gameState.hit(player1, 5, false, false);
        gameState.hit(player1, 5, false, false);
        gameState.hit(player1, 7, true, false);

        assertEquals(player2, gameState.getTurnPlayer());
        assertFalse(gameState.getPlayersPlay().get(player1).getShipBoard().getAssembledComponent(2, 1).isNotEmpty());
        assertTrue(gameState.getPlayersPlay().get(player1).getShipBoard().getAssembledComponent(1, 3).isNotEmpty());

        gameState.hit(player2, 4, false, false);
        gameState.hit(player2, 5, false, false);
        gameState.hit(player2, 7, false, true);

        assertTrue(gameState.getPlayersPlay().get(player2).getShipBoard().getAssembledComponent(2, 2).isNotEmpty());
        assertTrue(gameState.getPlayersPlay().get(player1).getShipBoard().getAssembledComponent(1, 3).isNotEmpty());

        assertEquals(State.CARD_PICKING, gameState.getGameState());
        assertEquals(2, gameState.getNumberBatteries(player1));
        assertEquals(3, gameState.getNumberBatteries(player2));
    }

    //The first player is hit by a meteor form south that splits its ship in two pieces. After the second player
    //has solved the card, the game enters the ship repair phase, where player one destroys one components
    //in order to validate its ship.
    @Test
    void testShipRepair(){
        gameState.setGameDeck(new Deck(new ArrayList<>(Arrays.asList(meteorsSwarm))));
        gameState.pickNextCard(player1);

        gameState.hit(player1, 6, false, false);
        gameState.hit(player1, 5, false, false);
        gameState.hit(player1, 4, false, false);

        gameState.hit(player2, 7, false, false);
        gameState.hit(player2, 5, false, false);
        gameState.hit(player2, 4, false, false);

        assertEquals(State.SHIP_REPAIR, gameState.getGameState());
        assertEquals(3, gameState.getNumberBatteries(player1));
        assertEquals(0, gameState.getNumberBatteries(player2));

        gameState.destroyComponent(player1, 2, 1);

        assertEquals(State.CARD_PICKING, gameState.getGameState());
    }

}

