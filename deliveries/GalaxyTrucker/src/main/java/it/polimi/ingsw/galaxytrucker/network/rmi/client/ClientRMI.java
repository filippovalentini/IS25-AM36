package it.polimi.ingsw.galaxytrucker.network.rmi.client;

import it.polimi.ingsw.galaxytrucker.network.GameSessionManager;
import it.polimi.ingsw.galaxytrucker.network.VirtualView;
import it.polimi.ingsw.galaxytrucker.view.View;
import it.polimi.ingsw.galaxytrucker.model.enumerations.*;
import it.polimi.ingsw.galaxytrucker.network.rmi.server.VirtualViewRMI;

import java.rmi.*;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

import static it.polimi.ingsw.galaxytrucker.network.MainClient.printCommands;

public class ClientRMI extends UnicastRemoteObject implements VirtualViewRMI, GameSessionManager {
    private final VirtualServerRMI server;      //instance of the related RMI server
    private View view;                          //view of the player
    private Integer gameID;                     //ID of the game in which the player is playing
    private String nickname;                    //nickname of the player
    private Color color;                        //color of the player

    public ClientRMI(VirtualServerRMI server) throws RemoteException {
        super();
        this.server = server;
    }

    //this method asks the user whether it wants to start a game or join one; in case of game creation,
    //it takes in input number of players and type of game. Once the game has been created (or an existing one
    //has been found) the method takes in input nickname and color of the player and adds it to the game.
    @Override
    public void run() throws RemoteException {
        /*boolean userStartsGame = requestStartOrJoinGame();
        if(userStartsGame){
            requestStartNewGame();
        }
        requestAddPlayerToGame(userStartsGame);
        runCli();*/ //commented due to gui usage
    }

    //this method asks the user to start a new game or join an existing one
    @Override
    public boolean requestStartOrJoinGame() {
        Scanner inputScanner = new Scanner(System.in);
        String input;
        System.out.println("||| WELCOME TO GALAXY TRUCKER |||\n");
        do{
            System.out.println("Choose to start a new game (S) or join a game (J): ");
            input = inputScanner.nextLine();
        }while(!input.equals("S") && !input.equals("J"));

        return input.equals("S");
    }

    //this method asks tho the user the parameters to set up a new game and asks the server to create the game
    @Override
    public void requestStartNewGame() {
        System.out.println("START A NEW GAME");
        int numPlayers;
        String gameID;
        int gID;
        String ff;
        boolean firstFlight;
        Scanner inputScanner = new Scanner(System.in);
        while(true){
            do{
                System.out.println("Insert game ID (3-digit number): ");
                gameID = inputScanner.nextLine();
            }while(gameID.length() != 3);
            gID = Integer.parseInt(gameID);
            do{
                System.out.println("Number of players (from 1 to 4): ");
                numPlayers = Integer.parseInt(inputScanner.nextLine());
            }while(numPlayers>4 || numPlayers<1);
            do{
                System.out.println("Standard game (S) or first flight (F): ");
                ff = inputScanner.nextLine();
            }while(!ff.equals("F") && !ff.equals("S"));
            firstFlight = (ff.equals("F"));

            boolean gameStarted = isGameStarted(gID);
            if(gameStarted){
                System.out.println("Error: game with this ID already started");
                continue;
            }

            gameStarted = startNewGame(null, gID, firstFlight, numPlayers);
            if(gameStarted){
                System.out.println("GAME STARTED");
                break;
            }
        }
        this.gameID = gID;
    }

    //this method asks the user for nickname and color and asks the server to add the player to the game
    @Override
    public void requestAddPlayerToGame(boolean userStartsGame) {
        System.out.println("JOIN GAME");
        Scanner inputScanner = new Scanner(System.in);
        String gameID;
        int gID;
        String nickname;
        Color color;
        String colorString;
        boolean addedToGame = false;
        while (!addedToGame) {
            if(!userStartsGame){
                do{
                    System.out.println("Insert game ID (3-digit number): ");
                    gameID = inputScanner.nextLine();
                }while(gameID.length() != 3);
                gID = Integer.parseInt(gameID);
                this.gameID = gID;
            }
            System.out.println("Insert nickname: ");
            nickname = inputScanner.nextLine();
            System.out.println("Insert color (RED, BLUE, YELLOW or GREEN): ");
            colorString = inputScanner.nextLine();
            color = Color.convertToColor(colorString);
            if (color == null) {
                System.out.println("Invalid color");
                continue;
            }
            this.nickname = nickname;
            this.color = color;
            boolean startedGame = isGameStarted(this.gameID);

            if(!startedGame){
                System.out.println("Error: game with this ID doesn't exist");
                continue;
            }

            addedToGame = addPlayerToGame(this.gameID);
        }
    }

