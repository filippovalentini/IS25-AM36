package it.polimi.ingsw.galaxytrucker.ui.gui;

import it.polimi.ingsw.galaxytrucker.model.enumerations.Color;
import it.polimi.ingsw.galaxytrucker.network.VirtualServer;
import it.polimi.ingsw.galaxytrucker.ui.gui.controllerInterfaces.FlightBoardController;
import it.polimi.ingsw.galaxytrucker.ui.gui.controllerInterfaces.GuiController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class FlightBoardControllerL1 implements FlightBoardController {
    @FXML private Label start;
    @FXML private Label pos0, pos1, pos2, pos3, pos4, pos5, pos6, pos7, pos8, pos9;
    @FXML private Label pos10, pos11, pos12, pos13, pos14, pos15, pos16, pos17;

    @FXML
    private Button backButton;
    int gameID;
    private String playerNickname;
    Color color;

    private List<Label> targetLabels;
    private VirtualServer server;


    @FXML
    public void initialize() {
        setupBackButton();
        // Inizializza la lista delle posizioni
        targetLabels = List.of(
                pos0, pos1, pos2, pos3, pos4, pos5, pos6, pos7, pos8,
                pos9, pos10, pos11, pos12, pos13, pos14, pos15, pos16, pos17
        );

        // Imposta comportamento di drag per la navicella
        start.setOnDragDetected(event -> {
            Dragboard db = start.startDragAndDrop(TransferMode.MOVE);

            ClipboardContent content = new ClipboardContent();
            content.putString("🚀");
            db.setContent(content);

            // Imposta immagine durante il drag
            Image rocketImage = new Image(getClass().getResourceAsStream("/it/polimi/ingsw/galaxytrucker/images/spaceShip.png"));
            db.setDragView(rocketImage, rocketImage.getWidth() / 2, rocketImage.getHeight() / 2);

            event.consume();
        });

        // Abilita il drop su ogni etichetta di posizione
        for (Label label : targetLabels) {
            enableDropOn(label);
        }
    }

    private void enableDropOn(Label label) {
        label.setOnDragOver(event -> {
            if (event.getGestureSource() != label && event.getDragboard().hasString()) {
                event.acceptTransferModes(TransferMode.MOVE);
            }
            event.consume();
        });

        label.setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();
            boolean success = false;
            if (db.hasString()) {
                label.setText("🔴");     // Mostra l'emoji a destinazione
                start.setText("");       // Rimuove la navicella
                start.setOnDragDetected(null); // ❌ Disabilita il drag dopo il primo utilizzo
                success = true;
            }
            event.setDropCompleted(success);
            event.consume();
        });

        label.setOnDragEntered(event -> {
            if (event.getGestureSource() != label && event.getDragboard().hasString()) {
                label.setStyle("-fx-border-color: white; -fx-border-width: 2px;");
            }
        });

        label.setOnDragExited(event -> label.setStyle(""));
    }

    public void setupBackButton() {
        backButton.setOnAction(event -> {
            try {
                // Carica la nuova schermata
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/it/polimi/ingsw/galaxytrucker/shipBuildingL1.fxml"));
                Parent root = fxmlLoader.load();

                // Ora FlightBoardControllerL1 ha il metodo setServer()
                ShipBuildingControllerL1 controller = fxmlLoader.getController();
                controller.setServer(this.server);

                // Ottieni lo stage corrente dal bottone
                Stage stage = (Stage) backButton.getScene().getWindow();

                // Imposta la nuova scena
                Scene scene = new Scene(root, 1210, 740);
                stage.setScene(scene);
                stage.show();

            } catch (IOException e) {
                e.printStackTrace();
                System.err.println("Errore nel caricamento della Shipboard: " + e.getMessage());
            }
        });
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

    //notifies a view about an error committed while executing a method on the remote server; the parameter
    //errorMessage describes the type of error
    @Override
    public void notifyError(String errorMessage) throws Exception{}

    //notifies the view about the fact that the corresponding player has successfully picked a deck; the parameter
    //contains the list of image IDs of the cards contained in the deck, so that the view can show the
    //correct adventure cards to the user
    @Override
    public void updatePickedDeck(List<Integer> deckIDs) throws Exception{}

    //notifies the view about the fact that the corresponding player has successfully released a deck
    @Override
    public void updateReleasedDeck() throws Exception{}

    //notifies the view about the fact that a player has finished the assembling phase and is
    //correctly positioned on the flight board; still, other players have to finish building their ships
    @Override
    public void updateFinishAssembling(String nickname, int position) throws Exception{}

    //notifies the view that the hourglass has been turned around
    @Override
    public void updateStartNewCycle() throws Exception{}

    //notifies the view that the hourglass has finished running
    @Override
    public void updateFinishedCycle() throws Exception{}
}