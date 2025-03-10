package it.polimi.ingsw.galaxytrucker.model.exceptions;
//this exception is thrown when a player is trying to add goods to a full cargo hold
public class FullCargoHoldException extends RuntimeException {
    public FullCargoHoldException(String message) {
        super(message);
    }
}