package it.polimi.ingsw.galaxytrucker.model.gameClasses;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LevelTwoPositionTest {
    private Position position;
    private List<Integer> busyCells;

    /**
     * Initializes a {@code LevelTwoPosition} at cell 0 and sets the initial list of busy cells.
     */
    @BeforeEach
    void init(){
        position = new LevelTwoPosition(0);
        busyCells = new ArrayList<>(Arrays.asList(0));
    }

    /**
     * Tests moving the position forward by one step.
     * Verifies that the cell is updated correctly.
     */
    @Test
    void testChangePositionOneStep() {
        position.changePosition(busyCells, 1);
        assertEquals(1, position.getCell());
    }

    /**
     * Tests completing one full lap (24 steps).
     * Verifies that the cell resets to 0 and the lap counter increases.
     */
    @Test
    void testChangePositionOneLap() {
        position.changePosition(busyCells, 24);
        assertEquals(0, position.getCell());
        assertEquals(1, position.getLap());
    }

    /**
     * Tests moving backward from a new lap.
     * Verifies that the cell and lap are updated correctly when stepping back.
     */
    @Test
    void testChangePositionNegativeStepFromLap(){
        position.changePosition(busyCells, 24);
        busyCells = new ArrayList<>(Arrays.asList(position.getCell()));
        position.changePosition(busyCells, -1);
        assertEquals(23, position.getCell());
        assertEquals(0, position.getLap());

        busyCells = new ArrayList<>(Arrays.asList(position.getCell()));
        position.changePosition(busyCells, -1);
        assertEquals(22, position.getCell());
    }

    /**
     * Tests that the position skips over busy cells when moving forward.
     * Verifies that the final cell does not overlap with any in the busy list.
     */
    @Test
    void testNoOverlappingPositions1() {
        busyCells = new ArrayList<>(Arrays.asList(0, 3, 5));
        position.changePosition(busyCells, 8);
        assertEquals(10, position.getCell());
    }

    /**
     * Tests that the position skips over busy cells when moving backward.
     * Verifies that the final cell avoids the specified busy positions.
     */
    @Test
    void testNoOverlappingPositions2() {
        busyCells = new ArrayList<>(Arrays.asList(0,19,23));
        position.changePosition(busyCells, -4);
        assertEquals(18, position.getCell());
    }
}
