package it.polimi.ingsw.galaxytrucker.model.componentClasses;

import it.polimi.ingsw.galaxytrucker.model.enumerations.Connector;
import it.polimi.ingsw.galaxytrucker.model.enumerations.Orientation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class EngineTest {
    private Engine engine1;
    private Engine engine2;

    @BeforeEach
    public void setUp() { // Initialize the engines with appropriate parameters
        List<Connector> connectors = new ArrayList<>();
        connectors.add(Connector.SMOOTH);
        connectors.add(Connector.SINGLE);
        connectors.add(Connector.DOUBLE);
        connectors.add(Connector.UNIVERSAL);
        // Initialize the Engine object with appropriate parameters
        // Assuming the constructor takes a boolean for double engine, an image ID, and a list of connectors
        engine1 = new Engine(true, 1, connectors);
        engine2= new Engine(false, 1, connectors);
    }

    //test for isWellOriented method
    @Test
    public void isWellOriented_true() { // Test when the engine is well oriented
        engine1.orientation= Orientation.NORTH; // Set orientation to NORTH
        assertTrue(engine1.isWellOriented()); // Check if the engine is well oriented
    }
    @Test
    public void isWellOriented_false() { // Test when the engine is not well oriented
        engine1.orientation= Orientation.EAST; // Set orientation to EAST
        assertFalse(engine1.isWellOriented()); // Check if the engine is not well oriented
    }
    //test for hasDoubleEngines method
    @Test
    public void hasDoubleEngines_true() {
        assertTrue(engine1.hasDoubleEngines());
    } // Test for hasDoubleEngines method when engine1 is a double engine
    @Test
    public void hasDoubleEngines_false() {
        assertFalse(engine2.hasDoubleEngines());
    } // Test for hasDoubleEngines method when engine2 is a single engine


    //test for hasSingleEngine method
    @Test
    public void hasSingleEngine_true() {
        assertTrue(engine2.hasSingleEngine());
    } // Test for hasSingleEngine method when engine2 is a single engine
    @Test
    public void hasSingleEngine_false() {
        assertFalse(engine1.hasSingleEngine());
    } // Test for hasSingleEngine method when engine1 is a double engine
    //test for clone method
    @Test
    public void cloneTest() { // Test for clone method
        Engine clonedEngine = (Engine) engine1.clone(); // Clone engine1
        assertEquals(clonedEngine.isDouble(), engine1.isDouble()); // Check if the cloned engine has the same double status
        assertEquals(clonedEngine.getImageID(), engine1.getImageID()); // Check if the cloned engine has the same image ID
        assertSame(clonedEngine.getOrientation(), engine1.getOrientation()); // Check if the cloned engine has the same orientation
        assertSame(clonedEngine.orientation, engine1.orientation); // Check if the cloned engine's orientation is the same as engine1's orientation
    }

}
