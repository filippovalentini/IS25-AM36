package it.polimi.ingsw.galaxytrucker.model.exceptions;
//this action is thrown when a player tries to access the game with a color that has already been chosen
public class UniquePlayerColorException extends RuntimeException {
    public UniquePlayerColorException(String message) {
        super(message);
    }
}
