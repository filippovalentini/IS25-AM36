package it.polimi.ingsw.galaxytrucker.network.socket.server;

import it.polimi.ingsw.galaxytrucker.controller.GameController;
import it.polimi.ingsw.galaxytrucker.model.enumerations.*;
import it.polimi.ingsw.galaxytrucker.network.VirtualServer;
import it.polimi.ingsw.galaxytrucker.network.socket.message.GameUpdateMessage;
import it.polimi.ingsw.galaxytrucker.network.socket.message.GameUpdateType;
import it.polimi.ingsw.galaxytrucker.network.socket.message.PlayerActionMessage;
import java.io.*;
import java.net.Socket;
import java.util.*;

//this class contains the methods to create and send serialized messages to a specific client starting
// from given parameters.
//it also manages the arrivals of messages from the handled client, converting them in specific invocations
//on the game controller
public class SocketClientHandler implements VirtualViewSocket {
    Socket socket;                  //socket for client-server communication
    GameController controller;      //controller of the game associated to the handled client
    ObjectInputStream in;
    ObjectOutputStream out;
    String nickname;
    boolean connectedClient;

    public SocketClientHandler(Socket socket) throws IOException {
        this.socket = socket;
        this.out = new ObjectOutputStream(socket.getOutputStream());
        this.in = new ObjectInputStream(socket.getInputStream());
        this.nickname = "";
        this.connectedClient = true;
    }

    //determines if the maneged client is siìtill correctly connected or not
    public boolean isClientConnected() {
        return connectedClient;
    }

    //sets the client connection status
    public void setClientStatus(boolean connected){
        this.connectedClient = connected;
    }

    //this method initializes the controller of the game associated to the handled client
    public void setController(GameController controller) {
        this.controller = controller;
    }

