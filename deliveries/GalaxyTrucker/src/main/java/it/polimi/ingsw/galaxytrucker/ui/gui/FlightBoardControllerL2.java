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

public class FlightBoardControllerL2 implements FlightBoardController {
    @FXML private Label start;
    @FXML private Label pos0, pos1, pos2, pos3, pos4, pos5, pos6, pos7, pos8, pos9;
    @FXML private Label pos10, pos11, pos12, pos13, pos14, pos15, pos16, pos17;
    @FXML private Label pos18, pos19, pos20, pos21, pos22, pos23;

    private List<Label> targetLabels;
    private int gameID;
    private String playerNickname;
    private Color color;

    @FXML
    private Button backButton;
    private VirtualServer server;

    @FXML
    public void initialize() {
        setupBackButton();

        targetLabels = List.of( //positions available to drag and drop
                pos0, pos1, pos2, pos3, pos4, pos5, pos6, pos7, pos8,
                pos9, pos10, pos11, pos12, pos13, pos14, pos15, pos16, pos17,
                pos18, pos19, pos20, pos21, pos22, pos23
        );
        start.setOnDragDetected(event -> { //called when object is dragged
            Dragboard db = start.startDragAndDrop(TransferMode.MOVE);
            ClipboardContent content = new ClipboardContent();
            content.putString("🚀");
            db.setContent(content);
            Image rocketImage = new Image(getClass().getResourceAsStream("/it/polimi/ingsw/galaxytrucker/images/spaceShip.png"));
            db.setDragView(rocketImage, rocketImage.getWidth() / 2, rocketImage.getHeight() / 2);
            event.consume(); //block propagation of the event
        });
        for (Label label : targetLabels) {
            enableDropOn(label);
        }
    }

    private void enableDropOn(Label label) {
        label.setOnDragOver(event -> { //called while is dragged
            if (event.getGestureSource() != label && event.getDragboard().hasString()) {
                event.acceptTransferModes(TransferMode.MOVE);
            }
            event.consume();
        });
        label.setOnDragDropped(event -> { //called while is dropped
            Dragboard db = event.getDragboard();
            boolean success = false;
            if (db.hasString()) {
                label.setText("🔴");
                start.setText("");
                start.setOnDragDetected(null);
                success = true;
            }
            event.setDropCompleted(success);
            event.consume();
        });
        label.setOnDragEntered(event -> { //called when near a node of the target positions
            if (event.getGestureSource() != label && event.getDragboard().hasString()) {
                label.setStyle("-fx-border-color: white; -fx-border-width: 2px;");
            }
        });
        label.setOnDragExited(event -> label.setStyle("")); //called when away from a node of the target positions
    }


    public void setupBackButton() {
        backButton.setOnAction(event -> {
            try {
                // Carica la nuova schermata
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/it/polimi/ingsw/galaxytrucker/shipBuildingL2.fxml"));
                Parent root = fxmlLoader.load();

                // Ora FlightBoardControllerL1 ha il metodo setServer()
                ShipBuildingControllerL2 controller = fxmlLoader.getController();
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
}
