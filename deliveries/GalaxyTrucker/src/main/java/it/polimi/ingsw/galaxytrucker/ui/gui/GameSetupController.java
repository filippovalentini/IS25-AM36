package it.polimi.ingsw.galaxytrucker.ui.gui;

import it.polimi.ingsw.galaxytrucker.model.enumerations.Color;
import it.polimi.ingsw.galaxytrucker.network.GameSessionManager;
import it.polimi.ingsw.galaxytrucker.network.VirtualServer;
import it.polimi.ingsw.galaxytrucker.network.rmi.client.ClientRMI;
import it.polimi.ingsw.galaxytrucker.network.rmi.client.VirtualServerRMI;
import it.polimi.ingsw.galaxytrucker.network.socket.client.SocketClient;
import javafx.animation.PauseTransition;
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

    private static Stage controlledStage; //stage of the JavaFX application

    public static void setControlledStage(Stage stage) {
        controlledStage = stage;
    }

    private static void changeScene(Scene scene) {
        controlledStage.setScene(scene);
    }

    public void setClientAndServer(GameSessionManager client, VirtualServer server) {
        this.server = server;
        this.client = client;
    }




    @FXML
    private TextField ipTextField;
    @FXML
    private Label ipErrorLabel;
    @FXML
    private Label invalidIdErrorLabel;
    @FXML
    private Label existingIdErrorLabel;
    @FXML
    private Label credentialsErrorLabel;
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
        credentialsErrorLabel.setText("Default error");
    }

    @FXML
    protected void onSocketButtonClick() {
        String ip = ipTextField.getText();
        try{
            startSocketClient(ip);
            // Carica la nuova schermata
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/it/polimi/ingsw/galaxytrucker/startOrJoin.fxml"));
            Parent root = fxmlLoader.load();
            GameSetupController controller = fxmlLoader.getController();
            controller.setClientAndServer(this.client, this.server);

            // Ottieni lo stage corrente dalla TextField o dal bottone (come preferisci)
            Stage stage = (Stage) socketButton.getScene().getWindow();

            // Imposta la nuova scena
            Scene scene = new Scene(root, 1210, 740); // usa la risoluzione adatta al tuo FXML
            stage.setScene(scene);
            stage.show();
        }
        catch (Exception e){
            ipErrorLabel.setVisible(true);

            PauseTransition pause = new PauseTransition(Duration.seconds(3));
            pause.setOnFinished(event -> ipErrorLabel.setVisible(false));
            pause.play();
        }
    }

    @FXML
    protected void onRMIButtonClick() {
        String ip = ipTextField.getText();
        try{
            startClientRMI(ip);

            // Carica la nuova schermata
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/it/polimi/ingsw/galaxytrucker/startOrJoin.fxml"));
            Parent root = fxmlLoader.load();
            GameSetupController controller = fxmlLoader.getController();
            controller.setClientAndServer(this.client, this.server);

            // Ottieni lo stage corrente dalla TextField o dal bottone (come preferisci)
            Stage stage = (Stage) rmiButton.getScene().getWindow();

            // Imposta la nuova scena
            Scene scene = new Scene(root, 1210, 740); // usa la risoluzione adatta al tuo FXML
            stage.setScene(scene);
            stage.show();
        }
        catch (Exception e){
            ipErrorLabel.setVisible(true);

            PauseTransition pause = new PauseTransition(Duration.seconds(3));
            pause.setOnFinished(event -> ipErrorLabel.setVisible(false));
            pause.play();
        }
    }

    @FXML
    private void onStartClick() {
        try {
            // Carica la nuova schermata
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/it/polimi/ingsw/galaxytrucker/setupGame.fxml"));
            Parent root = fxmlLoader.load();
            GameSetupController controller = fxmlLoader.getController();
            controller.setClientAndServer(this.client, this.server);

            // Ottieni lo stage corrente dalla TextField o dal bottone (come preferisci)
            Stage stage = (Stage) startButton.getScene().getWindow();

            // Imposta la nuova scena
            Scene scene = new Scene(root, 1210, 740); // usa la risoluzione adatta al tuo FXML
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace(); // Puoi loggare o mostrare un messaggio di errore grafico se preferisci
        }
    }

    @FXML
    private void onJoinClick() {
        try {
            // Carica la nuova schermata
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/it/polimi/ingsw/galaxytrucker/joinGame.fxml"));
            Parent root = fxmlLoader.load();
            GameSetupController controller = fxmlLoader.getController();
            controller.setClientAndServer(this.client, this.server);

            // Ottieni lo stage corrente dalla TextField o dal bottone (come preferisci)
            Stage stage = (Stage) joinButton.getScene().getWindow();

            // Imposta la nuova scena
            Scene scene = new Scene(root, 1210, 740); // usa la risoluzione adatta al tuo FXML
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace(); // Puoi loggare o mostrare un messaggio di errore grafico se preferisci
        }
    }

    @FXML
    private void onConfirmStartClick() {
        String gID = gameIdTextField.getText();
        if(gID.length() != 3){
            invalidIdErrorLabel.setVisible(true);

            PauseTransition pause = new PauseTransition(Duration.seconds(3));
            pause.setOnFinished(event -> invalidIdErrorLabel.setVisible(false));
            pause.play();

            return;
        }
        int gameID = Integer.parseInt(gID);
        int players = playersComboBox.getValue();
        String gameType = gameTypeComboBox.getValue();
        boolean firstFlight = gameType.equals("first flight");

        if(client.askIfGameStarted(gameID)){
            existingIdErrorLabel.setVisible(true);

            PauseTransition pause = new PauseTransition(Duration.seconds(3));
            pause.setOnFinished(event -> existingIdErrorLabel.setVisible(false));
            pause.play();

            return;
        }

        client.tryToStartNewGame(null, gameID, firstFlight, players);

        try {
            // Carica la nuova schermata
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/it/polimi/ingsw/galaxytrucker/joinGame.fxml"));
            Parent root = fxmlLoader.load();
            GameSetupController controller = fxmlLoader.getController();
            controller.setClientAndServer(this.client, this.server);

            // Ottieni lo stage corrente dalla TextField o dal bottone (come preferisci)
            Stage stage = (Stage) confirmStartButton.getScene().getWindow();

            // Imposta la nuova scena
            Scene scene = new Scene(root, 1210, 740); // usa la risoluzione adatta al tuo FXML
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace(); // Puoi loggare o mostrare un messaggio di errore grafico se preferisci
        }
    }

    @FXML
    private void onConfirmJoinClick() throws IOException {
        String gID = joinGameIdTextField.getText();
        if(gID.length() != 3){
            invalidIdErrorLabel.setVisible(true);

            PauseTransition pause = new PauseTransition(Duration.seconds(3));
            pause.setOnFinished(event -> invalidIdErrorLabel.setVisible(false));
            pause.play();

            return;
        }

        int gameID = Integer.parseInt(gID);
        String nickname = nicknameTextField.getText();
        Color color = Color.convertEmojiIntoColor(shipColorComboBox.getValue());
        GuiInterface.getInstance().setNickname(nickname);
        GuiInterface.getInstance().setColor(color);

        if(!client.askIfGameStarted(gameID)){
            existingIdErrorLabel.setVisible(true);

            PauseTransition pause = new PauseTransition(Duration.seconds(3));
            pause.setOnFinished(event -> existingIdErrorLabel.setVisible(false));
            pause.play();

            return;
        }

        if(client.tryToAddPlayerToGame(gameID, nickname, color)){
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/it/polimi/ingsw/galaxytrucker/lobby.fxml"));
            Parent root = fxmlLoader.load();
            LobbyController controller = fxmlLoader.getController();
            GuiInterface.getInstance().setLobbyController(controller);
            controller.setServer(this.server);

            Stage stage = (Stage) confirmJoinButton.getScene().getWindow();

            Scene scene = new Scene(root, 1210, 740); // usa la risoluzione adatta al tuo FXML
            stage.setScene(scene);
            stage.show();
        }

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
    public void notifyError(String errorMessage) throws Exception {

    }


}