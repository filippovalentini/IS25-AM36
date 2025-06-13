package it.polimi.ingsw.galaxytrucker.ui.gui.actionSettingsControllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;

public class DefeatEnemyController {
    @FXML
    private ComboBox<String> flightDaysComboBox;
    @FXML
    private ComboBox<Integer> batteryComboBox;
    @FXML
    private Button confirmButton;

    @FXML
    public void initialize() {
        for (int i = 0; i <= 10; i++) {
            batteryComboBox.getItems().add(i);
        }
        flightDaysComboBox.getItems().addAll("yes", "no");
        batteryComboBox.setVisibleRowCount(3);
        flightDaysComboBox.setVisibleRowCount(3);


        setupConfirmButton();
    }

    @FXML
    private void setupConfirmButton() {
        confirmButton.setOnAction(e -> {
            String flightDays = flightDaysComboBox.getValue();
            Integer batteries = batteryComboBox.getValue();
            System.out.println("Lost flight days: " + flightDays);
            System.out.println("Used batteries: " + batteries);
        });
    }
}
