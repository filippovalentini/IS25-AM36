package it.polimi.ingsw.galaxytrucker.model.componentClasses;

import it.polimi.ingsw.galaxytrucker.model.enumerations.Color;
import it.polimi.ingsw.galaxytrucker.model.enumerations.Connector;
import it.polimi.ingsw.galaxytrucker.model.enumerations.Orientation;
import it.polimi.ingsw.galaxytrucker.model.exceptions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static it.polimi.ingsw.galaxytrucker.model.enumerations.Connector.SINGLE;
import static it.polimi.ingsw.galaxytrucker.model.enumerations.Connector.SMOOTH;
import static org.junit.jupiter.api.Assertions.*;

public class ComponentTest {

    private Component component;



    @BeforeEach
    public void init() { // initialization of the component before each test
        List<Connector> connectors = new ArrayList<>();
        connectors.add(Connector.SMOOTH);
        connectors.add(Connector.SINGLE);
        connectors.add(Connector.DOUBLE);
        connectors.add(Connector.UNIVERSAL);

        component = new Component(1, connectors); // Create a new Component with ID 1 and the specified connectors

    }

    //Test rotateLeft()
    @Test
    public void testRotateLeft_NorthToWest() { // Test rotating from North to West
        component.rotateLeft(); // Rotate left from the initial orientation (North)
        assertEquals(Orientation.WEST, component.getOrientation()); // Assert that the orientation is now West
    }

    @Test
    public void testRotateLeft_WestToSouth() { // Test rotating from West to South
        component.orientation = Orientation.WEST; // Set the initial orientation to West
        component.rotateLeft(); // Rotate left
        assertEquals(Orientation.SOUTH, component.getOrientation()); // Assert that the orientation is now South
    }

    @Test
    public void testRotateLeft_SouthToEast() { // Test rotating from South to East
        component.orientation = Orientation.SOUTH; // Set the initial orientation to South
        component.rotateLeft(); // Rotate left
        assertEquals(Orientation.EAST, component.getOrientation()); // Assert that the orientation is now East
    }

    @Test
    public void testRotateLeft_EastToNorth() { // Test rotating from East to North
        component.orientation = Orientation.EAST; // Set the initial orientation to East
        component.rotateLeft(); // Rotate left
        assertEquals(Orientation.NORTH, component.getOrientation()); // Assert that the orientation is now North
    }

    //  Test  getNorthSide(), getEastSide(), getSouthSide(), getWestSide()
    @Test
    public void testGetNorthSide_OrientationNorth() { // Test for the North side when orientation is North
        component.orientation = Orientation.NORTH; // Set the orientation to North
        assertEquals(Connector.SMOOTH, component.getNorthSide()); // Assert that the North side is Smooth
    }
    @Test
    public void testGetNorthSide_OrientationEast() { // Test for the North side when orientation is East
        component.orientation = Orientation.EAST; // Set the orientation to East
        assertEquals(Connector.UNIVERSAL, component.getNorthSide()); // Assert that the North side is Universal
    }
    @Test
    public void testGetNorthSide_OrientationSouth() { // Test for the North side when orientation is South
        component.orientation = Orientation.SOUTH; // Set the orientation to South
        assertEquals(Connector.DOUBLE, component.getNorthSide()); // Assert that the North side is Double
    }
    @Test
    public void testGetNorthSide_OrientationWest() { // Test for the North side when orientation is West
        component.orientation = Orientation.WEST; // Set the orientation to West
        assertEquals(Connector.SINGLE,component.getNorthSide() ); // Assert that the North side is Single
    }

