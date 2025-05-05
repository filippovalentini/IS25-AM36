package it.polimi.ingsw.galaxytrucker.model.eventCardClasses;

import it.polimi.ingsw.galaxytrucker.model.enumerations.State;
import it.polimi.ingsw.galaxytrucker.model.exceptions.InvalidActionException;
import it.polimi.ingsw.galaxytrucker.model.exceptions.NoCrewException;
import it.polimi.ingsw.galaxytrucker.model.gameClasses.GameState;

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
    public boolean isUsed() {       //determines if the card has been used or not
        return used;
    }
    //invoked when a player decides to use the card
    public void setUsed() throws InvalidActionException{
        if(this.used) throw new InvalidActionException("Ship already used");
        this.used = true;
    }

    @Override
    //the player decides which crew members to remove from the ship and lands in the station
    public void landing(GameState gameState, String nickname, List<Integer> x, List<Integer> y, List<Integer> z) throws InvalidActionException, NoCrewException {
        if (this.used) {
            throw new InvalidActionException("Already used this card.");
        }
        if (gameState.getCrewCount(nickname)< this.requiredCrew) {
            throw new NoCrewException("You do not have enough crew members");
        }
        gameState.removeCrewMembers(nickname, x, y, z, this.requiredCrew);
        gameState.updatePlayerCredits(nickname, this.gainedCredits);
        gameState.changePlayerPosition(nickname, -this.lostDays);
        this.used = true;
        gameState.setGameState(State.CARD_PICKING);
        gameState.updateTurns();
    }

    @Override
    //invoked when a player doesn't want to land on the station
    public void skip(GameState gameState, String nickname) {
        if(gameState.isLastInTurn(nickname)) {
            gameState.setGameState(State.CARD_PICKING);
        }
        gameState.nextTurn();
    }
}