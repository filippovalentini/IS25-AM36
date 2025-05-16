package it.polimi.ingsw.galaxytrucker.network.socket.server;

import it.polimi.ingsw.galaxytrucker.model.enumerations.Color;
import it.polimi.ingsw.galaxytrucker.model.enumerations.Orientation;
import it.polimi.ingsw.galaxytrucker.network.VirtualServer;
import it.polimi.ingsw.galaxytrucker.network.VirtualView;

import java.io.IOException;
import java.util.List;

public interface VirtualViewSocket extends VirtualView {
    //notifies a view about the fact that the game has started or not
    void notifyStartedGame(boolean startedGame) throws IOException;

    //runs a command line interface to send requests to the server
    @Override
    void runCli() throws IOException;

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

    //notifies the view that the hourglass has been turned around
    @Override
    void updateStartNewCycle() throws IOException;

    //notifies the view that the hourglass has finished running
    @Override
    void updateFinishedCycle() throws IOException;

    //invoked when the game switches to the ship placement phase, which means that the players can only
    //place their ship on the flight board
    @Override
    void updateShipPlacement() throws IOException;

    //notifies the view that all the players have concluded the assembling phase, which means that the players
    //enter the ship control phase
    @Override
    void updateShipControl() throws IOException;

    //notifies the view that a component of a player's ship board has been destroyed
    @Override
    void updateDestroyedComponent(String nickname, int x, int y) throws IOException;

    //notifies the view about a change in the number of crew of a cabin
    @Override
    void updateCrewChange(String nickname, int x, int y, int change) throws IOException;

    //notifies the view that a player has initialized a battery container with batteries
    @Override
    void updateBatteries(String nickname, int x, int y, int change) throws IOException;

    //notifies the view about a change in the number of aliens of a cabin
    @Override
    void updateAlienChange(String nickname, int x, int y, boolean isPurple, boolean added) throws IOException;

    //notifies the view that a good has been loaded in a cargo hold
    @Override
    void updateLoadedGood(String nickname, int x, int y, Color good) throws IOException;

    //notifies the view that some goods have been removed form a cargo hold
    @Override
    void updateRemovedGoods(String nickname, int x, int y, Color good, int numberGoods) throws IOException;

    //notifies the view about the fact that a player has to pick a card in order to continue the game
    @Override
    void updateCardPicking() throws IOException;

    //notifies the view about the next player whose turn it is to perform an action
    @Override
    void updateNextTurn(String nickname) throws IOException;

    //notifies the view that a new card has been picked and must be solved
    @Override
    void updateCardSolving(int imageID) throws IOException;

    //notifies the view that a player has quit the game
    @Override
    void updatePlayerQuit(String nickname) throws IOException;

    //notifies the view that a player has gained/lost credits
    @Override
    void updatePlayerCredits(String nickname, int change) throws IOException;

    //notifies the view that the position of a player has changed
    @Override
    void updatePlayerPosition(String nickname, int lap, int cell) throws IOException;

    //notifies the view about the fact that the game is finished
    @Override
    void updateEndGame() throws IOException;
}
