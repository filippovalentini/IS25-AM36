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

    /**
     * Initializes a {@code CannonShot} instance with large size and NORTH orientation
     * before each test.
     */
    @BeforeEach
    public void setUp() {
        cannonShot = new CannonShot(true, Orientation.NORTH);
    }

    /**
     * Tests the {@code isLarge} method.
     * Verifies that the cannon shot is marked as large.
     */
    @Test
    public void testIsLarge() {
        assertTrue(cannonShot.isLarge());
    }

    /**
     * Tests the {@code getOrientation} method.
     * Verifies that the orientation of the cannon shot is correctly set to NORTH.
     */
    @Test
    public void testGetOrientation() {
        assertEquals(Orientation.NORTH, cannonShot.getOrientation());
    }
}
