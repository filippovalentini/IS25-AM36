package it.polimi.ingsw.galaxytrucker.model.gameClasses;

import it.polimi.ingsw.galaxytrucker.model.componentClasses.*;
import it.polimi.ingsw.galaxytrucker.model.enumerations.Color;
import it.polimi.ingsw.galaxytrucker.model.enumerations.Connector;
import it.polimi.ingsw.galaxytrucker.model.enumerations.Orientation;
import it.polimi.ingsw.galaxytrucker.model.exceptions.AssembledComponentException;
import it.polimi.ingsw.galaxytrucker.model.exceptions.PickedComponentException;
import it.polimi.ingsw.galaxytrucker.model.exceptions.ReservedComponentException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ShipBoardTest {
    private ShipBoard shipBoard;
    private Component component1;
    private Component component2;

    @BeforeEach
    void init(){
        shipBoard = new LevelTwoShipBoard("filippo", Color.RED); // shipboard of level two is used
        List<Connector> connectorList1 = new ArrayList<>();
        connectorList1.add(Connector.SINGLE);
        connectorList1.add(Connector.SINGLE);
        connectorList1.add(Connector.SINGLE);
        connectorList1.add(Connector.SMOOTH);
        List<Connector> connectorList2 = new ArrayList<>();
        connectorList1.add(Connector.SINGLE);
        connectorList1.add(Connector.SINGLE);
        connectorList1.add(Connector.SINGLE);
        connectorList1.add(Connector.SMOOTH);
        component1 = new Component(9, connectorList1);
        component2 = new Component(9, connectorList2);
    }

    @Test
    void testShouldConstructShipBoardWithCorrespondingInitCabin(){
        ShipBoard shipBoardBlue = new LevelTwoShipBoard("filippo", Color.BLUE);
        assertEquals(Color.BLUE, shipBoardBlue.getColor());
        Cabin blueCabin = new Cabin(318, new ArrayList<>(Arrays.asList(Connector.UNIVERSAL, Connector.UNIVERSAL, Connector.UNIVERSAL, Connector.UNIVERSAL)));
        assertEquals(shipBoardBlue.assembledComponents.get(2).get(3), blueCabin);

        ShipBoard shipBoardGreen = new LevelTwoShipBoard("filippo", Color.GREEN);
        assertEquals(Color.GREEN, shipBoardGreen.getColor());
        Cabin greenCabin = new Cabin(319, new ArrayList<>(Arrays.asList(Connector.UNIVERSAL, Connector.UNIVERSAL, Connector.UNIVERSAL, Connector.UNIVERSAL)));
        assertEquals(shipBoardGreen.assembledComponents.get(2).get(3), greenCabin);

        ShipBoard shipBoardRed = new LevelTwoShipBoard("filippo", Color.RED);
        assertEquals(Color.RED, shipBoardRed.getColor());
        Cabin redCabin = new Cabin(320, new ArrayList<>(Arrays.asList(Connector.UNIVERSAL, Connector.UNIVERSAL, Connector.UNIVERSAL, Connector.UNIVERSAL)));
        assertEquals(shipBoardRed.assembledComponents.get(2).get(3), redCabin);

        ShipBoard shipBoardYellow = new LevelTwoShipBoard("filippo", Color.YELLOW);
        assertEquals(Color.YELLOW, shipBoardYellow.getColor());
        Cabin yellowCabin = new Cabin(321, new ArrayList<>(Arrays.asList(Connector.UNIVERSAL, Connector.UNIVERSAL, Connector.UNIVERSAL, Connector.UNIVERSAL)));
        assertEquals(shipBoardYellow.assembledComponents.get(2).get(3), yellowCabin);
    }

    @Test
    void testPickAndReleaseComponent() {
        shipBoard.pickComponent(component1); //pick
        assertEquals(component1, shipBoard.getPickedComponent());
        Component releasedComponent = shipBoard.releaseComponent(); //release
        assertEquals(component1, releasedComponent);
        assertNull(shipBoard.getPickedComponent());
    }

    @Test
    void testShouldNotPickComponentIfAlreadyPicked(){
        shipBoard.pickComponent(component1);
        assertEquals(component1, shipBoard.getPickedComponent());
        assertThrows(PickedComponentException.class, () -> {shipBoard.pickComponent(component2);});
    }

    @Test
    void testShouldNotReleaseComponentIfNotPicked(){
        assertThrows(PickedComponentException.class, () -> {shipBoard.releaseComponent();});
    }

    @Test
    void testShouldNotReserveNullComponent(){
        assertThrows(PickedComponentException.class, () -> {shipBoard.reserveComponent();});
    }

    @Test
    void testShouldNotReserveComponentIfReservedAreFull(){
        List<Connector> connectorList3 = new ArrayList<>();
        connectorList3.add(Connector.SINGLE);
        connectorList3.add(Connector.SINGLE);
        connectorList3.add(Connector.SINGLE);
        connectorList3.add(Connector.SMOOTH);
        Component component3 = new Component(9, connectorList3);
        shipBoard.pickComponent(component1);
        shipBoard.reserveComponent();
        shipBoard.pickComponent(component2);
        shipBoard.reserveComponent();
        shipBoard.pickComponent(component3);
        assertThrows(ReservedComponentException.class, () -> shipBoard.reserveComponent());
    }

    @Test
    void testPickReservedComponent() {
        shipBoard.pickComponent(component1);
        shipBoard.reserveComponent();
        shipBoard.pickReservedComponent(0);
        assertEquals(component1, shipBoard.getPickedComponent());
    }

    @Test
    void testShouldNotPickReservedComponentIfInvalidPosition(){
        shipBoard.pickComponent(component1);
        shipBoard.reserveComponent();
        assertThrows(ReservedComponentException.class, () -> {shipBoard.pickReservedComponent(1);});
    }

    @Test
    void testShouldNotPickReservedComponentIfAlreadyPickedOne(){
        shipBoard.pickComponent(component1);
        shipBoard.reserveComponent();
        shipBoard.pickComponent(component2);
        assertThrows(PickedComponentException.class, () -> {shipBoard.pickReservedComponent(0);});
    }

    @Test
    void testAssembleComponent() {
        shipBoard.pickComponent(component1);
        shipBoard.assembleComponent(1, 3); //above the initial cabin
        assertEquals(component1, shipBoard.getAssembledComponent(1, 3));
    }

    @Test
    void testShouldNotAssembleOutside(){
        shipBoard.pickComponent(component1);
        assertThrows(AssembledComponentException.class, () -> {shipBoard.assembleComponent(0,0);});
    }

    @Test
    void testShouldNotAssembleOccupied(){
        shipBoard.pickComponent(component1);
        shipBoard.assembleComponent(1,3);
        shipBoard.pickComponent(component2);
        assertThrows(AssembledComponentException.class, () -> {shipBoard.assembleComponent(1,3);});
    }

    @Test
    void testShouldNotAssembleIfNotPickedComponent(){
        assertThrows(PickedComponentException.class, () -> {shipBoard.assembleComponent(1,3);});
    }

    @Test
    void testAssembleComponentGivenInParam(){
        Engine engineNorthUniversal = new Engine(false, 709, new ArrayList<>(Arrays.asList(Connector.UNIVERSAL, Connector.DOUBLE, Connector.SMOOTH, Connector.SMOOTH)));
        assertEquals(709, engineNorthUniversal.getImageID());
        shipBoard.assembleComponent(engineNorthUniversal,3,3);
        assertEquals(engineNorthUniversal, shipBoard.assembledComponents.get(3).get(3));
        assertTrue(shipBoard.isCorrect());
    }

    @Test
    void testDestroyComponent() {
        shipBoard.pickComponent(component1);
        shipBoard.assembleComponent(1, 3); //above the initial cabin
        assertFalse(shipBoard.isEmptyComponent(1,3));
        shipBoard.destroyComponent(1, 3);
        assertEquals(1, shipBoard.getLostComponents());
        assertTrue(shipBoard.isEmptyComponent(1, 3));
    }

    @Test
    void testUpdateCorrectnessFloatingComponents(){
        assertTrue(shipBoard.isCorrect());
        List<Connector> connectorList1 = new ArrayList<>();
        connectorList1.add(Connector.SINGLE);
        connectorList1.add(Connector.SINGLE);
        connectorList1.add(Connector.SINGLE);
        connectorList1.add(Connector.SMOOTH);
        component1 = new Component(9, connectorList1);
        Component cabin = new Cabin(0, connectorList1);
        shipBoard.pickComponent(component1);
        shipBoard.assembleComponent(1,1);
        assertFalse(shipBoard.isCorrect());
    }

    @Test
    void testShouldNotRotateComponentIfNotPicked(){
        assertThrows(PickedComponentException.class, () -> {shipBoard.rotatePickedComponent();});
    }

    @Test
    void testDestroyNorth(){
        assertEquals(320, shipBoard.getAssembledComponent(2,3).getImageID());
        shipBoard.destroyNorth(3);
        assertEquals(0, shipBoard.getAssembledComponent(2,3).getImageID());
    }

    @Test
    void testShouldNotDestroyComponentIfEmpty(){
        assertThrows(AssembledComponentException.class, () -> {shipBoard.destroyComponent(1,3);});
    }

    @Test
    void testLoseReservedComponents(){
        shipBoard.pickComponent(component1);
        shipBoard.reserveComponent();
        assertEquals(0, shipBoard.getLostComponents());
        shipBoard.loseReservedComponents();
        assertEquals(1, shipBoard.getLostComponents());
    }

    @Test
    void testSmoothSide() {
        Component shield = new Shield(44, new ArrayList<>(Arrays.asList(Connector.SMOOTH, Connector.SMOOTH, Connector.UNIVERSAL, Connector.UNIVERSAL)));
        shipBoard.pickComponent(shield);
        shipBoard.rotatePickedComponent();
        shipBoard.rotatePickedComponent();
        shipBoard.assembleComponent(1, 3);
        Component battery = new Battery(true, 45, new ArrayList<>(Arrays.asList(Connector.UNIVERSAL, Connector.UNIVERSAL, Connector.UNIVERSAL, Connector.UNIVERSAL)));
        shipBoard.pickComponent(battery);
        shipBoard.assembleComponent(2,2);
        boolean smooth = shipBoard.smoothSide(Orientation.WEST, 1);
        assertTrue(smooth);
    }

    @Test
    void testCountExposedConnectors(){
        Component struct = new Structural(101, new ArrayList<>(Arrays.asList(Connector.UNIVERSAL, Connector.UNIVERSAL, Connector.UNIVERSAL, Connector.UNIVERSAL)));
        Component engine = new Engine(false, 101, new ArrayList<>(Arrays.asList(Connector.UNIVERSAL, Connector.SMOOTH, Connector.SMOOTH, Connector.SMOOTH)));
        shipBoard.assembleComponent(struct, 1,3);
        shipBoard.assembleComponent(struct, 1,2);
        shipBoard.assembleComponent(struct, 1,4);
        shipBoard.assembleComponent(struct, 1,1);
        shipBoard.assembleComponent(struct, 1,5);
        shipBoard.assembleComponent(engine, 2,1);
        shipBoard.assembleComponent(engine, 2,5);
        assertEquals(12, shipBoard.countExposedConnectors()); // exposed connectors of initial cabin
    }

    @Test
    void testEpidemicEffect(){
        List<Connector> universalConnectorList = new ArrayList<>();
        universalConnectorList.add(Connector.UNIVERSAL);
        universalConnectorList.add(Connector.UNIVERSAL);
        universalConnectorList.add(Connector.UNIVERSAL);
        universalConnectorList.add(Connector.UNIVERSAL);
        Component cabin = new Cabin(0, universalConnectorList);
        shipBoard.pickComponent(cabin);
        shipBoard.assembleComponent(1, 3); // assemble cabin near the initial cabin
        shipBoard.addCrew(2,3);
        shipBoard.addCrew(1,3);
        assertEquals(4, shipBoard.getNumberCrew());
        shipBoard.epidemicEffect();
        assertEquals(2, shipBoard.getNumberCrew());
    }

    @Test
    void testProtectedShipBoardWithoutShields(){
        Orientation orientationN = Orientation.NORTH;
        assertFalse(shipBoard.protectedShipBoard(orientationN));
    }

    @Test
    void testProtectedShipBoardWithShields(){
        Orientation orientationN = Orientation.NORTH;
        Shield shieldN = new Shield(901, new ArrayList<>(Arrays.asList(Connector.SMOOTH, Connector.SINGLE, Connector.UNIVERSAL, Connector.SINGLE)));
        shipBoard.pickComponent(shieldN);
        shipBoard.assembleComponent(1,3); //above the init cabin
        assertTrue(shipBoard.protectedShipBoard(orientationN));
    }

    @Test
    void testArmedShipBoard(){
        Orientation orientationN = Orientation.NORTH;
        assertFalse(shipBoard.armedShipBoard(false, orientationN, 0));

        Cannon cannonDouble = new Cannon(true, 428,  new ArrayList<>(Arrays.asList(Connector.SMOOTH, Connector.SINGLE, Connector.UNIVERSAL, Connector.SMOOTH)));
        shipBoard.pickComponent(cannonDouble);
        shipBoard.assembleComponent(2,2); //left to the init cabin
        assertTrue(shipBoard.armedShipBoard(true, orientationN, 2));

        Cannon cannonSingle = new Cannon(false, 401,  new ArrayList<>(Arrays.asList(Connector.SMOOTH, Connector.SMOOTH, Connector.SINGLE, Connector.SMOOTH)));
        shipBoard.pickComponent(cannonSingle);
        shipBoard.assembleComponent(1,3); //above the init cabin
        assertTrue(shipBoard.armedShipBoard(false, orientationN, 3));
    }
}