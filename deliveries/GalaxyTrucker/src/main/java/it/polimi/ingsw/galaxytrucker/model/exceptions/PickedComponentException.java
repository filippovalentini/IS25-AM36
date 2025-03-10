package it.polimi.ingsw.galaxytrucker.model.exceptions;
//this exception is thrown when a player is trying to pick more than one component at the same time (during the assembling phase)
//ore when it is trying to place/assemble a component before picking it
public class PickedComponentException extends RuntimeException {
    public PickedComponentException(String message) {
        super(message);
    }
}
