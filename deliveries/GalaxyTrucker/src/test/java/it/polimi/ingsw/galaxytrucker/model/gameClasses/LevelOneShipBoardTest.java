package it.polimi.ingsw.galaxytrucker.model.gameClasses;

import it.polimi.ingsw.galaxytrucker.model.componentClasses.Component;
import it.polimi.ingsw.galaxytrucker.model.enumerations.Color;
import it.polimi.ingsw.galaxytrucker.model.enumerations.Connector;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LevelOneShipBoardTest {
    private LevelOneShipBoard levelOneShipBoard;
    private Component component1;

    @BeforeEach
    void init(){
        levelOneShipBoard = new LevelOneShipBoard(Color.RED);
        List<Connector> connectorList1 = new ArrayList<>();
        connectorList1.add(Connector.SINGLE);
        connectorList1.add(Connector.SINGLE);
        connectorList1.add(Connector.SINGLE);
        connectorList1.add(Connector.SMOOTH);
        component1 = new Component(9, connectorList1);
    }

    @Test
    void testPickAndReleaseComponent() {
        levelOneShipBoard.pickComponent(component1); //pick
        assertEquals(component1, levelOneShipBoard.getPickedComponent());
        Component releasedComponent = levelOneShipBoard.releaseComponent(); //release
        assertEquals(component1, releasedComponent);
        assertNull(levelOneShipBoard.getPickedComponent());
    }

    @Test
    void testPickReservedComponent() {
        levelOneShipBoard.pickComponent(component1);
        levelOneShipBoard.reserveComponent();
        levelOneShipBoard.pickReservedComponent(0);
        assertEquals(component1, levelOneShipBoard.getPickedComponent());
    }

    @Test
    void testAssembleComponent() {
        levelOneShipBoard.pickComponent(component1);
        levelOneShipBoard.assembleComponent(1, 2); //above the initial cabin
        assertEquals(component1, levelOneShipBoard.getAssembledComponent(1, 2));
    }

    @Test
    void testDestroyComponent() {
        levelOneShipBoard.pickComponent(component1);
        levelOneShipBoard.assembleComponent(1, 2); //above the initial cabin
        levelOneShipBoard.destroyComponent(1, 2);
        assertEquals(1, levelOneShipBoard.getLostComponents());
        assertTrue(levelOneShipBoard.isEmptyComponent(1, 2));
    }
}