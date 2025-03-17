package it.polimi.ingsw.galaxytrucker.model.eventCardClasses;

import it.polimi.ingsw.galaxytrucker.model.enumerations.Orientation;
import it.polimi.ingsw.galaxytrucker.model.shotClasses.CannonShot;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PiratesTest {
    private Pirates pirates;

    @BeforeEach
    void init(){
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
    void testDefeat(){

    }

    void testShouldNotDefeatNotEnoughStrength(){

    }

    @Test
    void testHitShip(){

    }

    void testShouldNotHitShipIfDefeated(){

    }
}