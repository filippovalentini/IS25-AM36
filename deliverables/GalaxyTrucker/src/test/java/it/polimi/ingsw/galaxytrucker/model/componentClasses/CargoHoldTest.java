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
    void init() { // Initialize the CargoHold instances with appropriate connectors
        List<Connector> sides = new ArrayList<>();
        sides.add(Connector.SMOOTH);
        sides.add(Connector.SMOOTH);
        sides.add(Connector.DOUBLE);
        sides.add(Connector.UNIVERSAL);
        tripleCargoHold = new CargoHold(false, 0, sides); //initializing cargo hold
        doubleCargoHold = new CargoHold(true, 1, sides); //initializing double cargo hold
    }

    @Test
    void testAddGoodToTripleCargoHold() { // Test adding a good to the triple cargo hold
        tripleCargoHold.addGood(Color.BLUE); // Adding a good of color BLUE
        assertEquals(1, tripleCargoHold.getNumberGoods()); // Check if the number of goods is 1
        assertTrue(tripleCargoHold.getGoods().contains(Color.BLUE)); // Check if the goods list contains the added good
    }

    @Test
    void testAddGoodToDoubleCargoHold() { // Test adding a good to the double cargo hold
        doubleCargoHold.addGood(Color.GREEN); // Adding a good of color GREEN
        assertEquals(1, doubleCargoHold.getNumberGoods()); // Check if the number of goods is 1
        assertTrue(doubleCargoHold.getGoods().contains(Color.GREEN)); // Check if the goods list contains the added good
    }

    @Test
    void testShouldNotAddRedGood() { // Test that adding a red good to a triple cargo hold throws an exception
        assertThrows(UnsupportedCargoColorException.class, () -> tripleCargoHold.addGood(Color.RED)); // Adding a red good should not be allowed
    }

    @Test
    void testShouldNotAddGoodToFullTripleCargoHold() { // Test that adding a good to a full triple cargo hold throws an exception
        tripleCargoHold.addGood(Color.BLUE); // Adding a good of color BLUE
        tripleCargoHold.addGood(Color.GREEN); // Adding a good of color GREEN
        tripleCargoHold.addGood(Color.YELLOW);   // Adding a good of color YELLOW
        assertThrows(FullCargoHoldException.class, () -> tripleCargoHold.addGood(Color.BLUE)); // Adding another good should throw an exception
    }

    @Test
    void testShouldNotAddGoodToFullDoubleCargoHold() { // Test that adding a good to a full double cargo hold throws an exception
        doubleCargoHold.addGood(Color.BLUE); // Adding a good of color BLUE
        doubleCargoHold.addGood(Color.GREEN); // Adding a good of color GREEN
        assertThrows(FullCargoHoldException.class, () -> doubleCargoHold.addGood(Color.YELLOW)); // Adding another good should throw an exception
    }

    @Test
    void testGetGoodsReturnsCopy() { // Test that the getGoods method returns a copy of the goods list
        tripleCargoHold.addGood(Color.BLUE); // Adding a good of color BLUE
        List<Color> goods = tripleCargoHold.getGoods(); // Getting the list of goods
        goods.add(Color.GREEN); // Attempting to modify the returned list
        assertEquals(1, tripleCargoHold.getNumberGoods()); // Check if the number of goods in the original cargo hold is still 1
    }
}