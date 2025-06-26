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

/**
 * This class handles the communication with a single client connected via socket.
 */
public class SocketClientHandler implements VirtualViewSocket {
    Socket socket;                  //socket for client-server communication
    GameController controller;      //controller of the game associated to the handled client
    ObjectInputStream in;
    ObjectOutputStream out;
    String nickname;
    boolean connectedClient;
    /**
     * Constructor for the SocketClientHandler.
     *
     * @param socket the socket connected to the client
     * @throws IOException if an I/O error occurs when creating the streams
     */
    public SocketClientHandler(Socket socket) throws IOException {
        this.socket = socket;
        this.out = new ObjectOutputStream(socket.getOutputStream());
        this.in = new ObjectInputStream(socket.getInputStream());
        this.nickname = "";
        this.connectedClient = true;
    }

    //determines if the maneged client is still correctly connected or not
    /**
     * Checks if the client is connected.
     *
     * @return true if the client is connected, false otherwise
     */
    public boolean isClientConnected() {
        return connectedClient;
    }

    //sets the client connection status
    /**
     * Sets the connection status of the client.
     *
     * @param connected true if the client is connected, false otherwise
     */
    public void setClientStatus(boolean connected){
        this.connectedClient = connected;
    }

    //this method initializes the controller of the game associated to the handled client
    /**
     * Sets the controller for the game associated with this client.
     *
     * @param controller the GameController to be set
     */
    public void setController(GameController controller) {
        this.controller = controller;
    }

