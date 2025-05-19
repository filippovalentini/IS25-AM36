package it.polimi.ingsw.galaxytrucker;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

public class HelloController {

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
