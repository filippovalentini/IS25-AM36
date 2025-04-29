package it.polimi.ingsw.galaxytrucker.network.socket.message;

import java.io.Serializable;
import java.util.List;

public interface GameMessage extends Serializable {
    String getGameParams(int paramIndex);
    List<String> getGameParams();
}
