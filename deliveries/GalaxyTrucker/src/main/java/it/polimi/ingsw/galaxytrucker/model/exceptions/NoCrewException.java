package it.polimi.ingsw.galaxytrucker.model.exceptions;
//this exception is thrown when a player is trying to remove more crew members than the ones contained in a cabin
public class NoCrewException extends RuntimeException {
    public NoCrewException(String message) {
        super(message);
    }
}
