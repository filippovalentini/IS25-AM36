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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class GameStateFirstFlightTest {

    private static GameState gameState;
    private String player1;
    private String player2;
    private ClientRMI cl1;
    private ClientRMI cl2;

    @BeforeEach
    void initFirstFlight() {
        gameState = new GameState(true, 2);
        player1 = "truck3r";
        player2 = "4lien";
        try{
            cl1 = null;
            cl2 = null;
        }
        catch (Exception e){
            System.err.println(e.getMessage());
        }
        gameState.addPlayer(cl1, player1, Color.RED);
        gameState.addPlayer(cl2, player2, Color.BLUE);
    }

    /**
     * Test to verify that players can be added correctly to the game state.
     * Verifies that players get assigned the correct colors and ship boards,
     * that the game state changes appropriately, and that adding too many players
     * throws an InvalidActionException.
     */
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

    /**
     * Test to verify that turn updates work correctly with maximum number of players.
     * Tests that players are ordered by position and that the turn order is updated
     * correctly when positions change.
     */
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

    /**
     * Test to verify that player positions are set correctly on the game board.
     * Verifies that players are assigned to the correct cells.
     */
    @Test
    void testSetPosition() {
        gameState.setPosition(player1, 0);
        gameState.setPosition(player2, 1);
        assertEquals(0, gameState.getPlayersPos().get(player1).getCell());
        assertEquals(1, gameState.getPlayersPos().get(player2).getCell());
    }

    /**
     * Test to verify that player positions can be changed correctly.
     * Tests both positive and negative position changes and ensures
     * positions are properly updated.
     */
    @Test
    void testChangePlayerPosition() {
        gameState.setPosition(player1, 0);
        gameState.setPosition(player2, 1);
        gameState.changePlayerPosition(player1, 3);
        gameState.changePlayerPosition(player2, -1);
        assertEquals(4, gameState.getPlayersPos().get(player1).getCell());
        assertEquals(0, gameState.getPlayersPos().get(player2).getCell());
    }

    /**
     * Test to verify that putting a component back to the shown components pile
     * correctly removes it from the player's picked component slot.
     */
    @Test
    void testPutShown() {
        gameState.pickHidden(player1);
        gameState.putShown(player1);
        assertNull(gameState.getPlayersPlay().get(player1).getShipBoard().getPickedComponent());
    }

    /**
     * Test to verify that picking a component from the hidden pile
     * correctly assigns it to the player's picked component slot.
     */
    @Test
    void testPickHidden() {
        gameState.pickHidden(player1);
        assertNotNull(gameState.getPlayersPlay().get(player1).getShipBoard().getPickedComponent());
    }

    /**
     * Test to verify that picking a component from the shown components pile
     * works correctly after putting a component back.
     */
    @Test
    void testPickShown() {
        gameState.pickHidden(player1);
        gameState.putShown(player1);
        gameState.pickShown(player1, 0);
        assertNotNull(gameState.getPlayersPlay().get(player1).getShipBoard().getPickedComponent());
    }

    /**
     * Test to verify that assembling a component works correctly.
     * Verifies that the component is removed from the picked slot after assembly
     * and that attempting to assemble at an occupied position throws an exception.
     */
    @Test
    void testAssembleComponent() {
        gameState.pickHidden(player1);
        gameState.assembleComponent(player1, 1, 3);
        assertNull(gameState.getPlayersPlay().get(player1).getShipBoard().getPickedComponent());
        gameState.pickHidden(player1);
        assertThrows(AssembledComponentException.class, () -> gameState.assembleComponent(player1, 1, 3));
    }

    /**
     * Test to verify that rotating a picked component changes its orientation
     * and that the component remains in the picked slot after rotation.
     */
    @Test
    void testRotatePickedComponent() {
        gameState.pickHidden(player1);
        Orientation o1 = gameState.getPlayersPlay().get(player1).getShipBoard().getPickedComponent().getOrientation();
        gameState.rotatePickedComponent(player1);
        Orientation o2 = gameState.getPlayersPlay().get(player1).getShipBoard().getPickedComponent().getOrientation();
        assertNotNull(gameState.getPlayersPlay().get(player1).getShipBoard().getPickedComponent());
        assertNotEquals(o1, o2);
    }

    /**
     * Test to verify that destroying a component works correctly and
     * that the game state transitions appropriately through the different phases.
     */
    @Test
    void testDestroyComponent() {
        gameState.pickHidden(player1);
        Component battery = new Battery(true, 1000, new ArrayList<>(Arrays.asList(Connector.SINGLE, Connector.SMOOTH, Connector.SMOOTH, Connector.SMOOTH)));
        gameState.assembleComponent(player1, battery, 2, 2);
        gameState.setPosition(player1, 0);
        assertEquals(State.SHIP_BUILDING, gameState.getGameState());
        gameState.setPosition(player2, 1);
        gameState.addCrew(player1, 2, 3);
        gameState.addCrew(player2, 2, 3);
        gameState.addBatteries(player1, 2, 2);
        assertEquals(State.SHIP_CONTROL, gameState.getGameState());
        gameState.destroyComponent(player1, 2, 2);
        assertEquals(State.CARD_PICKING, gameState.getGameState());
    }

    /**
     * Test to verify that a non-leader player cannot pick the next card.
     * Should throw an InvalidActionException when a non-leader tries to pick.
     */
    @Test
    void testPickNextCardNotLeader() {
        gameState.pickHidden(player1);
        gameState.assembleComponent(player1, 1, 3);
        gameState.pickHidden(player2);
        gameState.assembleComponent(player2, 1, 3);
        gameState.setPosition(player2, 1);
        gameState.setPosition(player1, 0);
        assertThrows(InvalidActionException.class, () -> gameState.pickNextCard(player1));
    }

    /**
     * Test to verify that player credits can be updated correctly.
     * Verifies that the credit amount is properly set for the player.
     */
    @Test
    void testUpdatePlayerCredits() {
        gameState.updatePlayerCredits(player1, 5);
        assertEquals(5, gameState.getPlayersPlay().get(player1).getCredits());
    }

    /**
     * Test to verify that crew count returns zero for a player with no crew.
     */
    @Test
    void testGetCrewCount() {
        assertEquals(0, gameState.getCrewCount(player1));
    }

    /**
     * Test to verify that the player with minimum crew count is correctly identified.
     * Tests crew assignment and verification that the player with fewer crew members
     * is returned as the minimum crew player.
     */
    @Test
    void testGetCrewMinPlayer() {
        Component cabin = new Cabin(1000, new ArrayList<>(Arrays.asList(Connector.UNIVERSAL, Connector.UNIVERSAL, Connector.UNIVERSAL, Connector.UNIVERSAL)));
        gameState.assembleComponent(player1, cabin, 2, 2);
        gameState.setPosition(player1, 0);
        assertEquals(State.SHIP_BUILDING, gameState.getGameState());
        gameState.setPosition(player2, 1);
        gameState.addCrew(player1, 2, 3);
        gameState.addCrew(player1, 2, 2);
        gameState.addCrew(player2, 2, 3);
        assertEquals(4, gameState.getCrewCount(player1));
        assertEquals(2, gameState.getCrewCount(player2));
        assertEquals(player2, gameState.getCrewMinPlayer());
    }

    /**
     * Test to verify that crew members can be removed correctly from specific cabins.
     * Verifies that the crew count decreases appropriately after removal.
     */
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

    /**
     * Test to verify that adding a player with a duplicate nickname
     * throws a UniqueNicknameException.
     */
    @Test
    void testAddPlayerDuplicateNickname() {
        GameState gs = new GameState(true, 2);
        player1 = "truck3r";
        gs.addPlayer(cl1, player1, Color.RED);
        assertThrows(UniqueNicknameException.class, () -> gs.addPlayer(cl1, player1, Color.GREEN));
    }

    /**
     * Test to verify that adding a player with a duplicate color
     * throws a UniquePlayerColorException.
     */
    @Test
    void testAddPlayerDuplicateColor() {
        GameState gs = new GameState(true, 2);
        player1 = "truck3r";
        gs.addPlayer(cl1, player1, Color.RED);
        assertThrows(UniquePlayerColorException.class, () -> gs.addPlayer(cl1,"newPlayer", Color.RED));
    }

    /**
     * Test to verify that setting a player position to an invalid cell
     * throws an InvalidPositionException.
     */
    @Test
    void testSetPositionInvalidCell() {
        assertThrows(InvalidPositionException.class, () -> gameState.setPosition(player1, 100));
    }

    /**
     * Test to verify that setting a player position to an already occupied cell
     * throws an InvalidPositionException.
     */
    @Test
    void testSetPositionDuplicateCell() {
        gameState.setPosition(player1, 1);
        assertThrows(InvalidPositionException.class, () -> gameState.setPosition(player2, 1));
    }

    /**
     * Test to verify that picking a shown component when not allowed
     * throws an InvalidActionException.
     */
    @Test
    void testPickShownInvalidAction() {
        gameState.setPosition(player1, 1);
        gameState.setPosition(player2, 4);
        assertThrows(InvalidActionException.class, () -> gameState.pickShown(player1, 0));
    }

    /**
     * Test to verify that assembling a component when not allowed
     * throws an InvalidActionException.
     */
    @Test
    void testAssembleComponentInvalidAction() {
        gameState.setPosition(player1, 1);
        gameState.setPosition(player2, 4);
        assertThrows(InvalidActionException.class, () -> gameState.assembleComponent(player1, 0, 0));
    }
}