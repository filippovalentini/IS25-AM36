package it.polimi.ingsw.galaxytrucker.ui.gui.controllerInterfaces;

import it.polimi.ingsw.galaxytrucker.network.VirtualServer;

import java.util.List;
/**
 * This interface defines the methods that the FlightBoardController must implement in order to notify the view
 * about changes in the flight board phase of the game.
 */
public interface FlightBoardController extends GuiController {
    //notifies the view about the fact that the corresponding player has successfully picked a deck; the parameter
    //contains the list of image IDs of the cards contained in the deck, so that the view can show the
    //correct adventure cards to the user
    /**
     * Updates the view with the IDs of the picked deck.
     *
     * @param deckIDs List of integers representing the IDs of the cards in the picked deck.
     * @throws Exception if an error occurs during the update.
     */
    void updatePickedDeck(List<Integer> deckIDs) throws Exception;

    //notifies the view about the fact that the corresponding player has successfully released a deck
    /**
     * Updates the view with the IDs of the released deck.
     *
     * @throws Exception if an error occurs during the update.
     */
    void updateReleasedDeck() throws Exception;

    //notifies the view about the fact that a player has finished the assembling phase and is
    //correctly positioned on the flight board; still, other players have to finish building their ships
    /**
     * Updates the view that a player has finished assembling their ship.
     *
     * @param nickname The nickname of the player who finished assembling.
     * @param position The position on the flight board where the player is placed.
     * @throws Exception if an error occurs during the update.
     */
    void updateFinishAssembling(String nickname, int position) throws Exception;

    //notifies the view that the hourglass has been turned around
    /**
     * Updates the view that the hourglass has been turned around.
     *
     * @throws Exception if an error occurs during the update.
     */
    void updateStartNewCycle() throws Exception;

    //notifies the view that the hourglass has finished running
    /**
     * Updates the view that the hourglass has finished running.
     *
     * @throws Exception if an error occurs during the update.
     */
    void updateFinishedCycle() throws Exception;

    //notifies the view that all the players have concluded the assembling phase, which means that the players
    //enter the ship control phase
    /**
     * Updates the view that all players have finished assembling their ships and are now in the ship control phase.
     *
     * @throws Exception if an error occurs during the update.
     */
    void updateShipControl() throws Exception;

    //notifies the view that a player has to repair its ship board before the player in turn can pick a new card
    /***
     * Updates the view that a player needs to repair their ship.
     *
     * @param nickname The nickname of the player who needs to repair their ship.
     * @throws Exception if an error occurs during the update.
     */
    void updateShipRepair(String nickname) throws Exception;

    //notifies the view about the fact that a player has to pick a card in order to continue the game
    /**
     * Notifies the view that a player needs to pick a card to continue the game.
     *
     * @throws Exception if an error occurs during the update.
     */
    void updateCardPicking() throws Exception;

    //notifies the view that a new card has been picked and must be solved
    /**
     * Updates the view with the ID of the newly picked card that needs to be solved.
     *
     * @param imageID The ID of the card that has been picked.
     * @throws Exception if an error occurs during the update.
     */
    void updateCardSolving(int imageID) throws Exception;

    //notifies the view that the position of a player has changed
    /**
     * Updates the view with the new position of a player on the flight board.
     *
     * @param nickname The nickname of the player whose position has changed.
     * @param cell The new cell position of the player on the flight board.
     * @throws Exception if an error occurs during the update.
     */
    void updatePlayerPosition(String nickname, int cell) throws Exception;

    //notifies the view about the fact that the game is finished
    /**
     * Notifies the view that the game has ended.
     *
     * @throws Exception if an error occurs during the update.
     */
    void updateEndGame() throws Exception;
}
