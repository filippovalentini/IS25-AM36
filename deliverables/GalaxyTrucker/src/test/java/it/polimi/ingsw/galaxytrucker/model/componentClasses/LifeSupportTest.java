package it.polimi.ingsw.galaxytrucker.model.componentClasses;

import it.polimi.ingsw.galaxytrucker.model.enumerations.Connector;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class LifeSupportTest {
    private LifeSupport lifeSupport_purple;
    private LifeSupport lifesupport_not_purple;

    @BeforeEach
    public void setUp() {
        List<Connector> connectors = new ArrayList<>();
        connectors.add(Connector.SMOOTH);
        connectors.add(Connector.SINGLE);
        connectors.add(Connector.DOUBLE);
        connectors.add(Connector.UNIVERSAL);
        lifeSupport_purple = new LifeSupport(true, 1, connectors); // Create a purple life support
        lifesupport_not_purple= new LifeSupport(false, 1, connectors); // Create a non-purple life support
    }
    //test for isPurple method
    @Test
    public void isPurple_true() {
        assertTrue(lifeSupport_purple.isPurple());
    }
    @Test
    public void isPurple_false() {
        assertFalse(lifesupport_not_purple.isPurple());
    }

    //test for clone method
    @Test
    public void cloneTest_purple(){
        LifeSupport ClonedLifeSupport_purple = (LifeSupport) lifeSupport_purple.clone();
        assertEquals(ClonedLifeSupport_purple.getImageID(), lifeSupport_purple.getImageID());
        assertSame(ClonedLifeSupport_purple.getOrientation(), lifeSupport_purple.getOrientation());
        assertEquals(ClonedLifeSupport_purple.isPurple(), lifeSupport_purple.isPurple());
    }
    @Test
    public void cloneTest_not_purple(){
        LifeSupport ClonedLifeSupport_not_purple = (LifeSupport) lifesupport_not_purple.clone();
        assertEquals(ClonedLifeSupport_not_purple.getImageID(), lifesupport_not_purple.getImageID());
        assertSame(ClonedLifeSupport_not_purple.getOrientation(), lifesupport_not_purple.getOrientation());
        assertEquals(ClonedLifeSupport_not_purple.isPurple(), lifesupport_not_purple.isPurple());
    }
    //test for supportsAlien method
    @Test
    public void supportsAlien_true() {

        assertTrue(lifeSupport_purple.supportsAlien(true)); // Purple life support supports purple aliens
        assertFalse(lifeSupport_purple.supportsAlien(false));
    }
    @Test
    public void supportsAlien_false() {
         // Purple life support does not support brown aliens
        assertFalse(lifesupport_not_purple.supportsAlien(true)); // Non-purple life support does not support purple aliens
        assertTrue(lifesupport_not_purple.supportsAlien(false)); // Non-purple life support supports brown aliens
    }
}
