package it.polimi.ingsw.galaxytrucker.ui.gui.otherControllers;

import it.polimi.ingsw.galaxytrucker.model.enumerations.Color;
import it.polimi.ingsw.galaxytrucker.network.GameSessionManager;
import it.polimi.ingsw.galaxytrucker.network.VirtualServer;
import it.polimi.ingsw.galaxytrucker.network.rmi.client.ClientRMI;
import it.polimi.ingsw.galaxytrucker.network.rmi.client.VirtualServerRMI;
import it.polimi.ingsw.galaxytrucker.network.socket.client.SocketClient;
import it.polimi.ingsw.galaxytrucker.ui.gui.GuiInterface;
import it.polimi.ingsw.galaxytrucker.ui.gui.controllerInterfaces.GuiController;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.Socket;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
/** * Controller for the game setup screen.
 */
public class GameSetupController implements GuiController {
    private VirtualServer server;
    private GameSessionManager client;
    private String playerNickname;
    private int gameID;
    private Color color;

    private Stage controlledStage; //stage of the JavaFX application
    /**
     * Default constructor initializing the playerNickname, gameID and color to null or 0.
     */
    public GameSetupController(){
        playerNickname = null;
        gameID = 0;
        color = null;
    }


    /**
     * Sets the controlled stage for this controller.
     * @param stage the stage to control
     */
    public void setControlledStage(Stage stage) {
        controlledStage = stage;
    }
    /**
     * Sets the client and server for this controller.
     * @param client the GameSessionManager client
     * @param server the VirtualServer server
     */
    public void setClientAndServer(GameSessionManager client, VirtualServer server) {
        setServer(server);
        this.client = client;
    }

    @FXML
    private TextField ipTextField;
    @FXML
    private Label errorLabel;
    @FXML
    private Button rmiButton;
    @FXML
    private Button socketButton;
    @FXML
    private Button startButton;
    @FXML
    private Button joinButton;
    @FXML
    private TextField gameIdTextField;
    @FXML
    private TextField joinGameIdTextField;
    @FXML
    private TextField nicknameTextField;
    @FXML
    private ComboBox<Integer> playersComboBox;
    @FXML
    private ComboBox<String> gameTypeComboBox;
    @FXML
    private Button confirmStartButton;
    @FXML
    private Button confirmJoinButton;
    @FXML
    private ComboBox<String> shipColorComboBox;


