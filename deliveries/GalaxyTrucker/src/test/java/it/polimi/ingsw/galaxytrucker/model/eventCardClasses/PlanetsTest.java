package it.polimi.ingsw.galaxytrucker.model.eventCardClasses;

import it.polimi.ingsw.galaxytrucker.model.enumerations.Color;
import it.polimi.ingsw.galaxytrucker.model.exceptions.InvalidActionException;
import it.polimi.ingsw.galaxytrucker.model.gameClasses.GameState;
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

    @BeforeEach
    void init(){
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
}