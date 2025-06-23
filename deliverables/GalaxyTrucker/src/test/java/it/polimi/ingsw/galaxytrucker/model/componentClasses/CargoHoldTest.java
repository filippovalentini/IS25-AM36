package it.polimi.ingsw.galaxytrucker.model.componentClasses;

import it.polimi.ingsw.galaxytrucker.model.enumerations.Color;
import it.polimi.ingsw.galaxytrucker.model.enumerations.Connector;
import it.polimi.ingsw.galaxytrucker.model.exceptions.FullCargoHoldException;
import it.polimi.ingsw.galaxytrucker.model.exceptions.UnsupportedCargoColorException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CargoHoldTest {
    private CargoHold tripleCargoHold;
    private CargoHold doubleCargoHold;

    @BeforeEach
    void init() {
        List<Connector> sides = new ArrayList<>();
        sides.add(Connector.SMOOTH);
        sides.add(Connector.SMOOTH);
        sides.add(Connector.DOUBLE);
        sides.add(Connector.UNIVERSAL);
        tripleCargoHold = new CargoHold(false, 0, sides);
        doubleCargoHold = new CargoHold(true, 1, sides);
    }

    @Test
    void testAddGoodToTripleCargoHold() {
        tripleCargoHold.addGood(Color.BLUE);
        assertEquals(1, tripleCargoHold.getNumberGoods());
        assertTrue(tripleCargoHold.getGoods().contains(Color.BLUE));
    }

    @Test
    void testAddGoodToDoubleCargoHold() {
        doubleCargoHold.addGood(Color.GREEN);
        assertEquals(1, doubleCargoHold.getNumberGoods());
        assertTrue(doubleCargoHold.getGoods().contains(Color.GREEN));
    }

    @Test
    void testShouldNotAddRedGood() {
        assertThrows(UnsupportedCargoColorException.class, () -> tripleCargoHold.addGood(Color.RED));
    }

    @Test
    void testShouldNotAddGoodToFullTripleCargoHold() {
        tripleCargoHold.addGood(Color.BLUE);
        tripleCargoHold.addGood(Color.GREEN);
        tripleCargoHold.addGood(Color.YELLOW);
        assertThrows(FullCargoHoldException.class, () -> tripleCargoHold.addGood(Color.BLUE));
    }

    @Test
    void testShouldNotAddGoodToFullDoubleCargoHold() {
        doubleCargoHold.addGood(Color.BLUE);
        doubleCargoHold.addGood(Color.GREEN);
        assertThrows(FullCargoHoldException.class, () -> doubleCargoHold.addGood(Color.YELLOW));
    }

    @Test
    void testGetGoodsReturnsCopy() {
        tripleCargoHold.addGood(Color.BLUE);
        List<Color> goods = tripleCargoHold.getGoods();
        goods.add(Color.GREEN);
        assertEquals(1, tripleCargoHold.getNumberGoods());
    }
}