package it.polimi.ingsw.galaxytrucker.model.eventCardClasses;

import it.polimi.ingsw.galaxytrucker.model.componentClasses.CargoHold;
import it.polimi.ingsw.galaxytrucker.model.componentClasses.CargoSpecial;
import it.polimi.ingsw.galaxytrucker.model.componentClasses.Component;
import it.polimi.ingsw.galaxytrucker.model.enumerations.Color;
import it.polimi.ingsw.galaxytrucker.model.enumerations.Connector;
import it.polimi.ingsw.galaxytrucker.model.enumerations.Orientation;
import it.polimi.ingsw.galaxytrucker.model.enumerations.State;
import it.polimi.ingsw.galaxytrucker.model.exceptions.InvalidActionException;
import it.polimi.ingsw.galaxytrucker.model.exceptions.NoGoodsException;
import it.polimi.ingsw.galaxytrucker.model.gameClasses.GameState;
import it.polimi.ingsw.galaxytrucker.network.rmi.client.ClientRMI;
import it.polimi.ingsw.galaxytrucker.network.rmi.server.VirtualViewRMI;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AbandonedStationTest {
    private String player1;
    private String player2;
    private VirtualViewRMI cl1;
    private VirtualViewRMI cl2;
    private GameState gameState;
    private AbandonedStation abandonedStation;
    private AbandonedStation abandonedStationFreeCrew;
    List<Integer> x_cargo;
    List<Integer> y_cargo;
    List<Integer> goodsPosCargo;

    @BeforeEach
    void init() {
        gameState = new GameState(false, 2);
        player1 = "player1";
        player2 = "player2";
        try{
            cl1 = new ClientRMI(player1, Color.RED);
            cl2 = new ClientRMI(player2, Color.BLUE);
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
        gameState.updateTurns();

        CargoHold cargoHoldUniversal = new CargoHold(true, -1, List.of(Connector.UNIVERSAL, Connector.UNIVERSAL, Connector.UNIVERSAL, Connector.UNIVERSAL));
        gameState.assembleComponent(player1, cargoHoldUniversal, 2, 2);
        gameState.checkShipBoards();

        List<Color> stationGoods = new ArrayList<Color>();
        stationGoods.add(Color.YELLOW);
        stationGoods.add(Color.GREEN);
        abandonedStation = new AbandonedStation(stationGoods, 5, 1, -1);
        //init abandonedStationFreeCrew
        List<Color> sG = new ArrayList<Color>();
        sG.add(Color.YELLOW);
        sG.add(Color.GREEN);
        abandonedStationFreeCrew = new AbandonedStation(sG, 0, 1, -1);
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
        gameState.pickGivenCard(abandonedStation);
        abandonedStation.setUsed();
        assertTrue(abandonedStation.isUsed());
    }

    @Test
    void testShouldNotUseStationAlreadyUsed() {
        gameState.pickGivenCard(abandonedStation);
        abandonedStation.setUsed();
        assertThrows(InvalidActionException.class, () -> abandonedStation.setUsed());
    }

    @Test
    void testLoadGoodsWithRequiredCrew() {
        assertTrue(gameState.getCrewCount(player1)>0);
        gameState.pickGivenCard(abandonedStationFreeCrew);
        abandonedStationFreeCrew.loadGoods(gameState, player1, x_cargo, y_cargo);
        assertTrue(abandonedStationFreeCrew.isUsed());
    }

    @Test
    void testShouldNotLoadGoodsMismatchingGoodsSize(){
        gameState.pickGivenCard(abandonedStationFreeCrew);
        assertThrows(NoGoodsException.class, () -> abandonedStationFreeCrew.loadGoods(gameState, player1, new ArrayList<>(), new ArrayList<>()));
    }

    @Test
    void testShouldNotLoadGoodsCardAlreadyUsed(){
        gameState.pickGivenCard(abandonedStationFreeCrew);
        abandonedStationFreeCrew.setUsed();
        assertThrows(InvalidActionException.class, () -> abandonedStationFreeCrew.loadGoods(gameState, player1, x_cargo, y_cargo));
    }
    @Test
    void testShouldNotLoadGoodsInvalidRequiredCrew() {
        gameState.pickGivenCard(abandonedStationFreeCrew);
        assertThrows(InvalidActionException.class, () -> abandonedStation.loadGoods(gameState, player1, x_cargo, y_cargo));
    }

}