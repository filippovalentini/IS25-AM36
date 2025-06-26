package it.polimi.ingsw.galaxytrucker.model.exceptions;
//this exception is thrown when a player haz zero engine strength
public class NoStrengthException extends RuntimeException {
    public NoStrengthException(String message) {
        super(message);
    }
}
