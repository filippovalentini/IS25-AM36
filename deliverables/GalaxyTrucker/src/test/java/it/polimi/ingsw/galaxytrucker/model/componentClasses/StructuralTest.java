package it.polimi.ingsw.galaxytrucker.model.componentClasses;

import it.polimi.ingsw.galaxytrucker.model.enumerations.Connector;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;

public class StructuralTest {
    private Structural structural;

    @BeforeEach
    public void setUp(){ // Initialize a Structural object with a list of connectors
        List<Connector> connectors = new ArrayList<>();
        connectors.add(Connector.SMOOTH);
        connectors.add(Connector.SINGLE);
        connectors.add(Connector.DOUBLE);
        connectors.add(Connector.UNIVERSAL);
        structural= new Structural(1, connectors); // Create a Structural object with ID 1 and the list of connectors
    }
    //test for clone method
    @Test
    public void cloneTest() { // Verify that cloning a Structural object creates a new instance with the same properties
        Structural clonedStructural = (Structural) structural.clone(); // Clone the structural object
        assertEquals(structural.getImageID(), clonedStructural.getImageID()); // Check if the image ID is the same
        assertEquals(structural.getOrientation(), clonedStructural.getOrientation()); // Check if the orientation is the same
    }

}
