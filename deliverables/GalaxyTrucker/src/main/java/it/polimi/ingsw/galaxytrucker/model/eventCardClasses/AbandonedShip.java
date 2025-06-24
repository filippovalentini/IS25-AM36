package it.polimi.ingsw.galaxytrucker.model.eventCardClasses;

import it.polimi.ingsw.galaxytrucker.model.enumerations.State;
import it.polimi.ingsw.galaxytrucker.model.exceptions.InvalidActionException;
import it.polimi.ingsw.galaxytrucker.model.exceptions.NoCrewException;
import it.polimi.ingsw.galaxytrucker.model.gameClasses.GameState;

import java.util.List;

/**
 * AbandonedShip class represents an event card that allows players to land on an abandoned ship.
 */
public class AbandonedShip extends SkipCard {
    private final int requiredCrew;      //required crew to land in the ship
    private final int gainedCredits;       //credits that a player can gain by using the card
    private boolean used;       //set to true if a player has already used the card

    /**
     * Constructor for AbandonedShip.
     * @param requiredCrew
     * @param gainedCredits
     * @param lostDays
     * @param imageID
     */
    public AbandonedShip(int requiredCrew, int gainedCredits, int lostDays, int imageID) {     //constructor
        super(lostDays, imageID);
        this.requiredCrew = requiredCrew;
        this.gainedCredits = gainedCredits;
        this.used = false;
    }

    /**
     * Determines if the card has been used or not.
     * @return true if the card has been used, false otherwise
     */
    public boolean isUsed() {       //determines if the card has been used or not
        return used;
    }

    /**
     * Sets the card as used.
     * @throws InvalidActionException if the card has already been used
     */
    public void setUsed() throws InvalidActionException{
        if(this.used) throw new InvalidActionException("Ship already used");
        this.used = true;
    }

    /**
     * The player decides which crew members to remove from the ship and lands in the station
     * @param gameState
     * @param nickname
     * @param x
     * @param y
     * @param crewInEachCabin
     * @throws InvalidActionException
     * @throws NoCrewException
     */
    @Override

    public void landing(GameState gameState, String nickname, List<Integer> x, List<Integer> y, List<Integer> crewInEachCabin) throws InvalidActionException, NoCrewException {
        if (this.used) {
            throw new InvalidActionException("Already used this card."); // Check if the card has already been used
        }
        if (gameState.getCrewCount(nickname)< this.requiredCrew) { // Check if the player has enough crew members
            throw new NoCrewException("You do not have enough crew members");
        }
        gameState.removeCrewMembers(nickname, x, y, crewInEachCabin, this.requiredCrew); // Remove the specified crew members from the ship
        gameState.updatePlayerCredits(nickname, this.gainedCredits); // Update the player's credits based on the card's effect
        gameState.changePlayerPosition(nickname, -this.lostDays); // Move the player back by the specified number of days
        this.used = true; // Mark the card as used
        gameState.setGameState(State.CARD_PICKING); // Set the game state to CARD_PICKING after the action is completed
        gameState.updateTurns(); // Update the turns in the game state
    }
}