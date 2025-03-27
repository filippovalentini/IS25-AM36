package it.polimi.ingsw.galaxytrucker.model.gameClasses;

import it.polimi.ingsw.galaxytrucker.model.enumerations.Color;
import it.polimi.ingsw.galaxytrucker.model.enumerations.State;
import it.polimi.ingsw.galaxytrucker.model.exceptions.InvalidActionException;
import it.polimi.ingsw.galaxytrucker.model.exceptions.InvalidPositionException;
import it.polimi.ingsw.galaxytrucker.model.exceptions.UniqueNicknameException;
import it.polimi.ingsw.galaxytrucker.model.exceptions.UniquePlayerColorException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class GameStateFirstFlightTest {

    private GameState gameState;
    private String player1;
    private String player2;

    @BeforeEach
    public void initFirstFlight() {
        gameState = new GameState(true, 2);
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
    public void testUpdateTurnsWithMaxPlayers(){
        gameState = new GameState(true, 4);
        player1 = "truck3r";
        player2 = "4lien";
        String player3 = "cr3w";
        String player4 = "pir4t3";
        gameState.addPlayer(player1, Color.RED);
        gameState.addPlayer(player2, Color.BLUE);
        gameState.addPlayer(player3, Color.YELLOW);
        gameState.addPlayer(player4, Color.GREEN);
        gameState.setPosition(player1, 0);
        gameState.setPosition(player2, 1);
        gameState.setPosition(player3, 2);
        gameState.setPosition(player4, 4);
        gameState.setGameState(State.CARD_SOLVING);
        gameState.updateTurns();
        assertEquals(player4, gameState.getPlayersPlay().entrySet().iterator().next().getKey());
    }

    @Test
    public void testSetPosition() {
        gameState.setPosition(player1, 0);
        gameState.setPosition(player2, 1);
        assertEquals(0, gameState.getPlayersPos().get(player1).getCell());
        assertEquals(1, gameState.getPlayersPos().get(player2).getCell());
    }

    @Test
    public void testChangePlayerPosition() {
        gameState.setPosition(player1, 0);
        gameState.setPosition(player2, 1);
        gameState.changePlayerPosition(player1, 3);
        gameState.changePlayerPosition(player2, -1);
        assertEquals(3, gameState.getPlayersPos().get(player1).getCell());
        assertEquals(0, gameState.getPlayersPos().get(player2).getCell());
    }

    @Test
    public void testPutShown() {
        gameState.pickShown(player1, 0);
        gameState.putShown(player1);

        assertEquals(null, gameState.getPlayersPlay().get(player1).getShipBoard().getPickedComponent());
    }

    @Test
    public void testPickHidden() {
        gameState.pickHidden(player1);
        assertNotNull(gameState.getPlayersPlay().get(player1).getShipBoard().getPickedComponent());
    }

    @Test
    public void testPickShown() {
        gameState.pickShown(player1, 0);
        assertNotNull(gameState.getPlayersPlay().get(player1).getShipBoard().getPickedComponent());
    }

    @Test
    public void testReserveComponent() {
        gameState.pickShown(player1, 0);
        gameState.reserveComponent(player1);
        assertNull(gameState.getPlayersPlay().get(player1).getShipBoard().getPickedComponent());
    }

    @Test
    public void testPickReservedComponent() {
        gameState.pickShown(player1, 0);
        gameState.reserveComponent(player1);
        gameState.pickReservedComponent(player1, 0);
        assertNotNull(gameState.getPlayersPlay().get(player1).getShipBoard().getPickedComponent());
    }

    @Test
    public void testAssembleComponent() {
        gameState.pickShown(player1, 0);
        gameState.assembleComponent(player1, 0, 0);
        assertNull(gameState.getPlayersPlay().get(player1).getShipBoard().getPickedComponent());
    }

    @Test
    public void testRotatePickedComponent() {
        gameState.pickShown(player1, 0);
        gameState.rotatePickedComponent(player1);
        assertNotNull(gameState.getPlayersPlay().get(player1).getShipBoard().getPickedComponent());
    }

    @Test
    public void testDestroyComponent() {
        gameState.pickShown(player1, 0);
        gameState.assembleComponent(player1, 0, 0);
        gameState.destroyComponent(player1, 0, 0);
        assertNull(gameState.getPlayersPlay().get(player1).getShipBoard().getAssembledComponent(0, 0));
    }

    @Test
    public void testCheckShipBoards() {
        gameState.pickShown(player1, 0);
        gameState.assembleComponent(player1, 0, 0);
        gameState.pickShown(player2, 0);
        gameState.assembleComponent(player2, 0, 0);
        gameState.checkShipBoards();
        assertEquals(State.CARD_PICKING, gameState.getGameState());
    }

    @Test
    public void testPickNextCard() {
        gameState.pickShown(player1, 0);
        gameState.assembleComponent(player1, 0, 0);
        gameState.pickShown(player2, 0);
        gameState.assembleComponent(player2, 0, 0);
        gameState.checkShipBoards();
        gameState.pickNextCard(player1);
        assertEquals(State.CARD_SOLVING, gameState.getGameState());
    }

    @Test
    public void testPickNextCardNotLeader() {
        gameState.pickShown(player1, 0);
        gameState.assembleComponent(player1, 0, 0);
        gameState.pickShown(player2, 0);
        gameState.assembleComponent(player2, 0, 0);
        gameState.checkShipBoards();
        assertThrows(InvalidActionException.class, () -> gameState.pickNextCard(player2));
    }

    @Test
    public void testUpdatePlayerCredits() {
        gameState.updatePlayerCredits(player1, 5);
        assertEquals(5, gameState.getPlayersPlay().get(player1).getCredits());
    }

    @Test
    public void testGetCrewCount() {
        assertEquals(0, gameState.getCrewCount(player1));
    }

    @Test
    public void testRemovedCrewMember() {
        List<Integer> x = new ArrayList<>();
        List<Integer> y = new ArrayList<>();
        List<Integer> eachCabinCrew = new ArrayList<>();
        gameState.removedCrewMember(player1, x, y, eachCabinCrew, 1);
        assertEquals(0, gameState.getCrewCount(player1));
    }

    @Test
    public void testAddPlayerDuplicateNickname() {
        assertThrows(UniqueNicknameException.class, () -> gameState.addPlayer(player1, Color.GREEN));
    }

    @Test
    public void testAddPlayerDuplicateColor() {
        assertThrows(UniquePlayerColorException.class, () -> gameState.addPlayer("newPlayer", Color.RED));
    }

    @Test
    public void testSetPositionInvalidCell() {
        assertThrows(InvalidPositionException.class, () -> gameState.setPosition(player1, 100));
    }

    @Test
    public void testSetPositionDuplicateCell() {
        gameState.setPosition(player1, 5);
        assertThrows(InvalidPositionException.class, () -> gameState.setPosition(player2, 5));
    }

    @Test
    public void testPickShownInvalidAction() {
        gameState.setPosition(player1, 5);
        gameState.setPosition(player2, 10);
        gameState.checkShipBoards();
        assertThrows(InvalidActionException.class, () -> gameState.pickShown(player1, 0));
    }

    @Test
    public void testAssembleComponentInvalidAction() {
        gameState.setPosition(player1, 5);
        gameState.setPosition(player2, 10);
        gameState.checkShipBoards();
        assertThrows(InvalidActionException.class, () -> gameState.assembleComponent(player1, 0, 0));
    }
}