package it.polimi.ingsw.galaxytrucker.network.socket.message;

import java.util.List;

//this message defines the format of a client-to-server message related to user actions

/**
 * This class represents a message sent by the player to the server, containing the type of action
 */
public class PlayerActionMessage implements GameMessage {
    private final PlayerActionType gameAction; //type of user action
    private final List<String> gameActionParams; //message parameters

    /**
     * Constructor for PlayerActionMessage
     * @param gameAction
     * @param gameActionParams
     */
    public PlayerActionMessage(PlayerActionType gameAction, List<String> gameActionParams) {{
        this.gameAction = gameAction;
        this.gameActionParams = gameActionParams;}
    }

    //returns the type of action message
    /**
     * Gets the type of action performed by the player
     * @return the type of action
     */
    public PlayerActionType getGameAction() {
        return gameAction;
    }

    //returns one of the parameters of the message
    /**
     * Gets a specific parameter of the action message
     * @param paramIndex the index of the parameter to retrieve
     * @return the parameter at the specified index
     */
    @Override
    public String getGameParams(int paramIndex) {
        return gameActionParams.get(paramIndex);
    }

    //returns the parameters of the message
    /**
     * Gets all parameters of the action message
     * @return a list of parameters
     */
    @Override
    public List<String> getGameParams() {
        return gameActionParams;
    }
}
