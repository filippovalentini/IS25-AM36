package it.polimi.ingsw.galaxytrucker.model.exceptions;
//this exception is thrown when a player tries to add an alien in a non-life-supported cabin
public class NoLifeSupportException extends RuntimeException {
    public NoLifeSupportException(String message) {
        super(message);
    }
}
