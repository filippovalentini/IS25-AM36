package it.polimi.ingsw.galaxytrucker.network.rmi.client;

import it.polimi.ingsw.galaxytrucker.view.View;
import it.polimi.ingsw.galaxytrucker.model.enumerations.*;
import it.polimi.ingsw.galaxytrucker.network.VirtualServer;
import it.polimi.ingsw.galaxytrucker.network.rmi.server.VirtualViewRMI;

import java.rmi.*;
import java.rmi.registry.*;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

public class ClientRMI extends UnicastRemoteObject implements VirtualViewRMI {
    private View view;
    private final String nickname;
    private final Color color;

    public ClientRMI(String nickname, Color color) throws RemoteException {
        super();
        this.nickname = nickname;
        this.color = color;
    }

    public static void main(String[] args) throws RemoteException, NotBoundException {
        final String serverName = "GalaxyTruckerServer";
        Registry registry = LocateRegistry.getRegistry(args[0], 1234);
        VirtualServerRMI server = (VirtualServerRMI) registry.lookup(serverName);
        System.out.println("Obtained remote object...");

        String nickname;
        Color color;
        boolean addedToGame = false;
        VirtualViewRMI client = null;
        while (!addedToGame) {
            System.out.println("Insert nickname: ");
            Scanner inputScanner = new Scanner(System.in);
            nickname = inputScanner.nextLine();
            System.out.println("Insert color: ");
            String colorString = inputScanner.nextLine();
            color = switch (colorString) {
                case "RED" -> Color.RED;
                case "GREEN" -> Color.GREEN;
                case "BLUE" -> Color.BLUE;
                case "YELLOW" -> Color.YELLOW;
                default -> null;
            };
            if (color == null) {
                System.out.println("Invalid color");
                continue;
            }
            client = new ClientRMI(nickname, color);
            addedToGame = server.addPlayer(client, nickname, color);
        }
        client.runCli(server);
    }

    //prints the list of commands available for the CLI of the game
    public static void printCommands(){
        System.out.println("Available commands:");
        System.out.println("0 - commands (list of available commands)");
        System.out.println("1 - shipBoard (view your shipboard)");
        System.out.println("2 - shipBoard <otherPlayerNickname> (view another player's shipboard)");
        System.out.println("3 - flightBoard (view the flight board)");
        System.out.println("4 - pickHidden (pick a hidden component)");
        System.out.println("5 - pickShown <index> (pick a shown component)");
        System.out.println("6 - release (release picked component)");
        System.out.println("7 - reserve (reserve picked component)");
        System.out.println("8 - pickReserved <position> (pick a reserved component)");
        System.out.println("9 - rotate (rotate picked component)");
        System.out.println("10 - assemble <x> <y> (assemble picked component)");
        System.out.println("11 - pickDeck <deckNumber> (pick a deck)");
        System.out.println("12 - releaseDeck (release picked deck)");
        System.out.println("13 - setPosition <initCell> (set initial position on the flight board)");
        System.out.println("14 - destroy <x> <y> (destroy a component)");
        System.out.println("15 - addCrew <x> <y> (add 2 crew members to a cabin)");
        System.out.println("16 - addAlien <purple/brown> <x> <y> (add an alien to a cabin)");
        System.out.println("17 - pickCard (pick a next card)");
        System.out.println("18 - quit (quit the game)");
    }

    //runs a command line interface to send requests to the server
    @Override
    public void runCli(VirtualServer server) throws RemoteException {
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
                        server.pickHidden(nickname);
                        break;
                    case "pickShown":
                        if (tokens.length < 2) {
                            System.out.println("Error: index required");
                            break;
                        }
                        int index = Integer.parseInt(tokens[1]);
                        server.pickShown(nickname, index);
                        break;
                    case "release":
                        server.putShown(nickname);
                        break;
                    case "reserve":
                        server.reserveComponent(nickname);
                        break;
                    case "pickReserved":
                        if (tokens.length < 2) {
                            System.out.println("Error: index required");
                            break;
                        }
                        int pos = Integer.parseInt(tokens[1]);
                        server.pickReservedComponent(nickname, pos);
                        break;
                    case "rotate":
                        server.rotatePickedComponent(nickname);
                        break;
                    case "assemble":
                        if (tokens.length < 3) {
                            System.out.println("Error: coordinates required");
                            break;
                        }
                        int x1 = Integer.parseInt(tokens[1]);
                        int y1 = Integer.parseInt(tokens[2]);
                        server.assembledComponent(nickname, x1, y1);
                        break;
                    case "pickDeck":
                        if (tokens.length < 2) {
                            System.out.println("Error: index required");
                            break;
                        }
                        int deck = Integer.parseInt(tokens[1]);
                        server.pickDeck(nickname, deck);
                        break;
                    case "releaseDeck":
                        server.releaseDeck(nickname);
                        break;
                    case "setPosition":
                        if (tokens.length < 2) {
                            System.out.println("Error: position required");
                            break;
                        }
                        int initCell = Integer.parseInt(tokens[1]);
                        server.setPosition(nickname, initCell);
                        break;
                    case "destroy":
                        if (tokens.length < 3) {
                            System.out.println("Error: coordinates required");
                            break;
                        }
                        int x2 = Integer.parseInt(tokens[1]);
                        int y2 = Integer.parseInt(tokens[2]);
                        server.destroyComponent(nickname, x2, y2);
                        break;
                    case "addCrew":
                        if (tokens.length < 3) {
                            System.out.println("Error: coordinates required");
                            break;
                        }
                        int x3 = Integer.parseInt(tokens[1]);
                        int y3 = Integer.parseInt(tokens[2]);
                        server.addCrew(nickname, x3, y3);
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
                        server.addAlien(nickname, isPurple, x4, y4);
                        break;
                    case "pickCard":
                        server.pickNextCard(nickname);
                        break;
                    case "quit":
                        server.quitGame(nickname);
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

    //notifies the view about a change in the number of aliens of a cabin
    @Override
    public void updateAlienChange(String nickname, int x, int y, boolean isPurple, boolean added) throws RemoteException{
        this.view.updateAlienChange(nickname, x, y, isPurple, added);
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
}
