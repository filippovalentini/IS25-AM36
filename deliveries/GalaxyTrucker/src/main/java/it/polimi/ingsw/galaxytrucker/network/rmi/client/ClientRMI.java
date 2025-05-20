package it.polimi.ingsw.galaxytrucker.network.rmi.client;

import it.polimi.ingsw.galaxytrucker.network.GameSessionManager;
import it.polimi.ingsw.galaxytrucker.network.VirtualView;
import it.polimi.ingsw.galaxytrucker.ui.UserInterface;
import it.polimi.ingsw.galaxytrucker.model.enumerations.*;
import it.polimi.ingsw.galaxytrucker.network.rmi.server.VirtualViewRMI;

import java.rmi.*;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

public class ClientRMI extends UnicastRemoteObject implements VirtualViewRMI, GameSessionManager {
    private final VirtualServerRMI server;      //instance of the related RMI server
    private final UserInterface ui;                          //view of the player

    public ClientRMI(UserInterface ui, VirtualServerRMI server) throws RemoteException {
        super();
        this.ui = ui;
        this.server = server;
    }


    //this method asks the server if a game with the specified ID has already started
    @Override
    public boolean askIfGameStarted(int gameID){
        try{
            return server.startedGame(gameID);
        }
        catch(RemoteException e){
            return false;
        }
    }

    //this method asks the server to add the player (associated to the client) to the game
    @Override
    public boolean tryToAddPlayerToGame(int gameID, String nickname, Color color) {
        try{
            return server.addPlayer(this, gameID, nickname, color);
        }
        catch(RemoteException e){
            return false;
        }
    }

    //this method asks the server to create a new game
    @Override
    public boolean tryToStartNewGame(VirtualView client, int gameID, boolean firstFlight, int numberPlayers){
        try{
            server.startNewGame(client, gameID, firstFlight, numberPlayers);
            return true;
        }
        catch(RemoteException e){
            return false;
        }
    }






    //notifies a view about an error committed while executing a method on the remote server; the parameter
    //errorMessage describes the type of error
    @Override
    public void notifyError(String errorMessage) throws RemoteException {
        System.out.println(errorMessage);
    }

