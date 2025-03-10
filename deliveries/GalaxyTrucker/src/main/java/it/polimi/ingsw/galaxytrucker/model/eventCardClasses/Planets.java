package it.polimi.ingsw.galaxytrucker.model.eventCardClasses;

import it.polimi.ingsw.galaxytrucker.model.enumerations.Color;
import it.polimi.ingsw.galaxytrucker.model.gameClasses.GameState;

import java.util.List;

//PLANETS
public class Planets extends DayLossCard{
    private List<List<Color>> planetGoods;      //each element of the main list represents the goods
    //that can be gained by landing on the corresponding planet
    //(each type of good corresponds to a color)

    public Planets(List<List<Color>> planetGoods, int lostDays, String imagePath) {     //constructor
        super(lostDays, imagePath);
        this.planetGoods = planetGoods;
    }
    public void landing(int numberPlanet){      //when a player lands on a planet, the corresponding element
        //is set to null, as the other players cannot land on it
        //and gain the goods
        planetGoods.set(numberPlanet, null);
    }
    public List<List<Color>> getPlanetGoods() {     //returns the goods for each planet
        return planetGoods;
    }
    @Override
    public void solve(GameState gameState){}       //implements the effect of the card
}
