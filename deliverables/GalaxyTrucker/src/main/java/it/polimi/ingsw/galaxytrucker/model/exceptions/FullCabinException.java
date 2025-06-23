package it.polimi.ingsw.galaxytrucker.model.exceptions;
//this exception is thrown when a player is trying to add crew members or an alien in a full cabin (it contains already
//2 crew members or an alien)
public class FullCabinException extends RuntimeException {
    public FullCabinException(String message) {
        super(message);
    }
}
