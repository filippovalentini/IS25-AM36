package it.polimi.ingsw.galaxytrucker.ui.gui;

import it.polimi.ingsw.galaxytrucker.model.enumerations.Color;
import it.polimi.ingsw.galaxytrucker.model.enumerations.Orientation;
import it.polimi.ingsw.galaxytrucker.network.VirtualServer;
import it.polimi.ingsw.galaxytrucker.ui.gui.controllerInterfaces.ShipBoardController;
import it.polimi.ingsw.galaxytrucker.ui.view.ViewComponent;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShipBoardControllerL1 implements ShipBoardController {

    @FXML
    private Label playerNicknameLabel;
    @FXML
    private Label playerColorLabel;
    @FXML
    private GridPane myGridPane;
    @FXML
    private Button backButton;

    private final String shipBoardPlayerNickname;
    private final Color shipBoardcolor;

    private int gameID;
    private String playerNickname;
    private Color color;
    private VirtualServer server;
    private Map<String, Image> componentImageMap = new HashMap<>();

    public ShipBoardControllerL1(String otherPlayerNickname) {
        this.shipBoardPlayerNickname = otherPlayerNickname;
        this.shipBoardcolor = GuiInterface.getInstance().getView().getCurrentPlayers().get(otherPlayerNickname);
    }

    @FXML
    private void initialize() {
        playerNicknameLabel.setText(shipBoardPlayerNickname);
        playerColorLabel.setText(Color.convertColorIntoEmoji(shipBoardcolor));
        componentImageMap = GuiInterface.getInstance().loadImageMap("components");
        setupBackButton();
        initializeAssembledComponents();
    }

    public void initializeAssembledComponents() {
        List<List<ViewComponent>> assembledComponents = GuiInterface.getInstance().getView().getAssembledComponents(this.shipBoardPlayerNickname);
        for(int i = 0; i < assembledComponents.size(); i++){
            for(int j = 0; j < assembledComponents.get(i).size(); j++){
                ViewComponent component = assembledComponents.get(i).get(j);
                if(component != null){
                    setImageOnGrid(component.getImageID(), component.getOrientation(), j, i);
                }
            }
        }
    }


    public void setImageOnGrid(String imageID, Orientation orientation, int col, int row) {
        if(imageID.equals("000") || imageID.equals("003")){
            return;
        }

        Image image = componentImageMap.get(imageID);
        ImageView imageView = new ImageView(image);

        double cellWidth = myGridPane.getColumnConstraints().get(col).getPrefWidth();
        double cellHeight = myGridPane.getRowConstraints().get(row).getPrefHeight();

        imageView.setFitWidth(cellWidth);
        imageView.setFitHeight(cellHeight);
        imageView.setPreserveRatio(false);
        if(orientation.equals(Orientation.WEST)){
            imageView.setRotate((imageView.getRotate() - 90) % 360);
        }
        else if(orientation.equals(Orientation.SOUTH)){
            imageView.setRotate((imageView.getRotate() - 180) % 360);
        }
        else if(orientation.equals(Orientation.EAST)){
            imageView.setRotate((imageView.getRotate() - 270) % 360);
        }

        myGridPane.add(imageView, col, row);
    }

    public void setupBackButton() {
        backButton.setOnAction(event -> {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/it/polimi/ingsw/galaxytrucker/shipBuildingL1.fxml"));
                Parent root = fxmlLoader.load();

                ShipBuildingControllerL1 controller = fxmlLoader.getController();
                controller.setServer(this.server);
                controller.setPlayerInfo(this.gameID, this.playerNickname, this.color);
                GuiInterface.getInstance().setShipBuildingController(controller);

                Stage stage = (Stage) backButton.getScene().getWindow();
                Scene scene = new Scene(root, 1210, 740);
                stage.setScene(scene);
                stage.show();

            } catch (IOException e) {
                e.printStackTrace();
                System.err.println("Errore nel caricamento della Shipboard: " + e.getMessage());
            }
        });
    }

    @Override
    public void setServer(VirtualServer server) {
        this.server = server;
    }

    @Override
    public void setPlayerInfo(int gameID, String playerNickname, Color color) {
        this.gameID = gameID;
        this.playerNickname = playerNickname;
        this.color = color;
    }

    @Override
    public void updateReservedComponent(String nickname, int imageID, boolean released) throws Exception {}

    @Override
    public void updateAssembledComponent(String nickname, int imageID, Orientation orientation, int x, int y) throws Exception {
        if(!nickname.equals(shipBoardPlayerNickname)){
            return;
        }
        Platform.runLater(() -> {
            setImageOnGrid(String.valueOf(imageID), orientation, y, x);
        });
    }
}
