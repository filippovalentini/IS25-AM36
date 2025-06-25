package it.polimi.ingsw.galaxytrucker.network.socket.message;

import java.io.Serializable;
import java.util.List;


/**
 * This interface is implemented by the classes defining the serializable messages user for communication through socket
 */
public interface GameMessage extends Serializable {
    //returns one of the parameters of the message

    /**
     * Returns one of the parameters of the message
     * @param paramIndex
     * @return the parameter at the specified index
     */
    String getGameParams(int paramIndex);

    //returns the parameters of the message
    /**
     * Returns the parameters of the message
     * @return a list of parameters
     */
    List<String> getGameParams();
}
