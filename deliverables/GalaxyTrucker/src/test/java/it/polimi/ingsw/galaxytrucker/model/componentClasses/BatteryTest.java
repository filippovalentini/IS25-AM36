package it.polimi.ingsw.galaxytrucker.model.componentClasses;

import it.polimi.ingsw.galaxytrucker.model.enumerations.Connector;
import it.polimi.ingsw.galaxytrucker.model.exceptions.NoBatteriesException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BatteryTest {
    private Battery battery;

    @BeforeEach
    void init() { // Initialize a Battery with a list of connectors
        List<Connector> sides = new ArrayList<Connector>();
        sides.add(Connector.SMOOTH);
        sides.add(Connector.SMOOTH);
        sides.add(Connector.DOUBLE);
        sides.add(Connector.UNIVERSAL);
        battery = new Battery(true, 0, sides);
    }

    @Test
    void testUseAvailableBattery() { // Test using a battery when available
        battery.addBatteries(); // Add a battery to the battery component
        battery.useBatteries(2); // Use 2 batteries
        assertEquals(0, battery.getNumberBatteries()); // Check that the number of batteries is now 0
    }

    @Test
    void testShouldNotUseUnavailableBattery() { // Test using a battery when none are available
        assertThrows(NoBatteriesException.class, () -> {battery.useBatteries(3);});  // Expect an exception when trying to use 3 batteries when none are available
    }
}