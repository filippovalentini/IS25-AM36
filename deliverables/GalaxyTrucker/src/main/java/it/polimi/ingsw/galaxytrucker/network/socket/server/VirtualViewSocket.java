package it.polimi.ingsw.galaxytrucker.network.socket.server;

import it.polimi.ingsw.galaxytrucker.model.enumerations.Color;
import it.polimi.ingsw.galaxytrucker.model.enumerations.Orientation;
import it.polimi.ingsw.galaxytrucker.network.VirtualServer;
import it.polimi.ingsw.galaxytrucker.network.VirtualView;

import java.io.IOException;
import java.util.List;



/**
 * This interface defines the methods that are invoked by the model in order to notify/update the views after a change in the model; in particular, this interface is exploited if the chosen network technology is Socket
 */
public interface VirtualViewSocket extends VirtualView {
    //notifies a view about the fact that the game has started or not
    void notifyStartedGame(boolean startedGame) throws IOException;

    //notifies a view about an error committed while executing a method on the remote server; the parameter
    //errorMessage describes the type of error
    /**
     * Notifies the view about an error that occurred during the execution of a method on the remote server.
     * @param errorMessage the error message to be sent to the view
     * @throws IOException if an I/O error occurs while sending the message
     */
    @Override
    void notifyError(String errorMessage) throws IOException;

    //notifies a view about the fact that the corresponding player has been correctly added to the game, but
    //the server is waiting for other players in order to start the assembling phase; the parameter firstFlight
    //in needed for the view to determine which type of ship board/flight board to show to the user
    /**
     * Notifies the view that the player has been added to the game and is waiting for other players.
     * @param firstFlight true if it's the first flight, false otherwise
     * @throws IOException if an I/O error occurs while sending the message
     */
    @Override
    void updateWaitingForPlayers(boolean firstFlight) throws IOException;

    //notifies a view about the presence of another player in the game; this method is invoked before the
    //beginning of the assembling phase, therefore just the nickname and color of the new player is required
    /**
     * Notifies the view about the presence of a new player in the game.
     * @param nickname the nickname of the new player
     * @param color the color of the new player
     * @throws IOException if an I/O error occurs while sending the message
     */
    @Override
    void updateNewPlayer(String nickname, Color color) throws IOException;

    //notifies a view about the beginning of the assembling phase
    /**
     * Notifies the view that the assembling phase has started.
     * @throws IOException if an I/O error occurs while sending the message
     */
    @Override
    void updateStartAssembling() throws IOException;

    //notifies the view about the fact that a component has been successfully picked/released (depending on
    //the value of the boolean parameter) by the corresponding player; the parameter imageID is needed for the
    //view in order to show the right component to the user
    /**
     * Notifies the view about a component that has been picked or released.
     * @param imageID the ID of the image representing the component
     * @param released true if the component has been released, false if it has been picked
     * @throws IOException if an I/O error occurs while sending the message
     */
    @Override
    void updatePickedComponent(int imageID, boolean released) throws IOException;

    //notifies the view about the fact that a shown component has been picked/released (depending on the value
    //of the boolean parameter); the parameter imageID is needed for the view in order to show the right
    //component to the user
    /**
     * Notifies the view about a shown component that has been picked or released.
     * @param imageID the ID of the image representing the component
     * @param released true if the component has been released, false if it has been picked
     * @throws IOException if an I/O error occurs while sending the message
     */
    @Override
    void updateShownComponent(int imageID, boolean released) throws IOException;

    //notifies the view about the fact that a player (identified by the nickname parameter) has picked a reserved
    //component/ reserved a component (depending on the value of the boolean parameter); the parameter imageID
    //is needed for the view in order to show the right component to the user
    /**
     * Notifies the view about a reserved component that has been picked or released by a player.
     * @param nickname the nickname of the player who picked or reserved the component
     * @param imageID the ID of the image representing the component
     * @param released true if the component has been released, false if it has been picked
     * @throws IOException if an I/O error occurs while sending the message
     */
    @Override
    void updateReservedComponent(String nickname, int imageID, boolean released) throws IOException;

    //notifies the view about the fact that the picked component of the corresponding player has been rotated
    /**
     * Notifies the view that the picked component has been rotated.
     * @throws IOException if an I/O error occurs while sending the message
     */
    @Override
    void updateRotatePickedComponent() throws IOException;

