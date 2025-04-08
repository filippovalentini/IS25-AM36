package it.polimi.ingsw.galaxytrucker.controller;

import it.polimi.ingsw.galaxytrucker.model.enumerations.*;
import it.polimi.ingsw.galaxytrucker.model.eventCardClasses.*;
import it.polimi.ingsw.galaxytrucker.model.exceptions.*;
import it.polimi.ingsw.galaxytrucker.model.gameClasses.*;
import it.polimi.ingsw.galaxytrucker.network.rmi.server.VirtualViewRMI;

import java.util.List;



public class GameController {
    private final GameState model;

    public GameController(boolean firstFlight, int numPlayers) {
        this.model = new GameState(firstFlight, numPlayers);
    }

    //STARTING PHASE

    //invoked when one of the players decides enter the game
    public void addPlayer(VirtualViewRMI client, String nickname, Color color) throws UniqueNicknameException, UniquePlayerColorException, InvalidActionException {
        synchronized (model) {
            model.addPlayer(client, nickname, color);
        }
    }

    //ASSEMBLING PHASE

    //invoked when a player wants to pick a component among the one placed face down (assembling phase)
    public void pickHidden(String nickname) throws PickedComponentException, InvalidActionException {
        synchronized (model) {
            model.pickHidden(nickname);
        }
    }

    //invoked when a player wants to pick a specific component among the one placed face up (assembling phase)
    public void pickShown(String nickname, int index) throws PickedComponentException, InvalidActionException {
        synchronized (model) {
            model.pickShown(nickname, index);
        }
    }

    //invoked when a player wants to reserve the component that it has picked for its ship board
    public void reserveComponent(String nickname) throws PickedComponentException, ReservedComponentException, InvalidActionException {
        synchronized (model) {
            model.reserveComponent(nickname);
        }
    }

    //invoked when a player wants to pick one of the components that it has reserved for its ship board
    public void pickReservedComponent(String nickname, int position) throws ReservedComponentException, PickedComponentException, InvalidActionException {
        synchronized (model) {
            model.pickReservedComponent(nickname, position);
        }
    }

    //invoked when a player wants to release (therefore, place face up) the component that it has picked
    public void putShown(String nickname) throws PickedComponentException, InvalidActionException {
        synchronized (model) {
            model.putShown(nickname);
        }
    }

    //invoked when a player wants to assemble on the ship board the component that it has picked
    public void assembleComponent(String nickname, int x, int y) throws AssembledComponentException, PickedComponentException, InvalidActionException {
        synchronized (model) {
            model.assembleComponent(nickname, x, y);
        }
    }

    //invoked when a player wants to change the orientation of the component that it has picked
    public void rotatePickedComponent(String nickname) throws InvalidActionException, PickedComponentException {
        synchronized (model) {
            model.rotatePickedComponent(nickname);
        }
    }

    //invoked when a player wants to pick a deck during the assembling phase to see its content
    public void pickDeck (String nickname, int deckNumber) throws PickedDeckException, InvalidActionException {
        synchronized (model) {
            model.pickDeck(nickname, deckNumber);
        }
    }

    //invoked when a player wants to release the deck it has picked, during the assembling phase
    public void releaseDeck(String nickname) throws InvalidActionException, PickedDeckException {
        synchronized (model) {
            model.releaseDeck(nickname);
        }
    }

    //invoked when a player has finished the assembling phase and has to pick a free position on the flight board
    public void setPosition(String nickname, int initCell) throws InvalidActionException, PickedDeckException{
        synchronized (model) {
            model.setPosition(nickname, initCell);
        }
    }

    //SHIP CONTROL PHASE

    public void destroyComponent(String nickname, int x, int y) throws AssembledComponentException, InvalidActionException {
        synchronized (model) {
            model.destroyComponent(nickname, x, y);
        }
    }

    //FLIGHT PHASE

    //this method is invoked when a player has to leave the game
    public void quitGame(String nickname) throws InvalidActionException{
        synchronized (model) {
            model.quitGame(nickname);
        }
    }

    //invoked when the leader draws a new card from the deck (during the game), which must be solved
    public void pickNextCard(String nickname) throws InvalidActionException {
        synchronized (model) {
            model.pickNextCard(nickname);
        }
    }

    //invoked when a player decides to land on a planet in order to gain goods
    public void planetLanding(String nickname, int numberPlanet) throws InvalidActionException {
        synchronized (model) {
            model.planetLanding(nickname, numberPlanet);
        }
    }

    //invoked when a player's ship has to be hit by a meteor/cannon shot; the player can decide whether to
    //activate a shield or a cannon to defend its ship
    public void hit(String nickname, int diceResult, boolean activateShield, boolean activateCannon) throws InvalidActionException, NoBatteriesException {
        synchronized (model) {
            model.hit(nickname, diceResult, activateShield, activateCannon);
        }
    }

    //invoked when a player decides to land on an abandoned station/ship
    public void landing(String nickname, List<Integer> x, List<Integer> y, List<Integer> z) throws InvalidActionException, NoCrewException {
        synchronized (model) {
            model.landing(nickname, x, y, z);
        }
    }

    //invoked when a player wants to defeat an enemy; the player can decide whether to lose flight days
    //to gain credits/goods or not
    public void defeat(String nickname, int usedBatteries, boolean loseDays) throws InvalidActionException, NoBatteriesException {
        synchronized (model) {
            model.defeat(nickname, usedBatteries, loseDays);
        }
    }

    //invoked when a player wants to fly across the flight board exploiting its engine strength
    public void fly(String nickname, int usedBatteries) throws InvalidActionException, NoBatteriesException {
        synchronized (model) {
            model.fly(nickname, usedBatteries);
        }
    }

    //invoked when a player wants to use batteries to have an advantage while solving a card
    public void useBatteries(String nickname, int usedBatteries) throws InvalidActionException, NoBatteriesException {
        synchronized (model) {
            model.useBatteries(nickname, usedBatteries);
        }
    }

    //invoked when a player doesn't want to exploit the benefits of a card and therefore skips the turn
    public void skip(String nickname) throws InvalidActionException {
        synchronized (model) {
            model.skip(nickname);
        }
    }

    //[method for testing]
    public State getModelState() {
        return model.getGameState();
    }

    //[method for testing]
    public void setModelDeck(Deck customDeck) {
        model.setGameDeck(customDeck);
    }

    //[method for testing]
    public EventCard getModelCurrentCard() {
        return model.getCurrentCard();
    }
}