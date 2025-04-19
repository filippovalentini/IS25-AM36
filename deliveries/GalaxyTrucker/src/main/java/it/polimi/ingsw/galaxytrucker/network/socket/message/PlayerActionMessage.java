package it.polimi.ingsw.galaxytrucker.network.socket.message;

import java.util.List;

public class PlayerActionMessage implements GameMessage {
    /*
        client-to-server messages related to players actions
     */
    private PlayerActionType gameAction; //action type
    private List<String> gameActionParams; //additional data related to action

    public PlayerActionMessage(PlayerActionType gameAction, List<String> gameActionParams) {{
        this.gameAction = gameAction;
        this.gameActionParams = gameActionParams;}
    }

    public PlayerActionType getGameAction() {
        return gameAction;
    }

    @Override
    public String getGameParams(int paramIndex) {
        return gameActionParams.get(paramIndex);
    }

    @Override
    public List<String> getGameParams() {
        return gameActionParams;
    }
}
