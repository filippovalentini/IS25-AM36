package it.polimi.ingsw.galaxytrucker.ui.gui;

import it.polimi.ingsw.galaxytrucker.model.enumerations.Color;
import it.polimi.ingsw.galaxytrucker.network.VirtualServer;
import it.polimi.ingsw.galaxytrucker.ui.gui.controllerInterfaces.GuiController;
import it.polimi.ingsw.galaxytrucker.ui.gui.controllerInterfaces.ShipBuildingController;
import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShownComponentsController implements GuiController {

    @FXML private GridPane componentGrid;
    @FXML private Button pickButton;
    @FXML private Pane notificationPane;
    @FXML private Pane errorPane;
    @FXML private Label notificationLabel;
    @FXML private Label errorLabel;
    @FXML private Button backButton;

    private Map<String, Image> componentImageMap = new HashMap<>();
    private ImageView selectedImageView = null;

    private final int CELL_WIDTH = 120;
    private final int CELL_HEIGHT = 120;
    private final int COLUMNS = 5;

    private boolean firstFlight;
    private VirtualServer server;
    private Integer gameId;
    private String playerNickname;
    private Color color;

    @FXML
    public void initialize() {
        setupPickButton();
        setUpBackButton();
        componentImageMap = GuiInterface.getInstance().loadImageMap("components");

        List<Integer> currentShownComponents = GuiInterface.getInstance().getView().getShownComponents();
        for(Integer id : currentShownComponents){
            updateShownComponents(id, true);
        }

        componentGrid.setMinWidth(COLUMNS * (CELL_WIDTH + 10)); // + hgap
        componentGrid.setPrefWidth(componentGrid.getMinWidth());
    }

    public void showNotification(String message) {
        notificationLabel.setText(message);
        fadeInThenOut(notificationPane);
    }

    public void showError(String message) {
        errorLabel.setText(message);
        fadeInThenOut(errorPane);
    }

    private void fadeInThenOut(Pane pane) {
        pane.setOpacity(1.0);

        // Timer: attende 3 secondi, poi parte il fade out
        PauseTransition wait = new PauseTransition(Duration.seconds(3));
        wait.setOnFinished(event -> {
            FadeTransition fade = new FadeTransition(Duration.seconds(1.5), pane);
            fade.setFromValue(1.0);
            fade.setToValue(0.0);
            fade.play();
        });
        wait.play();
    }

    private ImageView createImageView(Image image) {
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(CELL_WIDTH);
        imageView.setFitHeight(CELL_HEIGHT);
        imageView.setPreserveRatio(true);
        imageView.setSmooth(true);

        imageView.setOnMouseClicked((MouseEvent event) -> {
            if (selectedImageView != null) {
                selectedImageView.setEffect(null); // rimuovi highlight precedente
            }
            selectedImageView = imageView;
            Glow glow = new Glow();
            glow.setLevel(1);
            selectedImageView.setEffect(glow);
            selectedImageView.setOpacity(1);
            pickButton.setDisable(false);
        });

        return imageView;
    }

    public void updateShownComponents(int imageID, boolean released) {
        Platform.runLater(() -> {
            Image componentImage = componentImageMap.get(String.valueOf(imageID));
            if (released) {

                int count = componentGrid.getChildren().size();
                int col = count % COLUMNS;
                int row = count / COLUMNS;

                ImageView imageView = createImageView(componentImage);
                componentGrid.add(imageView, col, row);
            }
            else{
                if (componentImage == null) return;

                ImageView toRemove = null;
                for (javafx.scene.Node node : componentGrid.getChildren()) {
                    if (node instanceof ImageView imageView) {
                        if (imageView.getImage().equals(componentImage)) {
                            toRemove = imageView;
                            break;
                        }
                    }
                }

                if (toRemove == null) return;
                componentGrid.getChildren().remove(toRemove);

                var remaining = new java.util.ArrayList<javafx.scene.Node>(componentGrid.getChildren());
                componentGrid.getChildren().clear();

                for (int i = 0; i < remaining.size(); i++) {
                    int col = i % COLUMNS;
                    int row = i / COLUMNS;
                    componentGrid.add(remaining.get(i), col, row);
                }
            }
            clearSelection();
        });
    }

    public void notifyError(String message) {
        Platform.runLater(() -> {
            errorLabel.setText(message);
        });
    }

    @FXML
    public void setupPickButton() {
        pickButton.setOnAction(event -> {
            try{
                int position = getSelectedComponentIndex();
                server.pickShown(this.gameId, this.playerNickname, position);
            }
            catch(Exception e){
                showError(e.getMessage());
            }
        });
    }

    @FXML
    public void setUpBackButton() {
        backButton.setOnAction(event -> {
            try {
                FXMLLoader fxmlLoader;
                if(this.firstFlight){
                    fxmlLoader = new FXMLLoader(getClass().getResource("/it/polimi/ingsw/galaxytrucker/shipBuildingL1.fxml"));
                }
                else{
                    fxmlLoader = new FXMLLoader(getClass().getResource("/it/polimi/ingsw/galaxytrucker/shipBuildingL2.fxml"));
                }
                Parent root = fxmlLoader.load();
                ShipBuildingController controller = fxmlLoader.getController();
                controller.setServer(this.server);
                controller.setPlayerInfo(this.gameId, this.playerNickname, this.color);
                GuiInterface.getInstance().setShipBuildingController(controller);

                Stage stage = (Stage) backButton.getScene().getWindow();
                stage.setScene(new Scene(root, 1210, 740));
                stage.show();

            } catch (IOException e) {
                e.printStackTrace();
                System.err.println("Errore nel caricamento del FlightBoard: " + e.getMessage());
            }
        });
    }

    public Integer getSelectedComponentIndex() {
        if (selectedImageView == null) return null;

        var children = componentGrid.getChildren();
        for (int i = 0; i < children.size(); i++) {
            if (children.get(i) == selectedImageView) {
                return i;
            }
        }

        return null;
    }

    public void clearSelection() {
        if (selectedImageView != null) {
            selectedImageView.setEffect(null);
            selectedImageView = null;
            pickButton.setDisable(true);
        }
    }

    public void setGameType(boolean firstFlight){
        this.firstFlight = firstFlight;
    }

    @Override
    public void setServer(VirtualServer server) {
        this.server = server;
    }

    @Override
    public void setPlayerInfo(int gameID, String playerNickname, Color color) {
        this.gameId = gameID;
        this.playerNickname = playerNickname;
        this.color = color;
    }

}