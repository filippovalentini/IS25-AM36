package it.polimi.ingsw.galaxytrucker.ui.gui.actionSettingsControllers;

import it.polimi.ingsw.galaxytrucker.ui.gui.controllerInterfaces.ActionSettingsController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;

public class PlanetLandingController implements ActionSettingsController {
    @FXML
    private ComboBox<Integer> planetComboBox;
    @FXML
    private Button confirmButton;

    @FXML
    public void initialize() {
        for (int i = 1; i <= 4; i++) {
            planetComboBox.getItems().add(i);
        }
        planetComboBox.setVisibleRowCount(3);

        setupConfirmButton();
    }

    @FXML
    private void setupConfirmButton() {
        confirmButton.setOnAction(e -> {
            int selectedBatteries = planetComboBox.getValue();
            System.out.println("Planet number: " + selectedBatteries);
        });
    }
}