    //notifies the view about the fact that a player (identified by the nickname parameter) has assembled a
    //component in position (x,y) of its ship board; the parameter imageID is needed for the view in order
    //to show the right component to the user
    /**
     * Notifies the view that a player has assembled a component on their ship board.
     * @param nickname the nickname of the player who assembled the component
     * @param imageID the ID of the image representing the component
     * @param orientation the orientation of the component
     * @param x the x-coordinate of the position on the ship board
     * @param y the y-coordinate of the position on the ship board
     * @throws IOException if an I/O error occurs while sending the message
     */
    @Override
    void updateAssembledComponent(String nickname, int imageID, Orientation orientation, int x, int y) throws IOException;

    //notifies the view about the fact that the corresponding player has successfully picked a deck; the parameter
    //contains the list of image IDs of the cards contained in the deck, so that the view can show the
    //correct adventure cards to the user
    /**
     * Notifies the view that a player has successfully picked a deck of cards.
     * @param deckIDs the list of image IDs of the cards in the deck
     * @throws IOException if an I/O error occurs while sending the message
     */
    @Override
    void updatePickedDeck(List<Integer> deckIDs) throws IOException;

    //notifies the view about the fact that the corresponding player has successfully released a deck
    /**
     * Notifies the view that a player has successfully released a deck of cards.
     * @throws IOException if an I/O error occurs while sending the message
     */
    @Override
    void updateReleasedDeck() throws IOException;

    //notifies the view about the fact that the corresponding player has finished the assembling phase and is
    //correctly positioned on the flight board; still, other players have to finish building their ships
    /**
     * Notifies the view that a player has finished assembling their ship.
     * @param nickname the nickname of the player who finished assembling
     * @param position the position on the flight board where the player is positioned
     * @throws IOException if an I/O error occurs while sending the message
     */
    @Override
    void updateFinishAssembling(String nickname, int position) throws IOException;

    //notifies the view that the hourglass has been turned around
    /**
     * Notifies the view that the hourglass has been turned around.
     * @throws IOException if an I/O error occurs while sending the message
     */
    @Override
    void updateStartNewCycle() throws IOException;

    //notifies the view that the hourglass has finished running
    /**
     * Notifies the view that the hourglass has finished running.
     * @throws IOException if an I/O error occurs while sending the message
     */
    @Override
    void updateFinishedCycle() throws IOException;

    //invoked when the game switches to the ship placement phase, which means that the players can only
    //place their ship on the flight board
    /**
     * Notifies the view that the game has switched to the ship placement phase.
     * @throws IOException if an I/O error occurs while sending the message
     */
    @Override
    void updateShipPlacement() throws IOException;

    //notifies the view that all the players have concluded the assembling phase, which means that the players
    //enter the ship control phase
    /**
     * Notifies the view that all players have finished the assembling phase and are now in the ship control phase.
     * @throws IOException if an I/O error occurs while sending the message
     */
    @Override
    void updateShipControl() throws IOException;

    //notifies the view that a player has to repair its ship board before the player in turn can pick a new card
    /**
     * Notifies the view that a player has to repair their ship board.
     * @param nickname the nickname of the player who needs to repair their ship
     * @throws IOException if an I/O error occurs while sending the message
     */
    @Override
    void updateShipRepair(String nickname) throws IOException;

    //notifies the view that a component of a player's ship board has been destroyed
    /**
     * Notifies the view that a component of a player's ship board has been destroyed.
     * @param nickname the nickname of the player whose component was destroyed
     * @param x the x-coordinate of the destroyed component
     * @param y the y-coordinate of the destroyed component
     * @throws IOException if an I/O error occurs while sending the message
     */
    @Override
    void updateDestroyedComponent(String nickname, int x, int y) throws IOException;

    //notifies the view about a change in the number of crew of a cabin
    /**
     * Notifies the view about a change in the number of crew members in a cabin.
     * @param nickname the nickname of the player whose cabin is affected
     * @param x the x-coordinate of the cabin
     * @param y the y-coordinate of the cabin
     * @param change the change in the number of crew members (positive or negative)
     * @throws IOException if an I/O error occurs while sending the message
     */
    @Override
    void updateCrewChange(String nickname, int x, int y, int change) throws IOException;

