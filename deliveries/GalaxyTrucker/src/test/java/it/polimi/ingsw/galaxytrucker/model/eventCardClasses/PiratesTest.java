package it.polimi.ingsw.galaxytrucker.model.eventCardClasses;

import it.polimi.ingsw.galaxytrucker.model.enumerations.Color;
import it.polimi.ingsw.galaxytrucker.model.enumerations.Orientation;
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

    @BeforeEach
    void init(){
        gameState.addPlayer(nickname, Color.RED);
        gameState.addPlayer(nickname2, Color.BLUE);
        CannonShot cShotLarge = new CannonShot(true, Orientation.SOUTH);
        CannonShot cShotNotLarge1 = new CannonShot(false, Orientation.SOUTH);
        CannonShot cShotNotLarge2 = new CannonShot(false, Orientation.SOUTH);
        List<CannonShot> cannonShots = new ArrayList<>();
        cannonShots.add(cShotLarge);
        cannonShots.add(cShotNotLarge1);
        cannonShots.add(cShotNotLarge2);
        pirates = new Pirates(4, 5, cannonShots, 1, 0);
    }

    @Test
    void testDefeat() {
        int usedBatteries = 5;
        boolean looseDays = true;

        assertDoesNotThrow(() -> pirates.defeat(gameState, nickname, usedBatteries, looseDays));
    }

    @Test
    void testShouldNotDefeatNotEnoughStrength() {
        int usedBatteries = 3;
        boolean looseDays = true;

        assertDoesNotThrow(() -> pirates.defeat(gameState, nickname, usedBatteries, looseDays));
    }

    @Test
    void testHitShip() {
        int diceResult = 4;
        boolean activateShield = true;
        boolean activateCannon = true;

        assertDoesNotThrow(() -> pirates.hitShip(gameState, nickname, diceResult, activateShield, activateCannon));
    }

    @Test
    void testShouldNotHitShipIfDefeated() {
        int usedBatteries = 5;
        boolean looseDays = true;
        pirates.defeat(gameState, nickname, usedBatteries, looseDays);

        int diceResult = 4;
        boolean activateShield = true;
        boolean activateCannon = true;

        assertDoesNotThrow(() -> pirates.hitShip(gameState, nickname, diceResult, activateShield, activateCannon));
    }
}
