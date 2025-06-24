package it.polimi.ingsw.galaxytrucker.ui.gui;

import it.polimi.ingsw.galaxytrucker.model.enumerations.Color;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import java.io.IOException;

public class TestGUI extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        new GuiInterface();
        //playerone's view
        GuiInterface.getInstance().setNickname("playerone");
        GuiInterface.getInstance().setColor(Color.BLUE);
        //it creates interface and view and adds four players
        GuiInterface.getInstance().updateWaitingForPlayers(false);
        GuiInterface.getInstance().updateNewPlayer("playertwo", Color.YELLOW);
        GuiInterface.getInstance().updateNewPlayer("playerthree", Color.RED);
        GuiInterface.getInstance().updateNewPlayer("playerfour", Color.GREEN);
        FXMLLoader fxmlLoader = new FXMLLoader(TestGUI.class.getResource("/it/polimi/ingsw/galaxytrucker/fxml/mainScreens/flightPhaseL1.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 400, 200);
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