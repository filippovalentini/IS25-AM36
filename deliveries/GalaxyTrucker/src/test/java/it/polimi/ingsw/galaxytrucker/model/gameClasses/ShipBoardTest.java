package it.polimi.ingsw.galaxytrucker.model.gameClasses;

import it.polimi.ingsw.galaxytrucker.model.componentClasses.Component;
import it.polimi.ingsw.galaxytrucker.model.enumerations.Connector;
import it.polimi.ingsw.galaxytrucker.model.enumerations.Color;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ShipBoardTest {

    private ShipBoard shipBoard;
    private Component component1;
    private Component component2;

    @BeforeEach
    void init() {
        shipBoard = new ShipBoard(Color.BLUE);
        List<Connector> connectorList1 = new ArrayList<>();
        connectorList1.add(Connector.SINGLE);
        connectorList1.add(Connector.SINGLE);
        connectorList1.add(Connector.SINGLE);
        connectorList1.add(Connector.SMOOTH);
        component1 = new Component("SamplePath", connectorList1);
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
        shipBoard.getReservedComponents().add(component1);
        shipBoard.pickReservedComponent(0);

        assertEquals(component1, shipBoard.getPickedComponent());
    }

    @Test
    void testAssembleComponent() {
        shipBoard.pickComponent(component1);
        shipBoard.assembleComponent(6, 7); //above the initial cabin

        assertEquals(component1, shipBoard.getAssembledComponent(6, 7));
    }

    @Test
    public void testDestroyComponent() {
        shipBoard.pickComponent(component1);
        shipBoard.assembleComponent(6, 7); //above the initial cabin
        shipBoard.destroyComponent(6, 7);

        assertEquals(1, shipBoard.getLostComponents());
        assertEquals(null, shipBoard.getAssembledComponent(6,7));
    }
}