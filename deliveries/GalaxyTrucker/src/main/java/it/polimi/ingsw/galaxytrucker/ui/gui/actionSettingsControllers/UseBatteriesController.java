package it.polimi.ingsw.galaxytrucker.ui.gui.actionSettingsControllers;

import it.polimi.ingsw.galaxytrucker.ui.gui.controllerInterfaces.ActionSettingsController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;

public class UseBatteriesController implements ActionSettingsController {
    @FXML
    private ComboBox<Integer> batteryComboBox;
    @FXML
    private Button confirmButton;

    @FXML
    public void initialize() {
        for (int i = 0; i <= 10; i++) {
            batteryComboBox.getItems().add(i);
        }
        batteryComboBox.setVisibleRowCount(3);

        setupConfirmButton();
    }

    @FXML
    private void setupConfirmButton() {
        confirmButton.setOnAction(e -> {
            int selectedBatteries = batteryComboBox.getValue();
            System.out.println("Batterie selezionate: " + selectedBatteries);
        });
    }
}
