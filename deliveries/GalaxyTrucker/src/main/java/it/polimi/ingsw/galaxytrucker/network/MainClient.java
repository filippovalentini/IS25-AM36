package it.polimi.ingsw.galaxytrucker.network;

import it.polimi.ingsw.galaxytrucker.model.enumerations.Color;
import it.polimi.ingsw.galaxytrucker.network.rmi.client.ClientRMI;
import it.polimi.ingsw.galaxytrucker.network.rmi.client.VirtualServerRMI;
import it.polimi.ingsw.galaxytrucker.network.rmi.server.VirtualViewRMI;
import it.polimi.ingsw.galaxytrucker.network.socket.client.SocketClient;

import java.io.IOException;
import java.net.Socket;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.InputMismatchException;
import java.util.Scanner;

public class MainClient {
    //asks the user the technology to use (Socket or RMI) and launches the corresponding client typology
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        while(true) {
            try{
                System.out.println("Decide connection to use (1 -> Socket, 2 -> RMI): ");
                int connectionType = Integer.parseInt(sc.nextLine());

                if (connectionType == 1) {
                    startSocketClient(args[0]);
                    break;
                } else if (connectionType == 2) {
                    startClientRMI(args[0]);
                    break;
                } else {
                    System.out.println("Wrong connection type");
                }
            }
            catch (NumberFormatException e){
                System.out.println("Required integer argument");
            }
            catch(IOException e){
                System.out.println("Error while connecting to Socket server");
                break;
            }
            catch(Exception e){
                System.out.println("Error while connecting to RMI server");
                break;
            }
        }
    }

    //launches the socket client
    private static void startSocketClient(String IP) throws IOException {
        int port = 1235;
        Socket clientSocket = new Socket(IP, port);
        System.out.println("Connected to server...");
        new SocketClient(clientSocket).run();
    }

    //launches the RMI client
    private static void startClientRMI(String IP) throws RemoteException, NotBoundException {
        final String serverName = "GalaxyTruckerServer";
        int port = 1234;
        Registry registry = LocateRegistry.getRegistry(IP, port);
        VirtualServerRMI server = (VirtualServerRMI) registry.lookup(serverName);
        System.out.println("Obtained remote object...");

        String nickname;
        Color color;
        String colorString;
        boolean addedToGame = false;
        VirtualViewRMI client = null;
        while (!addedToGame) {
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
        System.out.println("14 - hourglass (turn around the hourglass)");
        System.out.println("15 - destroy <x> <y> (destroy a component)");
        System.out.println("16 - addCrew <x> <y> (add 2 crew members to a cabin)");
        System.out.println("17 - addAlien <purple/brown> <x> <y> (add an alien to a cabin)");
        System.out.println("18 - addBatteries <x> <y> (fill a battery component with batteries)");
        System.out.println("19 - pickCard (pick a next card)");
        System.out.println("20 - quit (quit the game)");
        System.out.println("(commands for card solving)");
        System.out.println("1 - dice (throw the dice)");
        System.out.println("2 - skip (skip an action)");
        System.out.println("3 - landing [<x> <y> <removedCrew>] ... (land in an abandoned ship specifying the cabins where to remove the crew and the respective quantity)");
        System.out.println("4 - hit <yes/no> <yes/no> (decide whether to activate or not shield and/or double cannon to protect your ship)");
        System.out.println("5 - fly <numberBatteries> (decide how many batteries to use to fly across the flight board)");
        System.out.println("6 - defeat <numberBatteries> <yes/no> (decide how many batteries to use to improve your cannon strength and whether to lose days to get a reward)");
        System.out.println("7 - loadGoods [<x> <y>] ... (specify the cargo holds where to load goods found during the flight; specify (0,0) if you want to discard a good)");
        System.out.println("8 - planet <planetNumber> (land in the specified planet)");
        System.out.println("9 - useBatteries <numberBatteries> (declare your engine/cannon strength specifying the number of batteries to use)");

    }

}
