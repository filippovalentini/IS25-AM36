package it.polimi.ingsw.galaxytrucker.model.eventCardClasses;

import it.polimi.ingsw.galaxytrucker.model.enumerations.Color;
import it.polimi.ingsw.galaxytrucker.model.enumerations.State;
import it.polimi.ingsw.galaxytrucker.model.exceptions.InvalidActionException;
import it.polimi.ingsw.galaxytrucker.model.exceptions.NoCrewException;
import it.polimi.ingsw.galaxytrucker.model.gameClasses.GameState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AbandonedShipTest {
    private AbandonedShip abandonedShip;
    private AbandonedShip abandonedShip1;
    private String player1;
    private String player2;
    private GameState gameState;

    @BeforeEach
    void init(){
        gameState = new GameState(false,2);
        player1 = "truck3r";
        player2 = "4lien";
        gameState.addPlayer(player1, Color.RED);
        gameState.addPlayer(player2, Color.BLUE);
        gameState.setPosition(player1, 6);
        gameState.setPosition(player2, 3);
        gameState.setGameState(State.CARD_PICKING); //end of assembling phase
        abandonedShip = new AbandonedShip(3, 3, 1, 0);
        abandonedShip1 = new AbandonedShip(1, 3, 1, 0);
    }

    @Test
    void testUseShip(){
        abandonedShip.setUsed();
        assertTrue(abandonedShip.isUsed());
    }

    @Test
    void testShouldNotUseShipAlreadyUsed(){
        abandonedShip.setUsed();
        assertThrows(InvalidActionException.class, () -> abandonedShip.setUsed());
    }

    @Test
    void testLandingWithRequiredCrew(){
        List<Integer> xCabin = new ArrayList<>();
        List<Integer> yCabin = new ArrayList<>();
        List<Integer> eachCabinCrew = new ArrayList<>();
        xCabin.add(2);
        yCabin.add(3);
        eachCabinCrew.add(1);
        abandonedShip1.landing(gameState, player1,xCabin,yCabin,eachCabinCrew);
        assertTrue(abandonedShip1.isUsed());
    }

    @Test
    void testShouldNotLandingInvalidRequiredCrew(){
        List<Integer> xCabin = new ArrayList<>();
        List<Integer> yCabin = new ArrayList<>();
        List<Integer> eachCabinCrew = new ArrayList<>();
        xCabin.add(2);
        yCabin.add(3);
        eachCabinCrew.add(2);
        assertThrows(NoCrewException.class,() -> abandonedShip.landing(gameState, player1,xCabin,yCabin,eachCabinCrew));
    }

}