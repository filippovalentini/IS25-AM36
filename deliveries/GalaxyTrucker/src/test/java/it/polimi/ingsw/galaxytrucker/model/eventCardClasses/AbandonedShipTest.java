package it.polimi.ingsw.galaxytrucker.model.eventCardClasses;

import it.polimi.ingsw.galaxytrucker.model.enumerations.Color;
import it.polimi.ingsw.galaxytrucker.model.exceptions.InvalidActionException;
import it.polimi.ingsw.galaxytrucker.model.gameClasses.GameState;
import it.polimi.ingsw.galaxytrucker.model.gameClasses.Player;
import it.polimi.ingsw.galaxytrucker.model.gameClasses.ShipBoard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AbandonedShipTest {
    private AbandonedShip abandonedShip;
    private String player1;
    private String player2;
    private GameState gameState;

    @BeforeEach
    void init(){
        GameState gs1 = new GameState(false,2);
        player1 = "truck3r";
        player2 = "4lien";
        gameState.addPlayer(player1, Color.RED);
        gameState.addPlayer(player2, Color.BLUE);
        abandonedShip = new AbandonedShip(2, 3, 1, 0);
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
        xCabin.add(1);
        yCabin.add(4);
        eachCabinCrew.add(2);
        int requiredCrew = 2;
        abandonedShip.landing(gameState, player1,xCabin,yCabin,eachCabinCrew,requiredCrew);
        assertTrue(abandonedShip.isUsed());
    }

    @Test
    void testShouldNotLandingInvalidRequiredCrew(){
        List<Integer> xCabin = new ArrayList<>();
        List<Integer> yCabin = new ArrayList<>();
        List<Integer> eachCabinCrew = new ArrayList<>();
        xCabin.add(1);
        yCabin.add(4);
        eachCabinCrew.add(2);
        int requiredCrew = 3;
        abandonedShip.landing(gameState, player1,xCabin,yCabin,eachCabinCrew,requiredCrew);
        assertTrue(abandonedShip.isUsed());
    }

}