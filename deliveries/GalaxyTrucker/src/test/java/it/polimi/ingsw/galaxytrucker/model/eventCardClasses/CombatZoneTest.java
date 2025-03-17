package it.polimi.ingsw.galaxytrucker.model.eventCardClasses;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CombatZoneTest {
    private CombatZone combatZoneLV1;
    private CombatZone combatZoneLV2;

    @BeforeEach
    void init() {
        combatZoneLV1 = new CombatZone(true, 0);
        combatZoneLV2 = new CombatZone(false, 0);
    }

    @Test
    void testSpecialEffectLV1() {

    }

    @Test
    void testSpecialEffectLV2() {

    }

    @Test
    void testUseBatteriesAvailable(){

    }

    @Test
    void testShouldNotUseBatteriesNotAvailable(){

    }

    @Test
    void testHitShip(){

    }
}