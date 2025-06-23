package it.polimi.ingsw.galaxytrucker.ui.gui.controllerInterfaces;

import it.polimi.ingsw.galaxytrucker.network.VirtualServer;

import java.util.List;

public interface FlightBoardController extends GuiController {
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

    //notifies the view that all the players have concluded the assembling phase, which means that the players
    //enter the ship control phase
    void updateShipControl() throws Exception;

    //notifies the view that a player has to repair its ship board before the player in turn can pick a new card
    void updateShipRepair(String nickname) throws Exception;

    //notifies the view about the fact that a player has to pick a card in order to continue the game
    void updateCardPicking() throws Exception;

    //notifies the view that a new card has been picked and must be solved
    void updateCardSolving(int imageID) throws Exception;

    //notifies the view that the position of a player has changed
    void updatePlayerPosition(String nickname, int cell) throws Exception;

    //notifies the view about the fact that the game is finished
    void updateEndGame() throws Exception;
}
