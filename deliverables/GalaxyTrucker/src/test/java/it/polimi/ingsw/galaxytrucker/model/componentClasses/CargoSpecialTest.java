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
    void init() { // Initialize the CargoSpecial instances with appropriate sides
        sides = new ArrayList<>();
        sides.add(Connector.SMOOTH);
        sides.add(Connector.SMOOTH);
        sides.add(Connector.DOUBLE);
        sides.add(Connector.UNIVERSAL);
        singleCargoSpecial = new CargoSpecial(false, 0, sides); // cargo special with no goods
        doubleCargoSpecial = new CargoSpecial(true, 1, sides); // Double cargo special with no goods
    }

    @Test
    void testAddGoodToSingleCargoSpecial() { // Test adding a good to a single cargo special
        singleCargoSpecial.addGood(Color.BLUE); // Add a good of color BLUE
        assertEquals(1, singleCargoSpecial.getNumberGoods()); // Check if the number of goods is 1
        assertTrue(singleCargoSpecial.getGoods().contains(Color.BLUE)); // Check if the goods list contains the added good
    }

    @Test
    void testAddGoodToDoubleCargoSpecial() { // Test adding a good to a double cargo special
        doubleCargoSpecial.addGood(Color.GREEN); // Add a good of color GREEN
        assertEquals(1, doubleCargoSpecial.getNumberGoods()); // Check if the number of goods is 1
        assertTrue(doubleCargoSpecial.getGoods().contains(Color.GREEN)); // Check if the goods list contains the added good
    }

    @Test
    void testAddRedGood() { // Test adding a red good to a single cargo special
        singleCargoSpecial.addGood(Color.RED); // Add a good of color RED
        assertEquals(1, singleCargoSpecial.getNumberGoods()); // Check if the number of goods is 1
        assertTrue(singleCargoSpecial.getGoods().contains(Color.RED)); // Check if the goods list contains the added good
    }

    @Test
    void testShouldNotAddGoodToFullSingleCargoSpecial() { // Test that a good cannot be added to a full single cargo special
        singleCargoSpecial.addGood(Color.BLUE); // Add a good of color BLUE
        assertThrows(FullCargoHoldException.class, () -> singleCargoSpecial.addGood(Color.GREEN)); // Attempt to add another good of color GREEN, which should throw an exception
    }

    @Test
    void testShouldNotAddGoodToFullDoubleCargoSpecial() { // Test that a good cannot be added to a full double cargo special
        doubleCargoSpecial.addGood(Color.BLUE); // Add a good of color BLUE
        doubleCargoSpecial.addGood(Color.GREEN); // Add another good of color GREEN
        assertThrows(FullCargoHoldException.class, () -> doubleCargoSpecial.addGood(Color.YELLOW)); // Attempt to add a third good of color YELLOW, which should throw an exception
    }

    @Test
    void testGetGoodsReturnsCopy() { // Test that the getGoods method returns a copy of the goods list
        singleCargoSpecial.addGood(Color.BLUE); // Add a good of color BLUE
        List<Color> goods = singleCargoSpecial.getGoods(); // Get the goods list
        goods.add(Color.GREEN); // Attempt to modify the goods list by adding a good of color GREEN
        assertEquals(1, singleCargoSpecial.getNumberGoods()); // Check if the number of goods in the original cargo special is still 1
    }
}