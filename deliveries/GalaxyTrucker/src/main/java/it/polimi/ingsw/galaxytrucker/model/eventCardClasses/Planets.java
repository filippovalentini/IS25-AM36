package it.polimi.ingsw.galaxytrucker.model.eventCardClasses;

import it.polimi.ingsw.galaxytrucker.model.enumerations.Color;
import it.polimi.ingsw.galaxytrucker.model.enumerations.State;
import it.polimi.ingsw.galaxytrucker.model.exceptions.FullCargoHoldException;
import it.polimi.ingsw.galaxytrucker.model.exceptions.InvalidActionException;
import it.polimi.ingsw.galaxytrucker.model.exceptions.NoGoodsException;
import it.polimi.ingsw.galaxytrucker.model.exceptions.UnsupportedCargoColorException;
import it.polimi.ingsw.galaxytrucker.model.gameClasses.GameState;

import java.util.ArrayList;
import java.util.List;

//PLANETS
public class Planets extends SkipCard{
    private List<List<Color>> planetGoods; //each element of the main list represents the goods
    //that can be gained by landing on the corresponding planet
    //(each type of good corresponds to a color)
    private int landedPlanet;   //index of the planet on which the current player has landed
    private boolean goodsExchangePhase;

    public Planets(List<List<Color>> planetGoods, int lostDays, int imageID) {     //constructor
        super(lostDays, imageID);
        this.planetGoods = planetGoods;
        this.landedPlanet = 0;
        this.goodsExchangePhase = false;

    }

    @Override
    //invoked by a player that wants to land on a specific planet
    public void planetLanding(GameState gameState, String nickname, int numberPlanet) throws InvalidActionException {      //when a player lands on a planet, the corresponding element
        //is set to null, as the other players cannot land on it
        //and gain the goods
        if (goodsExchangePhase) {
            throw new InvalidActionException("Invalid action");
        }
        if(planetGoods.get(numberPlanet)==null){
            throw new InvalidActionException("A player has already landed on planet number "+numberPlanet);
        }
        gameState.changePlayerPosition(nickname, -this.lostDays);
        landedPlanet = numberPlanet;
        goodsExchangePhase = true;
    }

    @Override
    //invoked to load in specific cargo holds the goods of the planet on which the player has landed
    public void loadGoods(GameState gameState, String nickname, List<Integer> x, List<Integer> y) throws InvalidActionException, UnsupportedCargoColorException, FullCargoHoldException, NoGoodsException {
        if (!goodsExchangePhase) {
            throw new InvalidActionException("Invalid action");
        }
        List<Color> goods = planetGoods.get(landedPlanet);
        if(x.size() != goods.size() || y.size() != goods.size()){
            throw new NoGoodsException("Specify where to put EACH planet good");
        }
        gameState.loadGoods(nickname, x, y, goods);
        planetGoods.set(landedPlanet, null);
        goodsExchangePhase = false;
        if(gameState.isLastInTurn(nickname)) {
            gameState.setGameState(State.CARD_PICKING);
        }
        gameState.nextTurn();
    }

    @Override
    //invoked when a player doesn't want to land on a planet
    public void skip(GameState gameState, String nickname) {
        if (goodsExchangePhase) {
            throw new InvalidActionException("Invalid action");
        }
        super.skip(gameState, nickname);
    }



    /*@Override
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
    */


}
