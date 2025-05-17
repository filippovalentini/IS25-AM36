package it.polimi.ingsw.galaxytrucker.network.socket.message;

import java.util.List;

//this message defines the format of a client-to-server message related to user actions
public class PlayerActionMessage implements GameMessage {
    private final PlayerActionType gameAction; //type of user action
    private final List<String> gameActionParams; //message parameters

    public PlayerActionMessage(PlayerActionType gameAction, List<String> gameActionParams) {{
        this.gameAction = gameAction;
        this.gameActionParams = gameActionParams;}
    }

    //returns the type of action message
    public PlayerActionType getGameAction() {
        return gameAction;
    }

    //returns one of the parameters of the message
    @Override
    public String getGameParams(int paramIndex) {
        return gameActionParams.get(paramIndex);
    }

    //returns the parameters of the message
    @Override
    public List<String> getGameParams() {
        return gameActionParams;
    }
}
