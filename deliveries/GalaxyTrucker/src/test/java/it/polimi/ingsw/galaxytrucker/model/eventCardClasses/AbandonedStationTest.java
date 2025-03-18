package it.polimi.ingsw.galaxytrucker.model.eventCardClasses;

import it.polimi.ingsw.galaxytrucker.model.enumerations.Color;
import it.polimi.ingsw.galaxytrucker.model.exceptions.InvalidActionException;
import it.polimi.ingsw.galaxytrucker.model.gameClasses.GameState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AbandonedStationTest {
    private String player1;
    private String player2;
    private GameState gameState;
    private AbandonedStation abandonedStation;

    @BeforeEach
    void init() {
        gameState = new GameState(false, 2);
        player1 = "player1";
        player2 = "player2";
        gameState.addPlayer(player1, Color.RED);
        gameState.addPlayer(player2, Color.BLUE);
        List<Color> stationGoods = new ArrayList<Color>();
        stationGoods.add(Color.YELLOW);
        stationGoods.add(Color.GREEN);
        abandonedStation = new AbandonedStation(stationGoods, 5, 1, 0);
    }

    @Test
    void testUseStation() {
        abandonedStation.setUsed();
        assertTrue(abandonedStation.isUsed());
    }

    @Test
    void testShouldNotUseStationAlreadyUsed() {
        abandonedStation.setUsed();
        assertThrows(InvalidActionException.class, () -> abandonedStation.setUsed());
    }

    @Test
    void testLandingWithRequiredCrew() {
        abandonedStation.landing(gameState, player1);
        //
    }

    @Test
    void testShouldNotLandingInvalidRequiredCrew() {
        abandonedStation.landing(gameState, player2);
        //
    }

}