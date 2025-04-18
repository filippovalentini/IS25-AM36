package it.polimi.ingsw.galaxytrucker.network.socket.message;

public enum PlayerActionType {
    ADD_PLAYER,
    //components
    PICK_HIDDEN,
    PICK_SHOWN,
    RELEASE,
    RESERVE,
    PICK_RESERVED,
    ROTATE,
    ASSEMBLE,
    //decks
    PICK_DECK,
    RELEASE_DECK,
    //others
    SET_POSITION
}
