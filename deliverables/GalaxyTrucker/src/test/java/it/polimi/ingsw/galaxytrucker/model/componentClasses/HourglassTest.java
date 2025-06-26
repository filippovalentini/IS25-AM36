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
    void init() { // Initialize the GameState and Hourglass before each test
        gs = new GameState(false, 2); // Create a GameState with 2 players
        gs.setGameState(State.SHIP_BUILDING); // Set the game state to SHIP_BUILDING
        hourglass = new Hourglass(1, gs); // 1 seconds for testing
    }

    @Test
    void testStartNewCycle() { // Test starting a new cycle
        hourglass.startNewCycle(); // Start the hourglass cycle
        assertEquals(1, hourglass.getNumberFlips()); // Verify that the number of flips is 1

    }

    @Test
    void testConstructorWithInvalidDuration_Negative() { // Test constructor with negative duration
        HourGlassException exception = assertThrows(HourGlassException.class, () -> { // Expect an exception to be thrown
            new Hourglass(-2, gs); // Create a new Hourglass with negative duration
        });
        assertEquals("Duration must be positive.", exception.getMessage()); // Verify the exception message
    }

    @Test
    void testStartNewCycle_AlreadyRunning() { // Test starting a new cycle when already running
        hourglass.startNewCycle(); // Start the hourglass cycle
        HourGlassException exception = assertThrows(HourGlassException.class, () -> {
            hourglass.startNewCycle(); // Attempt to start a new cycle while already running
        });
        assertEquals("Hourglass is already running.", exception.getMessage());  // Verify the exception message
    }

    @Test
    void testStartNewCycle_AlreadyFlipped() throws InterruptedException { // Test starting a new cycle after it has been flipped
        hourglass.startNewCycle(); // Start the hourglass cycle
        Thread.sleep(2000); // wait for the cycle to finish
        hourglass.startNewCycle(); // Start a new cycle after the first one has finished
        assertEquals(2, hourglass.getNumberFlips()); // Verify that the number of flips is now 2
    }
    @Test
    void testStartNewCycle_tooMuchFlips() throws InterruptedException { // Test starting a new cycle after too many flips
        hourglass.startNewCycle(); // Start the hourglass cycle
        Thread.sleep(2000); // wait for the cycle to finish
        hourglass.startNewCycle(); // Start a new cycle after the first one has finished
        Thread.sleep(2000); // wait for the second cycle to finish
        HourGlassException exception = assertThrows(HourGlassException.class, () -> { // Expect an exception to be thrown
            hourglass.startNewCycle();
        });
        assertEquals("Can't start a new cycle", exception.getMessage()); // Verify the exception message

    }
}