    //this method creates a loop that waits for client messages in order to correctly create a new game
    //and/or add a player to a game; it also initializes the controller associated to the client
    public void manageClientSetUp(Map<Integer, GameController> controllers) throws IOException {
        boolean clientAddedToGame = false;
        while (true) {
            try{
                PlayerActionMessage message = (PlayerActionMessage) in.readObject();
                switch(message.getGameAction()){
                    case ASK_STARTED_GAME:
                        boolean startedGame = controllers.containsKey(Integer.parseInt(message.getGameParams(0)));
                        notifyStartedGame(startedGame);
                        break;
                    case START_GAME:
                        int gameID = Integer.parseInt(message.getGameParams(0));
                        boolean firstFlight = Boolean.parseBoolean(message.getGameParams(1));
                        int numPlayers = Integer.parseInt(message.getGameParams(2));
                        GameController gameController = new GameController(firstFlight, numPlayers);
                        setController(gameController);
                        controllers.put(gameID, gameController);
                        notifyStartedGame(true);
                        break;
                    case ADD_PLAYER:
                        setController(controllers.get(Integer.parseInt(message.getGameParams(0))));
                        controller.addPlayer(this, message.getGameParams(1), Color.convertToColor(message.getGameParams(2)));
                        this.nickname = message.getGameParams(1);
                        clientAddedToGame = true;
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
            if(clientAddedToGame){
                break;
            }
        }
    }

    //this method creates a loop that waits for client's messages and (based on the type of message received)
    //updates the model state by invoking a method on the game controller
    public void manageClientMessages() throws IOException {
        new SocketPingThread(socket, this, in, out).start();
        while (true) {
            try{
                PlayerActionMessage message = (PlayerActionMessage) in.readObject();
                switch(message.getGameAction()){
                    case PONG:
                        setClientStatus(true);
                        break;
                    case PICK_HIDDEN:
                        controller.pickHidden(message.getGameParams(0));
                        break;
                    case PICK_SHOWN:
                        controller.pickShown(message.getGameParams(0), Integer.parseInt(message.getGameParams(1)));
                        break;
                    case RELEASE:
                        controller.putShown(message.getGameParams(0));
                        break;
                    case RESERVE:
                        controller.reserveComponent(message.getGameParams(0));
                        break;
                    case PICK_RESERVED:
                        controller.pickReservedComponent(message.getGameParams(0), Integer.parseInt(message.getGameParams(1)));
                        break;
                    case ROTATE:
                        controller.rotatePickedComponent(message.getGameParams(0));
                        break;
                    case ASSEMBLE:
                        controller.assembleComponent(message.getGameParams(0), Integer.parseInt(message.getGameParams(1)), Integer.parseInt(message.getGameParams(2)));
                        break;
                    case PICK_DECK:
                        controller.pickDeck(message.getGameParams(0), Integer.parseInt(message.getGameParams(1)));
                        break;
                    case RELEASE_DECK:
                        controller.releaseDeck(message.getGameParams(0));
                        break;
                    case SET_POSITION:
                        controller.setPosition(message.getGameParams(0), Integer.parseInt(message.getGameParams(1)));
                        break;
                    case HOURGLASS:
                        controller.startNewCycle(message.getGameParams(0));
                        break;
                    case DESTROY:
                        controller.destroyComponent(message.getGameParams(0), Integer.parseInt(message.getGameParams(1)), Integer.parseInt(message.getGameParams(2)));
                        break;
                    case ADD_CREW:
                        controller.addCrew(message.getGameParams(0), Integer.parseInt(message.getGameParams(1)), Integer.parseInt(message.getGameParams(2)));
                        break;
                    case ADD_BATTERIES:
                        controller.addBatteries(message.getGameParams(0), Integer.parseInt(message.getGameParams(1)), Integer.parseInt(message.getGameParams(2)));
                        break;
                    case ADD_ALIEN:
                        controller.addAlien(message.getGameParams(0), Boolean.parseBoolean(message.getGameParams(1)), Integer.parseInt(message.getGameParams(2)), Integer.parseInt(message.getGameParams(3)));
                        break;
                    case PICK_CARD:
                        controller.pickNextCard(message.getGameParams(0));
                        break;
                    case QUIT:
                        controller.quitGame(message.getGameParams(0));
                        break;
                    case SKIP:
                        controller.skip(message.getGameParams(0));
                        break;
                    case LANDING:
                        controller.landing(message.getGameParams(0), deserializeList(message.getGameParams(1)), deserializeList(message.getGameParams(2)), deserializeList(message.getGameParams(3)));
                        break;
                    case HIT_SHIP:
                        controller.hit(message.getGameParams(0), Integer.parseInt(message.getGameParams(1)), Boolean.parseBoolean(message.getGameParams(2)), Boolean.parseBoolean(message.getGameParams(3)));
                        break;
                    case FLY:
                        controller.fly(message.getGameParams(0), Integer.parseInt(message.getGameParams(1)));
                        break;
                    case DEFEAT:
                        controller.defeat(message.getGameParams(0), Integer.parseInt(message.getGameParams(1)), Boolean.parseBoolean(message.getGameParams(2)));
                        break;
                    case LOAD_GOODS:
                        controller.loadGoods(message.getGameParams(0), deserializeList(message.getGameParams(1)), deserializeList(message.getGameParams(2)));
                        break;
                    case PLANET_LANDING:
                        controller.planetLanding(message.getGameParams(0), Integer.parseInt(message.getGameParams(1)));
                        break;
                    case USE_BATTERIES:
                        controller.useBatteries(message.getGameParams(0), Integer.parseInt(message.getGameParams(1)));
                        break;
                    default:
                        notifyError("Error: unknown command sent by client");
                }
            }
            catch (IOException e) {
                controller.forceQuit(nickname);
                break;
            } catch (ClassNotFoundException e) {
                System.err.println("Error: failed to deserialize class");
                break;
            } catch (Exception e) {
                try{
                    notifyError(e.getMessage());
                }
                catch(Exception e1){
                    controller.forceQuit(nickname);
                    break;
                }
            }
        }
        in.close();
        out.close();
        socket.close();
    }

    //converts a serialized list of integers in an effective list
    public static List<Integer> deserializeList(String data) {
        List<Integer> list = new ArrayList<>();
        for (String num : data.split(",")) {
            list.add(Integer.parseInt(num));
        }
        return list;
    }

    //notifies a view about an error committed while executing a method on the remote server; the parameter
    //errorMessage describes the type of error
    @Override
    public void notifyError(String errorMessage) throws IOException{
        List<String> params = new ArrayList<>(Arrays.asList(errorMessage));
        GameUpdateMessage message = new GameUpdateMessage(GameUpdateType.ERROR, params);
        out.writeObject(message);
    }

    //notifies a view about the fact that the game has started or not
    @Override
    public void notifyStartedGame(boolean startedGame) throws IOException{
        List<String> params = new ArrayList<>(Arrays.asList(String.valueOf(startedGame)));
        GameUpdateMessage message = new GameUpdateMessage(GameUpdateType.STARTED_GAME, params);
        out.writeObject(message);
    }

    //notifies a view about the fact that the corresponding player has been correctly added to the game, but
    //the server is waiting for other players in order to start the assembling phase; the parameter firstFlight
    //in needed for the view to determine which type of ship board/flight board to show to the user
    @Override
    public void updateWaitingForPlayers(boolean firstFlight) throws IOException{
        List<String> params = new ArrayList<>(Arrays.asList(String.valueOf(firstFlight)));
        GameUpdateMessage message = new GameUpdateMessage(GameUpdateType.WAITING_FOR_PLAYERS, params);
        out.writeObject(message);
    }

    //notifies a view about the presence of another player in the game; this method is invoked before the
    //beginning of the assembling phase, therefore just the nickname and color of the new player is required
    @Override
    public void updateNewPlayer(String nickname, Color color) throws IOException{
        List<String> params = new ArrayList<>(Arrays.asList(nickname, color.toString()));
        GameUpdateMessage message = new GameUpdateMessage(GameUpdateType.NEW_PLAYER, params);
        out.writeObject(message);
    }

    //notifies a view about the beginning of the assembling phase
    @Override
    public void updateStartAssembling() throws IOException{
        List<String> params = new ArrayList<>();
        GameUpdateMessage message = new GameUpdateMessage(GameUpdateType.START_ASSEMBLING, params);
        out.writeObject(message);
    }

    //notifies the view about the fact that a component has been successfully picked/released (depending on
    //the value of the boolean parameter) by the corresponding player; the parameter imageID is needed for the
    //view in order to show the right component to the user
    @Override
    public void updatePickedComponent(int imageID, boolean released) throws IOException{
        List<String> params = new ArrayList<>(Arrays.asList(String.valueOf(imageID), String.valueOf(released)));
        GameUpdateMessage message = new GameUpdateMessage(GameUpdateType.PICKED_COMPONENT, params);
        out.writeObject(message);
    }

    //notifies the view about the fact that a shown component has been picked/released (depending on the value
    //of the boolean parameter); the parameter imageID is needed for the view in order to show the right
    //component to the user
    @Override
    public void updateShownComponent(int imageID, boolean released) throws IOException{
        List<String> params = new ArrayList<>(Arrays.asList(String.valueOf(imageID), String.valueOf(released)));
        GameUpdateMessage message = new GameUpdateMessage(GameUpdateType.SHOWN_COMPONENT, params);
        out.writeObject(message);
    }

    //notifies the view about the fact that a player (identified by the nickname parameter) has picked a reserved
    //component/ reserved a component (depending on the value of the boolean parameter); the parameter imageID
    //is needed for the view in order to show the right component to the user
    @Override
    public void updateReservedComponent(String nickname, int imageID, boolean released) throws IOException{
        List<String> params = new ArrayList<>(Arrays.asList(nickname, String.valueOf(imageID), String.valueOf(released)));
        GameUpdateMessage message = new GameUpdateMessage(GameUpdateType.RESERVED_COMPONENT, params);
        out.writeObject(message);
    }

    //notifies the view about the fact that the picked component of the corresponding player has been rotated
    @Override
    public void updateRotatePickedComponent() throws IOException{
        List<String> params = new ArrayList<>();
        GameUpdateMessage message = new GameUpdateMessage(GameUpdateType.ROTATE_PICKED_COMPONENT, params);
        out.writeObject(message);
    }

    //notifies the view about the fact that a player (identified by the nickname parameter) has assembled a
    //component in position (x,y) of its ship board; the parameter imageID is needed for the view in order
    //to show the right component to the user
    @Override
    public void updateAssembledComponent(String nickname, int imageID, Orientation orientation, int x, int y) throws IOException{
        List<String> params = new ArrayList<>(Arrays.asList(nickname, String.valueOf(imageID), orientation.toString(), String.valueOf(x), String.valueOf(y)));
        GameUpdateMessage message = new GameUpdateMessage(GameUpdateType.ASSEMBLED_COMPONENT, params);
        out.writeObject(message);
    }

    //notifies the view about the fact that the corresponding player has successfully picked a deck; the parameter
    //contains the list of image IDs of the cards contained in the deck, so that the view can show the
    //correct adventure cards to the user
    @Override
    public void updatePickedDeck(List<Integer> deckIDs) throws IOException{
        List<String> params = new ArrayList<>();
        for(Integer deckID : deckIDs){
            params.add(String.valueOf(deckID));
        }
        GameUpdateMessage message = new GameUpdateMessage(GameUpdateType.PICKED_DECK, params);
        out.writeObject(message);
    }

    //notifies the view about the fact that the corresponding player has successfully released a deck
    @Override
    public void updateReleasedDeck() throws IOException{
        List<String> params = new ArrayList<>();
        GameUpdateMessage message = new GameUpdateMessage(GameUpdateType.RELEASED_DECK, params);
        out.writeObject(message);
    }

    //notifies the view about the fact that the corresponding player has finished the assembling phase and is
    //correctly positioned on the flight board; still, other players have to finish building their ships
    @Override
    public void updateFinishAssembling(String nickname, int position) throws IOException{
        List<String> params = new ArrayList<>(Arrays.asList(nickname, String.valueOf(position)));
        GameUpdateMessage message = new GameUpdateMessage(GameUpdateType.FINISH_ASSEMBLING, params);
        out.writeObject(message);
    }

    //notifies the view that the hourglass has been turned around
    @Override
    public void updateStartNewCycle() throws IOException{
        List<String> params = new ArrayList<>();
        GameUpdateMessage message = new GameUpdateMessage(GameUpdateType.STARTED_CYCLE, params);
        out.writeObject(message);
    }

    //notifies the view that the hourglass has finished running
    @Override
    public void updateFinishedCycle() throws IOException{
        List<String> params = new ArrayList<>();
        GameUpdateMessage message = new GameUpdateMessage(GameUpdateType.FINISHED_CYCLE, params);
        out.writeObject(message);
    }

    //invoked when the game switches to the ship placement phase, which means that the players can only
    //place their ship on the flight board
    @Override
    public void updateShipPlacement() throws IOException{
        List<String> params = new ArrayList<>();
        GameUpdateMessage message = new GameUpdateMessage(GameUpdateType.SHIP_PLACEMENT, params);
        out.writeObject(message);
    }

    //notifies the view that all the players have concluded the assembling phase, which means that the players
    //enter the ship control phase
    @Override
    public void updateShipControl() throws IOException{
        List<String> params = new ArrayList<>();
        GameUpdateMessage message = new GameUpdateMessage(GameUpdateType.SHIP_CONTROL, params);
        out.writeObject(message);
    }

    //notifies the view that a component of a player's ship board has been destroyed
    @Override
    public void updateDestroyedComponent(String nickname, int x, int y) throws IOException{
        List<String> params = new ArrayList<>(Arrays.asList(nickname, String.valueOf(x), String.valueOf(y)));
        GameUpdateMessage message = new GameUpdateMessage(GameUpdateType.DESTROYED_COMPONENT, params);
        out.writeObject(message);
    }

    //notifies the view about a change in the number of crew of a cabin
    @Override
    public void updateCrewChange(String nickname, int x, int y, int change) throws IOException{
        List<String> params = new ArrayList<>(Arrays.asList(nickname, String.valueOf(x), String.valueOf(y), String.valueOf(change)));
        GameUpdateMessage message = new GameUpdateMessage(GameUpdateType.CREW_CHANGE, params);
        out.writeObject(message);
    }

    //notifies the view that a player has initialized a battery container with batteries
    @Override
    public void updateBatteries(String nickname, int x, int y, int change) throws IOException{
        List<String> params = new ArrayList<>(Arrays.asList(nickname, String.valueOf(x), String.valueOf(y), String.valueOf(change)));
        GameUpdateMessage message = new GameUpdateMessage(GameUpdateType.BATTERY_CHANGE, params);
        out.writeObject(message);
    }

    //notifies the view about a change in the number of aliens of a cabin
    @Override
    public void updateAlienChange(String nickname, int x, int y, boolean isPurple, boolean added) throws IOException{
        List<String> params = new ArrayList<>(Arrays.asList(nickname, String.valueOf(x), String.valueOf(y), String.valueOf(isPurple), String.valueOf(added)));
        GameUpdateMessage message = new GameUpdateMessage(GameUpdateType.ALIEN_CHANGE, params);
        out.writeObject(message);
    }

    //notifies the view that a good has been loaded in a cargo hold
    @Override
    public void updateLoadedGood(String nickname, int x, int y, Color good) throws IOException{
        List<String> params = new ArrayList<>(Arrays.asList(nickname, String.valueOf(x), String.valueOf(y), good.toString()));
        GameUpdateMessage message = new GameUpdateMessage(GameUpdateType.LOADED_GOOD, params);
        out.writeObject(message);
    }

    //notifies the view that some goods have been removed form a cargo hold
    @Override
    public void updateRemovedGoods(String nickname, int x, int y, Color good, int numberGoods) throws IOException{
        List<String> params = new ArrayList<>(Arrays.asList(nickname, String.valueOf(x), String.valueOf(y), good.toString(), String.valueOf(numberGoods)));
        GameUpdateMessage message = new GameUpdateMessage(GameUpdateType.REMOVED_GOODS, params);
        out.writeObject(message);
    }

    //notifies the view about the fact that a player has to pick a card in order to continue the game
    @Override
    public void updateCardPicking() throws IOException{
        List<String> params = new ArrayList<>();
        GameUpdateMessage message = new GameUpdateMessage(GameUpdateType.CARD_PICKING, params);
        out.writeObject(message);
    }

    //notifies the view about the next player whose turn it is to perform an action
    @Override
    public void updateNextTurn(String nickname) throws IOException{
        List<String> params = new ArrayList<>(Arrays.asList(nickname));
        GameUpdateMessage message = new GameUpdateMessage(GameUpdateType.NEXT_TURN, params);
        out.writeObject(message);
    }

    //notifies the view that a new card has been picked and must be solved
    @Override
    public void updateCardSolving(int imageID) throws IOException{
        List<String> params = new ArrayList<>(Arrays.asList(String.valueOf(imageID)));
        GameUpdateMessage message = new GameUpdateMessage(GameUpdateType.CARD_SOLVING, params);
        out.writeObject(message);
    }

    //notifies the view that a player has quit the game
    @Override
    public void updatePlayerQuit(String nickname) throws IOException{
        List<String> params = new ArrayList<>(Arrays.asList(nickname));
        GameUpdateMessage message = new GameUpdateMessage(GameUpdateType.PLAYER_QUIT, params);
        out.writeObject(message);
    }

    //notifies the view that a player has gained/lost credits
    @Override
    public void updatePlayerCredits(String nickname, int change) throws IOException{
        List<String> params = new ArrayList<>(Arrays.asList(nickname, String.valueOf(change)));
        GameUpdateMessage message = new GameUpdateMessage(GameUpdateType.CREDITS_CHANGE, params);
        out.writeObject(message);
    }

    //notifies the view that the position of a player has changed
    @Override
    public void updatePlayerPosition(String nickname, int lap, int cell) throws IOException{
        List<String> params = new ArrayList<>(Arrays.asList(nickname, String.valueOf(lap), String.valueOf(cell)));
        GameUpdateMessage message = new GameUpdateMessage(GameUpdateType.POSITION_CHANGE, params);
        out.writeObject(message);
    }

    //notifies the view about the fact that the game is finished
    @Override
    public void updateEndGame() throws IOException{
        List<String> params = new ArrayList<>();
        GameUpdateMessage message = new GameUpdateMessage(GameUpdateType.END_GAME, params);
        out.writeObject(message);
    }

}
