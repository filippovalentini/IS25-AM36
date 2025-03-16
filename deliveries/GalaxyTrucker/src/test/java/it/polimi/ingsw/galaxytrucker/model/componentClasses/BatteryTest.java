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
    void init() {
        List<Connector> sides = new ArrayList<Connector>();
        sides.add(Connector.SMOOTH);
        sides.add(Connector.SMOOTH);
        sides.add(Connector.DOUBLE);
        sides.add(Connector.UNIVERSAL);
        battery = new Battery(true, 0, sides);
    }

    @Test
    void testUseAvailableBattery() {
        battery.useBatteries(2);
        assertEquals(0, battery.getNumberBatteries());
    }

    @Test
    void testShouldNotUseUnavailableBattery() {
        assertThrows(NoBatteriesException.class, () -> {battery.useBatteries(3);});
    }
}