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
    }
    @Test
    void hasSingleCannonTest(){
        assertFalse( cannon.hasSingleCannon());
    }
    @Test
    void pointsForwardTest(){
        assertTrue(cannon.pointsForward());
        cannon.orientation = Orientation.EAST;
        assertFalse(cannon.pointsForward());
    }
    @Test
    void testGetSides() {
        ArrayList<Connector> sides = new ArrayList<>();
        sides.add(Connector.SINGLE);
        cannon = new Cannon(true, 0, sides);
        assertEquals(sides, cannon.getSides());
    }
    @Test
    void testClone() {
        Cannon clonedCannon = (Cannon) cannon.clone(); // Esegui il cast perché Component.clone() restituisce Component
        assertNotSame(cannon, clonedCannon);
        assertEquals(cannon.isDouble(), clonedCannon.isDouble());
        assertEquals(cannon.getImageID(), clonedCannon.getImageID()); // Assumendo un getter per imageID
        assertEquals(cannon.getOrientation(), clonedCannon.getOrientation());
        assertEquals(cannon.getSides(), clonedCannon.getSides());

    }


}
