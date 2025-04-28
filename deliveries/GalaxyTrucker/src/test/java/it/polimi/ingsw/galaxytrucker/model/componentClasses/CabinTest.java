package it.polimi.ingsw.galaxytrucker.model.componentClasses;

import it.polimi.ingsw.galaxytrucker.model.enumerations.Connector;
import it.polimi.ingsw.galaxytrucker.model.exceptions.FullCabinException;
import it.polimi.ingsw.galaxytrucker.model.exceptions.NoCrewException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CabinTest {
    private Cabin cabin;

    @BeforeEach
    void init(){
        List<Connector> sides = new ArrayList<Connector>();
        sides.add(Connector.SMOOTH);
        sides.add(Connector.SMOOTH);
        sides.add(Connector.DOUBLE);
        sides.add(Connector.UNIVERSAL);
        cabin = new Cabin(24748, sides);
    }

    @Test
    void testAddCrewEnoughSpace(){
        cabin.addCrew();
        assertEquals(2 , cabin.getNumberCrew());
    }

    @Test
    void testAddAlienWithSpace() {
        cabin.addAlien(true);
        assertTrue(cabin.hasPurpleAlien());
        assertFalse(cabin.hasBrownAlien());
    }

    @Test
    void testAddAlienWithoutSpaceCrew() {
        cabin.addCrew();
        assertThrows(FullCabinException.class, () -> cabin.addAlien(false));
    }

    @Test
    void testAddAlienWithoutSpaceAlien(){
        cabin.addAlien(true);
        assertThrows(FullCabinException.class, () -> cabin.addAlien(false));
    }

    @Test
    void testRemoveCrewNoCrew() {
        assertThrows(NoCrewException.class, () -> cabin.removeCrew(1));
    }

    @Test
    void testRemoveCrewWithCrew() {
        cabin.addCrew();
        cabin.removeCrew(1);
        assertEquals(1, cabin.getNumberCrew());
    }

    @Test
    void testRemoveAlien(){
        cabin.addAlien(true);
        cabin.removeAlien(true);
        assertFalse(cabin.hasPurpleAlien());
        cabin.addAlien(false);
        cabin.removeAlien(false);
        assertFalse(cabin.hasBrownAlien());
    }
}