    /** * Initializes the controller by setting default values for the text fields, combo boxes, and error label.
     */
    @FXML
    public void initialize() {
        try{joinGameIdTextField.setText("");}catch(NullPointerException ignored){}
        try{gameIdTextField.setText("");}catch(NullPointerException ignored){}
        try{nicknameTextField.setText("");}catch(NullPointerException ignored){}
        try{shipColorComboBox.setValue("");}catch(NullPointerException ignored){}
        try{playersComboBox.setValue(0);}catch(NullPointerException ignored){}
        try{gameTypeComboBox.setValue("");}catch(NullPointerException ignored){}
        try{errorLabel.setText("Default error");}catch(NullPointerException ignored){}
    }
    /** * Sets up the pick button to handle mouse clicks.
     */
    @FXML
    protected void onSocketButtonClick() {
        String ip = ipTextField.getText();
        try{ // Attempt to start the socket client with the provided IP
            startSocketClient(ip);
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/it/polimi/ingsw/galaxytrucker/fxml/mainScreens/startOrJoin.fxml"));
            Parent root = fxmlLoader.load(); // Load the FXML file for the start or join screen
            GameSetupController controller = fxmlLoader.getController(); // Get the controller from the loaded FXML
            GuiInterface.getInstance().setSetupController(controller); // Set the controller in the GUI interface
            controller.setControlledStage(controlledStage); // Set the controlled stage for the controller
            controller.setClientAndServer(this.client, this.server); // Set the client and server for the controller
            Scene scene = new Scene(root, 1210, 740); // Create a new scene with the loaded root
            controlledStage.setScene(scene); // Set the scene to the controlled stage
            controlledStage.show(); // Show the controlled stage
        }
        catch (Exception e){ // If an exception occurs during the connection attempt
            showError("Connection failed");
        }
    }
    /** * Handles the click event for the RMI button.
     * It attempts to start the RMI client with the provided IP and loads the start or join screen.
     */
    @FXML
    protected void onRMIButtonClick() {
        String ip = ipTextField.getText(); // Get the IP address from the text field
        try{
            startClientRMI(ip); // Attempt to start the RMI client with the provided IP
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/it/polimi/ingsw/galaxytrucker/fxml/mainScreens/startOrJoin.fxml"));
            Parent root = fxmlLoader.load(); // Load the FXML file for the start or join screen
            GameSetupController controller = fxmlLoader.getController(); // Get the controller from the loaded FXML
            GuiInterface.getInstance().setSetupController(controller); // Set the controller in the GUI interface
            controller.setControlledStage(controlledStage); // Set the controlled stage for the controller
            controller.setClientAndServer(this.client, this.server); // Set the client and server for the controller
            Scene scene = new Scene(root, 1210, 740); // Create a new scene with the loaded root
            controlledStage.setScene(scene); // Set the scene to the controlled stage
            controlledStage.show(); // Show the controlled stage
        }
        catch (Exception e){ // If an exception occurs during the connection attempt
            showError("Connection failed");
        }
    }
    /** * Handles the click event for the start button.
     * It loads the setup game screen for starting a new game.
     */
    @FXML
    private void onStartClick() {
        try { // Attempt to load the setup game screen
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/it/polimi/ingsw/galaxytrucker/fxml/mainScreens/setupGame.fxml"));
            Parent root = fxmlLoader.load();
            GameSetupController controller = fxmlLoader.getController();
            GuiInterface.getInstance().setSetupController(controller);
            controller.setClientAndServer(this.client, this.server);
            controller.setControlledStage(controlledStage);
            Scene scene = new Scene(root, 1210, 740);
            controlledStage.setScene(scene);
            controlledStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /** * Handles the click event for the join button.
     * It loads the join game screen for joining an existing game.
     */
    @FXML
    private void onJoinClick() {
        try { // Attempt to load the join game screen
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/it/polimi/ingsw/galaxytrucker/fxml/mainScreens/joinGame.fxml"));
            Parent root = fxmlLoader.load();
            GameSetupController controller = fxmlLoader.getController();
            GuiInterface.getInstance().setSetupController(controller);
            controller.setClientAndServer(this.client, this.server);
            controller.setControlledStage(controlledStage);
            Scene scene = new Scene(root, 1210, 740);
            controlledStage.setScene(scene);
            controlledStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /** * Handles the click event for the confirm start button.
     * It validates the input fields and attempts to start a new game with the provided parameters.
     */
    @FXML
    private void onConfirmStartClick() {
        if(gameIdTextField.getText().isEmpty() || playersComboBox.getValue()==0 || gameTypeComboBox.getValue().isEmpty()){ // Check if any of the required fields are empty
            showError("Please fill out all fields"); // Show an error message if fields are empty
            return;
        }
        String gID = gameIdTextField.getText(); // Get the game ID from the text field
        if(gID.length() != 3){ // Check if the game ID is not exactly 3 characters long
            showError("Invalid game ID"); // Show an error message if the game ID is invalid
            return;
        }
        this.gameID = Integer.parseInt(gID); // Parse the game ID as an integer
        int players = playersComboBox.getValue(); // Get the number of players from the combo box
        String gameType = gameTypeComboBox.getValue(); // Get the game type from the combo box
        boolean firstFlight = gameType.equals("First Flight"); // Determine if it's the first flight based on the selected game type
        if(client.askIfGameStarted(this.gameID)){ // Check if a game with the specified ID has already started
            showError("Game with the same ID already started"); // Show an error message if the game has already started
            return;
        }
        client.tryToStartNewGame(null, gameID, firstFlight, players); // Attempt to start a new game with the provided parameters
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/it/polimi/ingsw/galaxytrucker/fxml/mainScreens/joinGame.fxml"));
            Parent root = fxmlLoader.load(); // Load the FXML file for the join game screen
            GameSetupController controller = fxmlLoader.getController(); // Get the controller from the loaded FXML
            GuiInterface.getInstance().setSetupController(controller); // Set the controller in the GUI interface
            controller.setClientAndServer(this.client, this.server); // Set the client and server for the controller
            controller.setPlayerInfo(this.gameID, this.playerNickname, this.color); // Set the player information for the controller
            controller.setControlledStage(controlledStage); // Set the controlled stage for the controller
            Scene scene = new Scene(root, 1210, 740); // Create a new scene with the loaded root
            controlledStage.setScene(scene); // Set the scene to the controlled stage
            controlledStage.show(); // Show the controlled stage
        } catch (IOException e) { // If an exception occurs during the loading of the join game screen
            e.printStackTrace();
        }
    }
    /** * Handles the click event for the confirm join button.
     * It validates the input fields and attempts to join an existing game with the provided parameters.
     */
    @FXML
    private void onConfirmJoinClick() throws IOException {
        if(joinGameIdTextField.getText().isEmpty() || shipColorComboBox.getValue().isEmpty() || nicknameTextField.getText().isEmpty()){ // Check if any of the required fields are empty
            showError("Please fill out all fields"); // Show an error message if fields are empty
            return;
        }
        String gID = joinGameIdTextField.getText(); // Get the game ID from the text field
        if(gID.length() != 3){ // Check if the game ID is not exactly 3 characters long
            showError("Invalid game ID"); // Show an error message if the game ID is invalid
            return;
        }
        this.gameID = Integer.parseInt(gID); // Parse the game ID as an integer
        this.playerNickname = nicknameTextField.getText(); // Get the player nickname from the text field
        this.color = Color.convertToColor(shipColorComboBox.getValue()); // Convert the selected color from the combo box to a Color enum
        GuiInterface.getInstance().setNickname(this.playerNickname); // Set the player nickname in the GUI interface
        GuiInterface.getInstance().setColor(this.color); // Set the player color in the GUI interface
        if(!client.askIfGameStarted(this.gameID)){ // Check if a game with the specified ID has not started yet
            showError("Game with the specified ID doesn't exist");
            return;
        }
        if(client.tryToAddPlayerToGame(this.gameID, this.playerNickname, this.color)){ // Attempt to add the player to the game with the specified ID, nickname, and color
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/it/polimi/ingsw/galaxytrucker/fxml/mainScreens/lobby.fxml")); // Load the FXML file for the lobby screen
            Parent root = fxmlLoader.load(); // Load the FXML file
            LobbyController controller = fxmlLoader.getController(); // Get the controller from the loaded FXML
            GuiInterface.getInstance().setLobbyController(controller); // Set the lobby controller in the GUI interface
            controller.setServer(this.server); // Set the server for the controller
            controller.setPlayerInfo(this.gameID, this.playerNickname, this.color); // Set the player information for the controller
            controller.setControlledStage(controlledStage); // Set the controlled stage for the controller
            Scene scene = new Scene(root, 1210, 740); // Create a new scene with the loaded root
            controlledStage.setScene(scene); // Set the scene to the controlled stage
            controlledStage.show(); // Show the controlled stage
        }
    }

    /**
     * Displays an error message in the error label and hides it after a short duration.
     * @param error
     */
    public void showError(String error){
        errorLabel.setText(error);
        errorLabel.setVisible(true);
        PauseTransition pause = new PauseTransition(Duration.seconds(3));
        pause.setOnFinished(event -> errorLabel.setVisible(false));
        pause.play();
    }

    //launches the socket client
    /**
     * Starts the socket client with the provided IP address.
     * @param IP the IP address of the server
     * @throws IOException if an I/O error occurs when creating the socket
     */
    public void startSocketClient(String IP) throws IOException {
        String shortIP = IP; // only the ip without port
        int port = 1235; //default port
        if((IP.split(":")).length == 2) { // IP provided with port
            shortIP = IP.split(":")[0];
            port = Integer.parseInt(IP.split(":")[1]);
        }
        Socket clientSocket = new Socket(shortIP, port);
        SocketClient socketClient = new SocketClient(GuiInterface.getInstance(), clientSocket);
        this.server = socketClient.getServerHandler();
        this.client = socketClient;
    }

    //launches the RMI client
    /**
     * Starts the RMI client with the provided IP address.
     * @param IP the IP address of the server
     * @throws RemoteException if a remote communication error occurs
     * @throws NotBoundException if the specified name is not bound in the registry
     */
    public void startClientRMI(String IP) throws RemoteException, NotBoundException {
        final String serverName = "GalaxyTruckerServer";
        String shortIP = IP; // only the ip without port
        int port = 1234; // default port
        if((IP.split(":")).length == 2) { // IP provided with port
            shortIP = IP.split(":")[0];
            port = Integer.parseInt(IP.split(":")[1]);
        }
        Registry registry = LocateRegistry.getRegistry(shortIP, port);
        VirtualServerRMI server = (VirtualServerRMI) registry.lookup(serverName);
        this.server = server;
        this.client = new ClientRMI(GuiInterface.getInstance(), server);
    }

    /** * Sets the server for this controller.
     * @param server the VirtualServer to set
     */
    @Override
    public void setServer(VirtualServer server) {
        this.server = server;
    }

    //invoked to set the players information needed for method invocation on server
    /**
     * Sets the player information for this controller.
     * @param gameID the ID of the game
     * @param playerNickname the nickname of the player
     * @param color the color of the player's ship
     */
    @Override
    public void setPlayerInfo(int gameID, String playerNickname, Color color){
        this.playerNickname = playerNickname;
        this.color = color;
        this.gameID = gameID;
    }
    /**
     * Notifies the controller about an error and displays it in the error label.
     * @param error the error message to display
     */
    public void notifyError(String error){
        Platform.runLater(() -> showError(error));
    }
    /** * Notifies the controller about a change in the game phase.
     * This method is currently empty and does not perform any actions.
     * @param gamePhase the current game phase
     * @throws Exception if an error occurs during notification
     */
    @Override
    public void notifyGamePhase(String gamePhase) throws Exception {

    }


}