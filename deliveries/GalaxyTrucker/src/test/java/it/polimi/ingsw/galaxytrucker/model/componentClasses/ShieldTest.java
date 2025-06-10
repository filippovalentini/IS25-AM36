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
    public void setUp() {
        List<Connector> connectors = new ArrayList<>();
        connectors.add(Connector.SMOOTH);
        connectors.add(Connector.SINGLE);
        connectors.add(Connector.DOUBLE);
        connectors.add(Connector.UNIVERSAL);
        shield= new Shield(1, connectors);

    }
    // Test for protects method
    @Test
    public void protects_North() {
        shield.orientation = Orientation.NORTH;
        assertTrue(shield.protects(Orientation.NORTH));
        assertTrue(shield.protects(Orientation.EAST));
    }
    @Test
    public void protects_East() {
        shield.orientation = Orientation.EAST;
        assertTrue(shield.protects(Orientation.EAST));
        assertTrue(shield.protects(Orientation.SOUTH));
    }
    @Test
    public void protects_South() {
        shield.orientation = Orientation.SOUTH;
        assertTrue(shield.protects(Orientation.SOUTH));
        assertTrue(shield.protects(Orientation.WEST));
    }
    @Test
    public void protects_West() {
        shield.orientation = Orientation.WEST;
        assertTrue(shield.protects(Orientation.WEST));
        assertTrue(shield.protects(Orientation.NORTH));
    }
    // Test for clone method
    @Test
    public void cloneTest() {
        Shield clonedShield = (Shield) shield.clone();
        assertSame(shield.getImageID(), clonedShield.getImageID());
        assertSame(shield.getOrientation(), clonedShield.getOrientation());
    }
}
