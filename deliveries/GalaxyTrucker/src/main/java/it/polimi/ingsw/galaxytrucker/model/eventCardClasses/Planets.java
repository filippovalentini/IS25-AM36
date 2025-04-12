package it.polimi.ingsw.galaxytrucker.model.eventCardClasses;

import it.polimi.ingsw.galaxytrucker.model.enumerations.Color;
import it.polimi.ingsw.galaxytrucker.model.exceptions.InvalidActionException;
import it.polimi.ingsw.galaxytrucker.model.gameClasses.GameState;

import java.util.ArrayList;
import java.util.List;

//PLANETS
public class Planets extends DayLossCard{
    private List<List<Color>> planetGoods; //each element of the main list represents the goods
    //that can be gained by landing on the corresponding planet
    //(each type of good corresponds to a color)
    private int landedPlanet;

    public Planets(List<List<Color>> planetGoods, int lostDays, int imageID) {     //constructor
        super(lostDays, imageID);
        this.planetGoods = planetGoods;
    }
    @Override
    public void planetLanding(GameState gameState, String nickname, int numberPlanet) throws InvalidActionException {      //when a player lands on a planet, the corresponding element
        //is set to null, as the other players cannot land on it
        //and gain the goods
        landedPlanet = 1000;
        if(planetGoods.get(numberPlanet)==null){
            throw new InvalidActionException("Planet number "+numberPlanet+" is already used");
        }
       // planetGoods.set(numberPlanet, );
        landedPlanet = numberPlanet;
    }

    @Override
    public void switchGoods(GameState gamestate,String nickname,int cargo_row, int cargo_col, Color good, int pos) throws InvalidActionException {
        if(gamestate.getPlayersPlay().get(nickname).getShipBoard().getAssembledComponent(cargo_row,cargo_col).getImageID()>=501 && gamestate.getPlayersPlay().get(nickname).getShipBoard().getAssembledComponent(cargo_row,cargo_col).getImageID()<=609){
            for (Color g : planetGoods.get(landedPlanet)){
                if(g.equals(good)) {
                    gamestate.substituteGoods(nickname, cargo_row, cargo_col, good, pos);
                    break;
                }
            }
        }
    }



}
