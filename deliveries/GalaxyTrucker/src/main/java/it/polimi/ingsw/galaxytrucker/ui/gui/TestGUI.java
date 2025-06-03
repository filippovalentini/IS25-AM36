package it.polimi.ingsw.galaxytrucker.ui.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import java.io.IOException;

public class TestGUI extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(TestGUI.class.getResource("/it/polimi/ingsw/galaxytrucker/shownComponents.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1210, 740);
        stage.setTitle("Galaxy Trucker");
        Image icon = new Image(getClass().getResourceAsStream("/it/polimi/ingsw/galaxytrucker/images/galaxyTruckerAppIcon.png"));
        stage.getIcons().add(icon);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}