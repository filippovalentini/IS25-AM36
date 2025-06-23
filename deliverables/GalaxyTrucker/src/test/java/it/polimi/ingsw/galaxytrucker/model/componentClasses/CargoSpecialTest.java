package it.polimi.ingsw.galaxytrucker.model.componentClasses;

import it.polimi.ingsw.galaxytrucker.model.enumerations.Color;
import it.polimi.ingsw.galaxytrucker.model.enumerations.Connector;
import it.polimi.ingsw.galaxytrucker.model.exceptions.FullCargoHoldException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CargoSpecialTest {
    private CargoSpecial singleCargoSpecial;
    private CargoSpecial doubleCargoSpecial;
    private List<Connector> sides;

    @BeforeEach
    void init() {
        sides = new ArrayList<>();
        sides.add(Connector.SMOOTH);
        sides.add(Connector.SMOOTH);
        sides.add(Connector.DOUBLE);
        sides.add(Connector.UNIVERSAL);
        singleCargoSpecial = new CargoSpecial(false, 0, sides);
        doubleCargoSpecial = new CargoSpecial(true, 1, sides);
    }

    @Test
    void testAddGoodToSingleCargoSpecial() {
        singleCargoSpecial.addGood(Color.BLUE);
        assertEquals(1, singleCargoSpecial.getNumberGoods());
        assertTrue(singleCargoSpecial.getGoods().contains(Color.BLUE));
    }

    @Test
    void testAddGoodToDoubleCargoSpecial() {
        doubleCargoSpecial.addGood(Color.GREEN);
        assertEquals(1, doubleCargoSpecial.getNumberGoods());
        assertTrue(doubleCargoSpecial.getGoods().contains(Color.GREEN));
    }

    @Test
    void testAddRedGood() {
        singleCargoSpecial.addGood(Color.RED);
        assertEquals(1, singleCargoSpecial.getNumberGoods());
        assertTrue(singleCargoSpecial.getGoods().contains(Color.RED));
    }

    @Test
    void testShouldNotAddGoodToFullSingleCargoSpecial() {
        singleCargoSpecial.addGood(Color.BLUE);
        assertThrows(FullCargoHoldException.class, () -> singleCargoSpecial.addGood(Color.GREEN));
    }

    @Test
    void testShouldNotAddGoodToFullDoubleCargoSpecial() {
        doubleCargoSpecial.addGood(Color.BLUE);
        doubleCargoSpecial.addGood(Color.GREEN);
        assertThrows(FullCargoHoldException.class, () -> doubleCargoSpecial.addGood(Color.YELLOW));
    }

    @Test
    void testGetGoodsReturnsCopy() {
        singleCargoSpecial.addGood(Color.BLUE);
        List<Color> goods = singleCargoSpecial.getGoods();
        goods.add(Color.GREEN);
        assertEquals(1, singleCargoSpecial.getNumberGoods());
    }
}