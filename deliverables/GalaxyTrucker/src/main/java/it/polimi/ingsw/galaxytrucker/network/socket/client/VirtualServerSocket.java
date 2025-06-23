package it.polimi.ingsw.galaxytrucker.network.socket.client;

import it.polimi.ingsw.galaxytrucker.model.enumerations.Color;
import it.polimi.ingsw.galaxytrucker.network.VirtualServer;
import it.polimi.ingsw.galaxytrucker.network.VirtualView;

import java.io.IOException;
import java.util.List;

//this interface defines the methods that are implemented by the Socket server, which can be invoked by the remote
//client to change the state of the model
public interface VirtualServerSocket extends VirtualServer {
    //determines if a game with the specified ID has exists already
    @Override
    boolean startedGame(int gameID) throws IOException;

    //invoked when the first player decides to start the game
    @Override
    void startNewGame(VirtualView client, int gameID, boolean firstFlight, int numberPlayers) throws IOException;

    //invoked when one of the players decides enter the game; the remote client view is added to the list
    //of connected clients
    @Override
    boolean addPlayer(VirtualView client, int gameID, String nickname, Color color) throws IOException;

    //invoked when a player wants to pick a component among the one placed face down (assembling phase)
    @Override
    void pickHidden(int gameID, String nickname) throws IOException;

    //invoked when a player wants to pick a specific component among the one placed face up (assembling phase)
    @Override
    void pickShown(int gameID, String nickname, int index) throws IOException;

    //invoked when a player wants to release (therefore, place face up) the component that it has picked
    @Override
    void putShown(int gameID, String nickname) throws IOException;

    //invoked when a player wants to reserve the component that it has picked for its ship board
    @Override
    void reserveComponent(int gameID, String nickname) throws IOException;

    //invoked when a player wants to pick one of the components that it has reserved for its ship board
    @Override
    void pickReservedComponent(int gameID, String nickname, int position) throws IOException;

    //invoked when a player wants to change the orientation of the component that it has picked
    @Override
    void rotatePickedComponent(int gameID, String nickname) throws IOException;

    //invoked when a player wants to assemble on the ship board the component that it has picked
    @Override
    void assembledComponent(int gameID, String nickname, int x, int y) throws IOException;

    //invoked when a player wants to pick a deck during the assembling phase to see its content
    @Override
    void pickDeck(int gameID, String nickname, int deckNumber) throws IOException;

    //invoked when a player wants to release the deck it has picked, during the assembling phase
    @Override
    void releaseDeck(int gameID, String nickname) throws IOException;

    //invoked when a player has finished the assembling phase and has to pick a free position on the flight board
    @Override
    void setPosition(int gameID, String nickname, int initCell) throws IOException;

    //invoked when a player wants to turn around the hourglass
    @Override
    void startNewCycle(int gameID, String nickname) throws IOException;

    //invoked when a player wants to destroy a component in order to validate its ship board or when a
    //component is destroyed due to a cannon shot/meteor attack
    @Override
    void destroyComponent(int gameID, String nickname, int x, int y) throws IOException;

    //invoked when a player wants to initialize a cabin of its shipboard with 2 human crew members
    @Override
    void addCrew(int gameID, String nickname, int x, int y) throws IOException;

    //invoked when a player wants to initialize a battery container of its shipboard with batteries
    @Override
    void addBatteries(int gameID, String nickname, int x, int y) throws IOException;

    //invoked when a player wants to initialize a cabin of its shipboard with an alien
    @Override
    void addAlien(int gameID, String nickname, boolean isPurple, int x, int y) throws IOException;

    //invoked when a player wants to pick a new card from the game deck
    @Override
    void pickNextCard(int gameID, String nickname) throws IOException;

    //invoked when a player wants to leave the game
    @Override
    void quitGame(int gameID, String nickname) throws IOException;

    //invoked when a player wants to skip an action during the flight phase
    @Override
    void skip(int gameID, String nickname) throws IOException;

    //invoked when a player wants to land on an abandoned ship or station
    @Override
    void landing(int gameID, String nickname, List<Integer> x, List<Integer> y, List<Integer> z) throws IOException;

    //invoked when th ship board of a player must be hit by meteor/cannon shot
    @Override
    void hitShip(int gameID, String nickname, int diceResult, boolean activateShield, boolean activateCannon) throws IOException;

    //invoked when a player wants to fly across the flight board exploiting its engine strength
    @Override
    void fly(int gameID, String nickname, int usedBatteries) throws IOException;

    //invoked when a player wants to defeat an enemy; the player can decide whether to lose flight days
    //to gain credits/goods or not
    @Override
    void defeat(int gameID, String nickname, int usedBatteries, boolean loseDays) throws IOException;

    //invoked when a player decides to load goods inside cargo hold components of its ship
    @Override
    void loadGoods(int gameID, String nickname, List<Integer> x, List<Integer> y) throws IOException;

    //invoked when a player wants to land on a planet
    @Override
    void planetLanding(int gameID, String nickname, int numberPlanet) throws IOException;

    //invoked when a player wants to use batteries to declare its engine/cannon strength
    @Override
    void useBatteries(int gameID, String nickname, int usedBatteries) throws IOException;
}