    @Test
    public void testGetEastSide_OrientationNorth() { // Test for the East side when orientation is North
        component.orientation = Orientation.NORTH; // Set the orientation to North
        assertEquals(SINGLE, component.getEastSide()); // Assert that the East side is Single
    }
    @Test
    public void testGetEastSide_OrientationEast() { // Test for the East side when orientation is East
        component.orientation = Orientation.EAST; // Set the orientation to East
        assertEquals(SMOOTH, component.getEastSide()); // Assert that the East side is Smooth
    }
    @Test
    public void testGetEastSide_OrientationSouth() { // Test for the East side when orientation is South
        component.orientation = Orientation.SOUTH; // Set the orientation to South
        assertEquals(Connector.UNIVERSAL, component.getEastSide()); // Assert that the East side is Universal
    }
    @Test
    public void testGetEastSide_OrientationWest() { // Test for the East side when orientation is West
        component.orientation = Orientation.WEST; // Set the orientation to West
        assertEquals(Connector.DOUBLE, component.getEastSide()); // Assert that the East side is Double
    }




    @Test
    public void testGetSouthSide_OrientationNorth() { // Test for the South side when orientation is North
        component.orientation = Orientation.NORTH; // Set the orientation to North
        assertEquals(Connector.DOUBLE, component.getSouthSide()); // Assert that the South side is Double
    }
    @Test
    public void testGetSouthSide_OrientationEast() { // Test for the South side when orientation is East
        component.orientation = Orientation.EAST; // Set the orientation to East
        assertEquals(Connector.SINGLE, component.getSouthSide()); // Assert that the South side is Single
    }
    @Test
    public void testGetSouthSide_OrientationSouth() { // Test for the South side when orientation is South
        component.orientation = Orientation.SOUTH; // Set the orientation to South
        assertEquals(Connector.SMOOTH, component.getSouthSide()); // Assert that the South side is Smooth
    }
    @Test
    public void testGetSouthSide_OrientationWest() { // Test for the South side when orientation is West
        component.orientation = Orientation.WEST; // Set the orientation to West
        assertEquals(Connector.UNIVERSAL, component.getSouthSide()); // Assert that the South side is Universal
    }


    @Test
    public void testGetWestSide_OrientationNorth() { // Test for the West side when orientation is North
        component.orientation = Orientation.NORTH; // Set the orientation to North
        assertEquals(Connector.UNIVERSAL, component.getWestSide()); // Assert that the West side is Universal
    }
    @Test
    public void testGetWestSide_OrientationEast() { // Test for the West side when orientation is East
        component.orientation = Orientation.EAST; // Set the orientation to East
        assertEquals(Connector.DOUBLE, component.getWestSide()); // Assert that the West side is Double
    }
    @Test
    public void testGetWestSide_OrientationSouth() { // Test for the West side when orientation is South
        component.orientation = Orientation.SOUTH; // Set the orientation to South
        assertEquals(SINGLE, component.getWestSide()); // Assert that the West side is Single
    }
    @Test
    public void testGetWestSide_OrientationWest() { // Test for the West side when orientation is West
        component.orientation = Orientation.WEST; // Set the orientation to West
        assertEquals(SMOOTH, component.getWestSide()); // Assert that the West side is Smooth
    }
    // Test  isNotEmpty()
    @Test
   public void testIsNotEmpty_True() {
        assertTrue(component.isNotEmpty());
    } // Test for a non-empty component
        @Test
    public void testIsNotEmpty_False() { // Test for an empty component
        Component emptyComponent = new Empty(0, Arrays.asList(Connector.SMOOTH, Connector.SMOOTH, Connector.SMOOTH, Connector.SMOOTH));
        assertFalse(emptyComponent.isNotEmpty()); // Assert that the empty component is indeed empty
    }

