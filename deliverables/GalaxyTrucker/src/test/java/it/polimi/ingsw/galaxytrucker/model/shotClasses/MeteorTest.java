package it.polimi.ingsw.galaxytrucker.model.shotClasses;

import it.polimi.ingsw.galaxytrucker.model.enumerations.Orientation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MeteorTest {
    private Meteor meteor;

    /**
     * Initializes a {@code Meteor} instance with large size and NORTH orientation
     * before each test.
     */
    @BeforeEach
    public void setUp() {
        meteor = new Meteor(true, Orientation.NORTH);
    }

    /**
     * Tests the {@code isLarge} method.
     * Verifies that the meteor is marked as large.
     */
    @Test
    public void testIsLarge() {
        assertTrue(meteor.isLarge());
    }

    /**
     * Tests the {@code getOrientation} method.
     * Verifies that the orientation of the meteor is correctly set to NORTH.
     */
    @Test
    public void testGetOrientation() {
        assertEquals(Orientation.NORTH, meteor.getOrientation());
    }
}
