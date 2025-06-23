package it.polimi.ingsw.galaxytrucker.model.enumerations;
/**
 * Enumeration representing the different states of the game.
 * Each state corresponds to a specific phase in the game flow.
 */
public enum State {
    WAITING_FOR_PLAYERS, // Waiting for players to join
    SHIP_BUILDING, // Players are building their ships
    SHIP_PLACEMENT, // Players are placing their ships on the flight board
    SHIP_CONTROL, // Players are controlling ships
    CARD_PICKING,   //A new card must be picked
    CARD_SOLVING, // Players are solving cards
    SHIP_REPAIR,  // Player is repairing its damaged ship
    END // Game is over
}
