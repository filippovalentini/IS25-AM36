package it.polimi.ingsw.galaxytrucker.model.eventCardClasses;

import it.polimi.ingsw.galaxytrucker.model.enumerations.Color;
import it.polimi.ingsw.galaxytrucker.model.exceptions.*;
import it.polimi.ingsw.galaxytrucker.model.gameClasses.GameState;

import java.util.List;

//EVENT CARD
//this class is used to generalize the concept of adventure/event card used during the game; each subclass of "EventCard"
//represents a specific type of card
public class EventCard  {
    private final int imageID;       //path for the image associated to the adventure card

    public EventCard(int imageID) {        //constructor
        this.imageID = imageID;
    }
    public int getImageID() {
        return imageID;
    }

    //invoked when a player decides to land on a planet in order to gain goods
    public void planetLanding(GameState gameState, String nickname, int numberPlanet) throws InvalidActionException {
        throw new InvalidActionException("Invalid action");
    }
    //invoked when a player's ship has to be hit by a meteor/cannon shot; the player can decide whether to
    //activate a shield or a cannon to defend its ship
    public void hitShip(GameState gameState, String nickname, int diceResult, boolean activateShield, boolean activateCannon) throws InvalidActionException, NoBatteriesException {
        throw new InvalidActionException("Invalid action");
    }
    //invoked when a player decides to land on an abandoned station/ship
    public void landing(GameState gameState, String nickname, List<Integer> x, List<Integer> y, List<Integer> z) throws InvalidActionException, NoCrewException {
        throw new InvalidActionException("Invalid action");
    }
    //invoked when a card is picked from the deck during the solving phase; it has an effect on all the players of
    //the game without requiring user actions
    public void specialEffect(GameState gameState) throws InvalidActionException {
        return;
    }
    //invoked when a player wants to defeat an enemy; the player can decide whether to lose flight days
    //to gain credits/goods or not
    public void defeat(GameState gameState, String nickname, int usedBatteries, boolean loseDays) throws InvalidActionException, NoBatteriesException {
        throw new InvalidActionException("Invalid action");
    }
    //invoked when a player wants to fly across the flight board exploiting its engine strength
    public void fly(GameState gameState, String nickname, int usedBatteries) throws InvalidActionException, NoBatteriesException {
        throw new InvalidActionException("Invalid action");
    }
    //invoked when a player wants to use batteries to have an advantage while solving a card
    public void useBatteries(GameState gameState, String nickname, int usedBatteries) throws InvalidActionException, NoBatteriesException {
        throw new InvalidActionException("Invalid action");
    }
    //invoked when a player doesn't want to exploit the benefits of a card and therefore skips the turn
    public void skip(GameState gameState, String nickname) throws InvalidActionException {
        throw new InvalidActionException("Invalid action");
    }

    public void switchGoods(GameState gamestate, String nickname, int cargo_row, int cargo_col, Color good, int pos)throws InvalidActionException{
        throw new InvalidActionException("Invalid action");
    }

}












