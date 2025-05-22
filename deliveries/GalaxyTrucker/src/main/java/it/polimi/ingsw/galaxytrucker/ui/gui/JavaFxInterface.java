package it.polimi.ingsw.galaxytrucker.ui.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class JavaFxInterface extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(JavaFxInterface.class.getResource("/it/polimi/ingsw/galaxytrucker/selectnetwork.fxml"));
        JavaFxController.setControlledStage(stage); //share the stage with the controller
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Galaxy Trucker");
        stage.setScene(scene);
        stage.show();
    }

    public static void launchApplication(String[] args) {
        launch(args);
    }
}