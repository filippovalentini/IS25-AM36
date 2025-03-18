package it.polimi.ingsw.galaxytrucker.model.eventCardClasses;

import it.polimi.ingsw.galaxytrucker.model.gameClasses.GameState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import it.polimi.ingsw.galaxytrucker.model.enumerations.Color;
import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;

class CombatZoneTest {
    private CombatZone combatZoneLV1;
    private CombatZone combatZoneLV2;
    private GameState gameState;
    private String player1;
    private String player2;

    @BeforeEach
    void init() {
        gameState = new GameState(false, 2);
        gameState.addPlayer(player1, Color.RED);
        gameState.addPlayer(player2, Color.YELLOW);
        combatZoneLV1 = new CombatZone(true, 0);
        combatZoneLV2 = new CombatZone(false, 0);
    }

    @Test
    void testSpecialEffectLV1() {
        combatZoneLV1.specialEffect(gameState);
        //
    }

    @Test
    void testSpecialEffectLV2() {
        combatZoneLV2.specialEffect(gameState);
        //
    }

    @Test
    void testUseBatteriesAvailable(){
        combatZoneLV1.useBatteries(gameState, player1, 2);
        //
    }

    @Test
    void testShouldNotUseBatteriesNotAvailable(){
        combatZoneLV1.useBatteries(gameState, player2, 1000);
        //
    }

    @Test
    void testHitShip(){
        combatZoneLV1.hitShip(gameState, player1, 2, false, false);
        //
    }
}