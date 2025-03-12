package it.polimi.ingsw.galaxytrucker.model.gameClasses;

import it.polimi.ingsw.galaxytrucker.model.enumerations.Color;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GameStateFirstFlightTest {

    private GameState gameState;
    private String player1;
    private String player2;

    @BeforeEach
    public void initFirstFlight() {
        gameState = new GameState(true, 4);
        player1 = "truck3r";
        player2 = "4lien";
        gameState.addPlayer(player1, Color.RED);
        gameState.addPlayer(player2, Color.BLUE);
    }

    @Test
    public void testAddPlayer() {
        GameState gameStateAddPlayers = new GameState(true, 4);
        player1 = "truck3r";
        player2 = "4lien";
        gameStateAddPlayers.addPlayer(player1, Color.RED);
        gameStateAddPlayers.addPlayer(player2, Color.BLUE);
        assertEquals(Color.RED, gameStateAddPlayers.getPlayersPlay().get(player1).getShipBoard().getColor());
        assertEquals(Color.BLUE, gameStateAddPlayers.getPlayersPlay().get(player2).getShipBoard().getColor());
    }

    @Test
    public void testSetPosition() {
        gameState.setPosition(player1, 5);
        gameState.setPosition(player2, 10);

        assertEquals(5, gameState.getPlayersPos().get(player1).getCell());
        assertEquals(10, gameState.getPlayersPos().get(player2).getCell());
    }

    @Test
    public void testChangePlayerPosition() {
        gameState.setPosition(player1, 5);
        gameState.setPosition(player2, 10);
        gameState.changePlayerPosition(player1, 3);
        gameState.changePlayerPosition(player2, -1);

        assertEquals(8, gameState.getPlayersPos().get(player1).getCell());
        assertEquals(9, gameState.getPlayersPos().get(player2).getCell());
    }


    @Test
    public void testPutShown() {
        gameState.pickShown(player1, 0);
        gameState.putShown(player1);

        assertEquals(null, gameState.getPlayersPlay().get(player1).getShipBoard().getPickedComponent());
    }
}