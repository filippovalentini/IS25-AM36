package it.polimi.ingsw.galaxytrucker.network.socket.message;

import java.io.Serializable;
import java.util.List;

//this interface is implemented by the classes defining the serializable messages user for communication
//through socket
public interface GameMessage extends Serializable {
    //returns one of the parameters of the message
    String getGameParams(int paramIndex);

    //returns the parameters of the message
    List<String> getGameParams();
}
