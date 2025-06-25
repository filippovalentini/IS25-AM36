package it.polimi.ingsw.galaxytrucker.network;

import it.polimi.ingsw.galaxytrucker.model.enumerations.Color;

import java.util.List;

//this interface defines the methods that are used by the clients to interact with the server, in order to change
//the state of the model

/**
 * This interface defines the methods that are used by the clients to interact with the server,
 */
public interface VirtualServer {
    //determines if a game with the specified ID has exists already

    /**
     * Checks if a game with the specified ID has already started.
     * @param gameID
     * @return
     * @throws Exception
     */
    boolean startedGame(int gameID) throws Exception;

    //invoked when the first player decides to start the game

    /**
     * Starts a new game with the specified parameters.
     * @param client
     * @param gameID
     * @param firstFlight
     * @param numberPlayers
     * @throws Exception
     */
    void startNewGame(VirtualView client, int gameID, boolean firstFlight, int numberPlayers) throws Exception;

    //invoked when one of the players decides enter the game; the remote client view is added to the list
    //of connected clients

    /**
     * Adds a player to the game with the specified parameters.
     * @param client
     * @param gameID
     * @param nickname
     * @param color
     * @return
     * @throws Exception
     */
    boolean addPlayer(VirtualView client, int gameID, String nickname, Color color) throws Exception;

    //invoked when a player wants to pick a component among the one placed face down (assembling phase)

    /**
     * Invoked when a player wants to pick a hidden component during the assembling phase.
     * @param gameID
     * @param nickname
     * @throws Exception
     */
    void pickHidden(int gameID, String nickname) throws Exception;

    //invoked when a player wants to pick a specific component among the one placed face up (assembling phase)

    /**
     * Invoked when a player wants to pick a specific component that is shown face up during the assembling phase.
     * @param gameID
     * @param nickname
     * @param index
     * @throws Exception
     */
    void pickShown(int gameID, String nickname, int index) throws Exception;

    //invoked when a player wants to release (therefore, place face up) the component that it has picked

    /**
     * Invoked when a player wants to release the component that it has picked during the assembling phase.
     * @param gameID
     * @param nickname
     * @throws Exception
     */
    void putShown(int gameID, String nickname) throws Exception;

    //invoked when a player wants to reserve the component that it has picked for its ship board

    /**
     * Invoked when a player wants to reserve the component that it has picked for its ship board.
     * @param gameID
     * @param nickname
     * @throws Exception
     */
    void reserveComponent(int gameID, String nickname) throws Exception;

    //invoked when a player wants to pick one of the components that it has reserved for its ship board

    /**
     * Invoked when a player wants to pick one of the components that it has reserved for its ship board.
     * @param gameID
     * @param nickname
     * @param position
     * @throws Exception
     */
    void pickReservedComponent(int gameID, String nickname, int position) throws Exception;

    //invoked when a player wants to change the orientation of the component that it has picked

    /**
     * Invoked when a player wants to change the orientation of the component that it has picked.
     * @param gameID
     * @param nickname
     * @throws Exception
     */
    void rotatePickedComponent(int gameID, String nickname) throws Exception;

    //invoked when a player wants to assemble on the ship board the component that it has picked

    /**
     * Invoked when a player wants to assemble a component on its ship board.
     * @param gameID
     * @param nickname
     * @param x
     * @param y
     * @throws Exception
     */
    void assembledComponent(int gameID, String nickname, int x, int y) throws Exception;

    //invoked when a player wants to pick a deck during the assembling phase to see its content

    /**
     * Invoked when a player wants to pick a deck during the assembling phase to see its content.
     * @param gameID
     * @param nickname
     * @param deckNumber
     * @throws Exception
     */
    void pickDeck(int gameID, String nickname, int deckNumber) throws Exception;

    //invoked when a player wants to release the deck it has picked, during the assembling phase

    /**
     * Invoked when a player wants to release the deck it has picked during the assembling phase.
     * @param gameID
     * @param nickname
     * @throws Exception
     */
    void releaseDeck(int gameID, String nickname) throws Exception;

    //invoked when a player has finished the assembling phase and has to pick a free position on the flight board

    /**
     * Invoked when a player has finished the assembling phase and has to pick a free position on the flight board.
     * @param gameID
     * @param nickname
     * @param initCell
     * @throws Exception
     */
    void setPosition(int gameID, String nickname, int initCell) throws Exception;

    //invoked when a player wants to turn around the hourglass

    /**
     * Invoked when a player wants to turn around the hourglass to start a new cycle.
     * @param gameID
     * @param nickname
     * @throws Exception
     */
    void startNewCycle(int gameID, String nickname) throws Exception;

