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
    DESTROY,
    //decks
    PICK_DECK,
    RELEASE_DECK,
    //others
    SET_POSITION,
    HOURGLASS,
    QUIT,
    PICK_CARD,
    ADD_CREW,
    ADD_ALIEN,
    ADD_BATTERIES,
    SKIP,
    LANDING,
    HIT_SHIP,
    FLY,
    DEFEAT,
    LOAD_GOODS,
    PLANET_LANDING,
    USE_BATTERIES
}
