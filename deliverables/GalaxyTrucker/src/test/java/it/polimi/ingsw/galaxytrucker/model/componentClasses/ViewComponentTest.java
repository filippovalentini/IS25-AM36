package it.polimi.ingsw.galaxytrucker.model.componentClasses;

import it.polimi.ingsw.galaxytrucker.model.enumerations.Connector;
import it.polimi.ingsw.galaxytrucker.model.enumerations.Orientation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ViewComponentTest {
    private Component component;
    private List<Connector> sides;

    @BeforeEach
    void init() {  // Initialize the component with a specific orientation and sides
        sides = new ArrayList<>();
        sides.add(Connector.SMOOTH);
        sides.add(Connector.DOUBLE);
        sides.add(Connector.UNIVERSAL);
        sides.add(Connector.SMOOTH);
        component = new Component(0, sides);
    }

    @Test
    void testEquals() { // Test equality of components with the same ID and sides
        Component c1 = new Component(0, sides); // Same ID and sides
        Component c2 = new Component(1, sides); // Different ID but same sides
        assertEquals(component, c1); // Should be equal
        assertNotEquals(component, c2); // Should not be equal
    }

    @Test
    void testRotateLeft(){ // Test the rotation of the component to the left
        component.rotateLeft(); // Rotate left from initial orientation
        assertEquals(Orientation.WEST, component.getOrientation()); // After one left rotation, orientation should be WEST
        component.rotateLeft(); // Rotate left again
        assertEquals(Orientation.SOUTH, component.getOrientation()); // After two left rotations, orientation should be SOUTH
        component.rotateLeft(); // Rotate left again
        assertEquals(Orientation.EAST, component.getOrientation()); // After three left rotations, orientation should be EAST
        component.rotateLeft(); // Rotate left again
        assertEquals(Orientation.NORTH, component.getOrientation()); // After four left rotations, orientation should be back to NORTH
    }

    @Test
    void testGetNorthSide() { // Test the north side of the component
        assertEquals(Orientation.NORTH, component.getOrientation()); // Initial orientation should be NORTH
        assertEquals(Connector.SMOOTH, component.getNorthSide()); // North side should be SMOOTH initially
        component.rotateLeft(); // Rotate left to change orientation
        assertEquals(Connector.DOUBLE, component.getNorthSide()); // After one left rotation, north side should be DOUBLE
    }

    @Test
    void testGetEastSide() { // Test the east side of the component
        assertEquals(Connector.DOUBLE, component.getEastSide()); // East side should be DOUBLE initially
        component.rotateLeft(); // Rotate left to change orientation
        assertEquals(Connector.UNIVERSAL, component.getEastSide()); // After one left rotation, east side should be UNIVERSAL
    }

    @Test
    void testGetSouthSide() { // Test the south side of the component
        assertEquals(Connector.UNIVERSAL, component.getSouthSide()); // South side should be UNIVERSAL initially
        component.rotateLeft(); // Rotate left to change orientation
        assertEquals(Connector.SMOOTH, component.getSouthSide()); // After one left rotation, south side should be SMOOTH
    }

    @Test
    void testGetWestSide() { // Test the west side of the component
        assertEquals(Connector.SMOOTH, component.getWestSide()); // West side should be SMOOTH initially
        component.rotateLeft(); // Rotate left to change orientation
        assertEquals(Connector.SMOOTH, component.getWestSide()); // After one left rotation, west side should still be SMOOTH
    }

    @Test
    void testIsWellOriented() {
        assertTrue(component.isWellOriented());
    } // Test if the component is well oriented (initially true)

    @Test
    void testIsNotEmpty(){ // Test if the component is not empty based on its ID
        Component c1 = new Battery(false, 1, new ArrayList<>(Arrays.asList(Connector.UNIVERSAL, Connector.UNIVERSAL, Connector.UNIVERSAL, Connector.SMOOTH))); //
        assertTrue(c1.isNotEmpty()); // Should be true since ID is 1
        Component c2 = new Battery(false, 0, new ArrayList<>(Arrays.asList(Connector.UNIVERSAL, Connector.UNIVERSAL, Connector.UNIVERSAL, Connector.SMOOTH)));
        assertFalse(c2.isNotEmpty()); // Should be false since ID is 0
    }

}