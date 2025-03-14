package it.polimi.ingsw.galaxytrucker.model.enumerations;

public enum State {
    WAITING_FOR_PLAYERS, // Waiting for players to join
    SHIP_BUILDING, // Players are building their ships
    SHIP_CONTROL, // Players are controlling ships
    CARD_SOLVING, // Players are solving cards
    END // Game is over
}
