package it.polimi.ingsw.galaxytrucker.model.eventCardClasses;

import it.polimi.ingsw.galaxytrucker.model.enumerations.Color;
import it.polimi.ingsw.galaxytrucker.model.enumerations.State;
import it.polimi.ingsw.galaxytrucker.model.exceptions.*;
import it.polimi.ingsw.galaxytrucker.model.gameClasses.GameState;

import java.util.List;

/**
 * This class is used to generalize the concept of adventure/event card used during the game; each subclass of "EventCard" represents a specific type of card
 */
public class EventCard  {
    private final int imageID;       //path for the image associated to the adventure card

    /**
     * Constructor for the EventCard class
     * @param imageID
     */
    public EventCard(int imageID) {        //constructor
        this.imageID = imageID;
    }

    /**
     * Method that returns the image ID of the card
     * @return imageID
     */
    public int getImageID() {
        return imageID;
    }

    /**
     * Method invoked to manage the disconnection/quit of a player during the resolution of the card
     * @param gameState
     * @param nickname
     */
    public void manageGameQuit(GameState gameState, String nickname){
        if(gameState.isLastInTurn(nickname) && nickname.equals(gameState.getTurnPlayer())){ //if the player that has quit is the last in turn, the game state is set to CARD_PICKING
            gameState.setGameState(State.CARD_PICKING);
        }
    }

    /**
     * Method invoked when a player decides to land on a planet in order to gain goods
     * @param gameState
     * @param nickname
     * @param numberPlanet
     * @throws InvalidActionException
     */
    public void planetLanding(GameState gameState, String nickname, int numberPlanet) throws InvalidActionException {
        throw new InvalidActionException("Invalid action for solving the current card");
    }

    /**
     * Method invoked when a player's ship has to be hit by a meteor/cannon shot; the player can decide whether to activate a shield or a cannon to defend its ship
     * @param gameState
     * @param nickname
     * @param diceResult
     * @param activateShield
     * @param activateCannon
     * @throws InvalidActionException
     * @throws NoBatteriesException
     */
    public void hitShip(GameState gameState, String nickname, int diceResult, boolean activateShield, boolean activateCannon) throws InvalidActionException, NoBatteriesException {
        throw new InvalidActionException("Invalid action for solving the current card");
    }

    /**
     * Method invoked when a player decides to remove crew members from its ship
     * @param gameState
     * @param nickname
     * @param x
     * @param y
     * @param crewInEachCabin
     * @throws InvalidActionException
     * @throws NoCrewException
     */
    public void landing(GameState gameState, String nickname, List<Integer> x, List<Integer> y, List<Integer> crewInEachCabin) throws InvalidActionException, NoCrewException {
        throw new InvalidActionException("Invalid action for solving the current card");
    }

    /**
     * Method invoked when a card is picked from the deck during the solving phase; it has an effect on all the players of the game without requiring user actions
     * @param gameState
     * @throws InvalidActionException
     */
    public void specialEffect(GameState gameState) throws InvalidActionException {}

    /**
     * Method invoked when a player wants to defeat an enemy; the player can decide whether to lose flight days to gain credits/goods or not
     * @param gameState
     * @param nickname
     * @param usedBatteries
     * @param loseDays
     * @throws InvalidActionException
     * @throws NoBatteriesException
     */
    public void defeat(GameState gameState, String nickname, int usedBatteries, boolean loseDays) throws InvalidActionException, NoBatteriesException {
        throw new InvalidActionException("Invalid action for solving the current card");
    }

    /**
     * Method invoked when a player wants to fly across the flight board exploiting its engine strength
     * @param gameState
     * @param nickname
     * @param usedBatteries
     * @throws InvalidActionException
     * @throws NoBatteriesException
     */
    public void fly(GameState gameState, String nickname, int usedBatteries) throws InvalidActionException, NoBatteriesException {
        throw new InvalidActionException("Invalid action for solving the current card");
    }

    /**
     * Method invoked when a player wants to use batteries to have an advantage while solving a card
     * @param gameState
     * @param nickname
     * @param usedBatteries
     * @throws InvalidActionException
     * @throws NoBatteriesException
     */
    public void useBatteries(GameState gameState, String nickname, int usedBatteries) throws InvalidActionException, NoBatteriesException {
        throw new InvalidActionException("Invalid action for solving the current card");
    }

    /**
     * Method invoked when a player doesn't want to exploit the benefits of a card and therefore skips the turn
     *
     * @param gameState
     * @param nickname
     * @throws InvalidActionException
     */
    public void skip(GameState gameState, String nickname) throws InvalidActionException {
        throw new InvalidActionException("Invalid action for solving the current card");
    }

    /**
     * Method invoked when a player decides to load goods inside cargo hold components of its ship
     * @param gameState
     * @param nickname
     * @param x
     * @param y
     * @throws InvalidActionException
     * @throws UnsupportedCargoColorException
     * @throws FullCargoHoldException
     * @throws NoGoodsException
     */
    public void loadGoods(GameState gameState, String nickname, List<Integer> x, List<Integer> y) throws InvalidActionException, UnsupportedCargoColorException, FullCargoHoldException, NoGoodsException {
        throw new InvalidActionException("Invalid action for solving the current card");
    }

    /**
     * Method invoked when a player wants to switch goods inside its cargo holds
     * @param gamestate
     * @param nickname
     * @param cargo_row
     * @param cargo_col
     * @param good
     * @param pos
     * @throws InvalidActionException
     */
    public void switchGoods(GameState gamestate, String nickname, int cargo_row, int cargo_col, Color good, int pos)throws InvalidActionException{
        throw new InvalidActionException("Invalid action for solving the current card");
    }

}












