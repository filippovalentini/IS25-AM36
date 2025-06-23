package it.polimi.ingsw.galaxytrucker.model.eventCardClasses;

import it.polimi.ingsw.galaxytrucker.model.enumerations.Color;
import it.polimi.ingsw.galaxytrucker.model.enumerations.State;
import it.polimi.ingsw.galaxytrucker.model.exceptions.InvalidActionException;
import it.polimi.ingsw.galaxytrucker.model.exceptions.NoCrewException;
import it.polimi.ingsw.galaxytrucker.model.gameClasses.GameState;
import it.polimi.ingsw.galaxytrucker.network.rmi.client.ClientRMI;
import it.polimi.ingsw.galaxytrucker.network.rmi.server.VirtualViewRMI;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AbandonedShipTest {
    private AbandonedShip abandonedShipMoreCrew;
    private AbandonedShip abandonedShipLessCrew;
    private String player1;
    private String player2;
    private VirtualViewRMI cl1;
    private VirtualViewRMI cl2;
    private GameState gameState;

    @BeforeEach
    void init(){
        gameState = new GameState(false,2);
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
        gameState.setPosition(player1, 6);
        gameState.setPosition(player2, 3);
        gameState.addCrew(player1, 2, 3);
        gameState.addCrew(player2, 2, 3);
        gameState.checkShipBoards();

        abandonedShipMoreCrew = new AbandonedShip(3, 3, 1, 0);
        abandonedShipLessCrew = new AbandonedShip(1, 3, 1, 0);
    }

    @Test
    void testUseShip(){
        gameState.pickGivenCard(abandonedShipLessCrew);
        abandonedShipMoreCrew.setUsed();
        assertTrue(abandonedShipMoreCrew.isUsed());
    }

    @Test
    void testShouldNotUseShipAlreadyUsed(){
        gameState.pickGivenCard(abandonedShipMoreCrew);
        abandonedShipMoreCrew.setUsed();
        assertThrows(InvalidActionException.class, () -> abandonedShipMoreCrew.setUsed());
    }

    @Test
    void testLandingWithRequiredCrew(){
        gameState.pickGivenCard(abandonedShipLessCrew);
        List<Integer> xCabin = new ArrayList<>();
        List<Integer> yCabin = new ArrayList<>();
        List<Integer> eachCabinCrew = new ArrayList<>();
        xCabin.add(2);
        yCabin.add(3);
        eachCabinCrew.add(1);
        abandonedShipLessCrew.landing(gameState, player1,xCabin,yCabin,eachCabinCrew);
        assertTrue(abandonedShipLessCrew.isUsed());
    }

    @Test
    void testShouldNotLandingCardAlreadyUsed(){
        gameState.pickGivenCard(abandonedShipLessCrew);
        List<Integer> xCabin = new ArrayList<>();
        List<Integer> yCabin = new ArrayList<>();
        List<Integer> eachCabinCrew = new ArrayList<>();
        xCabin.add(2);
        yCabin.add(3);
        eachCabinCrew.add(2);
        abandonedShipLessCrew.setUsed();
        assertThrows(InvalidActionException.class, () -> abandonedShipLessCrew.landing(gameState, player1, xCabin, yCabin, eachCabinCrew));
    }

    @Test
    void testShouldNotLandingInvalidRequiredCrew(){ //not enough crew, player can not land in the station
        gameState.pickGivenCard(abandonedShipMoreCrew);
        List<Integer> xCabin = new ArrayList<>();
        List<Integer> yCabin = new ArrayList<>();
        List<Integer> eachCabinCrew = new ArrayList<>();
        xCabin.add(2);
        yCabin.add(3);
        eachCabinCrew.add(2);
        assertThrows(NoCrewException.class,() -> abandonedShipMoreCrew.landing(gameState, player1,xCabin,yCabin,eachCabinCrew));
    }

    @Test
    void testSkipFirstPlayer(){
        gameState.pickGivenCard(abandonedShipLessCrew);
        abandonedShipLessCrew.skip(gameState, player1);
        assertEquals(player2, gameState.getTurnPlayer()); //next player should be in turn
    }

    @Test
    void testSkipLastPlayer(){
        gameState.pickGivenCard(abandonedShipLessCrew);
        abandonedShipLessCrew.skip(gameState, player1);
        abandonedShipLessCrew.skip(gameState, player2);
        assertEquals(State.CARD_PICKING, gameState.getGameState()); //the phase should be CARD_PICKING
        assertEquals(player1, gameState.getTurnPlayer()); //player one should be in turn
    }
}