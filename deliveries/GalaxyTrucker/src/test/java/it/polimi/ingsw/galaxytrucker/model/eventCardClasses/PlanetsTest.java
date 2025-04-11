package it.polimi.ingsw.galaxytrucker.model.eventCardClasses;

import it.polimi.ingsw.galaxytrucker.model.componentClasses.Component;
import it.polimi.ingsw.galaxytrucker.model.enumerations.Color;
import it.polimi.ingsw.galaxytrucker.model.enumerations.State;
import it.polimi.ingsw.galaxytrucker.model.exceptions.InvalidActionException;
import it.polimi.ingsw.galaxytrucker.model.gameClasses.GameState;
import it.polimi.ingsw.galaxytrucker.network.rmi.client.ClientRMI;
import it.polimi.ingsw.galaxytrucker.network.rmi.server.VirtualViewRMI;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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

    @BeforeEach
    void init(){
        gameState = new GameState(false, 2);
        nickname="filippo";
        nickname2="thomas";
        try{
            cl1 = new ClientRMI();
            cl2 = new ClientRMI();
        }
        catch (Exception e){
            System.exit(-1);
        }
        gameState.addPlayer(cl1,nickname,Color.RED);
        gameState.addPlayer(cl2,nickname2,Color.BLUE);
        List<Color> planetOneGoods = new ArrayList<>();
        planetOneGoods.add(Color.YELLOW);
        planetOneGoods.add(Color.GREEN);
        planetOneGoods.add(Color.BLUE);
        planetOneGoods.add(Color.BLUE);
        List<Color> planetTwoGoods = new ArrayList<>();
        planetTwoGoods.add(Color.YELLOW);
        planetTwoGoods.add(Color.YELLOW);
        List<List<Color>> allPlanetsGoods = new ArrayList<>();
        allPlanetsGoods.add(planetOneGoods);
        allPlanetsGoods.add(planetTwoGoods);
        planets = new Planets(allPlanetsGoods, 3, 0);
        int h = 0;
        for (int i = 0; i < 151; i++) { //show all components
            gameState.pickHidden(nickname);
            gameState.putShown(nickname);
        }
        List<Component> shownComponents = gameState.getShownComponent();

        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 7; j++) {
                gameState.assembleComponent(nickname, gameState.getShownComponent().get(h), i, j);
                h += 1;
                if(gameState.getShownComponent().get(h).getImageID()>=501 && gameState.getShownComponent().get(h).getImageID()<=609){
                    for(Color col: planetOneGoods){
                        row=i;
                        column=j;
                        gameState.getShownComponent().get(h).addGood(col);
                    }
                }
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
        assertThrows(InvalidActionException.class, () -> planets.planetLanding(gameState, nickname, numberPlanet));
    }
   @Test
    void testSwitchGoods(){
     planets.switchGoods(gameState, nickname,row,column,Color.RED,1 );
    assertEquals(Color.RED,gameState.getPlayersPlay().get(nickname).getShipBoard().getAssembledComponent(row,column).getGoods().get(1));
    }

}