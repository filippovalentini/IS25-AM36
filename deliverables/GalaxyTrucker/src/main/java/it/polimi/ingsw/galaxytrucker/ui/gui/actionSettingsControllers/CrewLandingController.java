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


/**ù
 * This controller manages the graphic popup for the "crew landing" action
 */
public class CrewLandingController implements ActionSettingsController {
    @FXML
    private ComboBox<CabinCell> cellComboBox;
    @FXML
    private ComboBox<Integer> crewCountComboBox;
    @FXML
    private ListView<HBox> assignmentListView;
    @FXML
    private Button addButton;
    @FXML
    private Button confirmButton;

    private final List<CrewAssignment> assignments = new ArrayList<>();

    private VirtualServer server;
    private int gameID;
    private String playerNickname;
    private Runnable onConfirm;

    /**
     * This class represents a cabin cell in the ship.
     */
    private static class CabinCell {
        int row;
        int col;
        /**
         * Constructor for CabinCell.
         * @param row the row of the cabin cell
         * @param col the column of the cabin cell
         */
        public CabinCell(int row, int col){
            this.row = row;
            this.col = col;
        }
        /**
         * Getters for row and column.
         * @return the row and column of the cabin cell
         */
        public int getRow() {
            return row;
        }
/**
         * Getters for row and column.
         * @return the row and column of the cabin cell
         */
        public int getCol() {
            return col;
        }
        /**
         * String representation of the CabinCell.
         * @return a string in the format "Cell: (row, col)"
         */
        @Override
        public String toString(){
            return "Cell: (" + this.row + ", " + this.col + ")";
        }
    }
    /**
     * This class represents a crew assignment to a cabin cell.
     */
    private static class CrewAssignment{
        CabinCell cabinCell;
        int crew;
        /**
         * Constructor for CrewAssignment.
         * @param cabinCell the cabin cell to which the crew is assigned
         * @param crew the number of crew members to assign
         */
        public CrewAssignment(CabinCell cabinCell, int crew){
            this.cabinCell = cabinCell;
            this.crew = crew;
        }
        /**
         * Getters for cabin cell and crew count.
         * @return the cabin cell and the number of crew members assigned
         */
        public CabinCell getCell(){
            return cabinCell;
        }
        /**
         * Getters for crew count.
         * @return the number of crew members assigned to the cabin cell
         */
        public int getCrew(){
            return crew;
        }
        /**
         * String representation of the CrewAssignment.
         * @return a string in the format "Cell: (row, col)            crew to remove: crew"
         */
        @Override
        public String toString(){
            return this.cabinCell.toString() + "            crew to remove: " + this.crew;
        }
    }
    /**
     * Initializes the controller by populating the combo boxes with cabin cells and crew counts.
     */
    @FXML
    public void initialize() {
        for (int x = 5; x <= 9; x++) {
            for (int y = 4; y <= 10; y++) {
                cellComboBox.getItems().add(new CabinCell(x, y));
            }
        }
        crewCountComboBox.getItems().addAll(1, 2);

        cellComboBox.setVisibleRowCount(3);
        crewCountComboBox.setVisibleRowCount(3);

        setupAddButton();
        setupConfirmButton();
    }
    /**
     * Sets up the action for the "Add" button, allowing users to add crew assignments.
     */
    @FXML
    private void setupAddButton() {
        addButton.setOnAction(event -> {
            CabinCell cabinCell = cellComboBox.getValue();
            Integer crew = crewCountComboBox.getValue();

            if (cabinCell == null || crew == null) return;

            CrewAssignment assignment = new CrewAssignment(cabinCell, crew);
            assignments.add(assignment);

            HBox entryBox = new HBox(10);
            entryBox.setStyle("-fx-alignment: CENTER_LEFT;");
            Label entryLabel = new Label(assignment.toString());
            Button removeButton = new Button("x");
            removeButton.setStyle("-fx-text-fill: red;");
            removeButton.setOnAction(e -> {
                assignments.remove(assignment);
                assignmentListView.getItems().remove(entryBox);
            });

            entryBox.getChildren().addAll(entryLabel, removeButton);
            assignmentListView.getItems().add(entryBox);
        });
    }
    /**
     * Sets up the action for the "Confirm" button, sending the crew assignments to the server.
     */
    @FXML
    private void setupConfirmButton() {
        confirmButton.setOnAction(event -> {
            List<Integer> x = new ArrayList<>();
            List<Integer> y = new ArrayList<>();
            List<Integer> crewInEachCabin = new ArrayList<>();
            for(CrewAssignment assignment : assignments){
                x.add(assignment.getCell().getRow() - 5);
                y.add(assignment.getCell().getCol() - 4);
                crewInEachCabin.add(assignment.getCrew());
            }
            try{
                server.landing(this.gameID, this.playerNickname, x, y, crewInEachCabin);
                onConfirm.run();
            }
            catch(Exception ignored){}
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
     * Sets the action to be performed when the "Confirm" button is clicked.
     * @param onConfirm the Runnable action to set
     */
    @Override
    public void setOnConfirm(Runnable onConfirm) {
        this.onConfirm = onConfirm;
    }
}
