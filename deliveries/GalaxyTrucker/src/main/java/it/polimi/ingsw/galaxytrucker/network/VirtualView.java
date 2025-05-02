package it.polimi.ingsw.galaxytrucker.network;

import it.polimi.ingsw.galaxytrucker.model.enumerations.*;
import it.polimi.ingsw.galaxytrucker.model.gameClasses.Position;

import java.util.List;

//this interface defines the methods that are invoked by the model in order to notify/update the views after
//a change in the model

public interface VirtualView {
    //runs a command line interface to send requests to the server
    void runCli(VirtualServer server) throws Exception;

    //notifies a view about an error committed while executing a method on the remote server; the parameter
    //errorMessage describes the type of error
    void notifyError(String errorMessage) throws Exception;

    //notifies a view about the fact that the corresponding player has been correctly added to the game, but
    //the server is waiting for other players in order to start the assembling phase; the parameter firstFlight
    //in needed for the view to determine which type of ship board/flight board to show to the user
    void updateWaitingForPlayers(boolean firstFlight) throws Exception;

    //notifies a view about the presence of another player in the game; this method is invoked before the
    //beginning of the assembling phase, therefore just the nickname and color of the new player is required
    void updateNewPlayer(String nickname, Color color) throws Exception;

    //notifies a view about the beginning of the assembling phase
    void updateStartAssembling() throws Exception;

    //notifies the view about the fact that a component has been successfully picked/released (depending on
    //the value of the boolean parameter) by the corresponding player; the parameter imageID is needed for the
    //view in order to show the right component to the user
    void updatePickedComponent(int imageID, boolean released) throws Exception;

    //notifies the view about the fact that a shown component has been picked/released (depending on the value
    //of the boolean parameter); the parameter imageID is needed for the view in order to show the right
    //component to the user
    void updateShownComponent(int imageID, boolean released) throws Exception;

    //notifies the view about the fact that a player (identified by the nickname parameter) has picked a reserved
    //component/ reserved a component (depending on the value of the boolean parameter); the parameter imageID
    //is needed for the view in order to show the right component to the user
    void updateReservedComponent(String nickname, int imageID, boolean released) throws Exception;

    //notifies the view about the fact that the picked component of the corresponding player has been rotated
    void updateRotatePickedComponent() throws Exception;

    //notifies the view about the fact that a player (identified by the nickname parameter) has assembled a
    //component in position (x,y) of its ship board; the parameter imageID is needed for the view in order
    //to show the right component to the user
    void updateAssembledComponent(String nickname, int imageID, Orientation orientation, int x, int y) throws Exception;

    //notifies the view about the fact that the corresponding player has successfully picked a deck; the parameter
    //contains the list of image IDs of the cards contained in the deck, so that the view can show the
    //correct adventure cards to the user
    void updatePickedDeck(List<Integer> deckIDs) throws Exception;

    //notifies the view about the fact that the corresponding player has successfully released a deck
    void updateReleasedDeck() throws Exception;

    //notifies the view about the fact that a player has finished the assembling phase and is
    //correctly positioned on the flight board; still, other players have to finish building their ships
    void updateFinishAssembling(String nickname, int position) throws Exception;

    //notifies the view that all the players have concluded the assembling phase, which means that the players
    //enter the ship control phase
    void updateShipControl() throws Exception;

    //notifies the view that a component of a player's ship board has been destroyed
    void updateDestroyedComponent(String nickname, int x, int y) throws Exception;

    //notifies the view about a change in the number of crew of a cabin
    void updateCrewChange(String nickname, int x, int y, int change) throws Exception;

    //notifies the view that a player has initialized a battery container with batteries
    void updateBatteries(String nickname, int x, int y, int change) throws Exception;

    //notifies the view about a change in the number of aliens of a cabin
    void updateAlienChange(String nickname, int x, int y, boolean isPurple, boolean added) throws Exception;

    //notifies the view about the fact that a player has to pick a card in order to continue the game
    void updateCardPicking() throws Exception;

    //notifies the view about the next player whose turn it is to perform an action
    void updateNextTurn(String nickname) throws Exception;

    //notifies the view that a new card has been picked and must be solved
    void updateCardSolving(int imageID) throws Exception;

    //notifies the view that a player has quit the game
    void updatePlayerQuit(String nickname) throws Exception;

    //notifies the view that a player has gained/lost credits
    void updatePlayerCredits(String nickname, int change) throws Exception;

    //notifies the view that the position of a player has changed
    void updatePlayerPosition(String nickname, int lap, int cell) throws Exception;

    //notifies the view about the fact that the game is finished
    void updateEndGame() throws Exception;

}
