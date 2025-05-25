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
    void init() {
        sides = new ArrayList<>();
        sides.add(Connector.SMOOTH);
        sides.add(Connector.DOUBLE);
        sides.add(Connector.UNIVERSAL);
        sides.add(Connector.SMOOTH);
        component = new Component(0, sides);
    }

    @Test
    void testEquals() {
        Component c1 = new Component(0, sides);
        Component c2 = new Component(1, sides);
        assertEquals(component, c1);
        assertNotEquals(component, c2);
    }

    @Test
    void testRotateLeft(){
        component.rotateLeft();
        assertEquals(Orientation.WEST, component.getOrientation());
        component.rotateLeft();
        assertEquals(Orientation.SOUTH, component.getOrientation());
        component.rotateLeft();
        assertEquals(Orientation.EAST, component.getOrientation());
        component.rotateLeft();
        assertEquals(Orientation.NORTH, component.getOrientation());
    }

    @Test
    void testGetNorthSide() {
        assertEquals(Orientation.NORTH, component.getOrientation());
        assertEquals(Connector.SMOOTH, component.getNorthSide());
        component.rotateLeft();
        assertEquals(Connector.DOUBLE, component.getNorthSide());
    }

    @Test
    void testGetEastSide() {
        assertEquals(Connector.DOUBLE, component.getEastSide());
        component.rotateLeft();
        assertEquals(Connector.UNIVERSAL, component.getEastSide());
    }

    @Test
    void testGetSouthSide() {
        assertEquals(Connector.UNIVERSAL, component.getSouthSide());
        component.rotateLeft();
        assertEquals(Connector.SMOOTH, component.getSouthSide());
    }

    @Test
    void testGetWestSide() {
        assertEquals(Connector.SMOOTH, component.getWestSide());
        component.rotateLeft();
        assertEquals(Connector.SMOOTH, component.getWestSide());
    }

    @Test
    void testIsWellOriented() {
        assertTrue(component.isWellOriented());
    }

    @Test
    void testIsNotEmpty(){
        Component c1 = new Battery(false, 1, new ArrayList<>(Arrays.asList(Connector.UNIVERSAL, Connector.UNIVERSAL, Connector.UNIVERSAL, Connector.SMOOTH)));
        assertTrue(c1.isNotEmpty());
        Component c2 = new Battery(false, 0, new ArrayList<>(Arrays.asList(Connector.UNIVERSAL, Connector.UNIVERSAL, Connector.UNIVERSAL, Connector.SMOOTH)));
        assertFalse(c2.isNotEmpty());
    }

}