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

    @BeforeEach
    void init(){
        position = new LevelTwoPosition(0);
        busyCells = new ArrayList<>(Arrays.asList(0));
    }

    @Test
    void testChangePositionOneStep() {
        position.changePosition(busyCells, 1);
        assertEquals(1, position.getCell());
    }

    @Test
    void testChangePositionOneLap() {
        position.changePosition(busyCells, 24);
        assertEquals(0, position.getCell());
        assertEquals(1, position.getLap());
    }

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

    @Test
    void testNoOverlappingPositions1() {
        busyCells = new ArrayList<>(Arrays.asList(0,3,5));
        position.changePosition(busyCells, 8);
        assertEquals(10, position.getCell());
    }

    @Test
    void testNoOverlappingPositions2() {
        busyCells = new ArrayList<>(Arrays.asList(0,19,23));
        position.changePosition(busyCells, -4);
        assertEquals(18, position.getCell());
    }
}