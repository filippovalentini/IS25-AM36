package it.polimi.ingsw.galaxytrucker.ui.gui.actionSettingsControllers;

import it.polimi.ingsw.galaxytrucker.ui.gui.controllerInterfaces.ActionSettingsController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;

public class HitShipController implements ActionSettingsController {
    @FXML
    private ComboBox<String> shieldComboBox;
    @FXML
    private ComboBox<String> cannonComboBox;
    @FXML
    private Button confirmButton;

    @FXML
    public void initialize() {
        shieldComboBox.getItems().addAll("yes", "no");
        cannonComboBox.getItems().addAll("yes", "no");

        setupConfirmButton();
    }

    @FXML
    private void setupConfirmButton() {
        confirmButton.setOnAction(e -> {
            String shield = shieldComboBox.getValue();
            String cannon = cannonComboBox.getValue();
            System.out.println("Activate shield: " + shield);
            System.out.println("Activate double cannon: " + cannon);
        });
    }
}
