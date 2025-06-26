package it.polimi.ingsw.galaxytrucker.model.exceptions;
//this exception is thrown when a player tries to turn the hourglass while it is still running
public class HourGlassException extends RuntimeException {
    public HourGlassException(String message) {
        super(message);
    }
}
