package it.polimi.ingsw.galaxytrucker.controller;

import it.polimi.ingsw.galaxytrucker.model.componentClasses.CargoHold;
import it.polimi.ingsw.galaxytrucker.model.componentClasses.Component;
import it.polimi.ingsw.galaxytrucker.model.enumerations.Color;
import it.polimi.ingsw.galaxytrucker.model.enumerations.Connector;
import it.polimi.ingsw.galaxytrucker.model.enumerations.State;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GameControllerTest {
    GameController gcBasic; // WAITING_FOR_PLAYER state
    GameController gcShipAssembling; // SHIP_ASSEMBLING state
    GameController gcFlight; // CARD_PICKING state
    String player1 = "player1";
    String player2 = "player2";

    @BeforeEach
    void init(){
        gcBasic = new GameController(false, 2);
        gcShipAssembling = new GameController(false, 2);
        //gcShipAssembling.addPlayer(player1, Color.RED);
        //gcShipAssembling.addPlayer(player2, Color.BLUE);
        gcFlight = new GameController(false, 2);
        //gcFlight.addPlayer(player1, Color.RED);
        //gcFlight.addPlayer(player2, Color.BLUE);
        gcFlight.setPosition(player1, 3);
        gcFlight.setPosition(player2, 6);
    }

    //start of addPlayer(...) test
    @Test
    void testAddAllPlayers(){
        //assertEquals(0, gcBasic.addPlayer("player1", Color.BLUE));
        //assertEquals(1, gcBasic.addPlayer("player2", Color.RED));
    }

    @Test
    void testShouldNotAddPlayerGameStarted(){
        String player1 = "player1";
        String player2 = "player2";
        //gcBasic.addPlayer(player1, Color.BLUE);
        //gcBasic.addPlayer(player2, Color.RED);
        gcBasic.setPosition(player1, 6);
        gcBasic.setPosition(player2, 3);
        //assertEquals(-1, gcBasic.addPlayer("player3", Color.YELLOW));
    }

    @Test
    void testShouldNotAddPlayerExistingGame(){
        String player1 = "has_same_name";
        String player2 = "has_same_name";
        //gcBasic.addPlayer(player1, Color.BLUE);
        //assertEquals(-2, gcBasic.addPlayer(player2, Color.RED));
    }

    @Test
    void testShouldNotAddPlayerChosenColor(){
        String player1 = "player1";
        String player2 = "player2";
        Color sameColor = Color.GREEN;
        //gcBasic.addPlayer(player1, sameColor);
        //assertEquals(-3, gcBasic.addPlayer(player2, sameColor));
    }

    //start of pickHidden(...) test
    @Test
    void testPickHidden() {
        //assertEquals(0, gcShipAssembling.pickHidden(player1)); //problem
    }

    @Test
    void testShouldNotPickHiddenWrongPhase() {
        String player1 = "player1";
        //gcBasic.addPlayer(player1, Color.BLUE);
        //assertEquals(-1, gcBasic.pickHidden(player1));
    }

    @Test
    void testShouldNotPickHiddenAlreadyPicked() {
        //gcBasic.addPlayer("player1", Color.BLUE);
        //gcBasic.addPlayer("player2", Color.RED);
        gcBasic.pickHidden("player1");
        //assertEquals(-2, gcBasic.pickHidden("player1"));
    }

    //start of pickShown(...) test
    @Test
    void testPickShown() {
        gcShipAssembling.pickHidden(player1);
        gcShipAssembling.putShown(player1); //one component is put face up
        //assertEquals(0, gcShipAssembling.pickShown(player1,0));
    }

    @Test
    void testShouldNotPickShownWrongPhase() {
        gcShipAssembling.pickHidden(player1);
        gcShipAssembling.putShown(player1); //one component is put face up
        gcShipAssembling.setPosition(player1, 3);
        gcShipAssembling.setPosition(player2, 6); // CARD_PICKING phase (ships are correct)
        // assertEquals(-1, gcShipAssembling.pickShown(player1,0));
    }

    @Test
    void testShouldNotPickShownAlreadyPicked() {
        gcShipAssembling.pickHidden(player1);
        gcShipAssembling.putShown(player1); //one component is put face up
        gcShipAssembling.pickHidden(player1);
        //assertEquals(-2, gcShipAssembling.pickShown(player1,0));
    }

    //start of reserveComponent(...) test
    @Test
    void testReserveComponent() {
        gcShipAssembling.pickHidden(player1);
        //assertEquals(0, gcShipAssembling.reserveComponent(player1));
    }

    @Test
    void testShouldNotReserveComponentWrongPhase() {
        gcShipAssembling.setPosition(player1, 3);
        gcShipAssembling.setPosition(player2, 6); // CARD_PICKING phase (ships are correct)
        //assertEquals(-1, gcShipAssembling.reserveComponent(player1));
    }

    @Test
    void testShouldNotReserveComponentNotPicked() {
        //assertEquals(-2, gcShipAssembling.reserveComponent(player1));
    }

    //start of pickReservedComponent(...) test
    @Test
    void testPickReservedComponent() {
        gcShipAssembling.pickHidden(player1);
        gcShipAssembling.reserveComponent(player1);
        //assertEquals(0, gcShipAssembling.pickReservedComponent(player1,0));
    }

    @Test
    void testShouldNotPickReservedComponentWrongPhase() {
        gcShipAssembling.setPosition(player1, 3);
        gcShipAssembling.setPosition(player2, 6); // CARD_PICKING phase (ships are correct)
        //assertEquals(-1, gcShipAssembling.pickReservedComponent(player1, 0));
    }

    @Test
    void testShouldNotPickReservedComponentAlreadyPicked() {
        gcShipAssembling.pickHidden(player1);
        gcShipAssembling.reserveComponent(player1); // reserve component in position 0
        gcShipAssembling.pickHidden(player1);
        //assertEquals(-2,gcShipAssembling.pickReservedComponent(player1,0));
    }

    @Test
    void testShouldNotPickReservedComponentWrongPosition() {
        gcShipAssembling.pickHidden(player1);
        gcShipAssembling.reserveComponent(player1); // reserve component in position 0
        //assertEquals(-3,gcShipAssembling.pickReservedComponent(player1,1));
    }

    //start of putShown(...) test
    @Test
    void testPutShown() {
        gcShipAssembling.pickHidden(player1);
        //assertEquals(0,gcShipAssembling.putShown(player1));
    }

    @Test
    void testShouldNotPutShownWrongPhase() {
        gcShipAssembling.setPosition(player1, 3);
        gcShipAssembling.setPosition(player2, 6); // CARD_PICKING phase (ships are correct)
        //assertEquals(-1,gcShipAssembling.putShown(player1));
    }

    @Test
    void testShouldNotPutShownNoPicked() {
        //assertEquals(-2,gcShipAssembling.putShown(player1));
    }

    //start of assembleComponent(...) test
    @Test
    void testAssembleComponent() {
        gcShipAssembling.pickHidden(player1);
        //assertEquals(0, gcShipAssembling.assembleComponent(player1, 1, 3)); // assemble above the initial cabin
    }

    @Test
    void testShouldNotAssembleComponentWrongPhase() {
        gcShipAssembling.setPosition(player1, 3);
        gcShipAssembling.setPosition(player2, 6); // CARD_PICKING phase (ships are correct)
        //assertEquals(-1,gcShipAssembling.assembleComponent(player1,2,3));
    }

    @Test
    void testShouldNotAssembleComponentNotPicked() {
        //assertEquals(-2,gcShipAssembling.assembleComponent(player1,1,3)); // assemble above initial cabin not existent component
    }

    @Test
    void testShouldNotAssembleComponentAlreadyAssembled(){
        gcShipAssembling.pickHidden(player1);
        gcShipAssembling.assembleComponent(player1, 2, 3);
        gcShipAssembling.pickHidden(player1);
        //assertEquals(-3, gcShipAssembling.assembleComponent(player1, 2, 3)); // assemble at the same position
    }

    //start of rotatePickedComponent(...) test
    @Test
    void testRotatePickedComponent() {
        gcShipAssembling.pickHidden(player1);
        //assertEquals(0, gcShipAssembling.rotatePickedComponent(player1));
    }

    @Test
    void testShouldNotRotatePickedComponentWrongPhase() {
        gcShipAssembling.setPosition(player1, 3);
        gcShipAssembling.setPosition(player2, 6); // CARD_PICKING phase (ships are correct)
        //assertEquals(-1,gcShipAssembling.rotatePickedComponent(player1));
    }

    @Test
    void testShouldNotRotatePickedComponentNotPicked() {
        //assertEquals(-2,gcShipAssembling.rotatePickedComponent(player1));
    }

    //start of setPosition(...) test
    @Test
    void testSetPosition() {
        //assertEquals(0,gcShipAssembling.setPosition(player1, 3));
        //assertEquals(1,gcShipAssembling.setPosition(player2, 6));
    }

    @Test
    void testShouldNotSetPositionWrongPhase() {
        gcShipAssembling.setPosition(player1, 3);
        gcShipAssembling.setPosition(player2, 6); // CARD_PICKING phase (ships are correct)
        //assertEquals(-1,gcShipAssembling.setPosition(player1, 3));
    }

    @Test
    void testShouldNotSetPositionInvalidPosition() {
        //assertEquals(-2,gcShipAssembling.setPosition(player1, 10));
    }

    //start of destroyComponent(...) test
    @Test
    void testDestroyComponent() {
        //the player must add two incorrect components (for example not linked to other components)
        gcShipAssembling.pickHidden(player1);
        gcShipAssembling.assembleComponent(player1, 1, 1);
        gcShipAssembling.pickHidden(player1);
        gcShipAssembling.assembleComponent(player1, 2,0);
        gcShipAssembling.setPosition(player1, 3);
        gcShipAssembling.setPosition(player2, 6); // ship board correctness not implemented yet
        assertEquals(State.SHIP_CONTROL, gcShipAssembling.getModelState());
        //the player must destroy both
        //assertEquals(0, gcShipAssembling.destroyComponent(player1, 1,1));
        //assertEquals(1, gcShipAssembling.destroyComponent(player1, 2,0));
    }

    @Test
    void testShouldNotDestroyComponentWrongPhase() {
        gcShipAssembling.setPosition(player1, 3);
        gcShipAssembling.setPosition(player2, 6); // CARD_PICKING phase (ships are correct)
        //assertEquals(-1,gcShipAssembling.destroyComponent(player1, 2,3));
    }

    @Test
    void testShouldNotDestroyComponentNotAssembled() {
        gcShipAssembling.pickHidden(player1);
        gcShipAssembling.assembleComponent(player1, 1, 1); // wrong component
        gcShipAssembling.setPosition(player1, 3);
        gcShipAssembling.setPosition(player2, 6); // game state should be in SHIP_CONTROL state (ship correctness not implemented yet)
        //assertEquals(-2, gcShipAssembling.destroyComponent(player1, 1,2)); // wrong position
    }

    //start of quitGame(...) test
    @Test
    void testQuitGame() {
        //assertEquals(0,gcFlight.quitGame(player1));
    }

    //start of pickNextCard(...) test
    @Test
    void testPickNextCard() {/*
        assertEquals(State.CARD_PICKING, gcFlight.getModelState());
        int resPickNextCard = gcFlight.pickNextCard(player2);
        System.out.println(gcFlight.getModelCurrentCard().getClass());
        assertEquals(State.CARD_SOLVING, gcFlight.getModelState()); // problem with special event card
        assertEquals(0, resPickNextCard); // problem with planets
    */}

    @Test
    void testPickAllNextCards(){
        /*for(int i=0; i< 40; i++){ // all cards minus one are picked
            assertEquals(0, gcFlight.pickNextCard(player2));
        }
        assertEquals(1, gcFlight.pickNextCard(player2)); //last card is picked
        */
    }

    @Test
    void testShouldNotPickNextCardWrongPhase() {
        //assertEquals(-1,gcShipAssembling.pickNextCard(player2));
    }

    //start of planetLanding(...) test
    @Test
    void testPlanetLanding() {
        gcFlight.pickNextCard(player2); // abandoned station/ship card must be picked

    }

    @Test
    void testShouldNotPlanetLandingWrongPhase() {
        //assertEquals(-1, gcBasic.planetLanding(player1,0));
    }

    @Test
    void testShouldNotPlanetLandingNotEnoughCrew() {
        gcFlight.pickNextCard(player2); // abandoned station/ship card must be picked
    }

    //start of hit(...) test
    @Test
    void testHit() {
        //
    }

    @Test
    void testShouldNotHitWrongPhase() {
        //
    }

    @Test
    void testShouldNotHitNotEnoughBatteries() {
        //
    }

    //start of landing(...) test
    @Test
    void testLanding() {
        //
    }

    @Test
    void testShouldNotLandingWrongPhase() {
        //
    }

    @Test
    void testShouldNotLandingNotEnoughCrew() {
        //
    }

    //start of defeat(...) test
    @Test
    void testDefeat() {
        //
    }

    @Test
    void testShouldNotDefeatWrongPhase() {
        //
    }

    @Test
    void testShouldNotDefeatNotEnoughBatteries() {
        //
    }

    //start of fly(...) test
    @Test
    void testFly() {
        //
    }

    @Test
    void testShouldNotFlyWrongPhase() {
        //
    }

    @Test
    void testShouldNotFlyNotEnoughBatteries() {
        //
    }

    //start of useBatteries(...) test
    @Test
    void testUseBatteries() {
        //
    }

    @Test
    void testShouldNotUseBatteriesWrongPhase(){
        //
    }

    @Test
    void testShouldNotUseBatteriesNotEnough(){
        //
    }

    @Test
    void testSkip(){
        //
    }

    @Test
    void testShouldNotSkipWrongPhase(){
        //
    }
}


