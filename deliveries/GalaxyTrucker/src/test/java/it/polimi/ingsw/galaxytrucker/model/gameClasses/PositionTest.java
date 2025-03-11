package it.polimi.ingsw.galaxytrucker.model.gameClasses;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PositionTest {
    private Position position;

    @BeforeEach
    void init(){
        position = new Position(0);
    }

    @Test
    void testChangePositionOneStep() {
        position.changePosition(1);
        assertEquals(1, position.getCell());
    }

    @Test
    void testChangePositionOneLap() {
        position.changePosition(24);
        assertEquals(0, position.getCell());
        assertEquals(1, position.getLap());
    }

    @Test
    void testChangePositionNegativeStepFromLap(){
        position.changePosition(24);
        position.changePosition(-1);
        assertEquals(23, position.getCell());
        assertEquals(0, position.getLap());
    }
}