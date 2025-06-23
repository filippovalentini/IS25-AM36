package it.polimi.ingsw.galaxytrucker.model.exceptions;
//this exception is thrown when a player is trying to assemble a component in a non-free space
public class AssembledComponentException extends RuntimeException {
    public AssembledComponentException(String message) {
        super(message);
    }
}
