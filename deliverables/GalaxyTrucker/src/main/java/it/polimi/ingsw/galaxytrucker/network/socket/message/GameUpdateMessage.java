package it.polimi.ingsw.galaxytrucker.network.socket.message;

import java.util.List;

//this message defines the format of a server-to-client message related to game updates

/**
 * This class represents a message sent from the server to the client to update the game state.
 */
public class GameUpdateMessage implements GameMessage {
    private final GameUpdateType gameUpdateType;  //type of update message
    private final List<String> gameUpdateParams; //message parameters

    /**
     * Constructor for GameUpdateMessage.
     * @param gameUpdateType
     * @param gameUpdateParams
     */
    public GameUpdateMessage(GameUpdateType gameUpdateType, List<String> gameUpdateParams) {
        this.gameUpdateType = gameUpdateType;
        this.gameUpdateParams = gameUpdateParams;
    }

    //returns the type of update message
    /**
     * Gets the type of game update message.
     * @return the type of game update message
     */
    public GameUpdateType getGameUpdateType() {
        return gameUpdateType;
    }

    //returns one of the parameters of the message
    /**
     * Gets a specific game parameter by index.
     * @param paramIndex the index of the parameter to retrieve
     * @return the game parameter at the specified index
     */
    @Override
    public String getGameParams(int paramIndex) {
        return gameUpdateParams.get(paramIndex);
    }

    //returns the parameters of the message
    /**
     * Gets the list of game parameters.
     * @return the list of game parameters
     */
    @Override
    public List<String> getGameParams() {
        return gameUpdateParams;
    }
}
