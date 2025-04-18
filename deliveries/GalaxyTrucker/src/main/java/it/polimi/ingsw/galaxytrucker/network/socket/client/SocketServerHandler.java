package it.polimi.ingsw.galaxytrucker.network.socket.client;

import it.polimi.ingsw.galaxytrucker.model.enumerations.Color;
import it.polimi.ingsw.galaxytrucker.network.VirtualView;

import java.io.IOException;
import java.io.PrintWriter;

public class SocketServerHandler implements VirtualServerSocket {
    final PrintWriter output;

    public SocketServerHandler(PrintWriter output) {
        this.output = output;
    }

    //invoked when one of the players decides enter the game; the remote client view is added to the list
    //of connected clients
    @Override
    public boolean addPlayer(VirtualView client, String nickname, Color color) throws IOException{return true;}

    //invoked when a player wants to pick a component among the one placed face down (assembling phase)
    @Override
    public void pickHidden(String nickname) throws IOException{}

    //invoked when a player wants to pick a specific component among the one placed face up (assembling phase)
    @Override
    public void pickShown(String nickname, int index) throws IOException{}

    //invoked when a player wants to release (therefore, place face up) the component that it has picked
    @Override
    public void putShown(String nickname) throws IOException{}

    //invoked when a player wants to reserve the component that it has picked for its ship board
    @Override
    public void reserveComponent(String nickname) throws IOException{}

    //invoked when a player wants to pick one of the components that it has reserved for its ship board
    @Override
    public void pickReservedComponent(String nickname, int position) throws IOException{}

    //invoked when a player wants to change the orientation of the component that it has picked
    @Override
    public void rotatePickedComponent(String nickname) throws IOException{}

    //invoked when a player wants to assemble on the ship board the component that it has picked
    @Override
    public void assembledComponent(String nickname, int x, int y) throws IOException{}

    //invoked when a player wants to pick a deck during the assembling phase to see its content
    @Override
    public void pickDeck(String nickname, int deckNumber) throws IOException{}

    //invoked when a player wants to release the deck it has picked, during the assembling phase
    @Override
    public void releaseDeck(String nickname) throws IOException{}

    //invoked when a player has finished the assembling phase and has to pick a free position on the flight board
    @Override
    public void setPosition(String nickname, int initCell) throws IOException {}

}
