package it.polimi.ingsw.galaxytrucker.controller;

import it.polimi.ingsw.galaxytrucker.model.enumerations.Color;
import it.polimi.ingsw.galaxytrucker.model.gameClasses.GameState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameControllerTest {
    GameController gcBasic;

    @BeforeEach
    void init(){
        gcBasic = new GameController(false, 2);
    }

    //start of addPlayer(...) test
    @Test
    void testAddAllPlayers(){
        assertEquals(0, gcBasic.addPlayer("player1", Color.BLUE));
        assertEquals(1, gcBasic.addPlayer("player2", Color.RED));
    }

    @Test
    void testShouldNotAddPlayerGameStarted(){
        String player1 = "player1";
        String player2 = "player2";
        gcBasic.addPlayer(player1, Color.BLUE);
        gcBasic.addPlayer(player2, Color.RED);
        gcBasic.setPosition(player1, 6);
        gcBasic.setPosition(player2, 3);
        assertEquals(-1, gcBasic.addPlayer("player3", Color.YELLOW));
    }

    @Test
    void testShouldNotAddPlayerExistingGame(){
        String player1 = "has_same_name";
        String player2 = "has_same_name";
        gcBasic.addPlayer(player1, Color.BLUE);
        assertEquals(-2, gcBasic.addPlayer(player2, Color.RED));
    }

    @Test
    void testShouldNotAddPlayerChosenColor(){
        String player1 = "player1";
        String player2 = "player2";
        Color sameColor = Color.GREEN;
        gcBasic.addPlayer(player1, sameColor);
        assertEquals(-3, gcBasic.addPlayer(player2, sameColor));
    }

    //start of pickHidden(...) test
    @Test
    void testPickHidden() {
        String player1 = "player1";
        String player2 = "player2";
        gcBasic.addPlayer(player1, Color.BLUE);
        gcBasic.addPlayer(player2, Color.RED);
        gcBasic.setPosition(player1, 6);
        gcBasic.setPosition(player2, 3);
        assertEquals(0, gcBasic.pickHidden(player1)); //problem
    }

    @Test
    void testPickHiddenWrongPhase() {
        String player1 = "player1";
        gcBasic.addPlayer(player1, Color.BLUE);
        assertEquals(-1, gcBasic.pickHidden(player1));
    }

    @Test
    void testPickHiddenAlreadyPicked() {
        gcBasic.addPlayer("player1", Color.BLUE);
        gcBasic.addPlayer("player2", Color.RED);
        gcBasic.pickHidden("player1");
        assertEquals(-2, gcBasic.pickHidden("player1"));
    }

}

