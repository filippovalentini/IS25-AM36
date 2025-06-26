package it.polimi.ingsw.galaxytrucker.model.componentClasses;

import it.polimi.ingsw.galaxytrucker.model.enumerations.Connector;
import it.polimi.ingsw.galaxytrucker.model.enumerations.Orientation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ShieldTest {
    private Shield shield;
    @BeforeEach
    public void setUp() { // Initialize the shield with a list of connectors
        List<Connector> connectors = new ArrayList<>();
        connectors.add(Connector.SMOOTH);
        connectors.add(Connector.SINGLE);
        connectors.add(Connector.DOUBLE);
        connectors.add(Connector.UNIVERSAL);
        shield= new Shield(1, connectors); // Create a new Shield instance with ID 1 and the connectors list

    }
    // Test for protects method
    @Test
    public void protects_North() { // Test if the shield protects the North and East orientations
        shield.orientation = Orientation.NORTH; // Set the orientation of the shield to North
        assertTrue(shield.protects(Orientation.NORTH)); // Check if the shield protects North
        assertTrue(shield.protects(Orientation.EAST)); // Check if the shield protects East
    }
    @Test
    public void protects_East() { // Test if the shield protects the East and South orientations
        shield.orientation = Orientation.EAST; // Set the orientation of the shield to East
        assertTrue(shield.protects(Orientation.EAST)); // Check if the shield protects East
        assertTrue(shield.protects(Orientation.SOUTH)); // Check if the shield protects South
    }
    @Test
    public void protects_South() { // Test if the shield protects the South and West orientations
        shield.orientation = Orientation.SOUTH; // Set the orientation of the shield to South
        assertTrue(shield.protects(Orientation.SOUTH)); // Check if the shield protects South
        assertTrue(shield.protects(Orientation.WEST)); // Check if the shield protects West
    }
    @Test
    public void protects_West() { // Test if the shield protects the West and North orientations
        shield.orientation = Orientation.WEST; // Set the orientation of the shield to West
        assertTrue(shield.protects(Orientation.WEST)); // Check if the shield protects West
        assertTrue(shield.protects(Orientation.NORTH)); // Check if the shield protects North
    }
    // Test for clone method
    @Test
    public void cloneTest() { // Test the clone method of the Shield class
        Shield clonedShield = (Shield) shield.clone(); // Create a clone of the shield
        assertSame(shield.getImageID(), clonedShield.getImageID()); // Check if the image ID of the original and cloned shield are the same
        assertSame(shield.getOrientation(), clonedShield.getOrientation()); // Check if the orientation of the original and cloned shield are the same
    }
}
