package it.polimi.ingsw.galaxytrucker.model.exceptions;
//this exception is thrown when a player is trying to pick a card from an empty deck
public class EmptyDeckException extends RuntimeException {
    public EmptyDeckException(String message) {
        super(message);
    }
}
