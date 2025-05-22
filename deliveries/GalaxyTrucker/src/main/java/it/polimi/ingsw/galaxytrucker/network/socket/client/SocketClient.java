package it.polimi.ingsw.galaxytrucker.network.socket.client;

import it.polimi.ingsw.galaxytrucker.model.enumerations.Color;
import it.polimi.ingsw.galaxytrucker.model.enumerations.Orientation;
import it.polimi.ingsw.galaxytrucker.network.GameSessionManager;
import it.polimi.ingsw.galaxytrucker.network.VirtualServer;
import it.polimi.ingsw.galaxytrucker.network.VirtualView;
import it.polimi.ingsw.galaxytrucker.network.socket.message.GameUpdateMessage;
import it.polimi.ingsw.galaxytrucker.network.socket.message.GameUpdateType;
import it.polimi.ingsw.galaxytrucker.network.socket.server.VirtualViewSocket;
import it.polimi.ingsw.galaxytrucker.ui.UserInterface;

import java.io.*;
import java.net.Socket;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

//this class contains all the logic needed to connect to the server through socket, manage the user interaction
//and handle server responses
public class SocketClient implements VirtualViewSocket, GameSessionManager {
    private final UserInterface ui;                           //player's view
    private final Socket clientSocket;              //socket used for client-server communication
    private final ObjectInputStream in;             //input stream for client-server communication
    private final SocketServerHandler serverHandler;    //manages the creation of messages and sends them to the server

    public SocketClient(UserInterface ui, Socket clientSocket) throws IOException {
        super();
        this.ui = ui;
        this.clientSocket = clientSocket;
        this.serverHandler = new SocketServerHandler(new ObjectOutputStream(clientSocket.getOutputStream()));
        this.in = new ObjectInputStream(clientSocket.getInputStream());
    }

    //returns the instance of the sever handler associated to the client socket
    public VirtualServer getServerHandler() {
        return serverHandler;
    }

    //this method creates a loop that waits for server's messages and (based on the type of message received)
    //invokes a method on the view
    public void manageServerMessages() {
        while (true) {
            try{
                GameUpdateMessage message = (GameUpdateMessage) in.readObject();
                switch(message.getGameUpdateType()){
                    case ERROR:
                        notifyError(message.getGameParams(0));
                        break;
                    case NEW_PLAYER:
                        updateNewPlayer(message.getGameParams(0), Color.convertToColor(message.getGameParams(1)));
                        break;
                    case START_ASSEMBLING:
                        updateStartAssembling();
                        break;
                    case PICKED_COMPONENT:
                        updatePickedComponent(Integer.parseInt(message.getGameParams(0)), Boolean.parseBoolean(message.getGameParams(1)));
                        break;
                    case SHOWN_COMPONENT:
                        updateShownComponent(Integer.parseInt(message.getGameParams(0)), Boolean.parseBoolean(message.getGameParams(1)));
                        break;
                    case RESERVED_COMPONENT:
                        updateReservedComponent(message.getGameParams(0), Integer.parseInt(message.getGameParams(1)), Boolean.parseBoolean(message.getGameParams(2)));
                        break;
                    case ROTATE_PICKED_COMPONENT:
                        updateRotatePickedComponent();
                        break;
                    case ASSEMBLED_COMPONENT:
                        updateAssembledComponent(message.getGameParams(0), Integer.parseInt(message.getGameParams(1)), Orientation.convertToOrientation(message.getGameParams(2)), Integer.parseInt(message.getGameParams(3)), Integer.parseInt(message.getGameParams(4)));
                        break;
                    case PICKED_DECK:
                        List<Integer> deckIDs = new ArrayList<>();
                        for(String deckID : message.getGameParams()){
                            deckIDs.add(Integer.parseInt(deckID));
                        }
                        updatePickedDeck(deckIDs);
                        break;
                    case RELEASED_DECK:
                        updateReleasedDeck();
                        break;
                    case FINISH_ASSEMBLING:
                        updateFinishAssembling(message.getGameParams(0), Integer.parseInt(message.getGameParams(1)));
                        break;
                    case STARTED_CYCLE:
                        updateStartNewCycle();
                        break;
                    case FINISHED_CYCLE:
                        updateFinishedCycle();
                        break;
                    case SHIP_PLACEMENT:
                        updateShipPlacement();
                        break;
                    case SHIP_CONTROL:
                        updateShipControl();
                        break;
                    case DESTROYED_COMPONENT:
                        updateDestroyedComponent(message.getGameParams(0), Integer.parseInt(message.getGameParams(1)), Integer.parseInt(message.getGameParams(2)));
                        break;
                    case CREW_CHANGE:
                        updateCrewChange(message.getGameParams(0), Integer.parseInt(message.getGameParams(1)), Integer.parseInt(message.getGameParams(2)), Integer.parseInt(message.getGameParams(3)));
                        break;
                    case ALIEN_CHANGE:
                        updateAlienChange(message.getGameParams(0), Integer.parseInt(message.getGameParams(1)), Integer.parseInt(message.getGameParams(2)), Boolean.parseBoolean(message.getGameParams(3)), Boolean.parseBoolean(message.getGameParams(4)));
                        break;
                    case BATTERY_CHANGE:
                        updateBatteries(message.getGameParams(0), Integer.parseInt(message.getGameParams(1)), Integer.parseInt(message.getGameParams(2)), Integer.parseInt(message.getGameParams(3)));
                        break;
                    case LOADED_GOOD:
                        updateLoadedGood(message.getGameParams(0), Integer.parseInt(message.getGameParams(1)), Integer.parseInt(message.getGameParams(2)), Color.convertToColor(message.getGameParams(3)));
                        break;
                    case REMOVED_GOODS:
                        updateRemovedGoods(message.getGameParams(0), Integer.parseInt(message.getGameParams(1)), Integer.parseInt(message.getGameParams(2)), Color.convertToColor(message.getGameParams(3)), Integer.parseInt(message.getGameParams(4)));
                        break;
                    case CARD_PICKING:
                        updateCardPicking();
                        break;
                    case NEXT_TURN:
                        updateNextTurn(message.getGameParams(0));
                        break;
                    case CARD_SOLVING:
                        updateCardSolving(Integer.parseInt(message.getGameParams(0)));
                        break;
                    case PLAYER_QUIT:
                        updatePlayerQuit(message.getGameParams(0));
                        break;
                    case CREDITS_CHANGE:
                        updatePlayerCredits(message.getGameParams(0), Integer.parseInt(message.getGameParams(1)));
                        break;
                    case POSITION_CHANGE:
                        updatePlayerPosition(message.getGameParams(0), Integer.parseInt(message.getGameParams(1)), Integer.parseInt(message.getGameParams(2)));
                        break;
                    case END_GAME:
                        updateEndGame();
                        break;
                    default:
                        notifyError("Error: unknown command sent by client");
                }
            }
            catch (IOException e) {
                System.out.println("Error: failed I/O operation through socket");
                break;
            } catch (ClassNotFoundException e) {
                System.err.println("Error: failed to deserialize class");
                break;
            } catch (Exception e) {
                try{
                    notifyError(e.getMessage());
                }
                catch(Exception e1){
                    System.out.println("Error: failed I/O operation through socket");
                    break;
                }
            }
        }
        try{
            in.close();
            serverHandler.close();
            clientSocket.close();
        }
        catch (IOException e) {
            System.out.println("Error: failed to close socket");
        }
    }


