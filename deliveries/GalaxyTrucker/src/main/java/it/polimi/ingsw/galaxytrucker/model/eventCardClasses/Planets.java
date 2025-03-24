package it.polimi.ingsw.galaxytrucker.model.eventCardClasses;

import it.polimi.ingsw.galaxytrucker.model.enumerations.Color;
import it.polimi.ingsw.galaxytrucker.model.exceptions.InvalidActionException;
import it.polimi.ingsw.galaxytrucker.model.gameClasses.GameState;

import java.util.List;

//PLANETS
public class Planets extends DayLossCard{
    private List<List<Color>> planetGoods;      //each element of the main list represents the goods
    //that can be gained by landing on the corresponding planet
    //(each type of good corresponds to a color)

    public Planets(List<List<Color>> planetGoods, int lostDays, int imageID) {     //constructor
        super(lostDays, imageID);
        this.planetGoods = planetGoods;
    }
    @Override
    public void planetLanding(GameState gameState, String nickname, int numberPlanet) throws InvalidActionException {      //when a player lands on a planet, the corresponding element
        //is set to null, as the other players cannot land on it
        //and gain the goods
        if(planetGoods.get(numberPlanet)==null){
            throw new InvalidActionException("Planet number "+numberPlanet+" is already used");
        }
        planetGoods.set(numberPlanet, null);
    }

}
