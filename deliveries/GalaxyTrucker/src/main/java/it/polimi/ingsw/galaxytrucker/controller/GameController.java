package it.polimi.ingsw.galaxytrucker.controller;

import it.polimi.ingsw.galaxytrucker.model.enumerations.*;
import it.polimi.ingsw.galaxytrucker.model.eventCardClasses.*;
import it.polimi.ingsw.galaxytrucker.model.exceptions.*;
import it.polimi.ingsw.galaxytrucker.model.gameClasses.*;
import it.polimi.ingsw.galaxytrucker.network.VirtualView;

import java.rmi.RemoteException;
import java.util.List;



public class GameController {
    private GameState model;
    private final Object lock;

    public GameController() {
        this.lock = new Object();
    }

    //STARTING PHASE

    public boolean startedGame() {
        synchronized (lock) {
            return model != null;
        }
    }

    //invoked when the first player decides to start the game
    public void startNewGame(boolean firstFlight, int numberPlayers) throws InvalidActionException {
        synchronized (lock) {
            if (startedGame()) {
                throw new InvalidActionException("GAME ALREADY STARTED");
            }
            this.model = new GameState(firstFlight, numberPlayers);
        }
    }

    //invoked when one of the players decides enter the game
    public void addPlayer(VirtualView client, String nickname, Color color) throws UniqueNicknameException, UniquePlayerColorException, InvalidActionException {
        synchronized (lock) {
            model.addPlayer(client, nickname, color);
        }
    }

    //ASSEMBLING PHASE

    //invoked when a player wants to pick a component among the one placed face down (assembling phase)
    public void pickHidden(String nickname) throws PickedComponentException, InvalidActionException {
        synchronized (lock) {
            model.pickHidden(nickname);
        }
    }

    //invoked when a player wants to pick a specific component among the one placed face up (assembling phase)
    public void pickShown(String nickname, int index) throws PickedComponentException, InvalidActionException {
        synchronized (lock) {
            model.pickShown(nickname, index);
        }
    }

    //invoked when a player wants to reserve the component that it has picked for its ship board
    public void reserveComponent(String nickname) throws PickedComponentException, ReservedComponentException, InvalidActionException {
        synchronized (lock) {
            model.reserveComponent(nickname);
        }
    }

    //invoked when a player wants to pick one of the components that it has reserved for its ship board
    public void pickReservedComponent(String nickname, int position) throws ReservedComponentException, PickedComponentException, InvalidActionException {
        synchronized (lock) {
            model.pickReservedComponent(nickname, position);
        }
    }

    //invoked when a player wants to release (therefore, place face up) the component that it has picked
    public void putShown(String nickname) throws PickedComponentException, InvalidActionException {
        synchronized (lock) {
            model.putShown(nickname);
        }
    }

    //invoked when a player wants to assemble on the ship board the component that it has picked
    public void assembleComponent(String nickname, int x, int y) throws AssembledComponentException, PickedComponentException, InvalidActionException {
        synchronized (lock) {
            model.assembleComponent(nickname, x, y);
        }
    }

    //invoked when a player wants to change the orientation of the component that it has picked
    public void rotatePickedComponent(String nickname) throws InvalidActionException, PickedComponentException {
        synchronized (lock) {
            model.rotatePickedComponent(nickname);
        }
    }

    //invoked when a player wants to pick a deck during the assembling phase to see its content
    public void pickDeck (String nickname, int deckNumber) throws PickedDeckException, InvalidActionException {
        synchronized (lock) {
            model.pickDeck(nickname, deckNumber);
        }
    }

    //invoked when a player wants to release the deck it has picked, during the assembling phase
    public void releaseDeck(String nickname) throws InvalidActionException, PickedDeckException {
        synchronized (lock) {
            model.releaseDeck(nickname);
        }
    }

    //invoked when a player has finished the assembling phase and has to pick a free position on the flight board
    public void setPosition(String nickname, int initCell) throws InvalidActionException, PickedDeckException{
        synchronized (lock) {
            model.setPosition(nickname, initCell);
        }
    }

    //invoked when a player wants to turn around the hourglass
    public void startNewCycle(String nickname) throws InvalidActionException, HourGlassException{
        synchronized (lock) {
            model.startNewCycle(nickname);
        }
    }

