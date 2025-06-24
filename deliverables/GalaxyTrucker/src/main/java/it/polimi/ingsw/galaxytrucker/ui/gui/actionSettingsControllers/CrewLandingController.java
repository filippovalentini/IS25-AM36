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

//this controller manages the graphic popup for the "crew landing" action
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

    private static class CabinCell {
        int row;
        int col;

        public CabinCell(int row, int col){
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

    private static class CrewAssignment{
        CabinCell cabinCell;
        int crew;
        public CrewAssignment(CabinCell cabinCell, int crew){
            this.cabinCell = cabinCell;
            this.crew = crew;
        }

        public CabinCell getCell(){
            return cabinCell;
        }

        public int getCrew(){
            return crew;
        }

        @Override
        public String toString(){
            return this.cabinCell.toString() + "            crew to remove: " + this.crew;
        }
    }

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
