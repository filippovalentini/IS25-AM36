package it.polimi.ingsw.galaxytrucker.model.eventCardClasses;

import it.polimi.ingsw.galaxytrucker.model.enumerations.Color;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PlanetsTest {
    private Planets planets;

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
    void testPlanetLanding(){

    }

    @Test
    void testShouldNotLandingIfPlanetNull(){

    }
}