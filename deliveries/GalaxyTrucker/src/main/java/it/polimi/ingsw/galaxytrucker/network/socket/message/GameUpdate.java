package it.polimi.ingsw.galaxytrucker.network.socket.message;

public class GameUpdate implements GameMessage {
    /*
        server-to-client messages related to game updates
     */
    private GameUpdateType gameUpdateType;
    private String gameUpdateParams; //additional game update params
    public GameUpdate(GameUpdateType gameUpdateType, String gameUpdateParams) {
        this.gameUpdateType = gameUpdateType;
        this.gameUpdateParams = gameUpdateParams;
    }

    public GameUpdateType getGameUpdateType() {
        return gameUpdateType;
    }

    public String getGameUpdateParams() {
        return gameUpdateParams;
    }
}
