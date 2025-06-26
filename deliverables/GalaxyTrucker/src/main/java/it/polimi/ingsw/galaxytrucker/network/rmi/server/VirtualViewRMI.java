package it.polimi.ingsw.galaxytrucker.network.rmi.server;

import it.polimi.ingsw.galaxytrucker.model.enumerations.Color;
import it.polimi.ingsw.galaxytrucker.model.enumerations.Orientation;
import it.polimi.ingsw.galaxytrucker.network.VirtualView;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
/**
 * This interface extends the {@link VirtualView} interface. This interface defines the methods that are invoked by the model in order to notify/update the views after a change in the model; in particular, this interface is exploited if the chosen network technology is RMI
 */
public interface VirtualViewRMI extends Remote, VirtualView {

    //notifies a view about an error committed while executing a method on the remote server; the parameter
    //errorMessage describes the type of error

    /**
     * Notifies a view about an error that occurred during the execution of a method on the remote server.
     * @param errorMessage
     * @throws RemoteException
     */
    @Override
    void notifyError(String errorMessage) throws RemoteException;

    //notifies a view about the fact that the corresponding player has been correctly added to the game, but
    //the server is waiting for other players in order to start the assembling phase; the parameter firstFlight
    //in needed for the view to determine which type of ship board/flight board to show to the user

    /**
     * Notifies a view that the player has been successfully added to the game and is waiting for other players to join.
     * @param firstFlight
     * @throws RemoteException
     */
    @Override
    void updateWaitingForPlayers(boolean firstFlight) throws RemoteException;

    //notifies a view about the presence of another player in the game; this method is invoked before the
    //beginning of the assembling phase, therefore just the nickname and color of the new player is required

    /**
     * Notifies a view about the presence of a new player in the game.
     * @param nickname
     * @param color
     * @throws RemoteException
     */
    @Override
    void updateNewPlayer(String nickname, Color color) throws RemoteException;

    //notifies a view about the beginning of the assembling phase

    /**
     * Notifies a view about the beginning of the assembling phase.
     * @throws RemoteException
     */
    @Override
    void updateStartAssembling() throws RemoteException;

    //notifies the view about the fact that a component has been successfully picked/released (depending on
    //the value of the boolean parameter) by the corresponding player; the parameter imageID is needed for the
    //view in order to show the right component to the user

    /**
     * Notifies the view about a component that has been successfully picked or released by the player.
     * @param imageID
     * @param released
     * @throws RemoteException
     */
    @Override
    void updatePickedComponent(int imageID, boolean released) throws RemoteException;

    //notifies the view about the fact that a shown component has been picked/released (depending on the value
    //of the boolean parameter); the parameter imageID is needed for the view in order to show the right
    //component to the user

    /**
     * Notifies the view about a shown component that has been picked or released.
     * @param imageID
     * @param released
     * @throws RemoteException
     */
    @Override
    void updateShownComponent(int imageID, boolean released) throws RemoteException;

    //notifies the view about the fact that a player (identified by the nickname parameter) has picked a reserved
    //component/ reserved a component (depending on the value of the boolean parameter); the parameter imageID
    //is needed for the view in order to show the right component to the user

    /**
     * Notifies the view about a reserved component that has been picked or released by a player.
     * @param nickname
     * @param imageID
     * @param released
     * @throws RemoteException
     */
    @Override
    void updateReservedComponent(String nickname, int imageID, boolean released) throws RemoteException;

    //notifies the view about the fact that the picked component of the corresponding player has been rotated

    /**
     * Notifies the view that the picked component of the player has been rotated.
     * @throws RemoteException
     */
    @Override
    void updateRotatePickedComponent() throws RemoteException;

    //notifies the view about the fact that a player (identified by the nickname parameter) has assembled a
    //component in position (x,y) of its ship board; the parameter imageID is needed for the view in order
    //to show the right component to the user

    /**
     * Notifies the view that a player has assembled a component in a specific position of its ship board.
     * @param nickname
     * @param imageID
     * @param orientation
     * @param x
     * @param y
     * @throws RemoteException
     */
    @Override
    void updateAssembledComponent(String nickname, int imageID, Orientation orientation, int x, int y) throws RemoteException;

    //notifies the view about the fact that the corresponding player has successfully picked a deck; the parameter
    //contains the list of image IDs of the cards contained in the deck, so that the view can show the
    //correct adventure cards to the user

    /**
     * Notifies the view that the player has successfully picked a deck of cards.
     * @param deckIDs
     * @throws RemoteException
     */
    @Override
    void updatePickedDeck(List<Integer> deckIDs) throws RemoteException;

    //notifies the view about the fact that the corresponding player has successfully released a deck

    /**
     * Notifies the view that the player has successfully released a deck of cards.
     * @throws RemoteException
     */
    @Override
    void updateReleasedDeck() throws RemoteException;

    //notifies the view about the fact that the corresponding player has finished the assembling phase and is
    //correctly positioned on the flight board; still, other players have to finish building their ships

    /**
     * Notifies the view that the player has finished assembling their ship and is positioned on the flight board.
     * @param nickname
     * @param position
     * @throws RemoteException
     */
    @Override
    void updateFinishAssembling(String nickname, int position) throws RemoteException;

