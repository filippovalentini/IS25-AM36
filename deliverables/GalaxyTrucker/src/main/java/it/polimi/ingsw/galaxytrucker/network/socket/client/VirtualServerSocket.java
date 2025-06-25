package it.polimi.ingsw.galaxytrucker.network.socket.client;

import it.polimi.ingsw.galaxytrucker.model.enumerations.Color;
import it.polimi.ingsw.galaxytrucker.network.VirtualServer;
import it.polimi.ingsw.galaxytrucker.network.VirtualView;

import java.io.IOException;
import java.util.List;

//this interface defines the methods that are implemented by the Socket server, which can be invoked by the remote
//client to change the state of the model

/**
 * This interface defines the methods that are invoked by the client to interact with the server.
 */
public interface VirtualServerSocket extends VirtualServer {
    //determines if a game with the specified ID has exists already
    /**
     * Checks if a game with the specified ID has started.
     *
     * @param gameID the ID of the game to check
     * @return true if the game has started, false otherwise
     * @throws IOException if an I/O error occurs
     */
    @Override
    boolean startedGame(int gameID) throws IOException;

    //invoked when the first player decides to start the game
    /**
     * Starts a new game with the specified parameters.
     *
     * @param client       the client that is starting the game
     * @param gameID       the ID of the game to start
     * @param firstFlight  true if this is the first flight, false otherwise
     * @param numberPlayers the number of players in the game
     * @throws IOException if an I/O error occurs
     */
    @Override
    void startNewGame(VirtualView client, int gameID, boolean firstFlight, int numberPlayers) throws IOException;

    //invoked when one of the players decides enter the game; the remote client view is added to the list
    //of connected clients
    /**
     * Adds a player to the game.
     *
     * @param client   the client that is joining the game
     * @param gameID   the ID of the game to join
     * @param nickname the nickname of the player
     * @param color    the color of the player
     * @return true if the player was added successfully, false otherwise
     * @throws IOException if an I/O error occurs
     */
    @Override
    boolean addPlayer(VirtualView client, int gameID, String nickname, Color color) throws IOException;

    //invoked when a player wants to pick a component among the one placed face down (assembling phase)
    /**
     * Picks a hidden component for the specified game and player.
     *
     * @param gameID   the ID of the game
     * @param nickname the nickname of the player
     * @throws IOException if an I/O error occurs
     */
    @Override
    void pickHidden(int gameID, String nickname) throws IOException;

    //invoked when a player wants to pick a specific component among the one placed face up (assembling phase)
    /**
     * Picks a shown component for the specified game and player.
     *
     * @param gameID   the ID of the game
     * @param nickname the nickname of the player
     * @param index    the index of the component to pick
     * @throws IOException if an I/O error occurs
     */
    @Override
    void pickShown(int gameID, String nickname, int index) throws IOException;

    //invoked when a player wants to release (therefore, place face up) the component that it has picked
    /**
     * Releases a picked component for the specified game and player.
     *
     * @param gameID   the ID of the game
     * @param nickname the nickname of the player
     * @throws IOException if an I/O error occurs
     */
    @Override
    void putShown(int gameID, String nickname) throws IOException;

    //invoked when a player wants to reserve the component that it has picked for its ship board
    /**
     * Reserves a component for the specified game and player.
     *
     * @param gameID   the ID of the game
     * @param nickname the nickname of the player
     * @throws IOException if an I/O error occurs
     */
    @Override
    void reserveComponent(int gameID, String nickname) throws IOException;

    //invoked when a player wants to pick one of the components that it has reserved for its ship board
    /**
     * Picks a reserved component for the specified game and player.
     *
     * @param gameID   the ID of the game
     * @param nickname the nickname of the player
     * @param position the position of the reserved component to pick
     * @throws IOException if an I/O error occurs
     */
    @Override
    void pickReservedComponent(int gameID, String nickname, int position) throws IOException;

    //invoked when a player wants to change the orientation of the component that it has picked
    /**
     * Rotates the picked component for the specified game and player.
     *
     * @param gameID   the ID of the game
     * @param nickname the nickname of the player
     * @throws IOException if an I/O error occurs
     */
    @Override
    void rotatePickedComponent(int gameID, String nickname) throws IOException;

