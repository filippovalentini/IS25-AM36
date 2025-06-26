package it.polimi.ingsw.galaxytrucker.model.exceptions;
//this action is thrown when a player tries to access the game with a nickname that has already been chosen
public class UniqueNicknameException extends RuntimeException {
    public UniqueNicknameException(String message) {
        super(message);
    }
}
