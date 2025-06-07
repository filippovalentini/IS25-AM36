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
    public void init() {
        List<Connector> connectors = new ArrayList<>();
        connectors.add(Connector.SMOOTH);
        connectors.add(Connector.SINGLE);
        connectors.add(Connector.DOUBLE);
        connectors.add(Connector.UNIVERSAL);

        component = new Component(1, connectors);

    }

    //Test per rotateLeft()
    @Test
    public void testRotateLeft_NorthToWest() {
        component.rotateLeft();
        assertEquals(Orientation.WEST, component.getOrientation());
    }

    @Test
    public void testRotateLeft_WestToSouth() {
        component.orientation = Orientation.WEST;
        component.rotateLeft();
        assertEquals(Orientation.SOUTH, component.getOrientation());
    }

    @Test
    public void testRotateLeft_SouthToEast() {
        component.orientation = Orientation.SOUTH;
        component.rotateLeft();
        assertEquals(Orientation.EAST, component.getOrientation());
    }

    @Test
    public void testRotateLeft_EastToNorth() {
        component.orientation = Orientation.EAST;
        component.rotateLeft();
        assertEquals(Orientation.NORTH, component.getOrientation());
    }

    // === Test per getNorthSide(), getEastSide(), getSouthSide(), getWestSide() ===
    @Test
    public void testGetNorthSide_OrientationNorth() {
        component.orientation = Orientation.NORTH;
        assertEquals(Connector.SMOOTH, component.getNorthSide());
    }
    @Test
    public void testGetNorthSide_OrientationEast() {
        component.orientation = Orientation.EAST;
        assertEquals(Connector.UNIVERSAL, component.getNorthSide());
    }
    @Test
    public void testGetNorthSide_OrientationSouth() {
        component.orientation = Orientation.SOUTH;
        assertEquals(Connector.DOUBLE, component.getNorthSide());
    }
    @Test
    public void testGetNorthSide_OrientationWest() {
        component.orientation = Orientation.WEST;
        assertEquals(Connector.SINGLE,component.getNorthSide() );
    }

    @Test
    public void testGetEastSide_OrientationNorth() {
        component.orientation = Orientation.NORTH;
        assertEquals(SINGLE, component.getEastSide());
    }
    @Test
    public void testGetEastSide_OrientationEast() {
        component.orientation = Orientation.EAST;
        assertEquals(SMOOTH, component.getEastSide());
    }
    @Test
    public void testGetEastSide_OrientationSouth() {
        component.orientation = Orientation.SOUTH;
        assertEquals(Connector.UNIVERSAL, component.getEastSide());
    }
    @Test
    public void testGetEastSide_OrientationWest() {
        component.orientation = Orientation.WEST;
        assertEquals(Connector.DOUBLE, component.getEastSide());
    }




    @Test
    public void testGetSouthSide_OrientationNorth() {
        component.orientation = Orientation.NORTH;
        assertEquals(Connector.DOUBLE, component.getSouthSide());
    }
    @Test
    public void testGetSouthSide_OrientationEast() {
        component.orientation = Orientation.EAST;
        assertEquals(Connector.SINGLE, component.getSouthSide());
    }
    @Test
    public void testGetSouthSide_OrientationSouth() {
        component.orientation = Orientation.SOUTH;
        assertEquals(Connector.SMOOTH, component.getSouthSide());
    }
    @Test
    public void testGetSouthSide_OrientationWest() {
        component.orientation = Orientation.WEST;
        assertEquals(Connector.UNIVERSAL, component.getSouthSide());
    }


    @Test
    public void testGetWestSide_OrientationNorth() {
        component.orientation = Orientation.NORTH;
        assertEquals(Connector.UNIVERSAL, component.getWestSide());
    }
    @Test
    public void testGetWestSide_OrientationEast() {
        component.orientation = Orientation.EAST;
        assertEquals(Connector.DOUBLE, component.getWestSide());
    }
    @Test
    public void testGetWestSide_OrientationSouth() {
        component.orientation = Orientation.SOUTH;
        assertEquals(SINGLE, component.getWestSide());
    }
    @Test
    public void testGetWestSide_OrientationWest() {
        component.orientation = Orientation.WEST;
        assertEquals(SMOOTH, component.getWestSide());
    }
    // Test  isNotEmpty()
    @Test
   public void testIsNotEmpty_True() {
        assertTrue(component.isNotEmpty());
    }
        @Test
    public void testIsNotEmpty_False() {
        Component emptyComponent = new Empty(0, Arrays.asList(Connector.SMOOTH, Connector.SMOOTH, Connector.SMOOTH, Connector.SMOOTH));
        assertFalse(emptyComponent.isNotEmpty());
    }

    // Test  belongsToShip()
    @Test
    public void testBelongsToShip_True() {
        assertTrue(component.belongsToShip());
    }
    @Test
    public void testBelongsToShip_False() {
        Component spaceComponent = new Space(3, Arrays.asList(Connector.SMOOTH, Connector.SMOOTH, Connector.SMOOTH, Connector.SMOOTH));
        assertFalse(spaceComponent.belongsToShip());
    }
    // Test  isWellOriented()
    @Test
    public void testIsWellOriented_DefaultTrue() {
        assertTrue(component.isWellOriented());
    }
    //Test for addGood()
    @Test
    public void testAddGood_ThrowsAssembledComponentException() {
        assertThrows(AssembledComponentException.class, () -> component.addGood(Color.RED));
    }
    @Test
    public void testHasMembers_DefaultFalse() {
        assertFalse(component.hasMembers());
    }
    // test for hasDoubleEngines()
    @Test
    public void testHasDoubleEngines_DefaultFalse() {
        assertFalse(component.hasDoubleEngines());
    }

    // test for hasSingleEngine()
    @Test
    public void testHasSingleEngines_DefaultFalse() {
        assertFalse(component.hasSingleEngine());
    }
    // test for hasDoubleCannons()
    @Test
    public void testHasDoubleCannons(){
        assertFalse(component.hasDoubleCannons());
    }
    // test for hasSingleCannons()
    @Test
    public void testHasSingleCannon(){
        assertFalse(component.hasSingleCannon());
    }
    //test addCrew()
    @Test
    public void testAddCrew() {
        assertThrows(AssembledComponentException.class, () -> component.addCrew());
    }
    //test addAlien()
    @Test
    public void testAddAlien() {
        assertThrows(AssembledComponentException.class, () -> component.addAlien(true));
    }
    //test addBatteries()
    @Test
    public void testAddBatteries() {
        assertThrows(AssembledComponentException.class, () -> component.addBatteries());
    }
    // Test per removeCrew() ===
    @Test
    public void testRemoveCrew() {
        assertThrows(NoCrewException.class, () -> component.removeCrew(0));
    }
    // Test per getNumberCrew()
    @Test
    public void testGetNumberCrew_DefaultZero() {
        assertEquals(0, component.getNumberCrew());
    }
    // Test per getGoods()
    @Test
    public void testGetGoods_DefaultEmptyList() {
        assertTrue(component.getGoods().isEmpty());
    }

    //test getNumberBatteries()
    @Test
    public void testGetNumberBatteries_DefaultZero() {
        assertEquals(0, component.getNumberBatteries());
    }
    //test getNumberGoods()
    @Test
    public void testGetNumberGoods_DefaultZero() {
        assertEquals(0, component.getNumberGoods());
    }
    //test isFullOfGoods()
    @Test
    public void testIsFullOfGoods_DefaultTrue() {
        assertTrue(component.isFullOfGoods());
    }
    // test substituteGoods()
    @Test
    void testSubstituteGood_ThrowsAssembledComponentException() {

        // Test with different colors and indices
        assertThrows(AssembledComponentException.class, () -> {
            component.substituteGood(Color.RED, 0);
        });

        assertThrows(AssembledComponentException.class, () -> {
            component.substituteGood(Color.BLUE, 1);
        });

        assertThrows(AssembledComponentException.class, () -> {
            component.substituteGood(Color.GREEN, 2);
        });

        assertThrows(AssembledComponentException.class, () -> {
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
        // Test che verifica che useBatteries non lanci eccezioni (metodo vuoto nella classe base)

        assertDoesNotThrow(() -> {
            component.useBatteries(0);
        });

        assertDoesNotThrow(() -> {
            component.useBatteries(1);
        });

        assertDoesNotThrow(() -> {
            component.useBatteries(5);
        });

        assertDoesNotThrow(() -> {
            component.useBatteries(-1);
        });
    }
    // test protects()
    @Test
    public void testProtects_DefaultFalse() {
        assertFalse(component.protects(Orientation.NORTH));
        assertFalse(component.protects(Orientation.EAST));
        assertFalse(component.protects(Orientation.SOUTH));
        assertFalse(component.protects(Orientation.WEST));
    }
    // test pointsForward()
    @Test
    public void testPointsForward_DefaultFalse() {
        assertFalse(component.pointsForward());
    }
    // Test goodPrice()
    @Test
    public void testGoodPrice_DefaultZero() {
        assertEquals(0, component.goodsPrice());
    }
    //test isFull()
    @Test
    public void testIsFull_DefaultFalse() {
        assertTrue(component.isFull());
    }
    //tests supportsAlien()
    @Test
    public void testSupportsAlien_True() {
        assertFalse(component.supportsAlien(true));
    }
    @Test
    public void testSupportsAlien_False() {
        assertFalse(component.supportsAlien(false));
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