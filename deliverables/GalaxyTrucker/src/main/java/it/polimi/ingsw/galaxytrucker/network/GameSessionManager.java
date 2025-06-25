package it.polimi.ingsw.galaxytrucker.network;

import it.polimi.ingsw.galaxytrucker.model.enumerations.Color;

//this interface defines the methods that are exploited by a client to interact with the user in order
//to set up a new game or join an existing one

/**
 * This interface defines the methods that are used by the clients to interact with the server,
 */
public interface GameSessionManager {
    //this method asks the server if a game with the specified ID has already started

    /**
     * Checks if a game with the specified ID has already started.
     * @param gameID
     * @return true if the game has started, false otherwise
     */
    boolean askIfGameStarted(int gameID);

    //this method asks the server to add the player (associated to the client) to the game

    /**
     * Attempts to add a player to the game with the specified ID.
     * @param gameID
     * @param nickname
     * @param color
     * @return true if the player was successfully added, false otherwise
     */
    boolean tryToAddPlayerToGame(int gameID, String nickname, Color color);

    //this method asks the server to create a new game
    /**
     * Attempts to start a new game with the specified parameters.
     * @param client
     * @param gameID
     * @param firstFlight
     * @param numberPlayers
     * @return true if the game was successfully started, false otherwise
     */
    boolean tryToStartNewGame(VirtualView client, int gameID, boolean firstFlight, int numberPlayers);
}
