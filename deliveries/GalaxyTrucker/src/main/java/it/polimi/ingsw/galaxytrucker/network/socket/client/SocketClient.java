package it.polimi.ingsw.galaxytrucker.network.socket.client;

import it.polimi.ingsw.galaxytrucker.model.enumerations.Color;
import it.polimi.ingsw.galaxytrucker.model.enumerations.Orientation;
import it.polimi.ingsw.galaxytrucker.network.VirtualServer;
import it.polimi.ingsw.galaxytrucker.network.rmi.client.ClientRMI;
import it.polimi.ingsw.galaxytrucker.network.socket.message.GameUpdateMessage;
import it.polimi.ingsw.galaxytrucker.network.socket.message.GameUpdateType;
import it.polimi.ingsw.galaxytrucker.network.socket.server.VirtualViewSocket;
import it.polimi.ingsw.galaxytrucker.view.View;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static it.polimi.ingsw.galaxytrucker.network.MainClient.printCommands;

//this class contains all the logic needed to connect to the server through socket, manage the user interaction
//and handle server responses
public class SocketClient implements VirtualViewSocket {
    private View view;
    private String nickname;
    private Color color;
    private final Socket clientSocket;
    private final ObjectInputStream in;
    private final SocketServerHandler serverHandler;

    public SocketClient(Socket clientSocket) throws IOException {
        this.clientSocket = clientSocket;
        this.serverHandler = new SocketServerHandler(new ObjectOutputStream(clientSocket.getOutputStream()));
        this.in = new ObjectInputStream(clientSocket.getInputStream());
    }

