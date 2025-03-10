package it.polimi.ingsw.galaxytrucker.model.exceptions;

public class NoBatteriesException extends RuntimeException{
    public NoBatteriesException(String message) {
        super(message);
    }
}
