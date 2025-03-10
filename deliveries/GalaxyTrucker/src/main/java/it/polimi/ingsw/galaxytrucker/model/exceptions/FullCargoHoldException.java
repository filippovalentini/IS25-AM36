package it.polimi.ingsw.galaxytrucker.model.exceptions;

public class FullCargoHoldException extends RuntimeException {
    public FullCargoHoldException(String message) {
        super(message);
    }
}