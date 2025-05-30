package it.polimi.ingsw.galaxytrucker.model.eventCardClasses;

import it.polimi.ingsw.galaxytrucker.model.componentClasses.CargoHold;
import it.polimi.ingsw.galaxytrucker.model.componentClasses.Component;
import it.polimi.ingsw.galaxytrucker.model.enumerations.Color;
import it.polimi.ingsw.galaxytrucker.model.enumerations.Connector;
import it.polimi.ingsw.galaxytrucker.model.enumerations.State;
import it.polimi.ingsw.galaxytrucker.model.exceptions.InvalidActionException;
import it.polimi.ingsw.galaxytrucker.model.gameClasses.Deck;
import it.polimi.ingsw.galaxytrucker.model.gameClasses.GameState;
import it.polimi.ingsw.galaxytrucker.network.rmi.client.ClientRMI;
import it.polimi.ingsw.galaxytrucker.network.rmi.server.VirtualViewRMI;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PlanetsTest {
    private Planets planets;
    private GameState gameState;
    String nickname;
    String nickname2;
    private VirtualViewRMI cl1;
    private VirtualViewRMI cl2;
    List<Color> planetOneGoods;
    List<Color> planetTwoGoods;
    List<List<Color>> allPlanetsGoods;

    @BeforeEach
    void init() throws RemoteException {
        gameState = new GameState(false, 2);
        nickname="filippo";
        nickname2="thomas";
        cl1 = null;
        cl2 = null;
        gameState.addPlayer(cl1,nickname,Color.RED);
        gameState.addPlayer(cl2,nickname2,Color.BLUE);

        Component cargo1 = new CargoHold(false, 408, new ArrayList<>(Arrays.asList(Connector.UNIVERSAL, Connector.UNIVERSAL, Connector.UNIVERSAL, Connector.UNIVERSAL)));
        Component cargo2 = new CargoHold(false, 408, new ArrayList<>(Arrays.asList(Connector.UNIVERSAL, Connector.UNIVERSAL, Connector.UNIVERSAL, Connector.UNIVERSAL)));
        Component cargo3 = new CargoHold(false, 408, new ArrayList<>(Arrays.asList(Connector.UNIVERSAL, Connector.UNIVERSAL, Connector.UNIVERSAL, Connector.UNIVERSAL)));
        Component cargo4 = new CargoHold(false, 408, new ArrayList<>(Arrays.asList(Connector.UNIVERSAL, Connector.UNIVERSAL, Connector.UNIVERSAL, Connector.UNIVERSAL)));
        gameState.assembleComponent(nickname, cargo1, 2, 2);
        gameState.assembleComponent(nickname, cargo2, 2, 4);
        gameState.assembleComponent(nickname, cargo3, 1, 3);
        gameState.assembleComponent(nickname, cargo4, 3, 3);
        gameState.setPosition(nickname, 6);
        gameState.setPosition(nickname2, 3);
        gameState.addCrew(nickname, 2, 3);
        gameState.addCrew(nickname2, 2, 3);

        planetOneGoods = new ArrayList<>();
        planetOneGoods.add(Color.YELLOW);
        planetOneGoods.add(Color.GREEN);
        planetOneGoods.add(Color.BLUE);
        planetOneGoods.add(Color.BLUE);
        planetTwoGoods = new ArrayList<>();
        planetTwoGoods.add(Color.YELLOW);
        planetTwoGoods.add(Color.YELLOW);
        allPlanetsGoods = new ArrayList<>();
        allPlanetsGoods.add(planetOneGoods);
        allPlanetsGoods.add(planetTwoGoods);
        planets = new Planets(allPlanetsGoods, 3, 409);

        gameState.setGameDeck(new Deck(new ArrayList<>(Arrays.asList(planets))));
        gameState.pickNextCard(nickname);
    }


    @Test
    void testPlanetLanding() {
       int numberPlanet = 0;
       assertDoesNotThrow(() -> gameState.planetLanding(nickname, numberPlanet));
       assertEquals(3, gameState.getPlayersPos().get(nickname).getCell());
    }

    @Test
    void testShouldNotLandIfPlanetNull() {
        int numberPlanet = 0;
        List<Integer> x = new ArrayList<>(Arrays.asList(0,0,0,0));
        List<Integer> y = new ArrayList<>(Arrays.asList(0,0,0,0));
        gameState.planetLanding(nickname, numberPlanet);
        gameState.loadGoods(nickname, x, y);
        assertThrows(InvalidActionException.class, () -> gameState.planetLanding(nickname2, numberPlanet));
    }
   @Test
    void testSwitchGoods(){
        int numberPlanet = 0;
        List<Integer> x = new ArrayList<>(Arrays.asList(2,2,1,0));
        List<Integer> y = new ArrayList<>(Arrays.asList(2,4,3,0));
        gameState.planetLanding(nickname, numberPlanet);
        gameState.loadGoods(nickname, x, y);
        assertEquals(Color.YELLOW,gameState.getPlayersPlay().get(nickname).getShipBoard().getAssembledComponent(2,2).getGoods().get(0));
        assertEquals(Color.GREEN,gameState.getPlayersPlay().get(nickname).getShipBoard().getAssembledComponent(2,4).getGoods().get(0));
        assertEquals(Color.BLUE,gameState.getPlayersPlay().get(nickname).getShipBoard().getAssembledComponent(1,3).getGoods().get(0));
        assertEquals(0,gameState.getPlayersPlay().get(nickname).getShipBoard().getAssembledComponent(3,3).getGoods().size());

   }
}