    //this method creates a loop that waits for client messages in order to correctly create a new game
    //and/or add a player to a game; it also initializes the controller associated to the client
    /**
     * Manages the setup of the client, including starting a new game or adding a player to an existing game.
     *
     * @param controllers a map of game controllers indexed by game ID
     * @throws IOException if an I/O error occurs during communication with the client
     */
    public void manageClientSetUp(Map<Integer, GameController> controllers) throws IOException {
        boolean clientAddedToGame = false;
        while (true) { //loop until the client is added to a game
            try{
                PlayerActionMessage message = (PlayerActionMessage) in.readObject(); //reads a message from the client
                switch(message.getGameAction()){ //switches on the type of message received
                    case ASK_STARTED_GAME: //client asks if a game has already started
                        boolean startedGame = controllers.containsKey(Integer.parseInt(message.getGameParams(0))); //checks if the game with the given ID has already started
                        notifyStartedGame(startedGame); //notifies the client about the game status
                        break;
                    case START_GAME: //client wants to start a new game
                        int gameID = Integer.parseInt(message.getGameParams(0)); //retrieves the game ID from the message
                        boolean firstFlight = Boolean.parseBoolean(message.getGameParams(1)); //retrieves the first flight status from the message
                        int numPlayers = Integer.parseInt(message.getGameParams(2)); //retrieves the number of players from the message
                        GameController gameController = new GameController(firstFlight, numPlayers); //creates a new game controller with the specified parameters
                        setController(gameController); //sets the controller for this client
                        controllers.put(gameID, gameController); //adds the controller to the map of controllers
                        gameController.setEndGameManagement(()->{  //sets the end game management for the controller
                            controllers.remove(gameID); //removes the controller from the map of controllers
                        });
                        notifyStartedGame(true); //notifies the client that the game has started
                        break;
                    case ADD_PLAYER: //client wants to add a player to an existing game
                        setController(controllers.get(Integer.parseInt(message.getGameParams(0)))); //retrieves the controller for the game with the specified ID
                        controller.addPlayer(this, message.getGameParams(1), Color.convertToColor(message.getGameParams(2))); //adds the player to the game with the specified nickname and color
                        this.nickname = message.getGameParams(1); //sets the nickname for this client
                        clientAddedToGame = true; //sets the flag indicating that the client has been added to a game
                        break;
                    default:
                        notifyError("Error: unknown command sent by client"); //notifies the client about an unknown command
                }
            }
            catch (IOException e) {
                System.out.println("Error: failed I/O operation through socket"); //prints an error message if an I/O error occurs
                break;
            } catch (ClassNotFoundException e) {
                System.err.println("Error: failed to deserialize class"); //prints an error message if the class cannot be deserialized
                break;
            } catch (Exception e) {
                try{
                    notifyError(e.getMessage());
                }
                catch(Exception e1){
                    System.out.println("Error: failed I/O operation through socket"); //prints an error message if an exception occurs while notifying the client
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
    /**
     * Manages messages from the client, processing player actions and updating the game state accordingly.
     *
     * @throws IOException if an I/O error occurs during communication with the client
     */
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
                forceQuit();
                break;
            } catch (ClassNotFoundException e) {
                System.err.println("Error: failed to deserialize class");
                break;
            } catch (Exception e) {
                try{
                    notifyError(e.getMessage());
                }
                catch(Exception e1){
                    forceQuit();
                    break;
                }
            }
        }
        in.close();
        out.close();
        socket.close();
    }

    /**
     * Forces the player to quit the game
     *
     */
    public void forceQuit(){
        try{controller.forceQuit(nickname);} catch (Exception ignored) {}
    }

    //converts a serialized list of integers in an effective list
    /**
     * Deserializes a comma-separated string into a list of integers.
     *
     * @param data the string containing comma-separated integers
     * @return a list of integers
     */
    public static List<Integer> deserializeList(String data) {
        List<Integer> list = new ArrayList<>();
        for (String num : data.split(",")) {
            list.add(Integer.parseInt(num));
        }
        return list;
    }

    //notifies a view about an error committed while executing a method on the remote server; the parameter
    //errorMessage describes the type of error
    /** * Notifies the client about an error that occurred during the execution of a method on the server.
     *
     * @param errorMessage the error message to be sent to the client
     * @throws IOException if an I/O error occurs while sending the message
     */
    @Override
    public void notifyError(String errorMessage) throws IOException{
        List<String> params = new ArrayList<>(Arrays.asList(errorMessage));
        GameUpdateMessage message = new GameUpdateMessage(GameUpdateType.ERROR, params);
        out.writeObject(message);
    }

    //notifies a view about the fact that the game has started or not

    /**
     * Notifies the client whether the game has started or not.
     * @param startedGame
     * @throws IOException
     */
    @Override
    public void notifyStartedGame(boolean startedGame) throws IOException{
        List<String> params = new ArrayList<>(Arrays.asList(String.valueOf(startedGame)));
        GameUpdateMessage message = new GameUpdateMessage(GameUpdateType.STARTED_GAME, params);
        out.writeObject(message);
    }

    //notifies a view about the fact that the corresponding player has been correctly added to the game, but
    //the server is waiting for other players in order to start the assembling phase; the parameter firstFlight
    //in needed for the view to determine which type of ship board/flight board to show to the user
    /**
     * Notifies the client that the player is waiting for other players to join before starting the game.
     *
     * @param firstFlight true if this is the first flight, false otherwise
     * @throws IOException if an I/O error occurs while sending the message
     */
    @Override
    public void updateWaitingForPlayers(boolean firstFlight) throws IOException{
        List<String> params = new ArrayList<>(Arrays.asList(String.valueOf(firstFlight)));
        GameUpdateMessage message = new GameUpdateMessage(GameUpdateType.WAITING_FOR_PLAYERS, params);
        out.writeObject(message);
    }

    //notifies a view about the presence of another player in the game; this method is invoked before the
    //beginning of the assembling phase, therefore just the nickname and color of the new player is required
    /**
     * Notifies the client about a new player joining the game.
     *
     * @param nickname the nickname of the new player
     * @param color    the color of the new player
     * @throws IOException if an I/O error occurs while sending the message
     */
    @Override
    public void updateNewPlayer(String nickname, Color color) throws IOException{
        List<String> params = new ArrayList<>(Arrays.asList(nickname, color.toString()));
        GameUpdateMessage message = new GameUpdateMessage(GameUpdateType.NEW_PLAYER, params);
        out.writeObject(message);
    }

    //notifies a view about the beginning of the assembling phase
    /**
     * Notifies the client that the assembling phase has started.
     *
     * @throws IOException if an I/O error occurs while sending the message
     */
    @Override
    public void updateStartAssembling() throws IOException{
        List<String> params = new ArrayList<>();
        GameUpdateMessage message = new GameUpdateMessage(GameUpdateType.START_ASSEMBLING, params);
        out.writeObject(message);
    }

    //notifies the view about the fact that a component has been successfully picked/released (depending on
    //the value of the boolean parameter) by the corresponding player; the parameter imageID is needed for the
    //view in order to show the right component to the user
    /**
     * Notifies the client that a component has been picked or released.
     *
     * @param imageID  the ID of the component image
     * @param released true if the component has been released, false if it has been picked
     * @throws IOException if an I/O error occurs while sending the message
     */
    @Override
    public void updatePickedComponent(int imageID, boolean released) throws IOException{
        List<String> params = new ArrayList<>(Arrays.asList(String.valueOf(imageID), String.valueOf(released)));
        GameUpdateMessage message = new GameUpdateMessage(GameUpdateType.PICKED_COMPONENT, params);
        out.writeObject(message);
    }

    //notifies the view about the fact that a shown component has been picked/released (depending on the value
    //of the boolean parameter); the parameter imageID is needed for the view in order to show the right
    //component to the user
    /**
     * Notifies the client that a shown component has been picked or released.
     *
     * @param imageID  the ID of the component image
     * @param released true if the component has been released, false if it has been picked
     * @throws IOException if an I/O error occurs while sending the message
     */
    @Override
    public void updateShownComponent(int imageID, boolean released) throws IOException{
        List<String> params = new ArrayList<>(Arrays.asList(String.valueOf(imageID), String.valueOf(released)));
        GameUpdateMessage message = new GameUpdateMessage(GameUpdateType.SHOWN_COMPONENT, params);
        out.writeObject(message);
    }

    //notifies the view about the fact that a player (identified by the nickname parameter) has picked a reserved
    //component/ reserved a component (depending on the value of the boolean parameter); the parameter imageID
    //is needed for the view in order to show the right component to the user
    /**
     * Notifies the client that a reserved component has been picked or released by a player.
     *
     * @param nickname the nickname of the player
     * @param imageID  the ID of the component image
     * @param released true if the component has been released, false if it has been picked
     * @throws IOException if an I/O error occurs while sending the message
     */
    @Override
    public void updateReservedComponent(String nickname, int imageID, boolean released) throws IOException{
        List<String> params = new ArrayList<>(Arrays.asList(nickname, String.valueOf(imageID), String.valueOf(released)));
        GameUpdateMessage message = new GameUpdateMessage(GameUpdateType.RESERVED_COMPONENT, params);
        out.writeObject(message);
    }

    //notifies the view about the fact that the picked component of the corresponding player has been rotated
    /**
     * Notifies the client that the picked component has been rotated.
     *
     * @throws IOException if an I/O error occurs while sending the message
     */
    @Override
    public void updateRotatePickedComponent() throws IOException{
        List<String> params = new ArrayList<>();
        GameUpdateMessage message = new GameUpdateMessage(GameUpdateType.ROTATE_PICKED_COMPONENT, params);
        out.writeObject(message);
    }

    //notifies the view about the fact that a player (identified by the nickname parameter) has assembled a
    //component in position (x,y) of its ship board; the parameter imageID is needed for the view in order
    //to show the right component to the user
    /**
     * Notifies the client that a component has been assembled by a player.
     *
     * @param nickname    the nickname of the player who assembled the component
     * @param imageID     the ID of the component image
     * @param orientation the orientation of the component
     * @param x           the x-coordinate of the position on the ship board
     * @param y           the y-coordinate of the position on the ship board
     * @throws IOException if an I/O error occurs while sending the message
     */
    @Override
    public void updateAssembledComponent(String nickname, int imageID, Orientation orientation, int x, int y) throws IOException{
        List<String> params = new ArrayList<>(Arrays.asList(nickname, String.valueOf(imageID), orientation.toString(), String.valueOf(x), String.valueOf(y)));
        GameUpdateMessage message = new GameUpdateMessage(GameUpdateType.ASSEMBLED_COMPONENT, params);
        out.writeObject(message);
    }

    //notifies the view about the fact that the corresponding player has successfully picked a deck; the parameter
    //contains the list of image IDs of the cards contained in the deck, so that the view can show the
    //correct adventure cards to the user
    /**
     * Notifies the client that a deck has been picked by the player.
     *
     * @param deckIDs a list of image IDs of the cards in the picked deck
     * @throws IOException if an I/O error occurs while sending the message
     */
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
    /**
     * Notifies the client that a deck has been released by the player.
     *
     * @throws IOException if an I/O error occurs while sending the message
     */
    @Override
    public void updateReleasedDeck() throws IOException{
        List<String> params = new ArrayList<>();
        GameUpdateMessage message = new GameUpdateMessage(GameUpdateType.RELEASED_DECK, params);
        out.writeObject(message);
    }

    //notifies the view about the fact that the corresponding player has finished the assembling phase and is
    //correctly positioned on the flight board; still, other players have to finish building their ships
    /**
     * Notifies the client that a player has finished assembling their ship.
     *
     * @param nickname the nickname of the player who finished assembling
     * @param position the position on the flight board where the player is located
     * @throws IOException if an I/O error occurs while sending the message
     */
    @Override
    public void updateFinishAssembling(String nickname, int position) throws IOException{
        List<String> params = new ArrayList<>(Arrays.asList(nickname, String.valueOf(position)));
        GameUpdateMessage message = new GameUpdateMessage(GameUpdateType.FINISH_ASSEMBLING, params);
        out.writeObject(message);
    }

    //notifies the view that the hourglass has been turned around
    /**
     * Notifies the client that a new cycle has started.
     *
     * @throws IOException if an I/O error occurs while sending the message
     */
    @Override
    public void updateStartNewCycle() throws IOException{
        List<String> params = new ArrayList<>();
        GameUpdateMessage message = new GameUpdateMessage(GameUpdateType.STARTED_CYCLE, params);
        out.writeObject(message);
    }

    //notifies the view that the hourglass has finished running
    /**
     * Notifies the client that the current cycle has finished.
     *
     * @throws IOException if an I/O error occurs while sending the message
     */
    @Override
    public void updateFinishedCycle() throws IOException{
        List<String> params = new ArrayList<>();
        GameUpdateMessage message = new GameUpdateMessage(GameUpdateType.FINISHED_CYCLE, params);
        out.writeObject(message);
    }

    //invoked when the game switches to the ship placement phase, which means that the players can only
    //place their ship on the flight board
    /**
     * Notifies the client that the ship placement phase has started.
     *
     * @throws IOException if an I/O error occurs while sending the message
     */
    @Override
    public void updateShipPlacement() throws IOException{
        List<String> params = new ArrayList<>();
        GameUpdateMessage message = new GameUpdateMessage(GameUpdateType.SHIP_PLACEMENT, params);
        out.writeObject(message);
    }

    //notifies the view that all the players have concluded the assembling phase, which means that the players
    //enter the ship control phase
    /**
     * Notifies the client that all players have finished assembling their ships and are now in the ship control phase.
     *
     * @throws IOException if an I/O error occurs while sending the message
     */
    @Override
    public void updateShipControl() throws IOException{
        List<String> params = new ArrayList<>();
        GameUpdateMessage message = new GameUpdateMessage(GameUpdateType.SHIP_CONTROL, params);
        out.writeObject(message);
    }

    //notifies the view that a player has to repair its ship board before the player in turn can pick a new card
    /**
     * Notifies the client that a player needs to repair their ship.
     *
     * @param nickname the nickname of the player who needs to repair their ship
     * @throws IOException if an I/O error occurs while sending the message
     */
    @Override
    public void updateShipRepair(String nickname) throws IOException{
        List<String> params = new ArrayList<>(Arrays.asList(nickname));
        GameUpdateMessage message = new GameUpdateMessage(GameUpdateType.SHIP_REPAIR, params);
        out.writeObject(message);
    }

    //notifies the view that a component of a player's ship board has been destroyed
    /**
     * Notifies the client that a component of a player's ship has been destroyed.
     *
     * @param nickname the nickname of the player whose component was destroyed
     * @param x        the x-coordinate of the destroyed component
     * @param y        the y-coordinate of the destroyed component
     * @throws IOException if an I/O error occurs while sending the message
     */
    @Override
    public void updateDestroyedComponent(String nickname, int x, int y) throws IOException{
        List<String> params = new ArrayList<>(Arrays.asList(nickname, String.valueOf(x), String.valueOf(y)));
        GameUpdateMessage message = new GameUpdateMessage(GameUpdateType.DESTROYED_COMPONENT, params);
        out.writeObject(message);
    }

    //notifies the view about a change in the number of crew of a cabin
    /**
     * Notifies the client about a change in the number of crew members in a cabin.
     *
     * @param nickname the nickname of the player whose crew is changing
     * @param x        the x-coordinate of the cabin
     * @param y        the y-coordinate of the cabin
     * @param change   the change in the number of crew members (positive or negative)
     * @throws IOException if an I/O error occurs while sending the message
     */
    @Override
    public void updateCrewChange(String nickname, int x, int y, int change) throws IOException{
        List<String> params = new ArrayList<>(Arrays.asList(nickname, String.valueOf(x), String.valueOf(y), String.valueOf(change)));
        GameUpdateMessage message = new GameUpdateMessage(GameUpdateType.CREW_CHANGE, params);
        out.writeObject(message);
    }

    //notifies the view that a player has initialized a battery container with batteries

    /**
     * Notifies the client that a player has initialized a battery container with batteries.
     * @param nickname
     * @param x
     * @param y
     * @param change
     * @throws IOException
     */
    @Override
    public void updateBatteries(String nickname, int x, int y, int change) throws IOException{
        List<String> params = new ArrayList<>(Arrays.asList(nickname, String.valueOf(x), String.valueOf(y), String.valueOf(change)));
        GameUpdateMessage message = new GameUpdateMessage(GameUpdateType.BATTERY_CHANGE, params);
        out.writeObject(message);
    }

    //notifies the view about a change in the number of aliens of a cabin
    /**
     * Notifies the client about a change in the number of aliens in a cabin.
     *
     * @param nickname  the nickname of the player whose cabin is changing
     * @param x         the x-coordinate of the cabin
     * @param y         the y-coordinate of the cabin
     * @param isPurple  true if the alien is purple, false otherwise
     * @param added     true if an alien was added, false if one was removed
     * @throws IOException if an I/O error occurs while sending the message
     */
    @Override
    public void updateAlienChange(String nickname, int x, int y, boolean isPurple, boolean added) throws IOException{
        List<String> params = new ArrayList<>(Arrays.asList(nickname, String.valueOf(x), String.valueOf(y), String.valueOf(isPurple), String.valueOf(added)));
        GameUpdateMessage message = new GameUpdateMessage(GameUpdateType.ALIEN_CHANGE, params);
        out.writeObject(message);
    }

    //notifies the view that a good has been loaded in a cargo hold
    /**
     * Notifies the client that a good has been loaded into a cargo hold.
     *
     * @param nickname the nickname of the player who loaded the good
     * @param x        the x-coordinate of the cargo hold
     * @param y        the y-coordinate of the cargo hold
     * @param good     the color of the good that was loaded
     * @throws IOException if an I/O error occurs while sending the message
     */
    @Override
    public void updateLoadedGood(String nickname, int x, int y, Color good) throws IOException{
        List<String> params = new ArrayList<>(Arrays.asList(nickname, String.valueOf(x), String.valueOf(y), good.toString()));
        GameUpdateMessage message = new GameUpdateMessage(GameUpdateType.LOADED_GOOD, params);
        out.writeObject(message);
    }

    //notifies the view that some goods have been removed form a cargo hold
    /**
     * Notifies the client that goods have been removed from a cargo hold.
     *
     * @param nickname      the nickname of the player who removed the goods
     * @param x             the x-coordinate of the cargo hold
     * @param y             the y-coordinate of the cargo hold
     * @param good          the color of the good that was removed
     * @param numberGoods   the number of goods removed
     * @throws IOException if an I/O error occurs while sending the message
     */
    @Override
    public void updateRemovedGoods(String nickname, int x, int y, Color good, int numberGoods) throws IOException{
        List<String> params = new ArrayList<>(Arrays.asList(nickname, String.valueOf(x), String.valueOf(y), good.toString(), String.valueOf(numberGoods)));
        GameUpdateMessage message = new GameUpdateMessage(GameUpdateType.REMOVED_GOODS, params);
        out.writeObject(message);
    }

    //notifies the view about the fact that a player has to pick a card in order to continue the game
    /**
     * Notifies the client that a player needs to pick a card to continue the game.
     *
     * @throws IOException if an I/O error occurs while sending the message
     */
    @Override
    public void updateCardPicking() throws IOException{
        List<String> params = new ArrayList<>();
        GameUpdateMessage message = new GameUpdateMessage(GameUpdateType.CARD_PICKING, params);
        out.writeObject(message);
    }

    //notifies the view about the next player whose turn it is to perform an action
    /**
     * Notifies the client about the next player's turn.
     *
     * @param nickname the nickname of the player whose turn it is
     * @throws IOException if an I/O error occurs while sending the message
     */
    @Override
    public void updateNextTurn(String nickname) throws IOException{
        List<String> params = new ArrayList<>(Arrays.asList(nickname));
        GameUpdateMessage message = new GameUpdateMessage(GameUpdateType.NEXT_TURN, params);
        out.writeObject(message);
    }

    //notifies the view that a new card has been picked and must be solved
    /**
     * Notifies the client that a new card has been picked and must be solved.
     *
     * @param imageID the ID of the image of the card that has been picked
     * @throws IOException if an I/O error occurs while sending the message
     */
    @Override
    public void updateCardSolving(int imageID) throws IOException{
        List<String> params = new ArrayList<>(Arrays.asList(String.valueOf(imageID)));
        GameUpdateMessage message = new GameUpdateMessage(GameUpdateType.CARD_SOLVING, params);
        out.writeObject(message);
    }

    //notifies the view that a player has quit the game
    /**
     * Notifies the client that a player has quit the game.
     *
     * @param nickname the nickname of the player who quit
     * @throws IOException if an I/O error occurs while sending the message
     */
    @Override
    public void updatePlayerQuit(String nickname) throws IOException{
        List<String> params = new ArrayList<>(Arrays.asList(nickname));
        GameUpdateMessage message = new GameUpdateMessage(GameUpdateType.PLAYER_QUIT, params);
        out.writeObject(message);
    }

    //notifies the view that a player has gained/lost credits
    /**
     * Notifies the client that a player's credits have changed.
     *
     * @param nickname the nickname of the player whose credits have changed
     * @param change   the amount of credits gained or lost (positive for gain, negative for loss)
     * @throws IOException if an I/O error occurs while sending the message
     */
    @Override
    public void updatePlayerCredits(String nickname, int change) throws IOException{
        List<String> params = new ArrayList<>(Arrays.asList(nickname, String.valueOf(change)));
        GameUpdateMessage message = new GameUpdateMessage(GameUpdateType.CREDITS_CHANGE, params);
        out.writeObject(message);
    }

    //notifies the view that the position of a player has changed
    /**
     * Notifies the client that a player's position has changed.
     *
     * @param nickname the nickname of the player whose position has changed
     * @param lap      the current lap of the player
     * @param cell     the current cell of the player
     * @throws IOException if an I/O error occurs while sending the message
     */
    @Override
    public void updatePlayerPosition(String nickname, int lap, int cell) throws IOException{
        List<String> params = new ArrayList<>(Arrays.asList(nickname, String.valueOf(lap), String.valueOf(cell)));
        GameUpdateMessage message = new GameUpdateMessage(GameUpdateType.POSITION_CHANGE, params);
        out.writeObject(message);
    }

    //notifies the view about the fact that the game is finished
    /**
     * Notifies the client that the game has ended.
     *
     * @throws IOException if an I/O error occurs while sending the message
     */
    @Override
    public void updateEndGame() throws IOException{
        List<String> params = new ArrayList<>();
        GameUpdateMessage message = new GameUpdateMessage(GameUpdateType.END_GAME, params);
        out.writeObject(message);
    }

}
