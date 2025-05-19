package it.polimi.ingsw.galaxytrucker;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("hello-view.fxml"));

        Scene scene = new Scene(fxmlLoader.load(), 1210, 740);
        stage.setTitle("GalaxyTrucker");
        Image icon = new Image(getClass().getResourceAsStream("/images/tiles/engine7.jpg"));
        stage.getIcons().add(icon);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {


        launch(args);


    }
}