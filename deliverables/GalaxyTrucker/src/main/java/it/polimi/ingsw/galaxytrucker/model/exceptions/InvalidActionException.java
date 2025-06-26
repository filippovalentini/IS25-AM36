package it.polimi.ingsw.galaxytrucker.model.exceptions;
//this is a general exception that is thrown when a player wants to perform an action that is not valid according
//to the curren game state/phase
public class InvalidActionException extends RuntimeException {
    public InvalidActionException(String message) {
        super(message);
    }
}
