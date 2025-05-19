package it.polimi.ingsw.galaxytrucker.gui;

import it.polimi.ingsw.galaxytrucker.network.MainClient;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.rmi.NotBoundException;

public class Controller {
    private static Stage controlledStage; //stage of the JavaFX application

    public static void setControlledStage(Stage stage) {
        controlledStage = stage;
    }

    private static void changeScene(Scene scene) {
        controlledStage.setScene(scene);
    }

    @FXML
    private Label welcomeText;

    @FXML
    private TextField ipTextField;

    @FXML
    private Button rmiButton;
    @FXML
    private Button socketButton;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }

    @FXML
    protected void onSocketButtonClick() {
        try {
            MainClient.startSocketClient(ipTextField.getText());
            System.out.println("[gui]: client socket started");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    protected void onRMIButtonClick() {
        try {
            MainClient.startClientRMI(ipTextField.getText());
            System.out.println("[gui]: client rmi started");
            FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("/it/polimi/ingsw/galaxytrucker/selectnetwork.fxml"));
            Parent root = fxmlLoader.load();
            Scene secondaScena = new Scene(root, 600, 400);
            controlledStage.setScene(secondaScena);
            controlledStage.show();
        } catch (IOException | NotBoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static class HelloController {

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
        private Button reserveButton;

        private Button lastDroppedButton = null;
        private Double width;
        private Double height;




        @FXML
        public void initialize() {
            width = handComponentButton.getWidth();
            height = handComponentButton.getHeight();
            enableDrag(handComponentButton);

            myGridPane.setOnDragOver(event -> {
                if (event.getGestureSource() != myGridPane && event.getDragboard().hasString()) {
                    event.acceptTransferModes(TransferMode.MOVE);
                }
                event.consume();
            });

            myGridPane.setOnDragDropped(event -> {
                Dragboard db = event.getDragboard();
                boolean success = false;

                if (db.hasString() && db.getString().equals("handComponent")) {
                    Pane parent = (Pane) handComponentButton.getParent();
                    parent.getChildren().remove(handComponentButton);

                    double x = event.getX();
                    double y = event.getY();
                    int col = getColumnIndexFromX(x);
                    int row = getRowIndexFromY(y);

                    myGridPane.add(handComponentButton, col, row);

                    // Salva il riferimento all'ultimo bottone droppato
                    lastDroppedButton = handComponentButton;

                    success = true;
                }

                event.setDropCompleted(success);
                event.consume();
            });
            setButton.setOnAction(event -> {
                if (lastDroppedButton != null) {
                    // Disabilita il drag del bottone esistente
                    handComponentButton.setOnDragDetected(null);


                }
            });
        }

        private void enableDrag(Button button) {
            button.setOnDragDetected(event -> {
                Dragboard db = button.startDragAndDrop(TransferMode.MOVE);
                ClipboardContent content = new ClipboardContent();
                content.putString("handComponent");
                db.setContent(content);
                event.consume();
            });
        }

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
}