package it.polimi.ingsw.galaxytrucker.ui.gui.actionSettingsControllers;

import it.polimi.ingsw.galaxytrucker.ui.gui.controllerInterfaces.ActionSettingsController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;

import java.util.ArrayList;
import java.util.List;

public class CrewLandingController implements ActionSettingsController {
    @FXML
    private ComboBox<String> cellComboBox;
    @FXML
    private ComboBox<Integer> crewCountComboBox;
    @FXML
    private ListView<HBox> assignmentListView;
    @FXML
    private Button addButton;
    @FXML
    private Button confirmButton;

    private final List<String> assignments = new ArrayList<>();

    @FXML
    public void initialize() {
        for (int x = 0; x <= 4; x++) {
            for (int y = 0; y <= 6; y++) {
                cellComboBox.getItems().add("(" + x + ", " + y + ")");
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
            String cell = cellComboBox.getValue();
            Integer crew = crewCountComboBox.getValue();

            if (cell == null || crew == null) return;

            String entry = "cell:   " + cell + "        crew to remove:   " + crew;
            assignments.add(entry);

            HBox entryBox = new HBox(10);
            entryBox.setStyle("-fx-alignment: CENTER_LEFT;");
            Label entryLabel = new Label(entry);
            Button removeButton = new Button("x");
            removeButton.setStyle("-fx-text-fill: red;");
            removeButton.setOnAction(e -> {
                assignments.remove(entry);
                assignmentListView.getItems().remove(entryBox);
            });

            entryBox.getChildren().addAll(entryLabel, removeButton);
            assignmentListView.getItems().add(entryBox);
        });
    }

    @FXML
    private void setupConfirmButton() {
        confirmButton.setOnAction(event -> {
            System.out.println("Crew assignments:");
            for (String cell : assignments) {
                System.out.println(cell);
            }
        });
    }
}
