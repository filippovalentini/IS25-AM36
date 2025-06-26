package it.polimi.ingsw.galaxytrucker.model.componentClasses;
import it.polimi.ingsw.galaxytrucker.model.enumerations.Connector;
import it.polimi.ingsw.galaxytrucker.model.enumerations.Orientation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class CannonTest {
    private Cannon cannon;
    @BeforeEach
    void init() {
        cannon = new Cannon(true, 0, new ArrayList<>());
        cannon.orientation = Orientation.NORTH;

    }
    @Test
    void testHasDoubleCannonsTest() {
        assertTrue(cannon.hasDoubleCannons());
    } // Test for hasDoubleCannons method
    @Test
    void hasSingleCannonTest(){
        assertFalse( cannon.hasSingleCannon());
    } // Test for hasSingleCannon method
    @Test
    void pointsForwardTest(){ // Test for pointsForward method
        assertTrue(cannon.pointsForward()); // Assuming the cannon is oriented NORTH, it should point forward
        cannon.orientation = Orientation.EAST; // Change orientation to EAST
        assertFalse(cannon.pointsForward()); // Now it should not point forward
    }
    @Test
    void testGetSides() { // Test for getSides method
        ArrayList<Connector> sides = new ArrayList<>(); // Create a new list of sides
        sides.add(Connector.SINGLE); // Add a SINGLE connector
        cannon = new Cannon(true, 0, sides); // Initialize the cannon with the new sides
        assertEquals(sides, cannon.getSides()); // Check if the sides of the cannon match the expected sides
    }
    @Test
    void testClone() {
        Cannon clonedCannon = (Cannon) cannon.clone(); // Clone the cannon object
        assertNotSame(cannon, clonedCannon); // Ensure that the cloned object is not the same instance as the original
        assertEquals(cannon.isDouble(), clonedCannon.isDouble()); // Check if the double status is the same
        assertEquals(cannon.getImageID(), clonedCannon.getImageID()); // Check if the image ID is the same
        assertEquals(cannon.getOrientation(), clonedCannon.getOrientation()); // Check if the orientation is the same
        assertEquals(cannon.getSides(), clonedCannon.getSides()); // Check if the sides are the same

    }


}
