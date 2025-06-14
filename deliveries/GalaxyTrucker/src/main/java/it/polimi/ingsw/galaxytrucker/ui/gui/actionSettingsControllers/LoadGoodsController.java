package it.polimi.ingsw.galaxytrucker.ui.gui.actionSettingsControllers;

import it.polimi.ingsw.galaxytrucker.network.VirtualServer;
import it.polimi.ingsw.galaxytrucker.ui.gui.controllerInterfaces.ActionSettingsController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;

import java.util.ArrayList;
import java.util.List;

public class LoadGoodsController implements ActionSettingsController {
    @FXML
    private ComboBox<String> cellComboBox;
    @FXML
    private ListView<HBox> cellListView;
    @FXML
    private Button addButton;
    @FXML
    private Button confirmButton;

    private final List<String> selectedCells = new ArrayList<>();

    private VirtualServer server;
    private int gameID;
    private String playerNickname;
    private Runnable onConfirm;

    @FXML
    public void initialize() {
        for (int x = 0; x <= 4; x++) {
            for (int y = 0; y <= 6; y++) {
                cellComboBox.getItems().add("(" + x + ", " + y + ")");
            }
        }
        cellComboBox.setVisibleRowCount(3);

        setupAddButton();
        setupConfirmButton();
    }

    @FXML
    private void setupAddButton() {
        addButton.setOnAction(event -> {
            String selected = cellComboBox.getValue();
            if (selected == null || selectedCells.contains(selected)) return;

            selectedCells.add(selected);

            HBox cellBox = new HBox(10);
            cellBox.setStyle("-fx-alignment: CENTER_LEFT;");
            Label cellLabel = new Label(selected);
            Button removeBtn = new Button("x");
            removeBtn.setStyle("-fx-text-fill: red;");
            removeBtn.setOnAction(e -> {
                selectedCells.remove(selected);
                cellListView.getItems().remove(cellBox);
            });

            cellBox.getChildren().addAll(cellLabel, removeBtn);
            cellListView.getItems().add(cellBox);
        });
    }

    @FXML
    private void setupConfirmButton() {
        confirmButton.setOnAction(event -> {
            System.out.println("Selected cells:");
            for (String cell : selectedCells) {
                System.out.println(cell);
            }
        });
    }

    @Override
    public void setServer(VirtualServer server) {
        this.server = server;
    }

    @Override
    public void setPlayerInfo(int gameID, String playerNickname) {
        this.gameID = gameID;
        this.playerNickname = playerNickname;
    }

    @Override
    public void setOnConfirm(Runnable onConfirm) {
        this.onConfirm = onConfirm;
    }
}
