package it.polimi.ingsw.galaxytrucker.model.eventCardClasses;

import it.polimi.ingsw.galaxytrucker.model.enumerations.Color;
import it.polimi.ingsw.galaxytrucker.model.exceptions.InvalidActionException;
import it.polimi.ingsw.galaxytrucker.model.gameClasses.GameState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OpenSpaceTest {
    private OpenSpace os;
    private GameState gameState;

    @BeforeEach
    void init() {
        os = new OpenSpace(2);
        gameState = new GameState(true, 4);
        gameState.addPlayer("filippo", Color.RED);
    }

    @Test
    void testFly() throws InvalidActionException {
        os.fly(gameState, "filippo", 1);
    }
}