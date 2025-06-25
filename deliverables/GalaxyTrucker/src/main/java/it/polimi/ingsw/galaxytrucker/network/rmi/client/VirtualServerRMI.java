package it.polimi.ingsw.galaxytrucker.network.rmi.client;

import it.polimi.ingsw.galaxytrucker.model.enumerations.Color;
import it.polimi.ingsw.galaxytrucker.network.VirtualServer;
import it.polimi.ingsw.galaxytrucker.network.VirtualView;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/**
 * This interface defines the methods that are implemented by the RMI server, which can be invoked by the remote
 */
//this interface defines the methods that are implemented by the RMI server, which can be invoked by the remote
//RMI client to change the state of the model

public interface VirtualServerRMI extends Remote, VirtualServer {
    //determines if a game with the specified ID has exists already

    /**
     * Checks if a game with the specified ID has already started.
     * @param gameID
     * @return true if the game has started, false otherwise
     * @throws RemoteException
     */
    @Override
    boolean startedGame(int gameID) throws RemoteException;

    //invoked when a player decides to start a new game

    /**
     * Starts a new game with the specified parameters.
     * @param client
     * @param gameID
     * @param firstFlight
     * @param numberPlayers
     * @throws RemoteException
     */
    @Override
    void startNewGame(VirtualView client, int gameID, boolean firstFlight, int numberPlayers) throws RemoteException;

    //invoked when one of the players decides enter the game; the remote client view is added to the list
    //of connected clients

    /**
     * Adds a player to the game with the specified parameters.
     * @param client
     * @param gameID
     * @param nickname
     * @param color
     * @return true if the player was added successfully, false if the game is full or the nickname is already taken
     * @throws RemoteException
     */
    @Override
    boolean addPlayer(VirtualView client, int gameID, String nickname, Color color) throws RemoteException;

    //invoked when a player wants to pick a component among the one placed face down (assembling phase)

    /**
     * Invoked when a player wants to pick a hidden component during the assembling phase.
     * @param gameID
     * @param nickname
     * @throws RemoteException
     */
    @Override
    void pickHidden(int gameID, String nickname) throws RemoteException;

    //invoked when a player wants to pick a specific component among the one placed face up (assembling phase)

    /**
     * Invoked when a player wants to pick a specific component that is shown face up during the assembling phase.
     * @param gameID
     * @param nickname
     * @param index
     * @throws RemoteException
     */
    @Override
    void pickShown(int gameID, String nickname, int index) throws RemoteException;

    //invoked when a player wants to release (therefore, place face up) the component that it has picked

    /**
     * Invoked when a player wants to release the component that it has picked during the assembling phase.
     * @param gameID
     * @param nickname
     * @throws RemoteException
     */
    @Override
    void putShown(int gameID, String nickname) throws RemoteException;

    //invoked when a player wants to reserve the component that it has picked for its ship board

    /**
     * Invoked when a player wants to reserve a component for its ship board during the assembling phase.
     * @param gameID
     * @param nickname
     * @throws RemoteException
     */
    @Override
    void reserveComponent(int gameID, String nickname) throws RemoteException;

    //invoked when a player wants to pick one of the components that it has reserved for its ship board

    /**
     * Invoked when a player wants to pick a reserved component for its ship board during the assembling phase.
     * @param gameID
     * @param nickname
     * @param position
     * @throws RemoteException
     */
    @Override
    void pickReservedComponent(int gameID, String nickname, int position) throws RemoteException;

    //invoked when a player wants to change the orientation of the component that it has picked

    /**
     * Invoked when a player wants to rotate the component that it has picked during the assembling phase.
     * @param gameID
     * @param nickname
     * @throws RemoteException
     */
    @Override
    void rotatePickedComponent(int gameID, String nickname) throws RemoteException;

    //invoked when a player wants to assemble on the ship board the component that it has picked

    /**
     * Invoked when a player wants to assemble a component on its ship board during the assembling phase.
     * @param gameID
     * @param nickname
     * @param x
     * @param y
     * @throws RemoteException
     */
    @Override
    void assembledComponent(int gameID, String nickname, int x, int y) throws RemoteException;

    //invoked when a player wants to pick a deck during the assembling phase to see its content

    /**
     * Invoked when a player wants to pick a deck during the assembling phase to see its content.
     * @param gameID
     * @param nickname
     * @param deckNumber
     * @throws RemoteException
     */
    @Override
    void pickDeck(int gameID, String nickname, int deckNumber) throws RemoteException;

    //invoked when a player wants to release the deck it has picked, during the assembling phase

    /**
     * Invoked when a player wants to release the deck it has picked during the assembling phase.
     * @param gameID
     * @param nickname
     * @throws RemoteException
     */
    @Override
    void releaseDeck(int gameID, String nickname) throws RemoteException;

    //invoked when a player has finished the assembling phase and has to pick a free position on the flight board

    /**
     * Invoked when a player has finished the assembling phase and has to pick a free position on the flight board.
     * @param gameID
     * @param nickname
     * @param initCell
     * @throws RemoteException
     */
    @Override
    void setPosition(int gameID, String nickname, int initCell) throws RemoteException;

    //invoked when a player wants to turn around the hourglass

