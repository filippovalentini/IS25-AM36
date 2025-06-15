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
    private ComboBox<CargoCell> cellComboBox;
    @FXML
    private ListView<HBox> cellListView;
    @FXML
    private Button addButton;
    @FXML
    private Button confirmButton;

    private final List<CargoCell> selectedCells = new ArrayList<>();

    private VirtualServer server;
    private int gameID;
    private String playerNickname;
    private Runnable onConfirm;

    private static class CargoCell {
        int row;
        int col;

        public CargoCell(int row, int col){
            this.row = row;
            this.col = col;
        }

        public int getRow() {
            return row;
        }

        public int getCol() {
            return col;
        }

        @Override
        public String toString(){
            return "Cell: (" + this.row + ", " + this.col + ")";
        }
    }

    @FXML
    public void initialize() {
        for (int x = 5; x <= 9; x++) {
            for (int y = 4; y <= 10; y++) {
                cellComboBox.getItems().add(new CargoCell(x, y));
            }
        }
        cellComboBox.setVisibleRowCount(3);

        setupAddButton();
        setupConfirmButton();
    }

    @FXML
    private void setupAddButton() {
        addButton.setOnAction(event -> {
            CargoCell cargoCell = cellComboBox.getValue();
            if (cargoCell == null || selectedCells.contains(cargoCell)) return;

            selectedCells.add(cargoCell);

            HBox cellBox = new HBox(10);
            cellBox.setStyle("-fx-alignment: CENTER_LEFT;");
            Label cellLabel = new Label(cargoCell.toString());
            Button removeBtn = new Button("x");
            removeBtn.setStyle("-fx-text-fill: red;");
            removeBtn.setOnAction(e -> {
                selectedCells.remove(cargoCell);
                cellListView.getItems().remove(cellBox);
            });

            cellBox.getChildren().addAll(cellLabel, removeBtn);
            cellListView.getItems().add(cellBox);
        });
    }

    @FXML
    private void setupConfirmButton() {
        confirmButton.setOnAction(event -> {
            List<Integer> x = new ArrayList<>();
            List<Integer> y = new ArrayList<>();
            for(CargoCell cargoCell : selectedCells){
                x.add(cargoCell.getRow() - 5);
                y.add(cargoCell.getCol() - 4);
            }
            try{
                server.loadGoods(this.gameID, this.playerNickname, x, y);
                onConfirm.run();
            }
            catch(Exception ignored){}
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
