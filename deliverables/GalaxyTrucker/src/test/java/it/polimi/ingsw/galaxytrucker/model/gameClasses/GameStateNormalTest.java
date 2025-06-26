package it.polimi.ingsw.galaxytrucker.model.gameClasses;

import it.polimi.ingsw.galaxytrucker.model.componentClasses.Battery;
import it.polimi.ingsw.galaxytrucker.model.componentClasses.Cabin;
import it.polimi.ingsw.galaxytrucker.model.componentClasses.CargoHold;
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

    /**
     * Initializes game state and players before each test.
     */
    @BeforeEach
    void initLV2Flight() {
        gameState = new GameState(false, 2);
        player1 = "truck3r";
        player2 = "4lien";
        try{
            cl1 = null;
            cl2 = null;
        }
        catch (Exception e){
            System.exit(-1);
        }
        gameState.addPlayer(cl1, player1, Color.RED);
        gameState.addPlayer(cl2, player2, Color.BLUE);
    }

    /**
     * Tests adding players and game state transitions.
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
     * Tests turn updates with multiple players and position changes.
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
        gameState.updateTurns();
        assertEquals(player3, gameState.getTurnPlayer());
    }

    /**
     * Tests setting player positions.
     */
    @Test
    void testSetPosition() {
        gameState.setPosition(player1, 0);
        gameState.setPosition(player2, 1);
        assertEquals(0, gameState.getPlayersPos().get(player1).getCell());
        assertEquals(1, gameState.getPlayersPos().get(player2).getCell());
    }

    /**
     * Tests changing player positions, including wrapping.
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
     * Tests placing a picked component into the shown pile.
     */
    @Test
    void testPutShown() {
        gameState.pickHidden(player1);
        gameState.putShown(player1);
        assertNull(gameState.getPlayersPlay().get(player1).getShipBoard().getPickedComponent());
    }

    /**
     * Tests picking a component from the hidden pile.
     */
    @Test
    void testPickHidden() {
        gameState.pickHidden(player1);
        assertNotNull(gameState.getPlayersPlay().get(player1).getShipBoard().getPickedComponent());
    }

    /**
     * Tests picking a component from the shown pile.
     */
    @Test
    void testPickShown() {
        gameState.pickHidden(player1);
        gameState.putShown(player1);
        gameState.pickShown(player1, 0);
        assertNotNull(gameState.getPlayersPlay().get(player1).getShipBoard().getPickedComponent());
    }

    /**
     * Tests reserving a picked component.
     */
    @Test
    void testReserveComponent() {
        gameState.pickHidden(player1);
        gameState.reserveComponent(player1);
        assertNull(gameState.getPlayersPlay().get(player1).getShipBoard().getPickedComponent());
    }

    /**
     * Tests picking a component from the reserved pile.
     */
    @Test
    void testPickReservedComponent() {
        gameState.pickHidden(player1);
        gameState.reserveComponent(player1);
        gameState.pickReservedComponent(player1, 0);
        assertNotNull(gameState.getPlayersPlay().get(player1).getShipBoard().getPickedComponent());
    }

    /**
     * Tests assembling a component and handling occupied cells.
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
     * Tests rotating a picked component.
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
     * Tests destroying a component and subsequent game state changes.
     */
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

    /**
     * Tests updating player credits.
     */
    @Test
    void testUpdatePlayerCredits() {
        gameState.updatePlayerCredits(player1, 5);
        assertEquals(5, gameState.getPlayersPlay().get(player1).getCredits());
    }

    /**
     * Tests getting a player's crew count.
     */
    @Test
    void testGetCrewCount() {
        assertEquals(0, gameState.getCrewCount(player1));
    }

    /**
     * Tests identifying the player with the minimum crew count.
     */
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

    /**
     * Tests removing crew members from a player's ship.
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
     * Tests adding a player with a duplicate nickname.
     */
    @Test
    void testAddPlayerDuplicateNickname() {
        GameState gs = new GameState(true, 2);
        player1 = "truck3r";
        gs.addPlayer(cl1, player1, Color.RED);
        assertThrows(UniqueNicknameException.class, () -> gs.addPlayer(cl1, player1, Color.GREEN));
    }

    /**
     * Tests adding a player with a duplicate color.
     */
    @Test
    void testAddPlayerDuplicateColor() {
        GameState gs = new GameState(true, 2);
        player1 = "truck3r";
        gs.addPlayer(cl1, player1, Color.RED);
        assertThrows(UniquePlayerColorException.class, () -> gs.addPlayer(cl2,"newPlayer", Color.RED));
    }

    /**
     * Tests setting position to an invalid cell.
     */
    @Test
    void testSetPositionInvalidCell() {
        assertThrows(InvalidPositionException.class, () -> gameState.setPosition(player1, 100));
    }

    /**
     * Tests setting position to an already occupied cell.
     */
    @Test
    void testSetPositionDuplicateCell() {
        gameState.setPosition(player1, 1);
        assertThrows(InvalidPositionException.class, () -> gameState.setPosition(player2, 1));
    }

    /**
     * Tests picking a shown component in an invalid game state.
     */
    @Test
    void testPickShownInvalidAction() {
        gameState.setPosition(player1, 0);
        gameState.setPosition(player2, 6);
        assertThrows(InvalidActionException.class, () -> gameState.pickShown(player1, 0));
    }

    /**
     * Tests assembling a component in an invalid game state.
     */
    @Test
    void testAssembleComponentInvalidAction() {
        gameState.setPosition(player1, 0);
        gameState.setPosition(player2, 6);
        assertThrows(InvalidActionException.class, () -> gameState.assembleComponent(player1, 0, 0));
    }

    /**
     * Tests rewards based on player finishing order.
     */
    @Test
    void testFinishOrderReward() {
        gameState.setPosition(player1, 0);
        gameState.setPosition(player2, 1);
        gameState.updateTurns();
        gameState.finishOrderReward();
        assertEquals(8, gameState.getPlayersPlay().get(player2).getCredits());
        assertEquals(6, gameState.getPlayersPlay().get(player1).getCredits());
    }

    /**
     * Tests reward for the player with the best ship.
     */
    @Test
    void testBestShipReward() {
        Component cabin = new Cabin(-1, new ArrayList<>(Arrays.asList(Connector.UNIVERSAL, Connector.UNIVERSAL, Connector.UNIVERSAL, Connector.UNIVERSAL)));
        gameState.assembleComponent(player1, cabin, 2, 2);
        gameState.setPosition(player1, 0);
        gameState.setPosition(player2, 1);
        gameState.bestShipReward();
        assertEquals(4, gameState.getPlayersPlay().get(player2).getCredits());
    }

    /**
     * Tests penalties for lost components.
     */
    @Test
    void testLossPenalty(){
        Component cabin = new Cabin(-1, new ArrayList<>(Arrays.asList(Connector.UNIVERSAL, Connector.UNIVERSAL, Connector.UNIVERSAL, Connector.UNIVERSAL)));
        gameState.assembleComponent(player1, cabin, 2, 2);
        gameState.setPosition(player1, 0);
        gameState.setPosition(player2, 1);
        gameState.destroyComponent(player1, 2,2);
        gameState.lossPenalty();
        assertEquals(-1, gameState.getPlayersPlay().get(player1).getCredits());
    }

    /**
     * Tests rewards for selling goods.
     */
    @Test
    void testSaleOfGoodsReward(){
        CargoHold cargoHold = new CargoHold(true,-1, new ArrayList<>(Arrays.asList(Connector.UNIVERSAL, Connector.UNIVERSAL, Connector.UNIVERSAL, Connector.UNIVERSAL)));
        gameState.assembleComponent(player1, cargoHold, 2, 2);
        gameState.setPosition(player1, 0);
        gameState.setPosition(player2, 1);
        gameState.addCrew(player1, 2,3);
        gameState.addCrew(player2, 2,3);
        gameState.checkShipBoards();
        gameState.substituteGoods(player1, 2,2,Color.YELLOW,0);
        gameState.substituteGoods(player1, 2,2,Color.BLUE,1);
        gameState.quitGame(player1, false);
        gameState.saleOfGoodsReward();
        assertEquals(2, gameState.getPlayersPlay().get(player1).getCredits());
    }

    /**
     * Tests the computation of all total end-game rewards and penalties.
     */
    @Test
    void testComputeTotalRewards(){
        Component cabinToDestroy = new Cabin(-1, new ArrayList<>(Arrays.asList(Connector.UNIVERSAL, Connector.UNIVERSAL, Connector.UNIVERSAL, Connector.UNIVERSAL)));
        gameState.assembleComponent(player1, cabinToDestroy, 2, 1);
        gameState.setPosition(player1, 1);
        gameState.setPosition(player2, 0);
        gameState.addCrew(player1, 2,3);
        gameState.addCrew(player1, 2,1);
        gameState.addCrew(player2, 2,3);
        gameState.checkShipBoards();
        gameState.destroyComponent(player1, 2,1);
        gameState.checkShipBoards();
        CargoHold cargoHoldExposed = new CargoHold(true, -1, new ArrayList<>(Arrays.asList(Connector.UNIVERSAL, Connector.UNIVERSAL, Connector.UNIVERSAL, Connector.UNIVERSAL)));
        gameState.assembleComponent(player2, cargoHoldExposed, 2, 2);
        gameState.substituteGoods(player2, 2,2,Color.BLUE,0);
        gameState.substituteGoods(player2, 2,2,Color.BLUE,1);
        gameState.computeTotalRewards();
        assertEquals(8-1+4, gameState.getPlayersPlay().get(player1).getCredits());
        assertEquals(6+2, gameState.getPlayersPlay().get(player2).getCredits());
    }
}