    /**
     * Invoked when a player wants to start a new cycle by turning around the hourglass.
     * @param gameID
     * @param nickname
     * @throws RemoteException
     */
    @Override
    void startNewCycle(int gameID, String nickname) throws RemoteException;

    //invoked when a player wants to destroy a component in order to validate its ship board or when a
    //component is destroyed due to a cannon shot/meteor attack

    /**
     * Invoked when a player wants to destroy a component on its ship board, either to validate it or due to an attack.
     * @param gameID
     * @param nickname
     * @param x
     * @param y
     * @throws RemoteException
     */
    @Override
    void destroyComponent(int gameID, String nickname, int x, int y) throws RemoteException;

    //invoked when a player wants to initialize a cabin of its shipboard with 2 human crew members

    /**
     * Invoked when a player wants to initialize a cabin of its shipboard with 2 human crew members.
     * @param gameID
     * @param nickname
     * @param x
     * @param y
     * @throws RemoteException
     */
    @Override
    void addCrew(int gameID, String nickname, int x, int y) throws RemoteException;

    //invoked when a player wants to initialize a battery container of its shipboard with batteries

    /**
     * Invoked when a player wants to initialize a battery container of its shipboard with batteries.
     * @param gameID
     * @param nickname
     * @param x
     * @param y
     * @throws RemoteException
     */
    @Override
    void addBatteries(int gameID, String nickname, int x, int y) throws RemoteException;

    //invoked when a player wants to initialize a cabin of its shipboard with an alien

    /**
     * Invoked when a player wants to initialize a cabin of its shipboard with an alien.
     * @param gameID
     * @param nickname
     * @param isPurple
     * @param x
     * @param y
     * @throws RemoteException
     */
    @Override
    void addAlien(int gameID, String nickname, boolean isPurple, int x, int y) throws RemoteException;

    //invoked when a player wants to pick a new card from the game deck

    /**
     * Invoked when a player wants to pick the next card from the game deck.
     * @param gameID
     * @param nickname
     * @throws RemoteException
     */
    @Override
    void pickNextCard(int gameID, String nickname) throws RemoteException;

    //invoked when a player wants to leave the game

    /**
     * Invoked when a player wants to quit the game.
     * @param gameID
     * @param nickname
     * @throws RemoteException
     */
    @Override
    void quitGame(int gameID, String nickname) throws RemoteException;

    //invoked when a player wants to skip an action during the flight phase

    /**
     * Invoked when a player wants to skip its turn during the flight phase.
     * @param gameID
     * @param nickname
     * @throws RemoteException
     */
    @Override
    void skip(int gameID, String nickname) throws RemoteException;

    //invoked when a player wants to land on an abandoned ship or station

    /**
     * Invoked when a player wants to land on an abandoned ship or station.
     * @param gameID
     * @param nickname
     * @param x
     * @param y
     * @param z
     * @throws RemoteException
     */
    @Override
    void landing(int gameID, String nickname, List<Integer> x, List<Integer> y, List<Integer> z) throws RemoteException;

    //invoked when th ship board of a player must be hit by meteor/cannon shot

    /**
     * Invoked when a player's ship board must be hit by a meteor or cannon shot.
     * @param gameID
     * @param nickname
     * @param diceResult
     * @param activateShield
     * @param activateCannon
     * @throws RemoteException
     */
    @Override
    void hitShip(int gameID, String nickname, int diceResult, boolean activateShield, boolean activateCannon) throws RemoteException;

    //invoked when a player wants to fly across the flight board exploiting its engine strength

    /**
     * Invoked when a player wants to fly across the flight board using its engine strength.
     * @param gameID
     * @param nickname
     * @param usedBatteries
     * @throws RemoteException
     */
    @Override
    void fly(int gameID, String nickname, int usedBatteries) throws RemoteException;

    //invoked when a player wants to defeat an enemy; the player can decide whether to lose flight days
    //to gain credits/goods or not

    /**
     * Invoked when a player defeats an enemy ship.
     * @param gameID
     * @param nickname
     * @param usedBatteries
     * @param loseDays
     * @throws RemoteException
     */
    @Override
    void defeat(int gameID, String nickname, int usedBatteries, boolean loseDays) throws RemoteException;

    //invoked when a player decides to load goods inside cargo hold components of its ship

    /**
     * Invoked when a player wants to load goods into its cargo holds during the flight phase.
     * @param gameID
     * @param nickname
     * @param x
     * @param y
     * @throws RemoteException
     */
    @Override
    void loadGoods(int gameID, String nickname, List<Integer> x, List<Integer> y) throws RemoteException;

    //invoked when a player wants to land on a planet

    /**
     * Invoked when a player wants to land on a planet.
     * @param gameID
     * @param nickname
     * @param numberPlanet
     * @throws RemoteException
     */
    @Override
    void planetLanding(int gameID, String nickname, int numberPlanet) throws RemoteException;

    //invoked when a player wants to use batteries to declare its engine/cannon strength

    /**
     * Invoked when a player wants to use batteries to declare its engine or cannon strength.
     * @param gameID
     * @param nickname
     * @param usedBatteries
     * @throws RemoteException
     */
    @Override
    void useBatteries(int gameID, String nickname, int usedBatteries) throws RemoteException;

}
