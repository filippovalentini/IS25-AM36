package it.polimi.ingsw.galaxytrucker.model.eventCardClasses;

import it.polimi.ingsw.galaxytrucker.model.componentClasses.CargoHold;
import it.polimi.ingsw.galaxytrucker.model.componentClasses.Component;
import it.polimi.ingsw.galaxytrucker.model.enumerations.Color;
import it.polimi.ingsw.galaxytrucker.model.enumerations.State;
import it.polimi.ingsw.galaxytrucker.model.exceptions.InvalidActionException;
import it.polimi.ingsw.galaxytrucker.model.gameClasses.GameState;
import it.polimi.ingsw.galaxytrucker.network.rmi.client.ClientRMI;
import it.polimi.ingsw.galaxytrucker.network.rmi.server.VirtualViewRMI;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PlanetsTest {
    private Planets planets;
    private GameState gameState;
    String nickname;
    String nickname2;
    private VirtualViewRMI cl1;
    private VirtualViewRMI cl2;
    int row;
    int column;
    List<Color> planetOneGoods;
    List<Color> planetTwoGoods;
    List<List<Color>> allPlanetsGoods;

    @BeforeEach
    void init() throws RemoteException {
        gameState = new GameState(false, 2);
        nickname="filippo";
        nickname2="thomas";
        cl1 = new ClientRMI(nickname, Color.RED);
        cl2 = new ClientRMI(nickname2, Color.BLUE);
        gameState.addPlayer(cl1,nickname,Color.RED);
        gameState.addPlayer(cl2,nickname2,Color.BLUE);
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
        int h = 0;
        for (int i = 0; i < 151; i++) { //show all components
            gameState.pickHidden(nickname);
            gameState.putShown(nickname);
        }
        List<Component> shownComponents = gameState.getShownComponent();
        int count =0;
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 7; j++) {
                count=0;
                if(gameState.getShownComponent().get(h).getImageID()>=510 && gameState.getShownComponent().get(h).getImageID()<=515){
                    for(Color col: planetOneGoods){
                        if(count>=3){
                            break;
                        }
                        gameState.getShownComponent().get(h).addGood(col);
                        count++;
                        row=i;
                        column=j;
                    }
                }
                gameState.assembleComponent(nickname, gameState.getShownComponent().get(h), i, j);
                h += 1;
            }
        }
        gameState.setGameState(State.CARD_SOLVING);
    }


    @Test
    void testPlanetLanding() {
       int numberPlanet = 0;
        assertDoesNotThrow(() -> planets.planetLanding(gameState, nickname, numberPlanet));

    }

    @Test
    void testShouldNotLandingIfPlanetNull() {
        int numberPlanet = 0;
        planets.planetLanding(gameState, nickname, numberPlanet);
        assertThrows(InvalidActionException.class, () -> planets.planetLanding(gameState, nickname2, numberPlanet));
    }
   @Test
    void testSwitchGoods(){
        int numberPlanet = 0;
        planets.planetLanding(gameState, nickname, 1);
        planets.switchGoods(gameState, nickname,row,column,Color.YELLOW,0 );
        //assertEquals(CargoHold.class,gameState.getPlayersPlay().get(nickname).getShipBoard().getAssembledComponent(row,column).getClass());
        assertEquals(Color.YELLOW,gameState.getPlayersPlay().get(nickname).getShipBoard().getAssembledComponent(row,column).getGoods().get(0));
    }
}