    //notifies a view about the fact that the corresponding player has been correctly added to the game, but
    //the server is waiting for other players in order to start the assembling phase; the parameter firstFlight
    //in needed for the view to determine which type of ship board/flight board to show to the user
    @Override
    public void updateWaitingForPlayers(boolean firstFlight) throws RemoteException {
        try{
            this.ui.updateWaitingForPlayers(firstFlight);
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    //notifies a view about the presence of another player in the game; this method is invoked before the
    //beginning of the assembling phase, therefore just the nickname and color of the new player is required
    @Override
    public void updateNewPlayer(String nickname, Color color) throws RemoteException{
        try{
            this.ui.updateNewPlayer(nickname, color);
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    //notifies a view about the beginning of the assembling phase
    @Override
    public void updateStartAssembling() throws RemoteException {
        try{
            this.ui.updateStartAssembling();
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    //notifies the view about the fact that a component has been successfully picked/released (depending on
    //the value of the boolean parameter) by the corresponding player; the parameter imageID is needed for the
    //view in order to show the right component to the user
    @Override
    public void updatePickedComponent(int imageID, boolean released) throws RemoteException {
        try{
            this.ui.updatePickedComponent(imageID, released);
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    //notifies the view about the fact that a shown component has been picked/released (depending on the value
    //of the boolean parameter); the parameter imageID is needed for the view in order to show the right
    //component to the user
    @Override
    public void updateShownComponent(int imageID, boolean released) throws RemoteException {
        try{
            this.ui.updateShownComponent(imageID, released);
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    //notifies the view about the fact that a player (identified by the nickname parameter) has picked a reserved
    //component/ reserved a component (depending on the value of the boolean parameter); the parameter imageID
    //is needed for the view in order to show the right component to the user
    @Override
    public void updateReservedComponent(String nickname, int imageID, boolean released) throws RemoteException {
        try{
            this.ui.updateReservedComponent(nickname, imageID, released);
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    //notifies the view about the fact that the picked component of the corresponding player has been rotated
    @Override
    public void updateRotatePickedComponent() throws RemoteException {
        try{
            this.ui.updateRotatePickedComponent();
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    //notifies the view about the fact that a player (identified by the nickname parameter) has assembled a
    //component in position (x,y) of its ship board; the parameter imageID is needed for the view in order
    //to show the right component to the user
    @Override
    public void updateAssembledComponent(String nickname, int imageID, Orientation orientation, int x, int y) throws RemoteException {
        try{
            this.ui.updateAssembledComponent(nickname, imageID, orientation, x, y);
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    //notifies the view about the fact that the corresponding player has successfully picked a deck; the parameter
    //contains the list of image IDs of the cards contained in the deck, so that the view can show the
    //correct adventure cards to the user
    @Override
    public void updatePickedDeck(List<Integer> deckIDs) throws RemoteException {
        try{
            this.ui.updatePickedDeck(deckIDs);
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    //notifies the view about the fact that the corresponding player has successfully released a deck
    @Override
    public void updateReleasedDeck() throws RemoteException {
        try{
            this.ui.updateReleasedDeck();
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    //notifies the view about the fact that the corresponding player has finished the assembling phase and is
    //correctly positioned on the flight board; still, other players have to finish building their ships
    @Override
    public void updateFinishAssembling(String nickname, int position) throws RemoteException {
        try{
            this.ui.updateFinishAssembling(nickname, position);
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    //notifies the view that the hourglass has been turned around
    @Override
    public void updateStartNewCycle() throws RemoteException{
        try{
            this.ui.updateStartNewCycle();
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    //notifies the view that the hourglass has finished running
    @Override
    public void updateFinishedCycle() throws RemoteException{
        try{
            this.ui.updateFinishedCycle();
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    //invoked when the game switches to the ship placement phase, which means that the players can only
    //place their ship on the flight board
    @Override
    public void updateShipPlacement() throws RemoteException{
        try{
            this.ui.updateShipPlacement();
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    //notifies the view that all the players have concluded the assembling phase, which means that the players
    //enter the ship control phase
    @Override
    public void updateShipControl() throws RemoteException {
        try{
            this.ui.updateShipControl();
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    //notifies the view that a component of a player's ship board has been destroyed
    @Override
    public void updateDestroyedComponent(String nickname, int x, int y) throws RemoteException{
        try{
            this.ui.updateDestroyedComponent(nickname, x, y);
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    //notifies the view about a change in the number of crew of a cabin
    @Override
    public void updateCrewChange(String nickname, int x, int y, int change) throws RemoteException{
        try{
            this.ui.updateCrewChange(nickname, x, y, change);
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    //notifies the view that a player has initialized a battery container with batteries
    @Override
    public void updateBatteries(String nickname, int x, int y, int change) throws RemoteException{
        try{
            this.ui.updateBatteries(nickname, x, y, change);
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    //notifies the view about a change in the number of aliens of a cabin
    @Override
    public void updateAlienChange(String nickname, int x, int y, boolean isPurple, boolean added) throws RemoteException{
        try{
            this.ui.updateAlienChange(nickname, x, y, isPurple, added);
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    //notifies the view that a good has been loaded in a cargo hold
    @Override
    public void updateLoadedGood(String nickname, int x, int y, Color good) throws RemoteException{
        try{
            this.ui.updateLoadedGood(nickname, x, y, good);
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    //notifies the view that some goods have been removed form a cargo hold
    @Override
    public void updateRemovedGoods(String nickname, int x, int y, Color good, int numberGoods) throws RemoteException{
        try{
            this.ui.updateRemovedGoods(nickname, x, y, good, numberGoods);
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    //notifies the view about the fact that a player has to pick a card in order to continue the game
    @Override
    public void updateCardPicking() throws RemoteException{
        try{
            this.ui.updateCardPicking();
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    //notifies the view about the next player whose turn it is to perform an action
    @Override
    public void updateNextTurn(String nickname) throws RemoteException{
        try{
            this.ui.updateNextTurn(nickname);
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    //notifies the view that a new card has been picked and must be solved
    @Override
    public void updateCardSolving(int imageID) throws RemoteException{
        try{
            this.ui.updateCardSolving(imageID);
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    //notifies the view that a player has quit the game
    @Override
    public void updatePlayerQuit(String nickname) throws RemoteException{
        try{
            this.ui.updatePlayerQuit(nickname);
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    //notifies the view that a player has gained/lost credits
    @Override
    public void updatePlayerCredits(String nickname, int change) throws RemoteException{
        try{
            this.ui.updatePlayerCredits(nickname, change);
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    //notifies the view that the position of a player has changed
    @Override
    public void updatePlayerPosition(String nickname, int lap, int cell) throws RemoteException{
        try{
            this.ui.updatePlayerPosition(nickname, lap, cell);
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    //notifies the view about the fact that the game is finished
    @Override
    public void updateEndGame() throws RemoteException{
        try{
            this.ui.updateEndGame();
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
}
