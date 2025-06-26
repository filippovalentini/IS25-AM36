package it.polimi.ingsw.galaxytrucker.model.exceptions;
//this exception is thrown when a player tries to pick a deck that is in the hand of another player
public class PickedDeckException extends RuntimeException {
    public PickedDeckException(String message) {
        super(message);
    }
}