    //SHIP CONTROL PHASE

    //invoked when a player wants to destroy a component in order to correct its ship board
    public void destroyComponent(String nickname, int x, int y) throws AssembledComponentException, InvalidActionException {
        synchronized (lock) {
            model.destroyComponent(nickname, x, y);
        }
    }

    //invoked when a player wants to initialize a cabin of its shipboard with 2 human crew members
    public void addCrew(String nickname, int x, int y) throws AssembledComponentException, FullCabinException, InvalidActionException {
        synchronized (lock) {
            model.addCrew(nickname, x, y);
        }
    }
    //invoked when a player wants to initialize a cabin of its shipboard with an alien
    public void addAlien(String nickname, boolean isPurple, int x, int y) throws AssembledComponentException, FullCabinException, InvalidActionException {
        synchronized (lock) {
            model.addAlien(nickname, isPurple, x, y);
        }
    }

    //invoked when the player wants to initialize a battery container with batteries
    public void addBatteries(String nickname, int x, int y) throws AssembledComponentException, NoBatteriesException, InvalidActionException{
        synchronized (lock) {
            model.addBatteries(nickname, x, y);
        }
    }



    //FLIGHT PHASE

    //this method is invoked when a player has to leave the game
    public void quitGame(String nickname) throws InvalidActionException{
        synchronized (lock) {
            model.quitGame(nickname);
        }
    }

    //invoked when the leader draws a new card from the deck (during the game), which must be solved
    public void pickNextCard(String nickname) throws InvalidActionException {
        synchronized (lock) {
            model.pickNextCard(nickname);
        }
    }

    //invoked when a player decides to land on a planet in order to gain goods
    public void planetLanding(String nickname, int numberPlanet) throws InvalidActionException {
        synchronized (lock) {
            model.planetLanding(nickname, numberPlanet);
        }
    }

    public void switchGoods(String nickname,int cargo_row, int cargo_col, Color good, int pos) throws InvalidActionException {
        synchronized (lock) {
            model.switchGoods(nickname, cargo_row, cargo_col, good, pos);
        }
    }


        //invoked when a player's ship has to be hit by a meteor/cannon shot; the player can decide whether to
    //activate a shield or a cannon to defend its ship
    public void hit(String nickname, int diceResult, boolean activateShield, boolean activateCannon) throws InvalidActionException, NoBatteriesException {
        synchronized (lock) {
            model.hit(nickname, diceResult, activateShield, activateCannon);
        }
    }

    //invoked when a player decides to land on an abandoned station/ship
    public void landing(String nickname, List<Integer> x, List<Integer> y, List<Integer> z) throws InvalidActionException, NoCrewException {
        synchronized (lock) {
            model.landing(nickname, x, y, z);
        }
    }

    //invoked when a player wants to defeat an enemy; the player can decide whether to lose flight days
    //to gain credits/goods or not
    public void defeat(String nickname, int usedBatteries, boolean loseDays) throws InvalidActionException, NoBatteriesException {
        synchronized (lock) {
            model.defeat(nickname, usedBatteries, loseDays);
        }
    }

    //invoked when a player wants to fly across the flight board exploiting its engine strength
    public void fly(String nickname, int usedBatteries) throws InvalidActionException, NoBatteriesException {
        synchronized (lock) {
            model.fly(nickname, usedBatteries);
        }
    }

    //invoked when a player wants to use batteries to have an advantage while solving a card
    public void useBatteries(String nickname, int usedBatteries) throws InvalidActionException, NoBatteriesException {
        synchronized (lock) {
            model.useBatteries(nickname, usedBatteries);
        }
    }

    //invoked when a player doesn't want to exploit the benefits of a card and therefore skips the turn
    public void skip(String nickname) throws InvalidActionException {
        synchronized (lock) {
            model.skip(nickname);
        }
    }

    //invoked when a player decides to load goods inside cargo hold components of its ship
    public void loadGoods(String nickname, List<Integer> x, List<Integer> y) throws InvalidActionException, UnsupportedCargoColorException, FullCargoHoldException, NoGoodsException{
        synchronized (lock) {
            model.loadGoods(nickname, x, y);
        }
    }


}