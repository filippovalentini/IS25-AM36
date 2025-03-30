package it.polimi.ingsw.galaxytrucker.model.eventCardClasses;

import it.polimi.ingsw.galaxytrucker.model.componentClasses.CargoHold;
import it.polimi.ingsw.galaxytrucker.model.componentClasses.Component;
import it.polimi.ingsw.galaxytrucker.model.enumerations.Color;
import it.polimi.ingsw.galaxytrucker.model.enumerations.Connector;
import it.polimi.ingsw.galaxytrucker.model.enumerations.Orientation;
import it.polimi.ingsw.galaxytrucker.model.enumerations.State;
import it.polimi.ingsw.galaxytrucker.model.gameClasses.GameState;
import it.polimi.ingsw.galaxytrucker.model.shotClasses.Meteor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MeteorsSwarmTest {
    private MeteorsSwarm meteorsSwarm;
    private String player1;
    private String player2;
    private GameState gameState;


    @BeforeEach
    void init() {
        player1 = "thomas";
        player2 = "nico";
        gameState = new GameState(false, 2);
        gameState.addPlayer(player1, Color.RED);
        gameState.addPlayer(player2, Color.BLUE);
        gameState.setPosition(player1, 1);
        gameState.setPosition(player2, 0);
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
        int result=0;
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 7; j++) {
                if (gameState.getPlayersPlay().get(player1).getShipBoard().getAssembledComponent(i, j).getImageID()==0) {
                    result += 1;
                }
            }
        }

                //assertEquals(false, gameState.getPlayersPlay().get(player1).getShipBoard().getAssembledComponent(4,0).isNotEmpty());
                assertEquals(1, result);


                assertDoesNotThrow(() -> meteorsSwarm.hitShip(gameState, player1, diceResult, false, false));
                assertDoesNotThrow(() -> meteorsSwarm.hitShip(gameState, player1, diceResult, false, false));
                assertDoesNotThrow(() -> meteorsSwarm.hitShip(gameState, player1, diceResult, false, false));
                //player 2 hits

                assertDoesNotThrow(() -> meteorsSwarm.hitShip(gameState, player2, diceResult, false, false));
                assertDoesNotThrow(() -> meteorsSwarm.hitShip(gameState, player2, diceResult, false, false));
                gameState.setGameState(State.CARD_SOLVING);
                assertDoesNotThrow(() -> meteorsSwarm.hitShip(gameState, player2, diceResult, false, false));
            }

        }

