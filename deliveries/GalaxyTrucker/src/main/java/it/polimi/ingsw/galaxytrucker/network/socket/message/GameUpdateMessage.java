package it.polimi.ingsw.galaxytrucker.network.socket.message;

import java.util.List;

public class GameUpdateMessage implements GameMessage {
    /*
        server-to-client messages related to game updates
     */
    private GameUpdateType gameUpdateType;
    private List<String> gameUpdateParams; //additional game update params
    public GameUpdateMessage(GameUpdateType gameUpdateType, List<String> gameUpdateParams) {
        this.gameUpdateType = gameUpdateType;
        this.gameUpdateParams = gameUpdateParams;
    }

    public GameUpdateType getGameUpdateType() {
        return gameUpdateType;
    }

    @Override
    public String getGameParams(int paramIndex) {
        return gameUpdateParams.get(paramIndex);
    }
}
