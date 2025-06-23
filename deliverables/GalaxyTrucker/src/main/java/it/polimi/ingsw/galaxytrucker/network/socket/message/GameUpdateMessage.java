package it.polimi.ingsw.galaxytrucker.network.socket.message;

import java.util.List;

//this message defines the format of a server-to-client message related to game updates
public class GameUpdateMessage implements GameMessage {
    private final GameUpdateType gameUpdateType;  //type of update message
    private final List<String> gameUpdateParams; //message parameters

    public GameUpdateMessage(GameUpdateType gameUpdateType, List<String> gameUpdateParams) {
        this.gameUpdateType = gameUpdateType;
        this.gameUpdateParams = gameUpdateParams;
    }

    //returns the type of update message
    public GameUpdateType getGameUpdateType() {
        return gameUpdateType;
    }

    //returns one of the parameters of the message
    @Override
    public String getGameParams(int paramIndex) {
        return gameUpdateParams.get(paramIndex);
    }

    //returns the parameters of the message
    @Override
    public List<String> getGameParams() {
        return gameUpdateParams;
    }
}
