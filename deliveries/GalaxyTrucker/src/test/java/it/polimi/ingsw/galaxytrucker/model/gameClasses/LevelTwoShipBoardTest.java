package it.polimi.ingsw.galaxytrucker.model.gameClasses;

import it.polimi.ingsw.galaxytrucker.model.componentClasses.Component;
import it.polimi.ingsw.galaxytrucker.model.enumerations.Connector;
import it.polimi.ingsw.galaxytrucker.model.enumerations.Color;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class LevelTwoShipBoardTest {
    private LevelTwoShipBoard levelTwoShipBoard;
    private Component component1;

    @BeforeEach
    void init(){
        levelTwoShipBoard = new LevelTwoShipBoard(Color.RED);
        List<Connector> connectorList1 = new ArrayList<>();
        connectorList1.add(Connector.SINGLE);
        connectorList1.add(Connector.SINGLE);
        connectorList1.add(Connector.SINGLE);
        connectorList1.add(Connector.SMOOTH);
        component1 = new Component(9, connectorList1);
    }

    @Test
    void testPickAndReleaseComponent() {
        levelTwoShipBoard.pickComponent(component1); //pick
        assertEquals(component1, levelTwoShipBoard.getPickedComponent());
        Component releasedComponent = levelTwoShipBoard.releaseComponent(); //release
        assertEquals(component1, releasedComponent);
        assertNull(levelTwoShipBoard.getPickedComponent());
    }

    @Test
    void testPickReservedComponent() {
        levelTwoShipBoard.pickComponent(component1);
        levelTwoShipBoard.reserveComponent();
        levelTwoShipBoard.pickReservedComponent(0);
        assertEquals(component1, levelTwoShipBoard.getPickedComponent());
    }

    @Test
    void testAssembleComponent() {
        levelTwoShipBoard.pickComponent(component1);
        levelTwoShipBoard.assembleComponent(1, 3); //above the initial cabin
        assertEquals(component1, levelTwoShipBoard.getAssembledComponent(1, 3));
    }

    @Test
    public void testDestroyComponent() {
        levelTwoShipBoard.pickComponent(component1);
        levelTwoShipBoard.assembleComponent(1, 3); //above the initial cabin
        levelTwoShipBoard.destroyComponent(1, 3);
        assertEquals(1, levelTwoShipBoard.getLostComponents());
        assertTrue(levelTwoShipBoard.isEmptyComponent(1, 3));
    }
}