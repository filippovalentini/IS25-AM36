package it.polimi.ingsw.galaxytrucker.model.exceptions;
//this exception is thrown when a player is making an error while accessing the reserved components
public class ReservedComponentException extends RuntimeException {
    public ReservedComponentException(String message) {
        super(message);
    }
}
