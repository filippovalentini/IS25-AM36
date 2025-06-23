package it.polimi.ingsw.galaxytrucker.model.componentClasses;
import it.polimi.ingsw.galaxytrucker.model.enumerations.State;
import it.polimi.ingsw.galaxytrucker.model.exceptions.HourGlassException;
import it.polimi.ingsw.galaxytrucker.model.gameClasses.GameState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class HourglassTest {
    private Hourglass hourglass;
    GameState gs;


    @BeforeEach
    void init() {
        gs = new GameState(false, 2);
        gs.setGameState(State.SHIP_BUILDING);
        hourglass = new Hourglass(1, gs); // 1 seconds for testing
    }

    @Test
    void testStartNewCycle() {
        hourglass.startNewCycle();
        assertEquals(1, hourglass.getNumberFlips());

    }

    @Test
    void testConstructorWithInvalidDuration_Negative() {
        HourGlassException exception = assertThrows(HourGlassException.class, () -> {
            new Hourglass(-2, gs);
        });
        assertEquals("La durata del ciclo deve essere positiva.", exception.getMessage());
    }

    @Test
    void testStartNewCycle_AlreadyRunning() {
        hourglass.startNewCycle();
        HourGlassException exception = assertThrows(HourGlassException.class, () -> {
            hourglass.startNewCycle();
        });
        assertEquals("Hourglass is already running.", exception.getMessage());
    }

    @Test
    void testStartNewCycle_AlreadyFlipped() throws InterruptedException {
        hourglass.startNewCycle();
        Thread.sleep(2000); // wait for the cycle to finish
        hourglass.startNewCycle();
        assertEquals(2, hourglass.getNumberFlips());
    }
    @Test
    void testStartNewCycle_tooMuchFlips() throws InterruptedException {
        hourglass.startNewCycle();
        Thread.sleep(2000);
        hourglass.startNewCycle();
        Thread.sleep(2000);
        HourGlassException exception = assertThrows(HourGlassException.class, () -> {
            hourglass.startNewCycle();
        });
        assertEquals("Can't start a new cycle", exception.getMessage());

    }
}



