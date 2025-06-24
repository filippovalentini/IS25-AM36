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

/**
 * Planets class represents an event card that allows players to land on planets
 */
public class Planets extends SkipCard{
    private List<List<Color>> planetGoods; //each element of the main list represents the goods
    //that can be gained by landing on the corresponding planet
    //(each type of good corresponds to a color)
    private int landedPlanet;   //index of the planet on which the current player has landed
    private boolean goodsExchangePhase;

    /**
     * Constructor for the Planets class
     * @param planetGoods
     * @param lostDays
     * @param imageID
     */
    public Planets(List<List<Color>> planetGoods, int lostDays, int imageID) {     //constructor
        super(lostDays, imageID);
        this.planetGoods = planetGoods;
        this.landedPlanet = 0;
        this.goodsExchangePhase = false;

    }

    @Override
    /**
     * If a player leaves the game during the goods exchange phase, the card resolution switches to the planet landing phase for the next player in turn
     * @param gameState the current game state
     *@param nickname the nickname of the player who left the game
     */
    public void manageGameQuit(GameState gameState, String nickname){
        if(nickname.equals(gameState.getTurnPlayer())){ // if the player who left the game is the current player
            if(goodsExchangePhase){ // if the goods exchange phase is active, it is set to false
                goodsExchangePhase = false;
            }
            if(gameState.isLastInTurn(nickname)){ // if the player who left the game was the last in turn, the game state is set to CARD_PICKING
                gameState.setGameState(State.CARD_PICKING);
            }
        }
    }

    @Override
    /**
     * Method invoked by a player that wants to land on a specific planet
     * @param gameState the current game state
     * @param nickname the nickname of the player who wants to land on a planet
     * @param numberPlanet the index of the planet on which the player wants to land
     * @throws InvalidActionException if the player tries to land on a planet that has already been landed on
     */
    public void planetLanding(GameState gameState, String nickname, int numberPlanet) throws InvalidActionException {      //when a player lands on a planet, the corresponding element
        //is set to null, as the other players cannot land on it
        //and gain the goods
        if (goodsExchangePhase) {
            throw new InvalidActionException("Invalid action");
        }
        if(planetGoods.get(numberPlanet)==null){ //if the planet has already been landed on
            throw new InvalidActionException("A player has already landed on planet number "+ (numberPlanet+1)); // planets are indexed starting from 0
        }
        gameState.changePlayerPosition(nickname, -this.lostDays); //the player loses flight days for landing on a planet
        landedPlanet = numberPlanet;
        goodsExchangePhase = true;
    }

    @Override
    /**
     * Method invoked to load in specific cargo holds the goods of the planet on which the player has landed
     * @param gameState the current game state
     * @param nickname the nickname of the player who wants to load goods
     * @param x the list of indices of the cargo holds where the player wants to load the goods
     * @param y the list of indices of the cargo holds where the player wants to load the goods
     * @throws InvalidActionException if the player tries to load goods when the goods exchange phase is not active
     */
    public void loadGoods(GameState gameState, String nickname, List<Integer> x, List<Integer> y) throws InvalidActionException, UnsupportedCargoColorException, FullCargoHoldException, NoGoodsException {
        if (!goodsExchangePhase) { //if the goods exchange phase is not active, the player cannot load goods
            throw new InvalidActionException("Invalid action");
        }
        List<Color> goods = planetGoods.get(landedPlanet); //the goods of the planet on which the player has landed
        if(x.size() != goods.size() || y.size() != goods.size()){ //if the player has not specified the same number of cargo holds as the number of goods
            throw new NoGoodsException("Specify where to put EACH planet good");
        }
        gameState.loadGoods(nickname, x, y, goods); //the player loads the goods in the specified cargo holds
        planetGoods.set(landedPlanet, null); //the goods of the planet on which the player has landed are set to null, as the other players cannot land on it anymore
        goodsExchangePhase = false; //the goods exchange phase is set to false, as the player has loaded the goods
        if(gameState.isLastInTurn(nickname)) { //if the player who has just loaded goods is the last in turn, the game state is set to CARD_PICKING
            gameState.setGameState(State.CARD_PICKING);
        }
        gameState.nextTurn();
    }

    @Override
    /**
     * Method invoked when a player doesn't want to land on a planet
     * @param gameState the current game state
     * @param nickname the nickname of the player who wants to skip the planet landing
     * @throws InvalidActionException if the goods exchange phase is active, as the player cannot skip the planet landing in that case
     */
    public void skip(GameState gameState, String nickname) {
        if (goodsExchangePhase) {
            throw new InvalidActionException("Invalid action");
        }
        super.skip(gameState, nickname);
    }



}