    //this method asks the server if a game with the specified ID has already started
    @Override
    public boolean askIfGameStarted(int gameID){
        GameUpdateMessage message = null;
        try{
            serverHandler.startedGame(gameID);
            message = (GameUpdateMessage) in.readObject();
        }catch (ClassNotFoundException e){
            System.out.println("Error: received invalid message from server");
        }catch (IOException e){
            System.out.println("Remote error: " + e.getMessage());
        }
        if(message.getGameUpdateType() == GameUpdateType.ERROR){
            try{notifyError(message.getGameParams(0));}
            catch(Exception e){System.out.println("Error: failed I/O operation through socket");}
            return false;
        }
        else if (message.getGameUpdateType() == GameUpdateType.STARTED_GAME){
            return Boolean.parseBoolean(message.getGameParams(0));
        }
        return false;
    }

    //this method asks the server to add the player (associated to the client) to the game
    @Override
    public boolean tryToAddPlayerToGame(int gameID, String nickname, Color color) {
        GameUpdateMessage message;
        try{
            serverHandler.addPlayer(null, gameID, nickname, color);
            message = (GameUpdateMessage) in.readObject();
        }catch (ClassNotFoundException e){
            System.out.println("Error: received invalid message from server");
            return false;
        }catch (IOException e){
            System.out.println("Remote error: " + e.getMessage());
            return false;
        }
        if(message.getGameUpdateType() == GameUpdateType.ERROR){
            try{notifyError(message.getGameParams(0));}
            catch(Exception e){System.out.println("Error: failed I/O operation through socket");}
            return false;
        }
        else if (message.getGameUpdateType() == GameUpdateType.WAITING_FOR_PLAYERS){
            updateWaitingForPlayers(Boolean.parseBoolean(message.getGameParams(0)));
        }
        new Thread(this::manageServerMessages).start();
        return true;
    }

    //this method asks the server to create a new game
    @Override
    public boolean tryToStartNewGame(VirtualView client, int gameID, boolean firstFlight, int numberPlayers) {
        GameUpdateMessage message;
        try{
            serverHandler.startNewGame(client, gameID, firstFlight, numberPlayers);
            message = (GameUpdateMessage) in.readObject();
        }catch (ClassNotFoundException e){
            System.out.println("Error: received invalid message from server");
            return false;
        }catch (IOException e){
            System.out.println("Remote error: " + e.getMessage());
            return false;
        }
        if(message.getGameUpdateType() == GameUpdateType.ERROR){
            try{notifyError(message.getGameParams(0));}
            catch(Exception e){System.out.println("Error: failed I/O operation through socket");}
            return false;
        }
        else if (message.getGameUpdateType() == GameUpdateType.STARTED_GAME){

        }
        return true;
    }