    //invoked when a player wants to assemble on the ship board the component that it has picked
    /**
     * Assembles a component on the ship board for the specified game and player.
     *
     * @param gameID   the ID of the game
     * @param nickname the nickname of the player
     * @param x        the x coordinate of the position to assemble the component
     * @param y        the y coordinate of the position to assemble the component
     * @throws IOException if an I/O error occurs
     */
    @Override
    void assembledComponent(int gameID, String nickname, int x, int y) throws IOException;

    //invoked when a player wants to pick a deck during the assembling phase to see its content
    /**
     * Picks a deck for the specified game and player.
     *
     * @param gameID       the ID of the game
     * @param nickname     the nickname of the player
     * @param deckNumber   the number of the deck to pick
     * @throws IOException if an I/O error occurs
     */
    @Override
    void pickDeck(int gameID, String nickname, int deckNumber) throws IOException;

    //invoked when a player wants to release the deck it has picked, during the assembling phase
    /**
     * Releases a deck for the specified game and player.
     *
     * @param gameID   the ID of the game
     * @param nickname the nickname of the player
     * @throws IOException if an I/O error occurs
     */
    @Override
    void releaseDeck(int gameID, String nickname) throws IOException;

    //invoked when a player has finished the assembling phase and has to pick a free position on the flight board
    /**
     * Sets the position of a player on the flight board.
     *
     * @param gameID   the ID of the game
     * @param nickname the nickname of the player
     * @param initCell the initial cell position on the flight board
     * @throws IOException if an I/O error occurs
     */
    @Override
    void setPosition(int gameID, String nickname, int initCell) throws IOException;

    //invoked when a player wants to turn around the hourglass
    /**
     * Turns around the hourglass for the specified game and player.
     *
     * @param gameID   the ID of the game
     * @param nickname the nickname of the player
     * @throws IOException if an I/O error occurs
     */
    @Override
    void startNewCycle(int gameID, String nickname) throws IOException;

    //invoked when a player wants to destroy a component in order to validate its ship board or when a
    //component is destroyed due to a cannon shot/meteor attack
    /**
     * Destroys a component on the ship board for the specified game and player.
     *
     * @param gameID   the ID of the game
     * @param nickname the nickname of the player
     * @param x        the x coordinate of the component to destroy
     * @param y        the y coordinate of the component to destroy
     * @throws IOException if an I/O error occurs
     */
    @Override
    void destroyComponent(int gameID, String nickname, int x, int y) throws IOException;

    //invoked when a player wants to initialize a cabin of its shipboard with 2 human crew members
    /**
     * Adds crew members to a cabin on the ship board for the specified game and player.
     *
     * @param gameID   the ID of the game
     * @param nickname the nickname of the player
     * @param x        the x coordinate of the cabin
     * @param y        the y coordinate of the cabin
     * @throws IOException if an I/O error occurs
     */
    @Override
    void addCrew(int gameID, String nickname, int x, int y) throws IOException;

    //invoked when a player wants to initialize a battery container of its shipboard with batteries
    /**
     * Adds batteries to a battery container on the ship board for the specified game and player.
     *
     * @param gameID   the ID of the game
     * @param nickname the nickname of the player
     * @param x        the x coordinate of the battery container
     * @param y        the y coordinate of the battery container
     * @throws IOException if an I/O error occurs
     */
    @Override
    void addBatteries(int gameID, String nickname, int x, int y) throws IOException;

    //invoked when a player wants to initialize a cabin of its shipboard with an alien
    /**
     * Adds an alien to a cabin on the ship board for the specified game and player.
     *
     * @param gameID   the ID of the game
     * @param nickname the nickname of the player
     * @param isPurple true if the alien is purple, false otherwise
     * @param x        the x coordinate of the cabin
     * @param y        the y coordinate of the cabin
     * @throws IOException if an I/O error occurs
     */
    @Override
    void addAlien(int gameID, String nickname, boolean isPurple, int x, int y) throws IOException;

