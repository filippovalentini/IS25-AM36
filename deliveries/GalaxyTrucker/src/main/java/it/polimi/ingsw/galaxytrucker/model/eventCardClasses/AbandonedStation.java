package it.polimi.ingsw.galaxytrucker.model.eventCardClasses;

import it.polimi.ingsw.galaxytrucker.model.enumerations.Color;
import it.polimi.ingsw.galaxytrucker.model.enumerations.State;
import it.polimi.ingsw.galaxytrucker.model.exceptions.*;
import it.polimi.ingsw.galaxytrucker.model.gameClasses.GameState;

import java.util.List;

//ABANDONED STATION
public class AbandonedStation extends SkipCard{
    private final List<Color> stationGoods;     //goods that a player can gain by using the card
    private final int requiredCrew;     //required crew to land in the station
    private boolean used;       //set to true if a player has already used the card

    public AbandonedStation(List<Color> stationGoods, int requiredCrew, int lostDays, int imageID) { //constructor
        super(lostDays, imageID);
        this.stationGoods = stationGoods;
        this.requiredCrew = requiredCrew;
        this.used = false;
    }

    public boolean isUsed() {       //determines if the card has been used or not
        return used;
    }

    public void setUsed() {     //invoked when a player decides to use the card
        if (this.used) {throw new InvalidActionException("Abandoned station already used");}
        used = true;
    }

    @Override
    public void loadGoods(GameState gameState, String nickname, List<Integer> x, List<Integer> y) throws InvalidActionException, UnsupportedCargoColorException, FullCargoHoldException, NoGoodsException {
        if(x.size() != stationGoods.size() || y.size() != stationGoods.size()){
            throw new NoGoodsException("Specify where to put EACH station good");
        }
        if (this.used) {
            throw new InvalidActionException("Already used card");
        }
        if (gameState.getCrewCount(nickname)< this.requiredCrew) {
            throw new InvalidActionException("You don't have enough crew members");
        }
        gameState.loadGoods(nickname, x, y, stationGoods);
        gameState.changePlayerPosition(nickname, -this.lostDays);
        this.used = true;
        gameState.setGameState(State.CARD_PICKING);
        gameState.updateTurns();
    }

}
