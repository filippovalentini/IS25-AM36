package it.polimi.ingsw.galaxytrucker.gui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Application extends javafx.application.Application {
    private Stage mainStage;
    @Override
    public void start(Stage stage) throws IOException {
        mainStage = stage;
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("/it/polimi/ingsw/galaxytrucker/selectnetwork.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        mainStage.setTitle("Galaxy Trucker");
        mainStage.setScene(scene);
        mainStage.show();
    }

    public void changeScene(Scene scene) {
        mainStage.setScene(scene);
    }

    public static void main(String[] args) {
        launch();
    }
}