    // Test  belongsToShip()
    @Test
    public void testBelongsToShip_True() {
        assertTrue(component.belongsToShip());
    } // Test for a component that belongs to a ship
    @Test
    public void testBelongsToShip_False() { // Test for a component that does not belong to a ship
        Component spaceComponent = new Space(3, Arrays.asList(Connector.SMOOTH, Connector.SMOOTH, Connector.SMOOTH, Connector.SMOOTH));
        assertFalse(spaceComponent.belongsToShip()); // Assert that the space component does not belong to a ship
    }
    // Test  isWellOriented()
    @Test
    public void testIsWellOriented_DefaultTrue() {
        assertTrue(component.isWellOriented());
    } // Test for a component that is well oriented by default
    //Test for addGood()
    @Test
    public void testAddGood_ThrowsAssembledComponentException() { // Test for adding a good to a component that cannot hold goods
        assertThrows(AssembledComponentException.class, () -> component.addGood(Color.RED)); // Assert that an AssembledComponentException is thrown when trying to add a good
    }
    @Test
    public void testHasMembers_DefaultFalse() {
        assertFalse(component.hasMembers());
    } // Test for hasMembers() method, which should return false by default

    // test for hasDoubleEngines()
    @Test
    public void testHasDoubleEngines_DefaultFalse() {
        assertFalse(component.hasDoubleEngines()); // Test for hasDoubleEngines() method, which should return false by default
    }

    // test for hasSingleEngine()
    @Test
    public void testHasSingleEngines_DefaultFalse() { // Test for hasSingleEngines() method, which should return false by default
        assertFalse(component.hasSingleEngine());
    }
    // test for hasDoubleCannons()
    @Test
    public void testHasDoubleCannons(){ // Test for hasDoubleCannons() method, which should return false by default
        assertFalse(component.hasDoubleCannons());
    }
    // test for hasSingleCannons()
    @Test
    public void testHasSingleCannon(){ // Test for hasSingleCannon() method, which should return false by default
        assertFalse(component.hasSingleCannon());
    }
    //test addCrew()
    @Test
    public void testAddCrew() { // Test for addCrew() method, which should throw an AssembledComponentException
        assertThrows(AssembledComponentException.class, () -> component.addCrew());
    }
    //test addAlien()
    @Test
    public void testAddAlien() {
        assertThrows(AssembledComponentException.class, () -> component.addAlien(true)); // Test for addAlien() method, which should throw an AssembledComponentException
    }
    //test addBatteries()
    @Test
    public void testAddBatteries() { // Test for addBatteries() method, which should throw an AssembledComponentException
        assertThrows(AssembledComponentException.class, () -> component.addBatteries());
    }
    // Test per removeCrew() ===
    @Test
    public void testRemoveCrew() { // Test for removeCrew() method, which should throw a NoCrewException
        assertThrows(NoCrewException.class, () -> component.removeCrew(0));
    }
    // Test per getNumberCrew()
    @Test
    public void testGetNumberCrew_DefaultZero() { // Test for getNumberCrew() method, which should return 0 by default
        assertEquals(0, component.getNumberCrew());
    }
    // Test per getGoods()
    @Test
    public void testGetGoods_DefaultEmptyList() { // Test for getGoods() method, which should return an empty list by default
        assertTrue(component.getGoods().isEmpty());
    }

