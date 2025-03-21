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

class GameStateNormalTest {
    private GameState gameState;
    private String player1;
    private String player2;

    @BeforeEach
    public void initLV2Flight() {
        gameState = new GameState(false, 2);
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

        assertNull(gameState.getPlayersPlay().get(player1).getShipBoard().getPickedComponent());
    }

    @Test
    public void testWrongAssemblingPhase(){
        gameState.pickShown(player1, 0);
        gameState.putShown(player1);
        gameState.pickShown(player2, 0);
        gameState.checkShipBoards();
        assertEquals(State.SHIP_BUILDING, gameState.getGameState());
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
    public void testGetPlayerCrewCount() {
        assertEquals(0, gameState.getPlayerCrewCount(player1));
    }

    @Test
    public void testRemovedCrewMember() {
        List<Integer> x = new ArrayList<>();
        List<Integer> y = new ArrayList<>();
        List<Integer> eachCabinCrew = new ArrayList<>();
        gameState.removedCrewMember(player1, x, y, eachCabinCrew, 1);
        assertEquals(0, gameState.getPlayerCrewCount(player1));
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