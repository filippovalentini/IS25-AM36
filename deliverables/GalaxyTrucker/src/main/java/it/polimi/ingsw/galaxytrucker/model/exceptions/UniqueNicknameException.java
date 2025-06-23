package it.polimi.ingsw.galaxytrucker.model.exceptions;

public class UniqueNicknameException extends RuntimeException {
    public UniqueNicknameException(String message) {
        super(message);
    }
}
