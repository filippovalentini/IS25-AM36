package it.polimi.ingsw.galaxytrucker.network;

//this interface defines the methods that are exploited by a client to interact with the user in order
//to set up a new game or join an existing one
public interface GameSessionManager {
    //this method asks the user whether it wants to start a game or join one; in case of game creation,
    //it takes in input number of players and type of game. Once the game has been created (or an existing one
    //has been found) the method takes in input nickname and color of the player and adds it to the game.
    void run() throws Exception;

    //this method asks the user to start a new game or join an existing one
    boolean requestStartOrJoinGame();

    //this method asks tho the user the parameters to set up a new game and asks the server to create the game
    void requestStartNewGame();

    //this method asks the user for nickname and color and asks the server to add the player to the game
    void requestAddPlayerToGame(boolean userStartsGame);

    //this method asks the server if a game with the specified ID has already started
    boolean isGameStarted(int gameID);

    //this method asks the server to add the player (associated to the client) to the game
    boolean addPlayerToGame(int gameID);

    //this method asks the server to create a new game
    boolean startNewGame(VirtualView client, int gameID, boolean firstFlight, int numberPlayers);
}
