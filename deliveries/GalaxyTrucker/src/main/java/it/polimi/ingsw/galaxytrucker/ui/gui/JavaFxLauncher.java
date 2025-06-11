package it.polimi.ingsw.galaxytrucker.ui.gui;

import it.polimi.ingsw.galaxytrucker.ui.gui.otherControllers.GameSetupController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import java.io.IOException;

public class JavaFxLauncher extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(JavaFxLauncher.class.getResource("/it/polimi/ingsw/galaxytrucker/fxml/selectNetwork.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        GameSetupController controller = fxmlLoader.getController();
        controller.setControlledStage(stage);
        GuiInterface.getInstance().setSetupController(controller);
        stage.setTitle("Galaxy Trucker");
        Image icon = new Image(getClass().getResourceAsStream("/it/polimi/ingsw/galaxytrucker/images/galaxyTruckerAppIcon.png"));
        stage.getIcons().add(icon);
        stage.setScene(scene);
        stage.show();
    }

}