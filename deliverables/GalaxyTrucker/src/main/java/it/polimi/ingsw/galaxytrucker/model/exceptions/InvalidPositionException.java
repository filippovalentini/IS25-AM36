package it.polimi.ingsw.galaxytrucker.model.exceptions;
//this exception is thrown when a player tries to place its ship in an invalid position of the flight board
public class InvalidPositionException extends RuntimeException {
    public InvalidPositionException(String message) {
        super(message);
    }
}
