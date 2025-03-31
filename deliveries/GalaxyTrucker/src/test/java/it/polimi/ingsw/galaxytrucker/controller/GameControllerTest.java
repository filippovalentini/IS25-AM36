package it.polimi.ingsw.galaxytrucker.controller;

import it.polimi.ingsw.galaxytrucker.model.enumerations.Color;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameControllerTest {
    GameController gcBasic; // WAITING_FOR_PLAYER state
    GameController gcShipAssembling; // SHIP_ASSEMBLING state
    String player1 = "player1";
    String player2 = "player2";

    @BeforeEach
    void init(){
        gcBasic = new GameController(false, 2);
        gcShipAssembling = new GameController(false, 2);
        gcShipAssembling.addPlayer(player1, Color.RED);
        gcShipAssembling.addPlayer(player2, Color.BLUE);
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
        assertEquals(0, gcShipAssembling.pickHidden(player1)); //problem
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

    //start of pickShown(...) test
    @Test
    void testPickShown() {
        gcShipAssembling.pickHidden(player1);
        gcShipAssembling.putShown(player1); //one component is put face up
        assertEquals(0, gcShipAssembling.pickShown(player1,0));
    }

    @Test
    void testPickShownWrongPhase() {
        gcShipAssembling.pickHidden(player1);
        gcShipAssembling.putShown(player1); //one component is put face up
        gcShipAssembling.setPosition(player1, 3);
        gcShipAssembling.setPosition(player2, 6); // CARD_PICKING phase (ships are correct)
        assertEquals(-1, gcShipAssembling.pickShown(player1,0));
    }

    @Test
    void testPickShownAlreadyPicked() {
        gcShipAssembling.pickHidden(player1);
        gcShipAssembling.putShown(player1); //one component is put face up
        gcShipAssembling.pickHidden(player1);
        assertEquals(-2, gcShipAssembling.pickShown(player1,0));
    }

    //start of reserveComponent(...) test
    @Test
    void testReserveComponent() {
        gcShipAssembling.pickHidden(player1);
        assertEquals(0, gcShipAssembling.reserveComponent(player1));
    }

    @Test
    void testReserveComponentWrongPhase() {
        gcShipAssembling.setPosition(player1, 3);
        gcShipAssembling.setPosition(player2, 6); // CARD_PICKING phase (ships are correct)
        assertEquals(-1, gcShipAssembling.reserveComponent(player1));
    }

    @Test
    void testReserveComponentNotPicked() {
        assertEquals(-2, gcShipAssembling.reserveComponent(player1));
    }

    //start of pickReservedComponent(...) test
    @Test
    void testPickReservedComponent() {
        gcShipAssembling.pickHidden(player1);
        gcShipAssembling.reserveComponent(player1);
        assertEquals(0, gcShipAssembling.pickReservedComponent(player1,0));
    }

    @Test
    void testShouldNotPickReservedComponentWrongPhase() {
        gcShipAssembling.setPosition(player1, 3);
        gcShipAssembling.setPosition(player2, 6); // CARD_PICKING phase (ships are correct)
        assertEquals(-1, gcShipAssembling.pickReservedComponent(player1, 0));
    }

    @Test
    void testShouldNotPickReservedComponentAlreadyPicked() {
        gcShipAssembling.pickHidden(player1);
        gcShipAssembling.reserveComponent(player1); // reserve component in position 0
        gcShipAssembling.pickHidden(player1);
        assertEquals(-2,gcShipAssembling.pickReservedComponent(player1,0));
    }

    @Test
    void testShouldNotPickReservedComponentWrongPosition() {
        gcShipAssembling.pickHidden(player1);
        gcShipAssembling.reserveComponent(player1); // reserve component in position 0
        assertEquals(-3,gcShipAssembling.pickReservedComponent(player1,1));
    }

    //start of putShown(...) test
    @Test
    void testPutShown() {
        gcShipAssembling.pickHidden(player1);
        assertEquals(0,gcShipAssembling.putShown(player1));
    }

    @Test
    void testPutShownWrongPhase() {
        gcShipAssembling.setPosition(player1, 3);
        gcShipAssembling.setPosition(player2, 6); // CARD_PICKING phase (ships are correct)
        assertEquals(-1,gcShipAssembling.putShown(player1));
    }

    @Test
    void testPutShownNoPicked() {
        assertEquals(-2,gcShipAssembling.putShown(player1));
    }

    //start of assembleComponent(...) test
    @Test
    void testAssembleComponent() {
        gcShipAssembling.pickHidden(player1);
        assertEquals(0, gcShipAssembling.assembleComponent(player1, 2, 3));
    }

    @Test
    void testAssembleComponentWrongPhase() {
        gcShipAssembling.setPosition(player1, 3);
        gcShipAssembling.setPosition(player2, 6); // CARD_PICKING phase (ships are correct)
        assertEquals(-1,gcShipAssembling.assembleComponent(player1,2,3));
    }

    @Test
    void testAssembleComponentNotPicked() {
        assertEquals(-2,gcShipAssembling.assembleComponent(player1,2,3));
    }

    @Test
    void testAssembleComponentAlreadyAssembled(){
        //
    }

    @Test
    void testAssembleComponentWrongPosition(){
        //
    }

}

