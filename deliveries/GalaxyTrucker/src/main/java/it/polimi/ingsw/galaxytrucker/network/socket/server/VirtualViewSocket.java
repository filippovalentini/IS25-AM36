package it.polimi.ingsw.galaxytrucker.network.socket.server;

import it.polimi.ingsw.galaxytrucker.model.enumerations.Color;
import it.polimi.ingsw.galaxytrucker.model.enumerations.Orientation;
import it.polimi.ingsw.galaxytrucker.network.VirtualServer;
import it.polimi.ingsw.galaxytrucker.network.VirtualView;

import java.io.IOException;
import java.util.List;

public interface VirtualViewSocket extends VirtualView {
    //notifies a view about an error committed while executing a method on the remote server; the parameter
    //errorMessage describes the type of error
    @Override
    void notifyError(String errorMessage) throws IOException;

    //notifies a view about the fact that the corresponding player has been correctly added to the game, but
    //the server is waiting for other players in order to start the assembling phase; the parameter firstFlight
    //in needed for the view to determine which type of ship board/flight board to show to the user
    @Override
    void updateWaitingForPlayers(boolean firstFlight) throws IOException;

    //notifies a view about the presence of another player in the game; this method is invoked before the
    //beginning of the assembling phase, therefore just the nickname and color of the new player is required
    @Override
    void updateNewPlayer(String nickname, Color color) throws IOException;

    //notifies a view about the beginning of the assembling phase
    @Override
    void updateStartAssembling() throws IOException;

    //notifies the view about the fact that a component has been successfully picked/released (depending on
    //the value of the boolean parameter) by the corresponding player; the parameter imageID is needed for the
    //view in order to show the right component to the user
    @Override
    void updatePickedComponent(int imageID, boolean released) throws IOException;

    //notifies the view about the fact that a shown component has been picked/released (depending on the value
    //of the boolean parameter); the parameter imageID is needed for the view in order to show the right
    //component to the user
    @Override
    void updateShownComponent(int imageID, boolean released) throws IOException;

    //notifies the view about the fact that a player (identified by the nickname parameter) has picked a reserved
    //component/ reserved a component (depending on the value of the boolean parameter); the parameter imageID
    //is needed for the view in order to show the right component to the user
    @Override
    void updateReservedComponent(String nickname, int imageID, boolean released) throws IOException;

    //notifies the view about the fact that the picked component of the corresponding player has been rotated
    @Override
    void updateRotatePickedComponent() throws IOException;

    //notifies the view about the fact that a player (identified by the nickname parameter) has assembled a
    //component in position (x,y) of its ship board; the parameter imageID is needed for the view in order
    //to show the right component to the user
    @Override
    void updateAssembledComponent(String nickname, int imageID, Orientation orientation, int x, int y) throws IOException;

    //notifies the view about the fact that the corresponding player has successfully picked a deck; the parameter
    //contains the list of image IDs of the cards contained in the deck, so that the view can show the
    //correct adventure cards to the user
    @Override
    void updatePickedDeck(List<Integer> deckIDs) throws IOException;

    //notifies the view about the fact that the corresponding player has successfully released a deck
    @Override
    void updateReleasedDeck() throws IOException;

    //notifies the view about the fact that the corresponding player has finished the assembling phase and is
    //correctly positioned on the flight board; still, other players have to finish building their ships
    @Override
    void updateFinishAssembling(String nickname, int position) throws IOException;

    //notifies the view that all the players have concluded the assembling phase, which means that the players
    //enter the ship control phase
    @Override
    void updateShipControl() throws IOException;

    //runs a command line interface to send requests to the server
    @Override
    void runCli(VirtualServer server) throws IOException;
}
