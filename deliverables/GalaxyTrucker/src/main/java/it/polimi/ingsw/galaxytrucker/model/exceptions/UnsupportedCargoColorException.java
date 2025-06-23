package it.polimi.ingsw.galaxytrucker.model.exceptions;
//this exception is thrown when a player is trying to add a red good to a normal cargo hold
public class UnsupportedCargoColorException extends RuntimeException {
    public UnsupportedCargoColorException(String message) {
        super(message);
    }
}