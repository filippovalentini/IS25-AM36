package it.polimi.ingsw.galaxytrucker.network;

import it.polimi.ingsw.galaxytrucker.model.enumerations.Color;

//this interface defines the methods that are exploited by a client to interact with the user in order
//to set up a new game or join an existing one
public interface GameSessionManager {
    //this method asks the server if a game with the specified ID has already started
    boolean askIfGameStarted(int gameID);

    //this method asks the server to add the player (associated to the client) to the game
    boolean tryToAddPlayerToGame(int gameID, String nickname, Color color);

    //this method asks the server to create a new game
    boolean tryToStartNewGame(VirtualView client, int gameID, boolean firstFlight, int numberPlayers);
}
