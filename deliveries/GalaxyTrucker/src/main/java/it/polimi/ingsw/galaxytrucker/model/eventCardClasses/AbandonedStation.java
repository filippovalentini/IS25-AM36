package it.polimi.ingsw.galaxytrucker.model.eventCardClasses;

import it.polimi.ingsw.galaxytrucker.model.enumerations.Color;
import it.polimi.ingsw.galaxytrucker.model.enumerations.State;
import it.polimi.ingsw.galaxytrucker.model.exceptions.InvalidActionException;
import it.polimi.ingsw.galaxytrucker.model.gameClasses.GameState;

import java.util.List;

//ABANDONED STATION
public class AbandonedStation extends DayLossCard{
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
    //substitute the cargo goods (specified by coordinates of component) of the player with the station goods
    public void landing(GameState gameState, String nickname, List<Integer> x, List<Integer> y, List<Integer> z) throws InvalidActionException {
        if (this.used) {
            throw new InvalidActionException("Already used card");
        }
        if (gameState.getCrewCount(nickname)< this.requiredCrew) {
            throw new InvalidActionException("You don't have enough crew members");
        }
        for(int i=0; i<x.size(); i++){
            gameState.substitutePlayerGood(nickname, x.get(i), y.get(i), stationGoods.get(i), z.get(i));
        }
        gameState.changePlayerPosition(nickname, -this.lostDays);
        this.used = true;
        gameState.setGameState(State.CARD_PICKING);
        gameState.updateTurns();
    }

    @Override
    //invoked when a player doesn't want to land on the station
    public void skip(GameState gameState, String nickname) throws InvalidActionException {
        if(gameState.isLastInTurn(nickname)) {
            gameState.setGameState(State.CARD_PICKING);
        }
        gameState.nextTurn();
    }
}
