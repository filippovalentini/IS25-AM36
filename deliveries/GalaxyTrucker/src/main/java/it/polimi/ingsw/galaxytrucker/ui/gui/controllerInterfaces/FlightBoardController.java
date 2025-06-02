package it.polimi.ingsw.galaxytrucker.ui.gui.controllerInterfaces;

import it.polimi.ingsw.galaxytrucker.network.VirtualServer;

import java.util.List;

public interface FlightBoardController extends GuiController {
    @Override
    void setServer(VirtualServer server);

    //notifies a view about an error committed while executing a method on the remote server; the parameter
    //errorMessage describes the type of error
    void notifyError(String errorMessage) throws Exception;

    //notifies the view about the fact that the corresponding player has successfully picked a deck; the parameter
    //contains the list of image IDs of the cards contained in the deck, so that the view can show the
    //correct adventure cards to the user
    void updatePickedDeck(List<Integer> deckIDs) throws Exception;

    //notifies the view about the fact that the corresponding player has successfully released a deck
    void updateReleasedDeck() throws Exception;

    //notifies the view about the fact that a player has finished the assembling phase and is
    //correctly positioned on the flight board; still, other players have to finish building their ships
    void updateFinishAssembling(String nickname, int position) throws Exception;

    //notifies the view that the hourglass has been turned around
    void updateStartNewCycle() throws Exception;

    //notifies the view that the hourglass has finished running
    void updateFinishedCycle() throws Exception;
}
