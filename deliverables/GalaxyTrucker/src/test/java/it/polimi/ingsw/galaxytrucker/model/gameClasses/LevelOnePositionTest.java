package it.polimi.ingsw.galaxytrucker.model.gameClasses;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for LevelOnePosition.
 * It verifies the logic of movement, lap counting, and handling of occupied cells.
 */
class LevelOnePositionTest {
    // The instance of the class we are testing.
    private LevelOnePosition position;
    // A list representing cells that are currently occupied on the board.
    private List<Integer> busyCells;

    /**
     * This method runs before each test.
     * It initializes the state to ensure tests are independent.
     */
    @BeforeEach
    void init(){
        // Create a new position object, starting at cell 0.
        position = new LevelOnePosition(0);
        // Initialize the list of busy cells. The starting cell 0 is marked as busy.
        busyCells = new ArrayList<>(Arrays.asList(0));
    }

    /**
     * Verifies that a simple, single forward step correctly updates the cell position.
     */
    @Test
    void testChangePositionOneStep() {
        // Move the position by 1 step.
        position.changePosition(busyCells, 1);
        // Assert that the new cell is 1, as expected.
        assertEquals(1, position.getCell());
    }

    /**
     * Verifies that moving by the exact length of the track completes one lap
     * and correctly updates both cell and lap count.
     */
    @Test
    void testChangePositionOneLap() {
        // Move the position by 18 steps, which should be a full lap.
        position.changePosition(busyCells, 18);
        // Assert that the position has returned to cell 0.
        assertEquals(0, position.getCell());
        // Assert that the lap counter has been incremented to 1.
        assertEquals(1, position.getLap());
    }

    /**
     * Verifies that moving backward across the starting line correctly
     * decrements the lap count.
     */
    @Test
    void testChangePositionNegativeStepFromLap(){
        // First, complete one lap to be at cell 0, lap 1.
        position.changePosition(busyCells, 18);
        // Update the busy cells list with the current position.
        busyCells = new ArrayList<>(Arrays.asList(position.getCell()));

        // Move backward by 1 step, crossing the start/finish line.
        position.changePosition(busyCells, -1);
        // Assert that the cell is now 17 (the last cell on the track).
        assertEquals(17, position.getCell());
        // Assert that the lap count has been decremented back to 0.
        assertEquals(0, position.getLap());

        // Update busy cells again.
        busyCells = new ArrayList<>(Arrays.asList(position.getCell()));
        // Move backward one more step to ensure simple backward movement still works.
        position.changePosition(busyCells, -1);
        assertEquals(16, position.getCell());
    }

    /**
     * Verifies that the movement logic correctly skips over busy cells when moving forward.
     * A move of 8 steps should land on cell 10 because it skips busy cells 3 and 5.
     */
    @Test
    void testNoOverlappingPositions1() {
        // Set up a custom list of busy cells for this test case.
        busyCells = new ArrayList<>(Arrays.asList(0,3,5));
        // Move by 8 available steps.
        position.changePosition(busyCells, 8);
        // Assert that the final position is 10, not 8, because busy cells were skipped.
        assertEquals(10, position.getCell());
    }

    /**
     * Verifies that the movement logic correctly skips over busy cells when moving backward.
     * A move of -4 steps should land on 12 because it skips busy cells 17 and 15.
     */
    @Test
    void testNoOverlappingPositions2() {
        // Set up a custom list of busy cells.
        busyCells = new ArrayList<>(Arrays.asList(0,15,17));
        // Move backward by 4 available steps.
        position.changePosition(busyCells, -4);
        // Assert that the final position is 12, confirming the skipping logic works in reverse.
        assertEquals(12, position.getCell());
    }
}