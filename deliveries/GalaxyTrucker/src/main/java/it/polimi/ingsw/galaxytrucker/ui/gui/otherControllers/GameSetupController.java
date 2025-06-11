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

public class GameSetupController implements GuiController {
    private VirtualServer server;
    private GameSessionManager client;
    private String playerNickname;
    private int gameID;
    private Color color;

    private Stage controlledStage; //stage of the JavaFX application

    public GameSetupController(){
        playerNickname = null;
        gameID = 0;
        color = null;
    }


    public void setControlledStage(Stage stage) {
        controlledStage = stage;
    }

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

    @FXML
    protected void onSocketButtonClick() {
        String ip = ipTextField.getText();
        try{
            startSocketClient(ip);
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/it/polimi/ingsw/galaxytrucker/fxml/startOrJoin.fxml"));
            Parent root = fxmlLoader.load();
            GameSetupController controller = fxmlLoader.getController();
            GuiInterface.getInstance().setSetupController(controller);
            controller.setControlledStage(controlledStage);
            controller.setClientAndServer(this.client, this.server);
            Scene scene = new Scene(root, 1210, 740);
            controlledStage.setScene(scene);
            controlledStage.show();
        }
        catch (Exception e){
            showError("Connection failed");
        }
    }

    @FXML
    protected void onRMIButtonClick() {
        String ip = ipTextField.getText();
        try{
            startClientRMI(ip);
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/it/polimi/ingsw/galaxytrucker/fxml/startOrJoin.fxml"));
            Parent root = fxmlLoader.load();
            GameSetupController controller = fxmlLoader.getController();
            GuiInterface.getInstance().setSetupController(controller);
            controller.setControlledStage(controlledStage);
            controller.setClientAndServer(this.client, this.server);
            Scene scene = new Scene(root, 1210, 740);
            controlledStage.setScene(scene);
            controlledStage.show();
        }
        catch (Exception e){
            showError("Connection failed");
        }
    }

    @FXML
    private void onStartClick() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/it/polimi/ingsw/galaxytrucker/fxml/setupGame.fxml"));
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

    @FXML
    private void onJoinClick() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/it/polimi/ingsw/galaxytrucker/fxml/joinGame.fxml"));
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

    @FXML
    private void onConfirmStartClick() {
        if(gameIdTextField.getText().isEmpty() || playersComboBox.getValue()==0 || gameTypeComboBox.getValue().isEmpty()){
            showError("Please fill out all fields");
            return;
        }
        String gID = gameIdTextField.getText();
        if(gID.length() != 3){
            showError("Invalid game ID");
            return;
        }
        this.gameID = Integer.parseInt(gID);
        int players = playersComboBox.getValue();
        String gameType = gameTypeComboBox.getValue();
        boolean firstFlight = gameType.equals("First Flight");
        if(client.askIfGameStarted(this.gameID)){
            showError("Game with the same ID already started");
            return;
        }
        client.tryToStartNewGame(null, gameID, firstFlight, players);
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/it/polimi/ingsw/galaxytrucker/fxml/joinGame.fxml"));
            Parent root = fxmlLoader.load();
            GameSetupController controller = fxmlLoader.getController();
            GuiInterface.getInstance().setSetupController(controller);
            controller.setClientAndServer(this.client, this.server);
            controller.setPlayerInfo(this.gameID, this.playerNickname, this.color);
            controller.setControlledStage(controlledStage);
            Scene scene = new Scene(root, 1210, 740);
            controlledStage.setScene(scene);
            controlledStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onConfirmJoinClick() throws IOException {
        if(joinGameIdTextField.getText().isEmpty() || shipColorComboBox.getValue().isEmpty() || nicknameTextField.getText().isEmpty()){
            showError("Please fill out all fields");
            return;
        }
        String gID = joinGameIdTextField.getText();
        if(gID.length() != 3){
            showError("Invalid game ID");
            return;
        }
        this.gameID = Integer.parseInt(gID);
        this.playerNickname = nicknameTextField.getText();
        this.color = Color.convertEmojiIntoColor(shipColorComboBox.getValue());
        GuiInterface.getInstance().setNickname(this.playerNickname);
        GuiInterface.getInstance().setColor(this.color);
        if(!client.askIfGameStarted(this.gameID)){
            showError("Game with the specified ID doesn't exist");
            return;
        }
        if(client.tryToAddPlayerToGame(this.gameID, this.playerNickname, this.color)){
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/it/polimi/ingsw/galaxytrucker/fxml/lobby.fxml"));
            Parent root = fxmlLoader.load();
            LobbyController controller = fxmlLoader.getController();
            GuiInterface.getInstance().setLobbyController(controller);
            controller.setServer(this.server);
            controller.setPlayerInfo(this.gameID, this.playerNickname, this.color);
            controller.setControlledStage(controlledStage);
            Scene scene = new Scene(root, 1210, 740);
            controlledStage.setScene(scene);
            controlledStage.show();
        }
    }

    public void showError(String error){
        errorLabel.setText(error);
        errorLabel.setVisible(true);
        PauseTransition pause = new PauseTransition(Duration.seconds(3));
        pause.setOnFinished(event -> errorLabel.setVisible(false));
        pause.play();
    }

    //launches the socket client
    public void startSocketClient(String IP) throws IOException {
        int port = 1235;
        Socket clientSocket = new Socket(IP, port);
        SocketClient socketClient = new SocketClient(GuiInterface.getInstance(), clientSocket);
        this.server = socketClient.getServerHandler();
        this.client = socketClient;
    }

    //launches the RMI client
    public void startClientRMI(String IP) throws RemoteException, NotBoundException {
        final String serverName = "GalaxyTruckerServer";
        int port = 1234;
        Registry registry = LocateRegistry.getRegistry(IP, port);
        VirtualServerRMI server = (VirtualServerRMI) registry.lookup(serverName);
        this.server = server;
        this.client = new ClientRMI(GuiInterface.getInstance(), server);
    }


    @Override
    public void setServer(VirtualServer server) {
        this.server = server;
    }

    //invoked to set the players information needed for method invocation on server
    @Override
    public void setPlayerInfo(int gameID, String playerNickname, Color color){
        this.playerNickname = playerNickname;
        this.color = color;
        this.gameID = gameID;
    }

    public void notifyError(String error){
        Platform.runLater(() -> showError(error));
    }

    @Override
    public void notifyGamePhase(String gamePhase) throws Exception {

    }


}