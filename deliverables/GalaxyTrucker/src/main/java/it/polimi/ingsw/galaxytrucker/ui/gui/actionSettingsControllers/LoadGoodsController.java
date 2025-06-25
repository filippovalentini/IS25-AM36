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

/**
 * This controller manages the graphic popup for the "load goods" action
 */
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
        /**
         * Constructor for a CargoCell.
         * @param row the row of the cell (0-9)
         * @param col the column of the cell (0-10)
         */
        public CargoCell(int row, int col){
            this.row = row;
            this.col = col;
        }
        /**
         * Getters for row.
         * @return the row of the cell
         */
        public int getRow() {
            return row;
        }
        /**
         * Getters for column.
         * @return the column of the cell
         */
        public int getCol() {
            return col;
        }
        /**
         * Returns a string representation of the CargoCell.
         * @return a string in the format "Cell: (row, col)"
         */
        @Override
        public String toString(){
            return "Cell: (" + this.row + ", " + this.col + ")";
        }
    }
    /**
     * Initializes the controller by populating the cellComboBox with CargoCell options
     * and setting up the add and confirm buttons.
     */
    @FXML
    public void initialize() {
        cellComboBox.getItems().add(new CargoCell(0, 0));
        for (int x = 5; x <= 9; x++) {
            for (int y = 4; y <= 10; y++) {
                cellComboBox.getItems().add(new CargoCell(x, y));
            }
        }
        cellComboBox.setVisibleRowCount(3);

        setupAddButton();
        setupConfirmButton();
    }
    /**
     * Sets up the action for the add button, which adds the selected CargoCell to the list.
     */
    @FXML
    private void setupAddButton() {
        addButton.setOnAction(event -> { // When the add button is clicked
            CargoCell cargoCell = cellComboBox.getValue(); // Get the selected CargoCell from the combo box
            if (cargoCell == null) return; // If no cell is selected, do nothing

            selectedCells.add(cargoCell); // Add the selected cell to the list of selected cells

            HBox cellBox = new HBox(10); // Create a new HBox to hold the cell label and remove button
            cellBox.setStyle("-fx-alignment: CENTER_LEFT;"); // Align the contents to the left
            Label cellLabel = new Label(cargoCell.toString()); // Create a label to display the cell information
            Button removeBtn = new Button("x"); // Create a button to remove the cell from the list
            removeBtn.setStyle("-fx-text-fill: red;"); // Style the remove button to have red text
            removeBtn.setOnAction(e -> { // When the remove button is clicked
                selectedCells.remove(cargoCell); // Remove the cell from the list of selected cells
                cellListView.getItems().remove(cellBox); // Remove the corresponding HBox from the ListView
            });

            cellBox.getChildren().addAll(cellLabel, removeBtn); // Add the label and remove button to the HBox
            cellListView.getItems().add(cellBox); // Add the HBox to the ListView to display it
        });
    }
    /**
     * Sets up the action for the confirm button, which sends the selected cells to the server.
     */
    @FXML
    private void setupConfirmButton() {
        confirmButton.setOnAction(event -> {
            List<Integer> x = new ArrayList<>(); // List to hold the row indices of selected cells
            List<Integer> y = new ArrayList<>(); // List to hold the column indices of selected cells
            for(CargoCell cargoCell : selectedCells){ // For each selected CargoCell
                if(cargoCell.getRow() == 0 && cargoCell.getCol() == 0){ // If the cell is the default (0, 0)
                    x.add(0); // Add 0 to the row list
                    y.add(0); // Add 0 to the column list
                }else{
                    x.add(cargoCell.getRow() - 5); // Adjust the row index by subtracting 5
                    y.add(cargoCell.getCol() - 4); // Adjust the column index by subtracting 4
                }
            }
            try{ // Attempt to send the selected cells to the server
                server.loadGoods(this.gameID, this.playerNickname, x, y); // Send the adjusted row and column indices to the server
                onConfirm.run(); // Run the onConfirm action if provided
            }
            catch(Exception ignored){} // Catch any exceptions that occur during the server call
        });
    }
    /**
     * Sets the server for this controller.
     * @param server the VirtualServer instance to set
     */
    @Override
    public void setServer(VirtualServer server) {
        this.server = server;
    }
    /**
     * Sets the player information for this controller.
     * @param gameID the ID of the game
     * @param playerNickname the nickname of the player
     */
    @Override
    public void setPlayerInfo(int gameID, String playerNickname) {
        this.gameID = gameID;
        this.playerNickname = playerNickname;
    }
    /**
     * Sets the action to be performed when the confirm button is clicked.
     * @param onConfirm the Runnable action to set
     */
    @Override
    public void setOnConfirm(Runnable onConfirm) {
        this.onConfirm = onConfirm;
    }
}
