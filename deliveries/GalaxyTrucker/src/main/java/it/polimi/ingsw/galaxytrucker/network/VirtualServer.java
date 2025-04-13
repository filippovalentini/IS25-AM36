package it.polimi.ingsw.galaxytrucker.network;

import it.polimi.ingsw.galaxytrucker.model.enumerations.Color;
import it.polimi.ingsw.galaxytrucker.network.rmi.server.VirtualViewRMI;

//this interface defines the methods that are used by the clients to interact with the server, in order to change
//the state of the model

public interface VirtualServer {
    //invoked when one of the players decides enter the game; the remote client view is added to the list
    //of connected clients
    boolean addPlayer(VirtualViewRMI client, String nickname, Color color) throws Exception;

    //invoked when a player wants to pick a component among the one placed face down (assembling phase)
    void pickHidden(String nickname) throws Exception;

    //invoked when a player wants to pick a specific component among the one placed face up (assembling phase)
    void pickShown(String nickname, int index) throws Exception;

    //invoked when a player wants to release (therefore, place face up) the component that it has picked
    void putShown(String nickname) throws Exception;

    //invoked when a player wants to reserve the component that it has picked for its ship board
    void reserveComponent(String nickname) throws Exception;

    //invoked when a player wants to pick one of the components that it has reserved for its ship board
    void pickReservedComponent(String nickname, int position) throws Exception;

    //invoked when a player wants to change the orientation of the component that it has picked
    void rotatePickedComponent(String nickname) throws Exception;

    //invoked when a player wants to assemble on the ship board the component that it has picked
    void assembledComponent(String nickname, int x, int y) throws Exception;

    //invoked when a player wants to pick a deck during the assembling phase to see its content
    void pickDeck(String nickname, int deckNumber) throws Exception;

    //invoked when a player wants to release the deck it has picked, during the assembling phase
    void releaseDeck(String nickname) throws Exception;

    //invoked when a player has finished the assembling phase and has to pick a free position on the flight board
    void setPosition(String nickname, int initCell) throws Exception;
}
