package it.polimi.ingsw.galaxytrucker.model.eventCardClasses;

import it.polimi.ingsw.galaxytrucker.model.exceptions.InvalidActionException;
import it.polimi.ingsw.galaxytrucker.model.gameClasses.GameState;

//EVENT CARD
//this class is used to generalize the concept of adventure/event card used during the game; each subclass of "EventCard"
//represents a specific type of card
public class EventCard  {
    protected final int imageID;       //path for the image associated to the adventure card

    public EventCard(int imageID) {        //constructor
        this.imageID = imageID;
    }
    public int getImageID() {
        return imageID;
    }

    //applies the effect of the card to the players involved in the game; this abstract method is defined in different
    //ways by the different adventure card subclasses, according to their specific characteristics and to the user interactions
    //that they require

    public void planetLanding(GameState gameState, String nickname, int numberPlanet) throws InvalidActionException {
        throw new InvalidActionException("Invalid action");
    }
    public void hitShip(GameState gameState, String nickname, int diceResult, boolean activateShield, boolean activateCannon) throws InvalidActionException {
        throw new InvalidActionException("Invalid action");
    }
    public void landing(GameState gameState, String nickname) throws InvalidActionException {
        throw new InvalidActionException("Invalid action");
    }
    public void specialEffect(GameState gameState) throws InvalidActionException {
        throw new InvalidActionException("Invalid action");
    }
    public void defeat(GameState gameState, String nickname, int usedBatteries, boolean looseDays) throws InvalidActionException {
        throw new InvalidActionException("Invalid action");
    }
    public void fly(GameState gameState, String nickname, int usedBatteries) throws InvalidActionException {
        throw new InvalidActionException("Invalid action");
    }
    public void useBatteries(GameState gameState, String nickname, int usedBatteries) throws InvalidActionException {
        throw new InvalidActionException("Invalid action");
    }
}












