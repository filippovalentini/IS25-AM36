package it.polimi.ingsw.galaxytrucker.model.eventCardClasses;

import it.polimi.ingsw.galaxytrucker.model.exceptions.InvalidActionException;
import it.polimi.ingsw.galaxytrucker.model.gameClasses.GameState;
import it.polimi.ingsw.galaxytrucker.model.gameClasses.Player;

import java.util.List;

//ABANDONED SHIP
public class AbandonedShip extends DayLossCard{
    private final int requiredCrew;      //required crew to land in the ship
    private final int gainedCredits;       //credits that a player can gain by using the card
    private boolean used;       //set to true if a player has already used the card

    public AbandonedShip(int requiredCrew, int gainedCredits, int lostDays, int imageID) {     //constructor
        super(lostDays, imageID);
        this.requiredCrew = requiredCrew;
        this.gainedCredits = gainedCredits;
        this.used = false;
    }
    public int getRequiredCrew() {      //returns the required crew to land in the station
        return requiredCrew;
    }
    public int getGainedCredits() {     //returns the credits that a player can gain by using the card
        return gainedCredits;
    }
    public boolean isUsed() {       //determines if the card has been used or not
        return used;
    }

    public void setUsed() {     //invoked when a player decides to use the card
        used = true;
    }
    //the player decides which crew members to remove from the ship and lands in the station
    public void landing(GameState gameState, String nickname, List<Integer> x, List<Integer> y, List<Integer> eachCabinCrew, int numberCrewToRemove) throws InvalidActionException{
        if (this.used) {
            throw new InvalidActionException("Already used this card.");
        }
        if (gameState.getPlayerCrewCount(nickname)< this.requiredCrew) {
            throw new InvalidActionException("You do not have enough crew member");
        }
        gameState.removedCrewMember(nickname, x, y, eachCabinCrew, numberCrewToRemove);
        gameState.updatePlayerCredits(nickname,this.gainedCredits);
        gameState.changePlayerPosition(nickname,this.getLostDays());
        this.used = true;
    }
}