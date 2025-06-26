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
    void init(){ // Initialize a Cabin with 4 sides
        List<Connector> sides = new ArrayList<Connector>();
        sides.add(Connector.SMOOTH);
        sides.add(Connector.SMOOTH);
        sides.add(Connector.DOUBLE);
        sides.add(Connector.UNIVERSAL);
        cabin = new Cabin(24748, sides);
    }

    @Test
    void testAddCrewEnoughSpace(){ // Test adding crew when there is space
        cabin.addCrew(); // Add one crew member
        assertEquals(2 , cabin.getNumberCrew()); // Check if the number of crew members is 2 (1 initial + 1 added)
    }

    @Test
    void testAddAlienWithSpace() { // Test adding alien when there is space
        cabin.addAlien(true); // Add an alien
        assertTrue(cabin.hasAlien(true)); // Check if the cabin has an alien
        assertFalse(cabin.hasAlien(false)); // Check if the cabin does not have a space alien
    }

    @Test
    void testAddAlienWithoutSpaceCrew() { // Test adding alien when there is no space for crew
        cabin.addCrew(); // Add one crew member
        assertThrows(FullCabinException.class, () -> cabin.addAlien(false)); // Attempt to add a space alien, which should throw an exception
    }

    @Test
    void testAddAlienWithoutSpaceAlien(){ // Test adding alien when there is no space for aliens
        cabin.addAlien(true); // Add a purple alien
        assertThrows(FullCabinException.class, () -> cabin.addAlien(false)); // Attempt to add a brown alien, which should throw an exception
    }

    @Test
    void testRemoveCrewNoCrew() {
        assertThrows(NoCrewException.class, () -> cabin.removeCrew(1));
    } // Test removing crew when there is no crew

    @Test
    void testRemoveCrewWithCrew() { // Test removing crew when there is crew
        cabin.addCrew(); // Add one crew member
        cabin.removeCrew(1); // Remove one crew member
        assertEquals(1, cabin.getNumberCrew()); // Check if the number of crew members is 1 (initial 1 - 1 removed)
    }

    @Test
    void testRemoveAlien(){ // Test removing alien when there is an alien
        cabin.addAlien(true); // Add a purple alien
        cabin.removeAlien(true); // Remove the purple alien
        assertFalse(cabin.hasAlien(true)); // Check if the cabin does not have a purple alien
        cabin.addAlien(false); // Add a brown alien
        cabin.removeAlien(false); // Remove the brown alien
        assertFalse(cabin.hasAlien(false)); // Check if the cabin does not have a brown alien
    }
}