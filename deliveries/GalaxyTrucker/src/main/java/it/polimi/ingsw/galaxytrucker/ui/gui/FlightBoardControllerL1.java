package it.polimi.ingsw.galaxytrucker.ui.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.*;

import java.util.List;

public class FlightBoardControllerL1 implements GuiController {
    @FXML private Label start;
    @FXML private Label pos0, pos1, pos2, pos3, pos4, pos5, pos6, pos7, pos8, pos9;
    @FXML private Label pos10, pos11, pos12, pos13, pos14, pos15, pos16, pos17;

    @FXML
    private Button backButton;

    private List<Label> targetLabels;

    @FXML
    public void initialize() {
        targetLabels = List.of( //positions available to drag and drop
                pos0, pos1, pos2, pos3, pos4, pos5, pos6, pos7, pos8,
                pos9, pos10, pos11, pos12, pos13, pos14, pos15, pos16, pos17
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

    @Override
    public void notifyError(String errorMessage) throws Exception {

    }
}