    //notifies the view that the hourglass has been turned around

    /**
     * Notifies the view that the hourglass has been turned around to start a new cycle.
     * @throws RemoteException
     */
    @Override
    void updateStartNewCycle() throws RemoteException;

    //notifies the view that the hourglass has finished running

    /**
     * Notifies the view that the hourglass has finished running and the current cycle is over.
     * @throws RemoteException
     */
    @Override
    void updateFinishedCycle() throws RemoteException;

    //invoked when the game switches to the ship placement phase, which means that the players can only
    //place their ship on the flight board

    /**
     * Notifies the view that the game has switched to the ship placement phase.
     * @throws RemoteException
     */
    @Override
    void updateShipPlacement() throws RemoteException;

    //notifies the view that all the players have concluded the assembling phase, which means that the players
    //enter the ship control phase

    /**
     * Notifies the view that all players have finished assembling their ships and the game has entered the ship control phase.
     * @throws RemoteException
     */
    @Override
    void updateShipControl() throws RemoteException;

    //notifies the view that a player has to repair its ship board before the player in turn can pick a new card

    /**
     * Notifies the view that a player has to repair their ship board before proceeding with the game.
     * @param nickname
     * @throws RemoteException
     */
    @Override
    void updateShipRepair(String nickname) throws RemoteException;

    //notifies the view that a component of a player's ship board has been destroyed

    /**
     * Notifies the view that a component of a player's ship board has been destroyed.
     * @param nickname
     * @param x
     * @param y
     * @throws RemoteException
     */
    @Override
    void updateDestroyedComponent(String nickname, int x, int y) throws RemoteException;

    //notifies the view about a change in the number of crew of a cabin

    /**
     * Notifies the view about a change in the number of crew members in a cabin.
     * @param nickname
     * @param x
     * @param y
     * @param change
     * @throws RemoteException
     */
    @Override
    void updateCrewChange(String nickname, int x, int y, int change) throws RemoteException;

    //notifies the view that a player has initialized a battery container with batteries

    /**
     * Notifies the view that a player has initialized a battery container with batteries.
     * @param nickname
     * @param x
     * @param y
     * @param change
     * @throws RemoteException
     */
    @Override
    void updateBatteries(String nickname, int x, int y, int change) throws RemoteException;

    //notifies the view about a change in the number of aliens of a cabin

    /**
     * Notifies the view about a change in the number of aliens in a cabin.
     * @param nickname
     * @param x
     * @param y
     * @param isPurple
     * @param added
     * @throws RemoteException
     */
    @Override
    void updateAlienChange(String nickname, int x, int y, boolean isPurple, boolean added) throws RemoteException;

    //notifies the view that a good has been loaded in a cargo hold

    /**
     * Notifies the view that a good has been loaded in a cargo hold.
     * @param nickname
     * @param x
     * @param y
     * @param good
     * @throws RemoteException
     */
    @Override
    void updateLoadedGood(String nickname, int x, int y, Color good) throws RemoteException;

    //notifies the view that some goods have been removed form a cargo hold

    /**
     * Notifies the view that some goods have been removed from a cargo hold.
     * @param nickname
     * @param x
     * @param y
     * @param good
     * @param numberGoods
     * @throws RemoteException
     */
    @Override
    void updateRemovedGoods(String nickname, int x, int y, Color good, int numberGoods) throws RemoteException;

    //notifies the view about the fact that a player has to pick a card in order to continue the game

    /**
     * Notifies the view that a player has to pick a card in order to continue the game.
     * @throws RemoteException
     */
    @Override
    void updateCardPicking() throws RemoteException;

    //notifies the view about the next player whose turn it is to perform an action

    /**
     * Notifies the view about the next player whose turn it is to perform an action.
     * @param nickname
     * @throws RemoteException
     */
    @Override
    void updateNextTurn(String nickname) throws RemoteException;

    //notifies the view that a new card has been picked and must be solved

    /**
     * Notifies the view that a new card has been picked and must be solved.
     * @param imageID
     * @throws RemoteException
     */
    @Override
    void updateCardSolving(int imageID) throws RemoteException;

    //notifies the view that a player has quit the game

    /**
     * Notifies the view that a player has quit the game.
     * @param nickname
     * @throws RemoteException
     */
    @Override
    void updatePlayerQuit(String nickname) throws RemoteException;

    //notifies the view that a player has gained/lost credits

    /**
     * Notifies the view that a player has gained or lost credits.
     * @param nickname
     * @param change
     * @throws RemoteException
     */
    @Override
    void updatePlayerCredits(String nickname, int change) throws RemoteException;

    //notifies the view that the position of a player has changed

    /**
     * Notifies the view that the position of a player has changed.
     * @param nickname
     * @param lap
     * @param cell
     * @throws RemoteException
     */
    @Override
    void updatePlayerPosition(String nickname, int lap, int cell) throws RemoteException;

    //notifies the view about the fact that the game is finished

    /**
     * Notifies the view that the game is finished.
     * @throws RemoteException
     */
    @Override
    void updateEndGame() throws RemoteException;
}
