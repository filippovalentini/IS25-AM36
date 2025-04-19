package it.polimi.ingsw.galaxytrucker.network.socket.message;

import java.io.Serializable;
import java.util.List;

public interface GameMessage extends Serializable {
    public String getGameParams(int paramIndex);
    public List<String> getGameParams();
}
