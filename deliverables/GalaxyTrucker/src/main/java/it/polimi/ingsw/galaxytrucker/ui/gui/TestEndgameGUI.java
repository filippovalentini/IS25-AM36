package it.polimi.ingsw.galaxytrucker.ui.gui;

import it.polimi.ingsw.galaxytrucker.model.enumerations.Color;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * This class is used to test the endgame GUI.
 */
public class TestEndgameGUI extends Application {
    /**
     * Starts the GUI application and sets up the endgame view.
     *
     * @param stage the primary stage for this application
     * @throws Exception if an error occurs during loading the FXML
     */
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
        //adds arbitrary credit to the players
        GuiInterface.getInstance().updatePlayerCredits("lory", 5);
        GuiInterface.getInstance().updatePlayerCredits("fil", 10);
        GuiInterface.getInstance().updatePlayerCredits("tom", 15);
        GuiInterface.getInstance().updatePlayerCredits("nico", 20);
        //endgame state
        GuiInterface.getInstance().updateEndGame();
        FXMLLoader fxmlLoader = new FXMLLoader(TestEndgameGUI.class.getResource("/it/polimi/ingsw/galaxytrucker/fxml/mainScreens/endgame.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1210, 740);
        stage.setTitle("Galaxy Trucker");
        Image icon = new Image(getClass().getResourceAsStream("/it/polimi/ingsw/galaxytrucker/images/galaxyTruckerAppIcon.png"));
        stage.getIcons().add(icon);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Main method to launch the GUI application.
     * @param args
     */
    public static void main(String[] args) {
        launch(args);
    }
}