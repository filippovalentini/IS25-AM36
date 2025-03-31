package it.polimi.ingsw.galaxytrucker.model.eventCardClasses;

import it.polimi.ingsw.galaxytrucker.model.enumerations.Color;
import it.polimi.ingsw.galaxytrucker.model.enumerations.Orientation;
import it.polimi.ingsw.galaxytrucker.model.enumerations.State;
import it.polimi.ingsw.galaxytrucker.model.exceptions.InvalidActionException;
import it.polimi.ingsw.galaxytrucker.model.gameClasses.GameState;
import it.polimi.ingsw.galaxytrucker.model.shotClasses.CannonShot;
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
    List<CannonShot> cannonShots;

    @BeforeEach
    void init(){
        gameState = new GameState(false, 2);
        nickname = "player1";
        nickname2 = "player2";
        gameState.addPlayer(nickname, Color.RED);
        gameState.addPlayer(nickname2, Color.BLUE);
        gameState.setPosition(nickname, 0);
        gameState.setPosition(nickname2, 1);
        CannonShot cShotLarge = new CannonShot(true, Orientation.SOUTH);
        CannonShot cShotNotLarge1 = new CannonShot(false, Orientation.SOUTH);
        CannonShot cShotNotLarge2 = new CannonShot(false, Orientation.SOUTH);
        cannonShots = new ArrayList<>();
        cannonShots.add(cShotLarge);
        cannonShots.add(cShotNotLarge1);
        cannonShots.add(cShotNotLarge2);
        pirates = new Pirates(4, 0, cannonShots, 1, 0);
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
        boolean activateShield = false;
        boolean activateCannon = false;
        //assertDoesNotThrow(() -> pirates.hitShip(gameState, nickname, diceResult, activateShield, activateCannon));
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
        //assertDoesNotThrow(() -> pirates.hitShip(gameState, nickname2, diceResult, activateShield, activateCannon));
    }
}
