package it.polimi.ingsw.galaxytrucker.model.shotClasses;

import it.polimi.ingsw.galaxytrucker.model.enumerations.Connector;
import it.polimi.ingsw.galaxytrucker.model.enumerations.Orientation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CannonShotTest {
    private CannonShot cannonShot;

    @BeforeEach
    public void setUp() {
        cannonShot = new CannonShot(true, Orientation.NORTH);
    }

    //test isLarge()
    @Test
    public void testIsLarge() {
        assertTrue(cannonShot.isLarge());
    }
    //test getOrientation()
    @Test
    public void testGetOrientation() {
        assertEquals(Orientation.NORTH, cannonShot.getOrientation());
    }
}
