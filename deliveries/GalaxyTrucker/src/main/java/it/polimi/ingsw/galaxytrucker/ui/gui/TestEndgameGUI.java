package it.polimi.ingsw.galaxytrucker.ui.gui;

import it.polimi.ingsw.galaxytrucker.model.enumerations.Color;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class TestEndgameGUI extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        new GuiInterface();
        //lory's view
        GuiInterface.getInstance().setNickname("lory");
        GuiInterface.getInstance().setColor(Color.BLUE);
        //it creates interface and view and adds four players
        GuiInterface.getInstance().updateWaitingForPlayers(false);
        GuiInterface.getInstance().updateNewPlayer("fil", Color.YELLOW);
        GuiInterface.getInstance().updateNewPlayer("tom", Color.RED);
        GuiInterface.getInstance().updateNewPlayer("nico", Color.GREEN);
        FXMLLoader fxmlLoader = new FXMLLoader(TestEndgameGUI.class.getResource("/it/polimi/ingsw/galaxytrucker/endgame.fxml"));
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