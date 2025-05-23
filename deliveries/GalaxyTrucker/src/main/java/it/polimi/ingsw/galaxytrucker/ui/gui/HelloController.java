package it.polimi.ingsw.galaxytrucker.ui.gui;

import it.polimi.ingsw.galaxytrucker.model.enumerations.Color;
import it.polimi.ingsw.galaxytrucker.model.enumerations.Orientation;
import it.polimi.ingsw.galaxytrucker.network.GameSessionManager;
import it.polimi.ingsw.galaxytrucker.network.VirtualServer;
import it.polimi.ingsw.galaxytrucker.ui.UserInterface;
import it.polimi.ingsw.galaxytrucker.ui.cli.cliView.CliView;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class HelloController implements GuiController {
    private VirtualServer server;
    private GameSessionManager client;
    private CliView cliView;
    int gameID;
    String nickname;
    Color color;

    @FXML
    private Pane handComponentArea;

    @FXML
    private Button handComponentButton;

    @FXML
    private GridPane myGridPane;

    @FXML
    private Button setButton;

    @FXML
    private Button discardButton;

    @FXML
    private Button pickComponent;

    @FXML
    private Button reserveButton;

    private Button lastDroppedButton = null;
    private Boolean isComponentPlaced = false;
    private Boolean firstComponent = true;
    Map<String, Button> draggableButtons = new HashMap<>();


    @FXML
    public void initialize() {
        setupGridPaneDragOver();
        setupGridPaneDragDropped();
        setupSetButton();
        setupPickComponentButton();
    }

    private void setupGridPaneDragOver() {
        myGridPane.setOnDragOver(event -> {
            if (event.getGestureSource() != myGridPane && event.getDragboard().hasString()) {
                event.acceptTransferModes(TransferMode.MOVE);
            }
            event.consume();
        });
    }

    private void setupGridPaneDragDropped() {
        myGridPane.setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();
            boolean success = false;

            if (db.hasString()) {
                String btnId = db.getString();
                Button draggedButton = draggableButtons.get(btnId);

                if (draggedButton != null) {
                    Pane parent = (Pane) draggedButton.getParent();
                    parent.getChildren().remove(draggedButton);

                    double x = event.getX();
                    double y = event.getY();
                    int col = getColumnIndexFromX(x);
                    int row = getRowIndexFromY(y);

                    myGridPane.add(draggedButton, col, row);

                    lastDroppedButton = draggedButton;
                    success = true;
                }
            }

            event.setDropCompleted(success);
            event.consume();
        });
    }

    private void setupSetButton() {
        setButton.setOnAction(event -> {
            if (lastDroppedButton != null) {
                lastDroppedButton.setOnDragDetected(null);
                isComponentPlaced = true;
            }
        });
    }

    private void setupPickComponentButton() {
        pickComponent.setOnAction(event -> {
            if (isComponentPlaced || firstComponent) {
                //server.pickHidden(gameID,nickname);
                Button newButton = new Button("handComponent");
                newButton.setPrefSize(handComponentArea.getPrefWidth(), handComponentArea.getPrefHeight());

                String btnId = UUID.randomUUID().toString();
                newButton.setUserData(btnId);
                draggableButtons.put(btnId, newButton);

                newButton.setOnDragDetected(event2 -> {
                    Dragboard db = newButton.startDragAndDrop(TransferMode.MOVE);
                    ClipboardContent content = new ClipboardContent();
                    content.putString(btnId);
                    firstComponent = false;
                    db.setContent(content);
                    event2.consume();
                });

                handComponentArea.getChildren().clear();
                handComponentArea.getChildren().add(newButton);
            }
        });
    }



    /*private void enableDrag(Button button) {
        button.setOnDragDetected(event -> {
            Dragboard db = button.startDragAndDrop(TransferMode.MOVE);
            ClipboardContent content = new ClipboardContent();
            content.putString("handComponent");
            db.setContent(content);
            event.consume();
        });
    }*/

    // Metodo per calcolare la colonna da coordinata x
    private int getColumnIndexFromX(double x) {
        double widthSoFar = 0;
        for (int i = 0; i < myGridPane.getColumnConstraints().size(); i++) {
            widthSoFar += myGridPane.getColumnConstraints().get(i).getPrefWidth();
            if (x < widthSoFar) {
                return i;
            }
        }
        // Se oltre la somma delle colonne, metti nell'ultima
        return myGridPane.getColumnConstraints().size() - 1;
    }

    // Metodo per calcolare la riga da coordinata y
    private int getRowIndexFromY(double y) {
        double heightSoFar = 0;
        for (int i = 0; i < myGridPane.getRowConstraints().size(); i++) {
            heightSoFar += myGridPane.getRowConstraints().get(i).getPrefHeight();
            if (y < heightSoFar) {
                return i;
            }
        }
        return myGridPane.getRowConstraints().size() - 1;
    }



}
