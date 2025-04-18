package it.polimi.ingsw.galaxytrucker.network.socket.message;

import java.io.Serializable;

public interface GameMessage extends Serializable {
    public String getGameParams(int paramIndex);
}
