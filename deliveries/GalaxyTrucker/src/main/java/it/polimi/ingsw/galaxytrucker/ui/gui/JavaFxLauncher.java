package it.polimi.ingsw.galaxytrucker.ui.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class JavaFxLauncher extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(JavaFxLauncher.class.getResource("/it/polimi/ingsw/galaxytrucker/selectNetwork.fxml"));
        GameSetupController.setControlledStage(stage); //share the stage with the controller
        Scene scene = new Scene(fxmlLoader.load());

        GameSetupController controller = fxmlLoader.getController();
        GuiInterface.getInstance().setSetupController(controller);

        stage.setTitle("Galaxy Trucker");
        stage.setScene(scene);
        stage.show();
    }

}