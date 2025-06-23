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
    public void setUp() {
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
    public void isWellOriented_true() {
        engine1.orientation= Orientation.NORTH; // Set orientation to NORTH
        assertTrue(engine1.isWellOriented());
    }
    @Test
    public void isWellOriented_false() {
        engine1.orientation= Orientation.EAST; // Set orientation to EAST
        assertFalse(engine1.isWellOriented());
    }
    //test for hasDoubleEngines method
    @Test
    public void hasDoubleEngines_true() {
        assertTrue(engine1.hasDoubleEngines());
    }
    @Test
    public void hasDoubleEngines_false() {
        assertFalse(engine2.hasDoubleEngines());
    }
    //test for hasSingleEngine method
    @Test
    public void hasSingleEngine_true() {
        assertTrue(engine2.hasSingleEngine());
    }
    @Test
    public void hasSingleEngine_false() {
        assertFalse(engine1.hasSingleEngine());
    }
    //test for clone method
    @Test
    public void cloneTest() {
        Engine clonedEngine = (Engine) engine1.clone();
        assertEquals(clonedEngine.isDouble(), engine1.isDouble());
        assertEquals(clonedEngine.getImageID(), engine1.getImageID());
        assertSame(clonedEngine.getOrientation(), engine1.getOrientation());
        assertSame(clonedEngine.orientation, engine1.orientation);
    }

}