    //this method asks the server if a game with the specified ID has already started
    @Override
    public boolean isGameStarted(int gameID){
        try{
            return server.startedGame(gameID);
        }
        catch(RemoteException e){
            return false;
        }
    }

    //this method asks the server to add the player (associated to the client) to the game
    @Override
    public boolean addPlayerToGame(int gameID){
        try{
            return server.addPlayer(this, gameID, nickname, color);
        }
        catch(RemoteException e){
            return false;
        }
    }

    //this method asks the server to create a new game
    @Override
    public boolean startNewGame(VirtualView client, int gameID, boolean firstFlight, int numberPlayers){
        try{
            server.startNewGame(client, gameID, firstFlight, numberPlayers);
            return true;
        }
        catch(RemoteException e){
            return false;
        }
    }





    //runs a command line interface to send requests to the server
    @Override
    public void runCli() throws RemoteException {
        Scanner scan = new Scanner(System.in);

        printCommands();

        while (true) {
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
                        server.pickHidden(gameID, nickname);
                        break;
                    case "pickShown":
                        if (tokens.length < 2) {
                            System.out.println("Error: index required");
                            break;
                        }
                        int index = Integer.parseInt(tokens[1]);
                        server.pickShown(gameID, nickname, index);
                        break;
                    case "release":
                        server.putShown(gameID, nickname);
                        break;
                    case "reserve":
                        server.reserveComponent(gameID, nickname);
                        break;
                    case "pickReserved":
                        if (tokens.length < 2) {
                            System.out.println("Error: index required");
                            break;
                        }
                        int pos = Integer.parseInt(tokens[1]);
                        server.pickReservedComponent(gameID, nickname, pos);
                        break;
                    case "rotate":
                        server.rotatePickedComponent(gameID, nickname);
                        break;
                    case "assemble":
                        if (tokens.length < 3) {
                            System.out.println("Error: coordinates required");
                            break;
                        }
                        int x1 = Integer.parseInt(tokens[1]);
                        int y1 = Integer.parseInt(tokens[2]);
                        server.assembledComponent(gameID, nickname, x1, y1);
                        break;
                    case "pickDeck":
                        if (tokens.length < 2) {
                            System.out.println("Error: index required");
                            break;
                        }
                        int deck = Integer.parseInt(tokens[1]);
                        server.pickDeck(gameID, nickname, deck);
                        break;
                    case "releaseDeck":
                        server.releaseDeck(gameID, nickname);
                        break;
                    case "setPosition":
                        if (tokens.length < 2) {
                            System.out.println("Error: position required");
                            break;
                        }
                        int initCell = Integer.parseInt(tokens[1]);
                        server.setPosition(gameID, nickname, initCell);
                        break;
                    case "hourglass":
                        server.startNewCycle(gameID, nickname);
                        break;
                    case "destroy":
                        if (tokens.length < 3) {
                            System.out.println("Error: coordinates required");
                            break;
                        }
                        int x2 = Integer.parseInt(tokens[1]);
                        int y2 = Integer.parseInt(tokens[2]);
                        server.destroyComponent(gameID, nickname, x2, y2);
                        break;
                    case "addCrew":
                        if (tokens.length < 3) {
                            System.out.println("Error: coordinates required");
                            break;
                        }
                        int x3 = Integer.parseInt(tokens[1]);
                        int y3 = Integer.parseInt(tokens[2]);
                        server.addCrew(gameID, nickname, x3, y3);
                        break;
                    case "addBatteries":
                        if (tokens.length < 3) {
                            System.out.println("Error: coordinates required");
                            break;
                        }
                        int x5 = Integer.parseInt(tokens[1]);
                        int y5 = Integer.parseInt(tokens[2]);
                        server.addBatteries(gameID, nickname, x5, y5);
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
                        server.addAlien(gameID, nickname, isPurple, x4, y4);
                        break;
                    case "pickCard":
                        server.pickNextCard(gameID, nickname);
                        break;
                    case "quit":
                        server.quitGame(gameID, nickname);
                        break;
                    case "dice":
                        view.updateRollDice();
                        break;
                    case "skip":
                        server.skip(gameID, nickname);
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
                        server.hitShip(gameID, nickname, diceResult, activateShield, activateCannon);
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
                        server.fly(gameID, nickname, batteries);
                        break;
                    case "landing":
                        if ((tokens.length - 1)%3 != 0) {
                            System.out.println("Error: specify cabins and number of crew to remove form each cabin");
                            break;
                        }
                        List<Integer> x = new ArrayList<>();
                        List<Integer> y = new ArrayList<>();
                        List<Integer> removedCrew = new ArrayList<>();
                        for(int i=1; i< tokens.length; i+=3){
                            x.add(Integer.parseInt(tokens[i]));
                            y.add(Integer.parseInt(tokens[i+1]));
                            removedCrew.add(Integer.parseInt(tokens[i+2]));
                        }
                        server.landing(gameID, nickname, x, y, removedCrew);
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
                        server.defeat(gameID, nickname, batteries1, loseDays);
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
                        server.loadGoods(gameID, nickname, x6, y6);
                        break;
                    case "planet":
                        if (tokens.length < 2) {
                            System.out.println("Error: planet number required");
                            break;
                        }
                        int planetNumber = Integer.parseInt(tokens[1]);
                        server.planetLanding(gameID, nickname, planetNumber);
                        break;
                    case "useBatteries":
                        if (tokens.length < 2) {
                            System.out.println("Error: number of batteries required");
                            break;
                        }
                        int numberBatteries = Integer.parseInt(tokens[1]);
                        server.useBatteries(gameID, nickname, numberBatteries);
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
    public void notifyError(String errorMessage) throws RemoteException {
        System.out.println(errorMessage);
    }

    //notifies a view about the fact that the corresponding player has been correctly added to the game, but
    //the server is waiting for other players in order to start the assembling phase; the parameter firstFlight
    //in needed for the view to determine which type of ship board/flight board to show to the user
    @Override
    public void updateWaitingForPlayers(boolean firstFlight) throws RemoteException {
        this.view = new View(this.nickname, this.color, firstFlight);
    }

    //notifies a view about the presence of another player in the game; this method is invoked before the
    //beginning of the assembling phase, therefore just the nickname and color of the new player is required
    @Override
    public void updateNewPlayer(String nickname, Color color) throws RemoteException{
        this.view.updateNewPlayer(nickname, color);
    }

    //notifies a view about the beginning of the assembling phase
    @Override
    public void updateStartAssembling() throws RemoteException {
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
    public void updatePickedComponent(int imageID, boolean released) throws RemoteException {
        this.view.updatePickedComponent(imageID, released);
    }

    //notifies the view about the fact that a shown component has been picked/released (depending on the value
    //of the boolean parameter); the parameter imageID is needed for the view in order to show the right
    //component to the user
    @Override
    public void updateShownComponent(int imageID, boolean released) throws RemoteException {
        this.view.updateShownComponent(imageID, released);
    }

    //notifies the view about the fact that a player (identified by the nickname parameter) has picked a reserved
    //component/ reserved a component (depending on the value of the boolean parameter); the parameter imageID
    //is needed for the view in order to show the right component to the user
    @Override
    public void updateReservedComponent(String nickname, int imageID, boolean released) throws RemoteException {
        this.view.updateReservedComponent(nickname, imageID, released);
    }

    //notifies the view about the fact that the picked component of the corresponding player has been rotated
    @Override
    public void updateRotatePickedComponent() throws RemoteException {
        this.view.updateRotatePickedComponent();
    }

    //notifies the view about the fact that a player (identified by the nickname parameter) has assembled a
    //component in position (x,y) of its ship board; the parameter imageID is needed for the view in order
    //to show the right component to the user
    @Override
    public void updateAssembledComponent(String nickname, int imageID, Orientation orientation, int x, int y) throws RemoteException {
        this.view.updateAssembledComponent(nickname, imageID, orientation, x, y);
    }

    //notifies the view about the fact that the corresponding player has successfully picked a deck; the parameter
    //contains the list of image IDs of the cards contained in the deck, so that the view can show the
    //correct adventure cards to the user
    @Override
    public void updatePickedDeck(List<Integer> deckIDs) throws RemoteException {
        this.view.updatePickedDeck(deckIDs);
    }

    //notifies the view about the fact that the corresponding player has successfully released a deck
    @Override
    public void updateReleasedDeck() throws RemoteException {
        this.view.updateReleasedDeck();
    }

    //notifies the view about the fact that the corresponding player has finished the assembling phase and is
    //correctly positioned on the flight board; still, other players have to finish building their ships
    @Override
    public void updateFinishAssembling(String nickname, int position) throws RemoteException {
        this.view.updateFinishAssembling(nickname, position);
    }

    //notifies the view that the hourglass has been turned around
    @Override
    public void updateStartNewCycle() throws RemoteException{
        this.view.updateStartNewCycle();
    }

    //notifies the view that the hourglass has finished running
    @Override
    public void updateFinishedCycle() throws RemoteException{
        this.view.updateFinishedCycle();
    }

    //invoked when the game switches to the ship placement phase, which means that the players can only
    //place their ship on the flight board
    @Override
    public void updateShipPlacement() throws RemoteException{
        this.view.updateShipPlacement();
    }

    //notifies the view that all the players have concluded the assembling phase, which means that the players
    //enter the ship control phase
    @Override
    public void updateShipControl() throws RemoteException {
        this.view.updateShipControl();
    }

    //notifies the view that a component of a player's ship board has been destroyed
    @Override
    public void updateDestroyedComponent(String nickname, int x, int y) throws RemoteException{
        this.view.updateDestroyedComponent(nickname, x, y);
    }

    //notifies the view about a change in the number of crew of a cabin
    @Override
    public void updateCrewChange(String nickname, int x, int y, int change) throws RemoteException{
        this.view.updateCrewChange(nickname, x, y, change);
    }

    //notifies the view that a player has initialized a battery container with batteries
    @Override
    public void updateBatteries(String nickname, int x, int y, int change) throws RemoteException{
        this.view.updateBatteries(nickname, x, y, change);
    }

    //notifies the view about a change in the number of aliens of a cabin
    @Override
    public void updateAlienChange(String nickname, int x, int y, boolean isPurple, boolean added) throws RemoteException{
        this.view.updateAlienChange(nickname, x, y, isPurple, added);
    }

    //notifies the view that a good has been loaded in a cargo hold
    @Override
    public void updateLoadedGood(String nickname, int x, int y, Color good) throws RemoteException{
        this.view.updateLoadedGood(nickname, x, y, good);
    }

    //notifies the view that some goods have been removed form a cargo hold
    @Override
    public void updateRemovedGoods(String nickname, int x, int y, Color good, int numberGoods) throws RemoteException{
        this.view.updateRemovedGoods(nickname, x, y, good, numberGoods);
    }

    //notifies the view about the fact that a player has to pick a card in order to continue the game
    @Override
    public void updateCardPicking() throws RemoteException{
        this.view.updateCardPicking();
    }

    //notifies the view about the next player whose turn it is to perform an action
    @Override
    public void updateNextTurn(String nickname) throws RemoteException{
        this.view.updateNextTurn(nickname);
    }

    //notifies the view that a new card has been picked and must be solved
    @Override
    public void updateCardSolving(int imageID) throws RemoteException{
        this.view.updateCardSolving(imageID);
    }

    //notifies the view that a player has quit the game
    @Override
    public void updatePlayerQuit(String nickname) throws RemoteException{
        this.view.updatePlayerQuit(nickname);
    }

    //notifies the view that a player has gained/lost credits
    @Override
    public void updatePlayerCredits(String nickname, int change) throws RemoteException{
        this.view.updatePlayerCredits(nickname, change);
    }

    //notifies the view that the position of a player has changed
    @Override
    public void updatePlayerPosition(String nickname, int lap, int cell) throws RemoteException{
        this.view.updatePlayerPosition(nickname, lap, cell);
    }

    //notifies the view about the fact that the game is finished
    @Override
    public void updateEndGame() throws RemoteException{
        this.view.updateEndGame();
    }
}
