package it.polimi.ingsw.galaxytrucker.model.eventCardClasses;

import it.polimi.ingsw.galaxytrucker.model.componentClasses.Battery;
import it.polimi.ingsw.galaxytrucker.model.componentClasses.Component;
import it.polimi.ingsw.galaxytrucker.model.componentClasses.Engine;
import it.polimi.ingsw.galaxytrucker.model.enumerations.Color;
import it.polimi.ingsw.galaxytrucker.model.enumerations.Connector;
import it.polimi.ingsw.galaxytrucker.model.gameClasses.GameState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class OpenSpaceTest {
    private OpenSpace os;
    private GameState gameState;
    private String player1;
    private String player2;
    private Engine engine2;
    private List<Connector> sides;

    @BeforeEach
    void init() {
        os = new OpenSpace(2);
        gameState = new GameState(false, 2);
        player1="filippo";
        player2="thomas";
        gameState.addPlayer(player1, Color.RED);
        gameState.addPlayer(player2, Color.GREEN);
        gameState.setPosition(player1, 6);
        gameState.setPosition(player2, 3);
        sides = new ArrayList<>();
        sides.add();
        engine2= new Engine(false,1, sides);
        gameState.getPlayersPlay().get(player2).pickComponent(engine2);
        gameState.getPlayersPlay().get(player2).assembleComponent(1, 1);
    }

    @Test
    void testFly(){
        os.fly(gameState,player1,0);
        assertEquals(player1, gameState.getTurnPlayer(), "The leader should be player2 (thomas)");
    }
}