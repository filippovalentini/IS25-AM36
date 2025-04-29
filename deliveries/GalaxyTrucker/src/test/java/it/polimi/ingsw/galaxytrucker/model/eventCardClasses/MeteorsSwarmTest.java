package it.polimi.ingsw.galaxytrucker.model.eventCardClasses;

import it.polimi.ingsw.galaxytrucker.model.componentClasses.CargoHold;
import it.polimi.ingsw.galaxytrucker.model.componentClasses.Component;
import it.polimi.ingsw.galaxytrucker.model.enumerations.Color;
import it.polimi.ingsw.galaxytrucker.model.enumerations.Connector;
import it.polimi.ingsw.galaxytrucker.model.enumerations.Orientation;
import it.polimi.ingsw.galaxytrucker.model.enumerations.State;
import it.polimi.ingsw.galaxytrucker.model.gameClasses.GameState;
import it.polimi.ingsw.galaxytrucker.model.shotClasses.Meteor;
import it.polimi.ingsw.galaxytrucker.network.rmi.client.ClientRMI;
import it.polimi.ingsw.galaxytrucker.network.rmi.server.VirtualViewRMI;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
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
        try{
            cl1 = new ClientRMI(player1, Color.RED);
            cl2 = new ClientRMI(player2, Color.BLUE);
        }
        catch (Exception e){
            System.exit(-1);
        }
        gameState = new GameState(false, 2);
        gameState.addPlayer(cl1, player1, Color.RED);
        gameState.addPlayer(cl2, player2, Color.BLUE);
        gameState.setPosition(player1, 1);
        gameState.setPosition(player2, 0);
        gameState.addCrew(player1, 2, 3);
        gameState.addCrew(player2, 2, 3);
        Meteor mLarge = new Meteor(true, Orientation.SOUTH);
        Meteor mNotLarge1 = new Meteor(false, Orientation.EAST);
        Meteor mNotLarge2 = new Meteor(false, Orientation.WEST);
        List<Meteor> meteorsList = new ArrayList<>();
        meteorsList.add(mLarge);
        meteorsList.add(mNotLarge1);
        meteorsList.add(mNotLarge2);
        meteorsSwarm = new MeteorsSwarm(meteorsList, 1828);
        gameState.setGameState(State.SHIP_BUILDING);
        int h = 0;
        for (int i = 0; i < 151; i++) { //show all components
            gameState.pickHidden(player1);
            gameState.putShown(player1);
        }
        List<Component> shownComponents = gameState.getShownComponent();

        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 7; j++) {
                if (i == 2 && j == 3) {
                    continue; // skip the center cabin
                }
                gameState.assembleComponent(player1, gameState.getShownComponent().get(h), i, j);
                h += h;
            }
        }
        gameState.setGameState(State.CARD_SOLVING);
    }

    @Test
    void testHitShip() {
        int diceResult = 4;
        meteorsSwarm.hitShip(gameState, player1, diceResult, false, false);
        //player 1 hits

                //assertEquals(false, gameState.getPlayersPlay().get(player1).getShipBoard().getAssembledComponent(4,0).isNotEmpty());
        assertTrue(gameState.getPlayersPlay().get(player1).getShipBoard().getAssembledComponent(4, 0).getImageID() == 0);


                assertDoesNotThrow(() -> meteorsSwarm.hitShip(gameState, player1, diceResult, false, false));
                assertDoesNotThrow(() -> meteorsSwarm.hitShip(gameState, player1, diceResult, false, false));
                assertDoesNotThrow(() -> meteorsSwarm.hitShip(gameState, player1, diceResult, false, false));
                //player 2 hits

                assertDoesNotThrow(() -> meteorsSwarm.hitShip(gameState, player2, diceResult, false, false));
                assertDoesNotThrow(() -> meteorsSwarm.hitShip(gameState, player2, diceResult, false, false));
                gameState.setGameState(State.CARD_SOLVING);
                assertDoesNotThrow(() -> meteorsSwarm.hitShip(gameState, player2, diceResult, false, false));

        List<Integer> y= new ArrayList<>(1);
        List<Integer> x= new ArrayList<>(1);
        List<Integer> e= new ArrayList<>(1);
        x.add(2);
        y.add(3);
        e.add(2);
        gameState.removedCrewMember(player1,x,y,e,2);
        int prova= gameState.getCrewCount(player1);
        meteorsSwarm.hitShip(gameState, player1, diceResult, false, false);
        meteorsSwarm.hitShip(gameState, player1, diceResult, false, false);
        meteorsSwarm.hitShip(gameState, player1, diceResult, false, false);
        meteorsSwarm.hitShip(gameState, player1, diceResult, false, false);
        assertFalse(gameState.getPlayersPos().containsKey(player1));

    }

        }

