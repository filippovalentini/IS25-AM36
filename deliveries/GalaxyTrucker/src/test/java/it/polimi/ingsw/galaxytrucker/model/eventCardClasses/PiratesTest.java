package it.polimi.ingsw.galaxytrucker.model.eventCardClasses;

import it.polimi.ingsw.galaxytrucker.model.componentClasses.Component;
import it.polimi.ingsw.galaxytrucker.model.enumerations.Color;
import it.polimi.ingsw.galaxytrucker.model.enumerations.Orientation;
import it.polimi.ingsw.galaxytrucker.model.enumerations.State;
import it.polimi.ingsw.galaxytrucker.model.exceptions.InvalidActionException;
import it.polimi.ingsw.galaxytrucker.model.gameClasses.GameState;
import it.polimi.ingsw.galaxytrucker.model.shotClasses.CannonShot;
import it.polimi.ingsw.galaxytrucker.network.rmi.client.ClientRMI;
import it.polimi.ingsw.galaxytrucker.network.rmi.server.VirtualViewRMI;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
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
    void init(){
        gameState = new GameState(false, 2);
        nickname = "player1";
        nickname2 = "player2";
        try{
            cl1 = new ClientRMI();
            cl2 = new ClientRMI();
        }
        catch (Exception e){
            System.exit(-1);
        }
        gameState.addPlayer(cl1, nickname, Color.RED);
        gameState.addPlayer(cl2, nickname2, Color.BLUE);
        gameState.setPosition(nickname, 0);
        gameState.setPosition(nickname2, 1);
        CannonShot cShotLarge = new CannonShot(true, Orientation.SOUTH);
        CannonShot cShotNotLarge1 = new CannonShot(false, Orientation.SOUTH);
        CannonShot cShotNotLarge2 = new CannonShot(false, Orientation.SOUTH);
        cannonShots = new ArrayList<>();
        cannonShots.add(cShotLarge);
        cannonShots.add(cShotNotLarge1);
        cannonShots.add(cShotNotLarge2);
        pirates = new Pirates(4, 1, cannonShots, 1, 0);
        gameState.setGameState(State.SHIP_BUILDING);
        int c=0;
        for (int i = 0; i < 151; i++) { //show all components
            gameState.pickHidden(nickname);
            gameState.putShown(nickname);
        }
        List<Component> shownComponents = gameState.getShownComponent();
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 7; j++) {
                gameState.assembleComponent(nickname,gameState.getShownComponent().get(c), i, j);
                c += c;
            }
        }
        gameState.setGameState(State.CARD_SOLVING);
    }

    @Test
    void testDefeat() {
        int usedBatteries = 0;
        boolean looseDays = true;
        assertDoesNotThrow(() -> pirates.defeat(gameState, nickname, usedBatteries, looseDays));
    }

    @Test
    void testShouldNotDefeatNotEnoughStrength() {
        Pirates strongPirates = new Pirates(4, 1000, cannonShots, 1, 0);
        int usedBatteries = 0;
        boolean looseDays = true;
        assertDoesNotThrow(() -> pirates.defeat(gameState, nickname, usedBatteries, looseDays));
    }

    @Test
    void testHitShip() {
        int diceResult = 4;
        int usedBatteries = 0;
        boolean looseDays = true;
        boolean activateShield = false;
        boolean activateCannon = false;
        pirates.defeat(gameState, nickname, usedBatteries, looseDays);
        pirates.hitShip(gameState, nickname, diceResult, activateShield, activateCannon);
        assertEquals(true, gameState.getPlayersPlay().get(nickname).getShipBoard().getAssembledComponent(4, 0).getImageID()==0);
        assertDoesNotThrow(() -> pirates.hitShip(gameState, nickname, diceResult, activateShield, activateCannon));
    }

    @Test
    void testShouldHitShipEvenIfDefeated() {
        int usedBatteries = 0;
        boolean looseDays = false;
        pirates.defeat(gameState, nickname, usedBatteries, looseDays);
        int diceResult = 4;
        boolean activateShield = false;
        boolean activateCannon = false;
        gameState.setGameState(State.CARD_SOLVING);
        assertDoesNotThrow(() -> pirates.hitShip(gameState, nickname2, diceResult, activateShield, activateCannon));
    }
}