    //notifies a view about an error committed while executing a method on the remote server; the parameter
    //errorMessage describes the type of error
    @Override
    public void notifyError(String errorMessage) {
        try{
            this.ui.notifyError(errorMessage);
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    //notifies a view about the fact that the game has started or not
    @Override
    public void notifyStartedGame(boolean startedGame) {}

    //notifies a view about the fact that the corresponding player has been correctly added to the game, but
    //the server is waiting for other players in order to start the assembling phase; the parameter firstFlight
    //in needed for the view to determine which type of ship board/flight board to show to the user
    @Override
    public void updateWaitingForPlayers(boolean firstFlight) {
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
    public void updateNewPlayer(String nickname, Color color) {
        try{
            this.ui.updateNewPlayer(nickname, color);
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    //notifies a view about the beginning of the assembling phase
    @Override
    public void updateStartAssembling() {
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
    public void updatePickedComponent(int imageID, boolean released) {
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
    public void updateShownComponent(int imageID, boolean released) {
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
    public void updateReservedComponent(String nickname, int imageID, boolean released) {
        try{
            this.ui.updateReservedComponent(nickname, imageID, released);
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    //notifies the view about the fact that the picked component of the corresponding player has been rotated
    @Override
    public void updateRotatePickedComponent() {
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
    public void updateAssembledComponent(String nickname, int imageID, Orientation orientation, int x, int y) {
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
    public void updatePickedDeck(List<Integer> deckIDs) {
        try{
            this.ui.updatePickedDeck(deckIDs);
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    //notifies the view about the fact that the corresponding player has successfully released a deck
    @Override
    public void updateReleasedDeck() {
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
    public void updateFinishAssembling(String nickname, int position) {
        try{
            this.ui.updateFinishAssembling(nickname, position);
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    //notifies the view that the hourglass has been turned around
    @Override
    public void updateStartNewCycle() {
        try{
            this.ui.updateStartNewCycle();
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    //notifies the view that the hourglass has finished running
    @Override
    public void updateFinishedCycle() {
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
    public void updateShipPlacement() {
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
    public void updateShipControl() {
        try{
            this.ui.updateShipControl();
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    //notifies the view that a component of a player's ship board has been destroyed
    @Override
    public void updateDestroyedComponent(String nickname, int x, int y) {
        try{
            this.ui.updateDestroyedComponent(nickname, x, y);
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    //notifies the view about a change in the number of crew of a cabin
    @Override
    public void updateCrewChange(String nickname, int x, int y, int change) {
        try{
            this.ui.updateCrewChange(nickname, x, y, change);
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    //notifies the view that a player has initialized a battery container with batteries
    @Override
    public void updateBatteries(String nickname, int x, int y, int change) {
        try{
            this.ui.updateBatteries(nickname, x, y, change);
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    //notifies the view about a change in the number of aliens of a cabin
    @Override
    public void updateAlienChange(String nickname, int x, int y, boolean isPurple, boolean added) {
        try{
            this.ui.updateAlienChange(nickname, x, y, isPurple, added);
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    //notifies the view that a good has been loaded in a cargo hold
    @Override
    public void updateLoadedGood(String nickname, int x, int y, Color good) {
        try{
            this.ui.updateLoadedGood(nickname, x, y, good);
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    //notifies the view that some goods have been removed form a cargo hold
    @Override
    public void updateRemovedGoods(String nickname, int x, int y, Color good, int numberGoods) {
        try{
            this.ui.updateRemovedGoods(nickname, x, y, good, numberGoods);
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    //notifies the view about the fact that a player has to pick a card in order to continue the game
    @Override
    public void updateCardPicking() {
        try{
            this.ui.updateCardPicking();
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    //notifies the view about the next player whose turn it is to perform an action
    @Override
    public void updateNextTurn(String nickname) {
        try{
            this.ui.updateNextTurn(nickname);
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    //notifies the view that a new card has been picked and must be solved
    @Override
    public void updateCardSolving(int imageID) {
        try{
            this.ui.updateCardSolving(imageID);
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    //notifies the view that a player has quit the game
    @Override
    public void updatePlayerQuit(String nickname) {
        try{
            this.ui.updatePlayerQuit(nickname);
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    //notifies the view that a player has gained/lost credits
    @Override
    public void updatePlayerCredits(String nickname, int change) {
        try{
            this.ui.updatePlayerCredits(nickname, change);
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    //notifies the view that the position of a player has changed
    @Override
    public void updatePlayerPosition(String nickname, int lap, int cell) {
        try{
            this.ui.updatePlayerPosition(nickname, lap, cell);
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    //notifies the view about the fact that the game is finished
    @Override
    public void updateEndGame() {
        try{
            this.ui.updateEndGame();
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
}
