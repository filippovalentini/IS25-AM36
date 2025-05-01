package it.polimi.ingsw.galaxytrucker.model.gameClasses;

import it.polimi.ingsw.galaxytrucker.model.componentClasses.Battery;
import it.polimi.ingsw.galaxytrucker.model.componentClasses.Cabin;
import it.polimi.ingsw.galaxytrucker.model.componentClasses.Component;
import it.polimi.ingsw.galaxytrucker.model.enumerations.Color;
import it.polimi.ingsw.galaxytrucker.model.enumerations.Connector;
import it.polimi.ingsw.galaxytrucker.model.enumerations.Orientation;
import it.polimi.ingsw.galaxytrucker.model.enumerations.State;
import it.polimi.ingsw.galaxytrucker.model.exceptions.*;
import it.polimi.ingsw.galaxytrucker.network.rmi.client.ClientRMI;
import it.polimi.ingsw.galaxytrucker.network.rmi.server.VirtualViewRMI;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GameStateNormalTest {
    private GameState gameState;
    private String player1;
    private String player2;
    private VirtualViewRMI cl1;
    private VirtualViewRMI cl2;

    @BeforeEach
    void initLV2Flight() {
        gameState = new GameState(false, 2);
        player1 = "truck3r";
        player2 = "4lien";
        try{
            cl1 = new ClientRMI(player1, Color.RED);
            cl2 = new ClientRMI(player2, Color.BLUE);
        }
        catch (Exception e){
            System.exit(-1);
        }
        gameState.addPlayer(cl1, player1, Color.RED);
        gameState.addPlayer(cl2, player2, Color.BLUE);
    }

    @Test
    void testAddPlayer() {
        GameState gs = new GameState(true, 2);
        player1 = "truck3r";
        player2 = "4lien";
        String player3 = "carg0";
        gs.addPlayer(cl1, player1, Color.RED);
        assertEquals(Color.RED, gs.getPlayersPlay().get(player1).getShipBoard().getColor());
        assertEquals(State.WAITING_FOR_PLAYERS, gs.getGameState());
        gs.addPlayer(cl2, player2, Color.BLUE);
        assertEquals(Color.BLUE, gs.getPlayersPlay().get(player2).getShipBoard().getColor());
        assertEquals(State.SHIP_BUILDING, gs.getGameState());
        assertThrows(InvalidActionException.class, () -> gs.addPlayer(cl1, player3, Color.YELLOW));
    }

    @Test
    void testUpdateTurnsWithMaxPlayers(){
        gameState = new GameState(true, 4);
        player1 = "truck3r";
        player2 = "4lien";
        String player3 = "cr3w";
        String player4 = "pir4t3";
        gameState.addPlayer(cl1, player1, Color.RED);
        gameState.addPlayer(cl2, player2, Color.BLUE);
        gameState.addPlayer(cl1, player3, Color.YELLOW);
        gameState.addPlayer(cl2, player4, Color.GREEN);
        gameState.setPosition(player1, 0);
        gameState.setPosition(player2, 1);
        gameState.setPosition(player3, 2);
        gameState.setPosition(player4, 4);
        gameState.setGameState(State.CARD_SOLVING);
        gameState.updateTurns();
        assertEquals(player4, gameState.getTurnPlayer());
        gameState.changePlayerPosition(player3,3);
        gameState.updateTurns(); //player 3 should be first
        assertEquals(player3, gameState.getTurnPlayer());
    }

    @Test
    void testSetPosition() {
        gameState.setPosition(player1, 0);
        gameState.setPosition(player2, 1);
        assertEquals(0, gameState.getPlayersPos().get(player1).getCell());
        assertEquals(1, gameState.getPlayersPos().get(player2).getCell());
    }

    @Test
    void testChangePlayerPosition() {
        gameState.setPosition(player1, 0);
        gameState.setPosition(player2, 1);
        gameState.changePlayerPosition(player1, 3);
        gameState.changePlayerPosition(player2, -1);
        assertEquals(3, gameState.getPlayersPos().get(player1).getCell());
        assertEquals(0, gameState.getPlayersPos().get(player2).getCell());
    }

    @Test
    void testPutShown() {
        gameState.pickHidden(player1);
        gameState.putShown(player1);
        assertNull(gameState.getPlayersPlay().get(player1).getShipBoard().getPickedComponent());
    }

    @Test
    void testPickHidden() {
        gameState.pickHidden(player1);
        assertNotNull(gameState.getPlayersPlay().get(player1).getShipBoard().getPickedComponent());
    }

    @Test
    void testPickShown() {
        gameState.pickHidden(player1);
        gameState.putShown(player1);
        gameState.pickShown(player1, 0);
        assertNotNull(gameState.getPlayersPlay().get(player1).getShipBoard().getPickedComponent());
    }

    @Test
    void testReserveComponent() {
        gameState.pickHidden(player1);
        gameState.reserveComponent(player1);
        assertNull(gameState.getPlayersPlay().get(player1).getShipBoard().getPickedComponent());
    }

    @Test
    void testPickReservedComponent() {
        gameState.pickHidden(player1);
        gameState.reserveComponent(player1);
        gameState.pickReservedComponent(player1, 0);
        assertNotNull(gameState.getPlayersPlay().get(player1).getShipBoard().getPickedComponent());
    }

    @Test
    void testAssembleComponent() {
        gameState.pickHidden(player1);
        gameState.assembleComponent(player1, 0, 2);
        assertNull(gameState.getPlayersPlay().get(player1).getShipBoard().getPickedComponent());
        gameState.pickHidden(player1);
        assertThrows(AssembledComponentException.class, () -> gameState.assembleComponent(player1, 0, 2));
    }

    @Test
    void testRotatePickedComponent() {
        gameState.pickHidden(player1);
        Orientation o1 = gameState.getPlayersPlay().get(player1).getShipBoard().getPickedComponent().getOrientation();
        gameState.rotatePickedComponent(player1);
        Orientation o2 = gameState.getPlayersPlay().get(player1).getShipBoard().getPickedComponent().getOrientation();
        assertNotNull(gameState.getPlayersPlay().get(player1).getShipBoard().getPickedComponent());
        assertNotEquals(o1, o2);
    }

    @Test
    void testDestroyComponent() {
        gameState.pickHidden(player1);
        Component battery = new Battery(true, 1000, new ArrayList<>(Arrays.asList(Connector.SINGLE, Connector.SMOOTH, Connector.SMOOTH, Connector.SMOOTH)));
        gameState.assembleComponent(player1, battery, 2, 2);
        gameState.setPosition(player1, 0);
        assertEquals(State.SHIP_BUILDING, gameState.getGameState());
        gameState.setPosition(player2, 1);
        assertEquals(State.SHIP_CONTROL, gameState.getGameState());
        gameState.destroyComponent(player1, 2, 2);
        gameState.addCrew(player1, 2, 3);
        gameState.addCrew(player2, 2, 3);
        assertEquals(State.CARD_PICKING, gameState.getGameState());
    }

    @Test
    void testPickNextCard() {
        gameState.pickHidden(player1);
        gameState.putShown(player1);
        gameState.setPosition(player2, 1);
        gameState.setPosition(player1, 0);
        gameState.addCrew(player1, 2, 3);
        gameState.addCrew(player2, 2, 3);
        assertEquals(player2, gameState.getTurnPlayer());
        gameState.pickNextCard(player2);
        assertEquals(State.CARD_SOLVING, gameState.getGameState());
    }

    @Test
    void testPickNextCardNotLeader() {
        gameState.pickHidden(player1);
        gameState.assembleComponent(player1, 0, 2);
        gameState.pickHidden(player2);
        gameState.assembleComponent(player2, 0, 2);
        gameState.setPosition(player2, 1);
        gameState.setPosition(player1, 0);
        assertThrows(InvalidActionException.class, () -> gameState.pickNextCard(player1));
    }

    @Test
    void testUpdatePlayerCredits() {
        gameState.updatePlayerCredits(player1, 5);
        assertEquals(5, gameState.getPlayersPlay().get(player1).getCredits());
    }

    @Test
    void testGetCrewCount() {
        assertEquals(0, gameState.getCrewCount(player1));
    }

    @Test
    void testGetCrewMinPlayer() {
        Component cabin = new Cabin(1000, new ArrayList<>(Arrays.asList(Connector.UNIVERSAL, Connector.UNIVERSAL, Connector.UNIVERSAL, Connector.UNIVERSAL)));
        gameState.assembleComponent(player1, cabin, 2, 2);
        gameState.setPosition(player1, 0);
        assertEquals(State.SHIP_BUILDING, gameState.getGameState());
        gameState.setPosition(player2, 1);
        assertEquals(State.SHIP_CONTROL, gameState.getGameState());
        gameState.addCrew(player1, 2, 3);
        gameState.addCrew(player1, 2, 2);
        gameState.addCrew(player2, 2, 3);
        assertEquals(State.CARD_PICKING, gameState.getGameState());
        assertEquals(4, gameState.getCrewCount(player1));
        assertEquals(2, gameState.getCrewCount(player2));
        assertEquals(player2, gameState.getCrewMinPlayer());
    }

    @Test
    void testRemoveCrewMembers() {
        List<Integer> x = new ArrayList<>(Arrays.asList(2));
        List<Integer> y = new ArrayList<>(Arrays.asList(3));
        List<Integer> eachCabinCrew = new ArrayList<>(Arrays.asList(2));
        gameState.setPosition(player1, 0);
        gameState.setPosition(player2, 1);
        gameState.addCrew(player1, 2, 3);
        gameState.removeCrewMembers(player1, x, y, eachCabinCrew, 2);
        assertEquals(0, gameState.getCrewCount(player1));
    }

    @Test
    void testAddPlayerDuplicateNickname() {
        GameState gs = new GameState(true, 2);
        player1 = "truck3r";
        gs.addPlayer(cl1, player1, Color.RED);
        assertThrows(UniqueNicknameException.class, () -> gs.addPlayer(cl1, player1, Color.GREEN));
    }

    @Test
    void testAddPlayerDuplicateColor() {
        GameState gs = new GameState(true, 2);
        player1 = "truck3r";
        gs.addPlayer(cl1, player1, Color.RED);
        assertThrows(UniquePlayerColorException.class, () -> gs.addPlayer(cl2,"newPlayer", Color.RED));
    }

    @Test
    void testSetPositionInvalidCell() {
        assertThrows(InvalidPositionException.class, () -> gameState.setPosition(player1, 100));
    }

    @Test
    void testSetPositionDuplicateCell() {
        gameState.setPosition(player1, 1);
        assertThrows(InvalidPositionException.class, () -> gameState.setPosition(player2, 1));
    }

    @Test
    void testPickShownInvalidAction() {
        gameState.setPosition(player1, 0);
        gameState.setPosition(player2, 6);
        assertThrows(InvalidActionException.class, () -> gameState.pickShown(player1, 0));
    }

    @Test
    void testAssembleComponentInvalidAction() {
        gameState.setPosition(player1, 0);
        gameState.setPosition(player2, 6);
        assertThrows(InvalidActionException.class, () -> gameState.assembleComponent(player1, 0, 0));
    }
}