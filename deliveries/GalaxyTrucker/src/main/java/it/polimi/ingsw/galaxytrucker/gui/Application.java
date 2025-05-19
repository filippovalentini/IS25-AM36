package it.polimi.ingsw.galaxytrucker.gui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Application extends javafx.application.Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("/it/polimi/ingsw/galaxytrucker/selectnetwork.fxml"));
        Controller.setControlledStage(stage); //share the stage with the controller
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Galaxy Trucker");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}