    //invoked when a player wants to destroy a component in order to validate its ship board or when a
    //component is destroyed due to a cannon shot/meteor attack

    /**
     * Invoked when a player wants to destroy a component in order to validate its ship board or when a
     * @param gameID
     * @param nickname
     * @param x
     * @param y
     * @throws Exception
     */
    void destroyComponent(int gameID, String nickname, int x, int y) throws Exception;

    //invoked when a player wants to initialize a cabin of its shipboard with 2 human crew members

    /**
     * Invoked when a player wants to initialize a cabin of its shipboard with 2 human crew members.
     * @param gameID
     * @param nickname
     * @param x
     * @param y
     * @throws Exception
     */
    void addCrew(int gameID, String nickname, int x, int y) throws Exception;

    //invoked when a player wants to initialize a battery container of its shipboard with batteries

    /**
     * Invoked when a player wants to initialize a battery container of its shipboard with batteries.
     * @param gameID
     * @param nickname
     * @param x
     * @param y
     * @throws Exception
     */
    void addBatteries(int gameID, String nickname, int x, int y) throws Exception;

    //invoked when a player wants to initialize a cabin of its shipboard with an alien

    /**
     * Invoked when a player wants to initialize a cabin of its shipboard with an alien.
     * @param gameID
     * @param nickname
     * @param isPurple
     * @param x
     * @param y
     * @throws Exception
     */
    void addAlien(int gameID, String nickname, boolean isPurple, int x, int y) throws Exception;

    //invoked when a player wants to pick a new card from the game deck

    /**
     * Invoked when a player wants to pick a new card from the game deck.
     * @param gameID
     * @param nickname
     * @throws Exception
     */
    void pickNextCard(int gameID, String nickname) throws Exception;

    //invoked when a player wants to leave the game

    /**
     * Invoked when a player wants to leave the game.
     * @param gameID
     * @param nickname
     * @throws Exception
     */
    void quitGame(int gameID, String nickname) throws Exception;




    //invoked when a player wants to skip an action during the flight phase

    /**
     * Invoked when a player wants to skip an action during the flight phase.
     * @param gameID
     * @param nickname
     * @throws Exception
     */
    void skip(int gameID, String nickname) throws Exception;

    //invoked when a player wants to land on an abandoned ship or station

    /**
     * Invoked when a player wants to land on an abandoned ship or station.
     * @param gameID
     * @param nickname
     * @param x
     * @param y
     * @param z
     * @throws Exception
     */
    void landing(int gameID, String nickname, List<Integer> x, List<Integer> y, List<Integer> z) throws Exception;

    //invoked when th ship board of a player must be hit by meteor/cannon shot

    /**
     * Invoked when the ship board of a player must be hit by meteor/cannon shot.
     * @param gameID
     * @param nickname
     * @param diceResult
     * @param activateShield
     * @param activateCannon
     * @throws Exception
     */
    void hitShip(int gameID, String nickname, int diceResult, boolean activateShield, boolean activateCannon) throws Exception;

    //invoked when a player wants to fly across the flight board exploiting its engine strength

    /**
     * Invoked when a player wants to fly across the flight board exploiting its engine strength.
     * @param gameID
     * @param nickname
     * @param usedBatteries
     * @throws Exception
     */
    void fly(int gameID, String nickname, int usedBatteries) throws Exception;

    //invoked when a player wants to defeat an enemy; the player can decide whether to lose flight days

    /**
     * Invoked when a player wants to defeat an enemy; the player can decide whether to lose flight days.
     * @param gameID
     * @param nickname
     * @param usedBatteries
     * @param loseDays
     * @throws Exception
     */
    void defeat(int gameID, String nickname, int usedBatteries, boolean loseDays) throws Exception;

    //invoked when a player decides to load goods inside cargo hold components of its ship

    /**
     * Invoked when a player decides to load goods inside cargo hold components of its ship.
     * @param gameID
     * @param nickname
     * @param x
     * @param y
     * @throws Exception
     */
    void loadGoods(int gameID, String nickname, List<Integer> x, List<Integer> y) throws Exception;

    //invoked when a player wants to land on a planet

    /**
     * Invoked when a player wants to land on a planet.
     * @param gameID
     * @param nickname
     * @param numberPlanet
     * @throws Exception
     */
    void planetLanding(int gameID, String nickname, int numberPlanet) throws Exception;

    //invoked when a player wants to use batteries to declare its engine/cannon strength

    /**
     * Invoked when a player wants to use batteries to declare its engine/cannon strength.
     * @param gameID
     * @param nickname
     * @param usedBatteries
     * @throws Exception
     */
    void useBatteries(int gameID, String nickname, int usedBatteries) throws Exception;

}