    //this method gets nickname and color from the user and tries to add the player to the game; in case of success
    //it activates (in different threads) the methods to manage commands from the user (CLI) and messages from
    //the server
    public void run() throws IOException {
        GameUpdateMessage message;
        String nickname;
        String colorString;
        Color color;
        //nickname and color taken in input until they are both valid
        while (true) {
            System.out.println("Insert nickname: ");
            Scanner inputScanner = new Scanner(System.in);
            nickname = inputScanner.nextLine();
            System.out.println("Insert color: ");
            colorString = inputScanner.nextLine();
            color = Color.convertToColor(colorString);
            if (color == null) {
                System.out.println("Invalid color");
                continue;
            }
            this.nickname = nickname;
            this.color = color;
            serverHandler.addPlayer(null, nickname, color);
            try{
                message = (GameUpdateMessage) in.readObject();
            }catch (ClassNotFoundException e){
                System.out.println("Error: received invalid message from server");
                continue;
            }
            if(message.getGameUpdateType() == GameUpdateType.ERROR){
                notifyError(message.getGameParams(0));
            }
            else if (message.getGameUpdateType() == GameUpdateType.WAITING_FOR_PLAYERS){
                updateWaitingForPlayers(Boolean.parseBoolean(message.getGameParams(0)));
                break;
            }
        }
        //start a thread to manage messages received by the server
        new Thread(() -> {
            try {
                manageServerMessages();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }).start();
        //manage commands sent by the user
        runCli(serverHandler);
    }

    //this method creates a loop that waits for server's messages and (based on the type of message received)
    //invokes a method on the view
    private void manageServerMessages() throws IOException {
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
        in.close();
        serverHandler.close();
        clientSocket.close();
    }

    //runs a command line interface to send requests to the server
    @Override
    public void runCli(VirtualServer serverHandler)  {
        Scanner scan = new Scanner(System.in);

        printCommands();

        while (true) {
            try{
                Thread.sleep(100);
            }
            catch (Exception e){
                System.out.println(e.getMessage());
            }
            System.out.print("> ");
            String input = scan.nextLine().trim();
            String[] tokens = input.split("\\s+");
            if (tokens.length == 0) continue;

            try {
                String command = tokens[0];
                switch (command) {
                    case "commands":
                        printCommands();
                        break;
                    case "shipBoard":
                        if (tokens.length == 1) {
                            view.visualizeShip();
                        }
                        else if(tokens.length == 2) {
                            view.visualizeShip(tokens[1]);
                        }
                        else {
                            System.out.println("Error: insert a nickname of another player");
                        }
                        break;
                    case "flightBoard":
                        view.visualizeFlightBoard();
                        break;
                    case "pickHidden":
                        serverHandler.pickHidden(nickname);
                        break;
                    case "pickShown":
                        if (tokens.length < 2) {
                            System.out.println("Error: index required");
                            break;
                        }
                        int index = Integer.parseInt(tokens[1]);
                        serverHandler.pickShown(nickname, index);
                        break;
                    case "release":
                        serverHandler.putShown(nickname);
                        break;
                    case "reserve":
                        serverHandler.reserveComponent(nickname);
                        break;
                    case "pickReserved":
                        if (tokens.length < 2) {
                            System.out.println("Error: index required");
                            break;
                        }
                        int pos = Integer.parseInt(tokens[1]);
                        serverHandler.pickReservedComponent(nickname, pos);
                        break;
                    case "rotate":
                        serverHandler.rotatePickedComponent(nickname);
                        break;
                    case "assemble":
                        if (tokens.length < 3) {
                            System.out.println("Error: coordinates required");
                            break;
                        }
                        int x1 = Integer.parseInt(tokens[1]);
                        int y1 = Integer.parseInt(tokens[2]);
                        serverHandler.assembledComponent(nickname, x1, y1);
                        break;
                    case "pickDeck":
                        if (tokens.length < 2) {
                            System.out.println("Error: index required");
                            break;
                        }
                        int deck = Integer.parseInt(tokens[1]);
                        serverHandler.pickDeck(nickname, deck);
                        break;
                    case "releaseDeck":
                        serverHandler.releaseDeck(nickname);
                        break;
                    case "setPosition":
                        if (tokens.length < 2) {
                            System.out.println("Error: position required");
                            break;
                        }
                        int initCell = Integer.parseInt(tokens[1]);
                        serverHandler.setPosition(nickname, initCell);
                        break;
                    case "hourglass":
                        serverHandler.startNewCycle(nickname);
                        break;
                    case "destroy":
                        if (tokens.length < 3) {
                            System.out.println("Error: coordinates required");
                            break;
                        }
                        int x2 = Integer.parseInt(tokens[1]);
                        int y2 = Integer.parseInt(tokens[2]);
                        serverHandler.destroyComponent(nickname, x2, y2);
                        break;
                    case "addCrew":
                        if (tokens.length < 3) {
                            System.out.println("Error: coordinates required");
                            break;
                        }
                        int x3 = Integer.parseInt(tokens[1]);
                        int y3 = Integer.parseInt(tokens[2]);
                        serverHandler.addCrew(nickname, x3, y3);
                        break;
                    case "addBatteries":
                        if (tokens.length < 3) {
                            System.out.println("Error: coordinates required");
                            break;
                        }
                        int x5 = Integer.parseInt(tokens[1]);
                        int y5 = Integer.parseInt(tokens[2]);
                        serverHandler.addBatteries(nickname, x5, y5);
                        break;
                    case "addAlien":
                        if (tokens.length < 4) {
                            System.out.println("Error: coordinates required");
                            break;
                        }
                        if(!tokens[1].equals("purple") && !tokens[1].equals("brown")) {
                            System.out.println("Error: alien can only be purple or brown");
                            break;
                        }
                        boolean isPurple = (tokens[1].equals("purple"));
                        int x4 = Integer.parseInt(tokens[2]);
                        int y4 = Integer.parseInt(tokens[3]);
                        serverHandler.addAlien(nickname, isPurple, x4, y4);
                        break;
                    case "pickCard":
                        serverHandler.pickNextCard(nickname);
                        break;
                    case "quit":
                        serverHandler.quitGame(nickname);
                        break;
                    case "dice":
                        view.updateRollDice();
                        break;
                    case "skip":
                        serverHandler.skip(nickname);
                        break;
                    case "hit":
                        if (tokens.length < 3) {
                            System.out.println("Error: specify shield and cannon activation");
                            break;
                        }
                        if(!tokens[1].equals("yes") && !tokens[1].equals("no")) {
                            System.out.println("Error: specify yes or no for shield activation");
                            break;
                        }
                        if(!tokens[2].equals("yes") && !tokens[2].equals("no")) {
                            System.out.println("Error: specify yes or no for cannon activation");
                            break;
                        }
                        int diceResult = view.diceResult();
                        if(diceResult == 0){
                            System.out.println("Error: first throw the dice");
                            break;
                        }
                        boolean activateShield = (tokens[1].equals("yes"));
                        boolean activateCannon = (tokens[2].equals("yes"));
                        serverHandler.hitShip(nickname, diceResult, activateShield, activateCannon);
                        view.updateInvalidDice();
                        break;
                    case "fly":
                        if (tokens.length < 2) {
                            System.out.println("Error: specify batteries to use");
                            break;
                        }
                        int batteries = Integer.parseInt(tokens[1]);
                        if(batteries < 0){
                            System.out.println("Error: batteries cannot be negative");
                            break;
                        }
                        serverHandler.fly(nickname, batteries);
                        break;
                    case "landing":
                        if ((tokens.length - 1)%3 != 0) {
                            System.out.println("Error: specify cabins and number of crew to remove form each cabin");
                            break;
                        }
                        List<Integer> x = new ArrayList<>();
                        List<Integer> y = new ArrayList<>();
                        List<Integer> removedCrew = new ArrayList<>();
                        for(int i=1; i< (tokens.length-1)/3; i+=3){
                            x.add(Integer.parseInt(tokens[i]));
                            y.add(Integer.parseInt(tokens[i+1]));
                            removedCrew.add(Integer.parseInt(tokens[i+2]));
                        }
                        serverHandler.landing(nickname, x, y, removedCrew);
                        break;
                    case "defeat":
                        if (tokens.length < 3) {
                            System.out.println("Error: specify batteries to use and whether to lose days or not");
                            break;
                        }
                        if(!tokens[2].equals("yes") && !tokens[2].equals("no")) {
                            System.out.println("Error: specify yes or no for losing days or not");
                            break;
                        }
                        int batteries1 = Integer.parseInt(tokens[1]);
                        boolean loseDays = (tokens[2].equals("yes"));
                        serverHandler.defeat(nickname, batteries1, loseDays);
                        break;
                    case "loadGoods":
                        if ((tokens.length - 1)%2 != 0) {
                            System.out.println("Error: specify both coordinates for each cargo hold");
                            break;
                        }
                        List<Integer> x6 = new ArrayList<>();
                        List<Integer> y6 = new ArrayList<>();
                        for(int i=1; i< tokens.length; i+=2){
                            x6.add(Integer.parseInt(tokens[i]));
                            y6.add(Integer.parseInt(tokens[i+1]));
                        }
                        serverHandler.loadGoods(nickname, x6, y6);
                        break;
                    case "planet":
                        if (tokens.length < 2) {
                            System.out.println("Error: planet number required");
                            break;
                        }
                        int planetNumber = Integer.parseInt(tokens[1]);
                        serverHandler.planetLanding(nickname, planetNumber);
                        break;
                    case "useBatteries":
                        if (tokens.length < 2) {
                            System.out.println("Error: number of batteries required");
                            break;
                        }
                        int numberBatteries = Integer.parseInt(tokens[1]);
                        serverHandler.useBatteries(nickname, numberBatteries);
                        break;
                    default:
                        System.out.println("Error: unknown command");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid number format");
            } catch (Exception e) {
                System.out.println("Remote error: " + e.getMessage());
            }
        }
    }

    //notifies a view about an error committed while executing a method on the remote server; the parameter
    //errorMessage describes the type of error
    @Override
    public void notifyError(String errorMessage) {
        System.out.println(errorMessage);
    }

    //notifies a view about the fact that the corresponding player has been correctly added to the game, but
    //the server is waiting for other players in order to start the assembling phase; the parameter firstFlight
    //in needed for the view to determine which type of ship board/flight board to show to the user
    @Override
    public void updateWaitingForPlayers(boolean firstFlight) {
        this.view = new View(nickname, color, firstFlight);
    }

    //notifies a view about the presence of another player in the game; this method is invoked before the
    //beginning of the assembling phase, therefore just the nickname and color of the new player is required
    @Override
    public void updateNewPlayer(String nickname, Color color) {
        this.view.updateNewPlayer(nickname, color);
    }

    //notifies a view about the beginning of the assembling phase
    @Override
    public void updateStartAssembling() {
        this.view.updateStartAssembling();
        new Thread(() -> {
            try{
                Thread.sleep(1000);
                System.out.println(3);
                Thread.sleep(1000);
                System.out.println(2);
                Thread.sleep(1000);
                System.out.println(1);
                Thread.sleep(1000);
                System.out.println("START ASSEMBLING!!!");
            }
            catch (Exception e) {System.out.println("Error on wait");}
        }).start();
    }

    //notifies the view about the fact that a component has been successfully picked/released (depending on
    //the value of the boolean parameter) by the corresponding player; the parameter imageID is needed for the
    //view in order to show the right component to the user
    @Override
    public void updatePickedComponent(int imageID, boolean released) {
        this.view.updatePickedComponent(imageID, released);
    }

    //notifies the view about the fact that a shown component has been picked/released (depending on the value
    //of the boolean parameter); the parameter imageID is needed for the view in order to show the right
    //component to the user
    @Override
    public void updateShownComponent(int imageID, boolean released) {
        this.view.updateShownComponent(imageID, released);
    }

    //notifies the view about the fact that a player (identified by the nickname parameter) has picked a reserved
    //component/ reserved a component (depending on the value of the boolean parameter); the parameter imageID
    //is needed for the view in order to show the right component to the user
    @Override
    public void updateReservedComponent(String nickname, int imageID, boolean released) {
        this.view.updateReservedComponent(nickname, imageID, released);
    }

    //notifies the view about the fact that the picked component of the corresponding player has been rotated
    @Override
    public void updateRotatePickedComponent() {
        this.view.updateRotatePickedComponent();
    }

    //notifies the view about the fact that a player (identified by the nickname parameter) has assembled a
    //component in position (x,y) of its ship board; the parameter imageID is needed for the view in order
    //to show the right component to the user
    @Override
    public void updateAssembledComponent(String nickname, int imageID, Orientation orientation, int x, int y) {
        this.view.updateAssembledComponent(nickname, imageID, orientation, x, y);
    }

    //notifies the view about the fact that the corresponding player has successfully picked a deck; the parameter
    //contains the list of image IDs of the cards contained in the deck, so that the view can show the
    //correct adventure cards to the user
    @Override
    public void updatePickedDeck(List<Integer> deckIDs) {
        this.view.updatePickedDeck(deckIDs);
    }

    //notifies the view about the fact that the corresponding player has successfully released a deck
    @Override
    public void updateReleasedDeck() {
        this.view.updateReleasedDeck();
    }

    //notifies the view about the fact that the corresponding player has finished the assembling phase and is
    //correctly positioned on the flight board; still, other players have to finish building their ships
    @Override
    public void updateFinishAssembling(String nickname, int position) {
        this.view.updateFinishAssembling(nickname, position);
    }

    //notifies the view that the hourglass has been turned around
    @Override
    public void updateStartNewCycle() {
        this.view.updateStartNewCycle();
    }

    //notifies the view that the hourglass has finished running
    @Override
    public void updateFinishedCycle() {
        this.view.updateFinishedCycle();
    }

    //invoked when the game switches to the ship placement phase, which means that the players can only
    //place their ship on the flight board
    @Override
    public void updateShipPlacement() {
        this.view.updateShipPlacement();
    }

    //notifies the view that all the players have concluded the assembling phase, which means that the players
    //enter the ship control phase
    @Override
    public void updateShipControl() {
        this.view.updateShipControl();
    }

    //notifies the view that a component of a player's ship board has been destroyed
    @Override
    public void updateDestroyedComponent(String nickname, int x, int y) {
        this.view.updateDestroyedComponent(nickname, x, y);
    }

    //notifies the view about a change in the number of crew of a cabin
    @Override
    public void updateCrewChange(String nickname, int x, int y, int change){
        this.view.updateCrewChange(nickname, x, y, change);
    }

    //notifies the view that a player has initialized a battery container with batteries
    @Override
    public void updateBatteries(String nickname, int x, int y, int change) throws IOException{
        this.view.updateBatteries(nickname, x, y, change);
    }

    //notifies the view about a change in the number of aliens of a cabin
    @Override
    public void updateAlienChange(String nickname, int x, int y, boolean isPurple, boolean added) {
        this.view.updateAlienChange(nickname, x, y, isPurple, added);
    }

    //notifies the view that a good has been loaded in a cargo hold
    @Override
    public void updateLoadedGood(String nickname, int x, int y, Color good){
        this.view.updateLoadedGood(nickname, x, y, good);
    }

    //notifies the view that some goods have been removed form a cargo hold
    @Override
    public void updateRemovedGoods(String nickname, int x, int y, Color good, int numberGoods) {
        this.view.updateRemovedGoods(nickname, x, y, good, numberGoods);
    }

    //notifies the view about the fact that a player has to pick a card in order to continue the game
    @Override
    public void updateCardPicking() {
        this.view.updateCardPicking();
    }

    //notifies the view about the next player whose turn it is to perform an action
    @Override
    public void updateNextTurn(String nickname) {
        this.view.updateNextTurn(nickname);
    }

    //notifies the view that a new card has been picked and must be solved
    @Override
    public void updateCardSolving(int imageID) {
        this.view.updateCardSolving(imageID);
    }

    //notifies the view that a player has quit the game
    @Override
    public void updatePlayerQuit(String nickname) {
        this.view.updatePlayerQuit(nickname);
    }

    //notifies the view that a player has gained/lost credits
    @Override
    public void updatePlayerCredits(String nickname, int change) throws IOException{
        this.view.updatePlayerCredits(nickname, change);
    }

    //notifies the view that the position of a player has changed
    @Override
    public void updatePlayerPosition(String nickname, int lap, int cell) throws IOException{
        this.view.updatePlayerPosition(nickname, lap, cell);
    }

    //notifies the view about the fact that the game is finished
    @Override
    public void updateEndGame() throws IOException{
        this.view.updateEndGame();
    }
}
