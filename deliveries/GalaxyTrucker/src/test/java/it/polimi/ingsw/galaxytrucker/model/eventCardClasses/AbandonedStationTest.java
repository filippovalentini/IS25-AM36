package it.polimi.ingsw.galaxytrucker.model.eventCardClasses;

import it.polimi.ingsw.galaxytrucker.model.componentClasses.CargoHold;
import it.polimi.ingsw.galaxytrucker.model.componentClasses.Component;
import it.polimi.ingsw.galaxytrucker.model.enumerations.Color;
import it.polimi.ingsw.galaxytrucker.model.enumerations.Connector;
import it.polimi.ingsw.galaxytrucker.model.enumerations.State;
import it.polimi.ingsw.galaxytrucker.model.exceptions.InvalidActionException;
import it.polimi.ingsw.galaxytrucker.model.exceptions.NoCrewException;
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
    List<Integer> x_cargo;
    List<Integer> y_cargo;
    List<Integer> goodsPosCargo;

    @BeforeEach
    void init() {
        gameState = new GameState(false, 2);
        player1 = "player1";
        player2 = "player2";
        gameState.addPlayer(player1, Color.RED);
        gameState.addPlayer(player2, Color.BLUE);
        gameState.setPosition(player1, 6);
        gameState.setPosition(player2, 3);
        gameState.setGameState(State.SHIP_BUILDING);
        for(int i=0; i<151; i++){ //show all components
            gameState.pickHidden(player1);
            gameState.putShown(player1);
        }
        List<Component> shownComponents = gameState.getShownComponent();
        for(int i=0; i<shownComponents.size(); i++) { //assemble the cargo left to the initial cabin
            if (shownComponents.get(i).getClass() == CargoHold.class && shownComponents.get(i).getEastSide()!= Connector.SMOOTH) {
                gameState.pickShown(player1, i);
                gameState.assembleComponent(player1, 2, 2);
                i=100000;
            }
        }
        gameState.setGameState(State.CARD_PICKING); //end of assembling phase
        List<Color> stationGoods = new ArrayList<Color>();
        stationGoods.add(Color.YELLOW);
        stationGoods.add(Color.GREEN);
        abandonedStation = new AbandonedStation(stationGoods, 5, 1, 0);
        x_cargo = new ArrayList<>();
        y_cargo = new ArrayList<>();
        goodsPosCargo = new ArrayList<>();
        x_cargo.add(2);
        y_cargo.add(2);
        goodsPosCargo.add(0);
        x_cargo.add(2);
        y_cargo.add(2);
        goodsPosCargo.add(1);
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
        List<Color> sG = new ArrayList<Color>();
        sG.add(Color.YELLOW);
        sG.add(Color.GREEN);
        AbandonedStation abandonedStationFreeCrew = new AbandonedStation(sG, 0, 1, 0);
        assertTrue(gameState.getPlayerCrewCount(player1)>0);
        abandonedStationFreeCrew.landing(gameState, player1, x_cargo, y_cargo, goodsPosCargo);
        assertTrue(abandonedStationFreeCrew.isUsed());
    }

    @Test
    void testShouldNotLandingInvalidRequiredCrew() {
        assertThrows(InvalidActionException.class, () -> abandonedStation.landing(gameState, player1, x_cargo, y_cargo, goodsPosCargo));
    }

}