    //notifies the view that a player has initialized a battery container with batteries
    /**
     * Notifies the view that a player has initialized a battery container with batteries.
     * @param nickname the nickname of the player who initialized the battery container
     * @param x the x-coordinate of the battery container
     * @param y the y-coordinate of the battery container
     * @param change the change in the number of batteries (positive or negative)
     * @throws IOException if an I/O error occurs while sending the message
     */
    @Override
    void updateBatteries(String nickname, int x, int y, int change) throws IOException;

    //notifies the view about a change in the number of aliens of a cabin
    /**
     * Notifies the view about a change in the number of aliens in a cabin.
     * @param nickname the nickname of the player whose cabin is affected
     * @param x the x-coordinate of the cabin
     * @param y the y-coordinate of the cabin
     * @param isPurple true if the alien is purple, false otherwise
     * @param added true if an alien has been added, false if it has been removed
     * @throws IOException if an I/O error occurs while sending the message
     */
    @Override
    void updateAlienChange(String nickname, int x, int y, boolean isPurple, boolean added) throws IOException;

    //notifies the view that a good has been loaded in a cargo hold
    /**
     * Notifies the view that a good has been loaded in a cargo hold.
     * @param nickname the nickname of the player who loaded the good
     * @param x the x-coordinate of the cargo hold
     * @param y the y-coordinate of the cargo hold
     * @param good the color of the good that has been loaded
     * @throws IOException if an I/O error occurs while sending the message
     */
    @Override
    void updateLoadedGood(String nickname, int x, int y, Color good) throws IOException;

    //notifies the view that some goods have been removed form a cargo hold
    /**
     * Notifies the view that goods have been removed from a cargo hold.
     * @param nickname the nickname of the player whose cargo hold was affected
     * @param x the x-coordinate of the cargo hold
     * @param y the y-coordinate of the cargo hold
     * @param good the color of the goods that have been removed
     * @param numberGoods the number of goods that have been removed
     * @throws IOException if an I/O error occurs while sending the message
     */
    @Override
    void updateRemovedGoods(String nickname, int x, int y, Color good, int numberGoods) throws IOException;

    //notifies the view about the fact that a player has to pick a card in order to continue the game
    /**
     * Notifies the view that a player has to pick a card to continue the game.
     * @throws IOException if an I/O error occurs while sending the message
     */
    @Override
    void updateCardPicking() throws IOException;

    //notifies the view about the next player whose turn it is to perform an action
    /**
     * Notifies the view about the next player whose turn it is to perform an action.
     * @param nickname the nickname of the player whose turn it is
     * @throws IOException if an I/O error occurs while sending the message
     */
    @Override
    void updateNextTurn(String nickname) throws IOException;

    //notifies the view that a new card has been picked and must be solved
    /**
     * Notifies the view that a new card has been picked and must be solved.
     * @param imageID the ID of the image representing the card
     * @throws IOException if an I/O error occurs while sending the message
     */
    @Override
    void updateCardSolving(int imageID) throws IOException;

    //notifies the view that a player has quit the game
    /**
     * Notifies the view that a player has quit the game.
     * @param nickname the nickname of the player who quit
     * @throws IOException if an I/O error occurs while sending the message
     */
    @Override
    void updatePlayerQuit(String nickname) throws IOException;

    //notifies the view that a player has gained/lost credits
    /**
     * Notifies the view that a player has gained or lost credits.
     * @param nickname the nickname of the player whose credits have changed
     * @param change the amount of credits gained (positive) or lost (negative)
     * @throws IOException if an I/O error occurs while sending the message
     */
    @Override
    void updatePlayerCredits(String nickname, int change) throws IOException;

    //notifies the view that the position of a player has changed
    /**
     * Notifies the view that the position of a player has changed.
     * @param nickname the nickname of the player whose position has changed
     * @param lap the lap number
     * @param cell the cell number
     * @throws IOException if an I/O error occurs while sending the message
     */
    @Override
    void updatePlayerPosition(String nickname, int lap, int cell) throws IOException;

    //notifies the view about the fact that the game is finished
    /**
     * Notifies the view that the game has ended.
     * @throws IOException if an I/O error occurs while sending the message
     */
    @Override
    void updateEndGame() throws IOException;
}
