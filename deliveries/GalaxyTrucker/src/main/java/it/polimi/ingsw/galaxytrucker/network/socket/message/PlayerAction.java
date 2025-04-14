package it.polimi.ingsw.galaxytrucker.network.socket.message;

public class PlayerAction implements GameMessage {
    /*
        client-to-server messages related to players actions
     */
    private PlayerActionType gameAction; //action type
    private String gameActionParams; //additional data related to action

    public PlayerAction(PlayerActionType gameAction, String gameActionParams) {{
        this.gameAction = gameAction;
        this.gameActionParams = gameActionParams;}
    }

    public PlayerActionType getGameAction() {
        return gameAction;
    }

    public String getGameActionParams() {
        return gameActionParams;
    }
}
