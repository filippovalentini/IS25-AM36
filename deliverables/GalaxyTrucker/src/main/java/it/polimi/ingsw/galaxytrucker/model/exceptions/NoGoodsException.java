package it.polimi.ingsw.galaxytrucker.model.exceptions;
//this exception is thrown when a player tries to load goods in non-cargo-hold components
public class NoGoodsException extends RuntimeException {
    public NoGoodsException(String message) {
        super(message);
    }
}