    //test getNumberBatteries()
    @Test
    public void testGetNumberBatteries_DefaultZero() { // Test for getNumberBatteries() method, which should return 0 by default
        assertEquals(0, component.getNumberBatteries());
    }
    //test getNumberGoods()
    @Test
    public void testGetNumberGoods_DefaultZero() { // Test for getNumberGoods() method, which should return 0 by default
        assertEquals(0, component.getNumberGoods());
    }
    //test isFullOfGoods()
    @Test
    public void testIsFullOfGoods_DefaultTrue() { // Test for isFullOfGoods() method, which should return true by default
        assertTrue(component.isFullOfGoods());
    }
    // test substituteGoods()
    @Test
    void testSubstituteGood_ThrowsAssembledComponentException() { // Test for substituteGood() method, which should throw an AssembledComponentException

        // Test with different colors and indices
        assertThrows(AssembledComponentException.class, () -> { // Attempt to substitute a good with a color and index
            component.substituteGood(Color.RED, 0);
        });

        assertThrows(AssembledComponentException.class, () -> { // Attempt to substitute a good with a different color and index
            component.substituteGood(Color.BLUE, 1);
        });

        assertThrows(AssembledComponentException.class, () -> { // Attempt to substitute a good with a color and index that is out of bounds
            component.substituteGood(Color.GREEN, 2);
        });

        assertThrows(AssembledComponentException.class, () -> { // Attempt to substitute a good with a color and index that is negative
            component.substituteGood(Color.YELLOW, -1);
        });

        // verify that the exception message is as expected
        AssembledComponentException exception = assertThrows(AssembledComponentException.class, () -> {
            component.substituteGood(Color.RED    , 0);
        });
        assertEquals("Can't substitute good outside a cargo hold", exception.getMessage());
    }
    //test useBatteries()
    @Test
    void testUseBatteries_DoesNotThrowException() {
        // Test that verifies that using batteries does not throw an exception

        assertDoesNotThrow(() -> { // Attempt to use 0 batteries
            component.useBatteries(0);
        });

        assertDoesNotThrow(() -> { // Attempt to use 1 battery
            component.useBatteries(1);
        });

        assertDoesNotThrow(() -> { // Attempt to use 5 batteries
            component.useBatteries(5);
        });

        assertDoesNotThrow(() -> {
            component.useBatteries(-1);
        });
    }
    // test protects()
    @Test
    public void testProtects_DefaultFalse() { // Test for protects() method, which should return false by default
        assertFalse(component.protects(Orientation.NORTH));
        assertFalse(component.protects(Orientation.EAST));
        assertFalse(component.protects(Orientation.SOUTH));
        assertFalse(component.protects(Orientation.WEST));
    }
    // test pointsForward()
    @Test
    public void testPointsForward_DefaultFalse() {
        assertFalse(component.pointsForward());
    } // Test for pointsForward() method, which should return false by default
    // Test goodPrice()
    @Test
    public void testGoodPrice_DefaultZero() {
        assertEquals(0, component.goodsPrice()); // Test for goodsPrice() method, which should return 0 by default
    }
    //test isFull()
    @Test
    public void testIsFull_DefaultFalse() {
        assertTrue(component.isFull()); // Test for isFull() method, which should return false by default
    }
    //tests supportsAlien()
    @Test
    public void testSupportsAlien_True() {
        assertFalse(component.supportsAlien(true)); // Test for supportsAlien() method with a purple alien, which should return false by default
    }
    @Test
    public void testSupportsAlien_False() {
        assertFalse(component.supportsAlien(false)); // Test for supportsAlien() method with a brown alien, which should return false by default
    }
    


















































    //    // === Test per equals() ===
    @Test
    public void testEquals_SameObject() {
        assertTrue(component.equals(component));
    }

    @Test
    public void testEquals_NullObject() {
        assertFalse(component.equals(null));
    }

//    @Test
//    public void testEquals_DifferentImageID() {
//        Component otherComponent = new Component(2, connectors);
//        assertFalse(component.equals(otherComponent));
//    }

    @Test
    public void testEquals_SameImageID() {
        Component otherComponent = new Component(1, new ArrayList<>());
        assertTrue(component.equals(otherComponent));
    }

    @Test
    public void testRemoveCrew_ThrowsNoCrewException() {
        assertThrows(NoCrewException.class, () -> component.removeCrew(0));
    }

    // === Test per metodi che restituiscono valori default ===








    // === Test per clone() ===
    @Test
    public void testClone_CreatesNewInstance() {
        Component clonedComponent = component.clone();
        assertNotSame(component, clonedComponent);
    }

    @Test
    public void testClone_CopiesFieldsCorrectly() {
        Component clonedComponent = component.clone();
        assertEquals(component.getImageID(), clonedComponent.getImageID());
        assertEquals(component.getOrientation(), clonedComponent.getOrientation());
        assertEquals(component.getNorthSide(), clonedComponent.getNorthSide());
    }
}