package it.polimi.ingsw.galaxytrucker.model.gameClasses;

import it.polimi.ingsw.galaxytrucker.model.componentClasses.Battery;
import it.polimi.ingsw.galaxytrucker.model.componentClasses.Cabin;
import it.polimi.ingsw.galaxytrucker.model.componentClasses.Component;
import it.polimi.ingsw.galaxytrucker.model.componentClasses.Shield;
import it.polimi.ingsw.galaxytrucker.model.enumerations.Color;
import it.polimi.ingsw.galaxytrucker.model.enumerations.Connector;
import it.polimi.ingsw.galaxytrucker.model.enumerations.Orientation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ShipBoardTest {
    private ShipBoard shipBoard;
    private Component component1;

    @BeforeEach
    void init(){
        shipBoard = new LevelTwoShipBoard(Color.RED); // shipboard of level two is used
        List<Connector> connectorList1 = new ArrayList<>();
        connectorList1.add(Connector.SINGLE);
        connectorList1.add(Connector.SINGLE);
        connectorList1.add(Connector.SINGLE);
        connectorList1.add(Connector.SMOOTH);
        component1 = new Component(9, connectorList1);
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
    void testPickReservedComponent() {
        shipBoard.pickComponent(component1);
        shipBoard.reserveComponent();
        shipBoard.pickReservedComponent(0);
        assertEquals(component1, shipBoard.getPickedComponent());
    }

    @Test
    void testAssembleComponent() {
        shipBoard.pickComponent(component1);
        shipBoard.assembleComponent(1, 3); //above the initial cabin
        assertEquals(component1, shipBoard.getAssembledComponent(1, 3));
    }

    @Test
    public void testDestroyComponent() {
        shipBoard.pickComponent(component1);
        shipBoard.assembleComponent(1, 3); //above the initial cabin
        shipBoard.destroyComponent(1, 3);
        assertEquals(1, shipBoard.getLostComponents());
        assertTrue(shipBoard.isEmptyComponent(1, 3));
    }

    @Test
    public void testUpdateCorrectnessFloatingComponents(){
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
    public void testDestroyNorth(){
        assertEquals(320, shipBoard.getAssembledComponent(2,3).getImageID());
        shipBoard.destroyNorth(3);
        assertEquals(0, shipBoard.getAssembledComponent(2,3).getImageID());
    }

    @Test
    public void testSmoothSide() {
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
    public void testCountExposedConnectors(){
        assertEquals(4, shipBoard.countExposedConnectors()); // exposed connectors of initial cabin
    }

    @Test
    public void testEpidemicEffect(){
        List<Connector> universalConnectorList = new ArrayList<>();
        universalConnectorList.add(Connector.UNIVERSAL);
        universalConnectorList.add(Connector.UNIVERSAL);
        universalConnectorList.add(Connector.UNIVERSAL);
        universalConnectorList.add(Connector.UNIVERSAL);
        Component cabin = new Cabin(0, universalConnectorList);
        shipBoard.pickComponent(cabin);
        shipBoard.assembleComponent(1, 3); // assemble cabin near the initial cabin
        assertEquals(4, shipBoard.getNumberCrew());
        shipBoard.epidemicEffect();
        assertEquals(2, shipBoard.getNumberCrew());
    }
}