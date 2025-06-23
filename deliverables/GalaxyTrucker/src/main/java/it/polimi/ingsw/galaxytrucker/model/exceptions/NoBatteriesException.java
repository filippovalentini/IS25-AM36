package it.polimi.ingsw.galaxytrucker.model.exceptions;
//this exception is thrown when a player is trying to use batteries form an empty battery container
public class NoBatteriesException extends RuntimeException{
    public NoBatteriesException(String message) {
        super(message);
    }
}
