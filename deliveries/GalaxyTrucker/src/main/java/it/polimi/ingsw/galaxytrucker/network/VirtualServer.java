package it.polimi.ingsw.galaxytrucker.network;

import it.polimi.ingsw.galaxytrucker.model.enumerations.Color;
import it.polimi.ingsw.galaxytrucker.model.exceptions.*;
import it.polimi.ingsw.galaxytrucker.model.gameClasses.GameState;
import it.polimi.ingsw.galaxytrucker.network.rmi.server.VirtualViewRMI;

import java.util.List;

//this interface defines the methods that are used by the clients to interact with the server, in order to change
//the state of the model

public interface VirtualServer {
    //invoked when one of the players decides enter the game; the remote client view is added to the list
    //of connected clients
    boolean addPlayer(VirtualView client, String nickname, Color color) throws Exception;

    //invoked when a player wants to pick a component among the one placed face down (assembling phase)
    void pickHidden(String nickname) throws Exception;

    //invoked when a player wants to pick a specific component among the one placed face up (assembling phase)
    void pickShown(String nickname, int index) throws Exception;

    //invoked when a player wants to release (therefore, place face up) the component that it has picked
    void putShown(String nickname) throws Exception;

    //invoked when a player wants to reserve the component that it has picked for its ship board
    void reserveComponent(String nickname) throws Exception;

    //invoked when a player wants to pick one of the components that it has reserved for its ship board
    void pickReservedComponent(String nickname, int position) throws Exception;

    //invoked when a player wants to change the orientation of the component that it has picked
    void rotatePickedComponent(String nickname) throws Exception;

    //invoked when a player wants to assemble on the ship board the component that it has picked
    void assembledComponent(String nickname, int x, int y) throws Exception;

    //invoked when a player wants to pick a deck during the assembling phase to see its content
    void pickDeck(String nickname, int deckNumber) throws Exception;

    //invoked when a player wants to release the deck it has picked, during the assembling phase
    void releaseDeck(String nickname) throws Exception;

    //invoked when a player has finished the assembling phase and has to pick a free position on the flight board
    void setPosition(String nickname, int initCell) throws Exception;

    //invoked when a player wants to destroy a component in order to validate its ship board or when a
    //component is destroyed due to a cannon shot/meteor attack
    void destroyComponent(String nickname, int x, int y) throws Exception;

    //invoked when a player wants to initialize a cabin of its shipboard with 2 human crew members
    void addCrew(String nickname, int x, int y) throws Exception;

    //invoked when a player wants to initialize a battery container of its shipboard with batteries
    void addBatteries(String nickname, int x, int y) throws Exception;

    //invoked when a player wants to initialize a cabin of its shipboard with an alien
    void addAlien(String nickname, boolean isPurple, int x, int y) throws Exception;

    //invoked when a player wants to pick a new card from the game deck
    void pickNextCard(String nickname) throws Exception;

    //invoked when a player wants to leave the game
    void quitGame(String nickname) throws Exception;




    //invoked when a player wants to skip an action during the flight phase
    void skip(String nickname) throws Exception;

    //invoked when a player wants to land on an abandoned ship or station
    void landing(String nickname, List<Integer> x, List<Integer> y, List<Integer> z) throws Exception;

    //invoked when th ship board of a player must be hit by meteor/cannon shot
    void hitShip(String nickname, int diceResult, boolean activateShield, boolean activateCannon) throws Exception;

    //invoked when a player wants to fly across the flight board exploiting its engine strength
    void fly(String nickname, int usedBatteries) throws Exception;

    //invoked when a player wants to defeat an enemy; the player can decide whether to lose flight days
    //to gain credits/goods or not
    void defeat(String nickname, int usedBatteries, boolean loseDays) throws Exception;

    //invoked when a player decides to load goods inside cargo hold components of its ship
    void loadGoods(String nickname, List<Integer> x, List<Integer> y) throws Exception;
}
