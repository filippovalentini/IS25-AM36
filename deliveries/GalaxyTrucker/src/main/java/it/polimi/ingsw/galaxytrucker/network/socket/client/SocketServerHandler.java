package it.polimi.ingsw.galaxytrucker.network.socket.client;

import it.polimi.ingsw.galaxytrucker.model.enumerations.Color;
import it.polimi.ingsw.galaxytrucker.network.VirtualView;
import it.polimi.ingsw.galaxytrucker.network.socket.message.PlayerActionMessage;
import it.polimi.ingsw.galaxytrucker.network.socket.message.PlayerActionType;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

//this class contains the methods to create and send serialized messages to the server starting from given
//parameters
public class SocketServerHandler implements VirtualServerSocket {
    final ObjectOutputStream out;

    public SocketServerHandler(ObjectOutputStream out) {
        this.out = out;
    }

    //closes the client's output stream
    public void close() throws IOException {
        this.out.close();
    }

    //invoked when one of the players decides enter the game; the remote client view is added to the list
    //of connected clients
    @Override
    public boolean addPlayer(VirtualView client, String nickname, Color color) throws IOException{
        List<String> params = new ArrayList<>(Arrays.asList(nickname, color.toString()));
        PlayerActionMessage message = new PlayerActionMessage(PlayerActionType.ADD_PLAYER, params);
        out.writeObject(message);
        return true;
    }

    //invoked when a player wants to pick a component among the one placed face down (assembling phase)
    @Override
    public void pickHidden(String nickname) throws IOException{
        List<String> params = new ArrayList<>(Arrays.asList(nickname));
        PlayerActionMessage message = new PlayerActionMessage(PlayerActionType.PICK_HIDDEN, params);
        out.writeObject(message);
    }

    //invoked when a player wants to pick a specific component among the one placed face up (assembling phase)
    @Override
    public void pickShown(String nickname, int index) throws IOException{
        List<String> params = new ArrayList<>(Arrays.asList(nickname, String.valueOf(index)));
        PlayerActionMessage message = new PlayerActionMessage(PlayerActionType.PICK_SHOWN, params);
        out.writeObject(message);
    }

    //invoked when a player wants to release (therefore, place face up) the component that it has picked
    @Override
    public void putShown(String nickname) throws IOException{
        List<String> params = new ArrayList<>(Arrays.asList(nickname));
        PlayerActionMessage message = new PlayerActionMessage(PlayerActionType.RELEASE, params);
        out.writeObject(message);
    }

    //invoked when a player wants to reserve the component that it has picked for its ship board
    @Override
    public void reserveComponent(String nickname) throws IOException{
        List<String> params = new ArrayList<>(Arrays.asList(nickname));
        PlayerActionMessage message = new PlayerActionMessage(PlayerActionType.RESERVE, params);
        out.writeObject(message);
    }

    //invoked when a player wants to pick one of the components that it has reserved for its ship board
    @Override
    public void pickReservedComponent(String nickname, int position) throws IOException{
        List<String> params = new ArrayList<>(Arrays.asList(nickname, String.valueOf(position)));
        PlayerActionMessage message = new PlayerActionMessage(PlayerActionType.PICK_RESERVED, params);
        out.writeObject(message);
    }

    //invoked when a player wants to change the orientation of the component that it has picked
    @Override
    public void rotatePickedComponent(String nickname) throws IOException{
        List<String> params = new ArrayList<>(Arrays.asList(nickname));
        PlayerActionMessage message = new PlayerActionMessage(PlayerActionType.ROTATE, params);
        out.writeObject(message);
    }

    //invoked when a player wants to assemble on the ship board the component that it has picked
    @Override
    public void assembledComponent(String nickname, int x, int y) throws IOException{
        List<String> params = new ArrayList<>(Arrays.asList(nickname, String.valueOf(x), String.valueOf(y)));
        PlayerActionMessage message = new PlayerActionMessage(PlayerActionType.ASSEMBLE, params);
        out.writeObject(message);
    }

    //invoked when a player wants to pick a deck during the assembling phase to see its content
    @Override
    public void pickDeck(String nickname, int deckNumber) throws IOException{
        List<String> params = new ArrayList<>(Arrays.asList(nickname, String.valueOf(deckNumber)));
        PlayerActionMessage message = new PlayerActionMessage(PlayerActionType.PICK_DECK, params);
        out.writeObject(message);
    }

    //invoked when a player wants to release the deck it has picked, during the assembling phase
    @Override
    public void releaseDeck(String nickname) throws IOException{
        List<String> params = new ArrayList<>(Arrays.asList(nickname));
        PlayerActionMessage message = new PlayerActionMessage(PlayerActionType.RELEASE_DECK, params);
        out.writeObject(message);
    }

    //invoked when a player has finished the assembling phase and has to pick a free position on the flight board
    @Override
    public void setPosition(String nickname, int initCell) throws IOException {
        List<String> params = new ArrayList<>(Arrays.asList(nickname, String.valueOf(initCell)));
        PlayerActionMessage message = new PlayerActionMessage(PlayerActionType.SET_POSITION, params);
        out.writeObject(message);
    }

}