    //invoked when a player wants to pick a new card from the game deck
    /**
     * Picks the next card for the specified game and player.
     *
     * @param gameID   the ID of the game
     * @param nickname the nickname of the player
     * @throws IOException if an I/O error occurs
     */
    @Override
    void pickNextCard(int gameID, String nickname) throws IOException;

    //invoked when a player wants to leave the game
    /**
     * Quits the game for the specified game and player.
     *
     * @param gameID   the ID of the game
     * @param nickname the nickname of the player
     * @throws IOException if an I/O error occurs
     */
    @Override
    void quitGame(int gameID, String nickname) throws IOException;

    //invoked when a player wants to skip an action during the flight phase
    /**
     * Skips the current action for the specified game and player.
     *
     * @param gameID   the ID of the game
     * @param nickname the nickname of the player
     * @throws IOException if an I/O error occurs
     */
    @Override
    void skip(int gameID, String nickname) throws IOException;

    //invoked when a player wants to land on an abandoned ship or station
    /**
     * Lands on an abandoned ship or station for the specified game and player.
     *
     * @param gameID   the ID of the game
     * @param nickname the nickname of the player
     * @param x        the x coordinates of the landing position
     * @param y        the y coordinates of the landing position
     * @param z        the z coordinates of the landing position
     * @throws IOException if an I/O error occurs
     */
    @Override
    void landing(int gameID, String nickname, List<Integer> x, List<Integer> y, List<Integer> z) throws IOException;

    //invoked when th ship board of a player must be hit by meteor/cannon shot
    /**
     * Hits a ship for the specified game and player.
     *
     * @param gameID           the ID of the game
     * @param nickname         the nickname of the player
     * @param diceResult       the result of the dice roll
     * @param activateShield   true if the shield is activated, false otherwise
     * @param activateCannon   true if the cannon is activated, false otherwise
     * @throws IOException if an I/O error occurs
     */
    @Override
    void hitShip(int gameID, String nickname, int diceResult, boolean activateShield, boolean activateCannon) throws IOException;

    //invoked when a player wants to fly across the flight board exploiting its engine strength
    /**
     * Flies across the flight board for the specified game and player.
     *
     * @param gameID         the ID of the game
     * @param nickname       the nickname of the player
     * @param usedBatteries  the number of batteries used for flying
     * @throws IOException if an I/O error occurs
     */
    @Override
    void fly(int gameID, String nickname, int usedBatteries) throws IOException;

    //invoked when a player wants to defeat an enemy; the player can decide whether to lose flight days
    //to gain credits/goods or not

    /**
     * Defeats an enemy for the specified game and player.
     * @param gameID
     * @param nickname
     * @param usedBatteries
     * @param loseDays
     * @throws IOException
     */
    @Override
    void defeat(int gameID, String nickname, int usedBatteries, boolean loseDays) throws IOException;

    //invoked when a player decides to load goods inside cargo hold components of its ship
    /**
     * Loads goods into cargo holds for the specified game and player.
     *
     * @param gameID   the ID of the game
     * @param nickname the nickname of the player
     * @param x        the x coordinates of the cargo holds
     * @param y        the y coordinates of the cargo holds
     * @throws IOException if an I/O error occurs
     */
    @Override
    void loadGoods(int gameID, String nickname, List<Integer> x, List<Integer> y) throws IOException;

    //invoked when a player wants to land on a planet
    /**
     * Lands on a planet for the specified game and player.
     *
     * @param gameID       the ID of the game
     * @param nickname     the nickname of the player
     * @param numberPlanet the number of the planet to land on
     * @throws IOException if an I/O error occurs
     */
    @Override
    void planetLanding(int gameID, String nickname, int numberPlanet) throws IOException;

    //invoked when a player wants to use batteries to declare its engine/cannon strength
    /**
     * Uses batteries for the specified game and player.
     *
     * @param gameID         the ID of the game
     * @param nickname       the nickname of the player
     * @param usedBatteries  the number of batteries used
     * @throws IOException if an I/O error occurs
     */
    @Override
    void useBatteries(int gameID, String nickname, int usedBatteries) throws IOException;
}
