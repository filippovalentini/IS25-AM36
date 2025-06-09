package it.polimi.ingsw.galaxytrucker.ui.gui;

import it.polimi.ingsw.galaxytrucker.model.enumerations.Color;
import it.polimi.ingsw.galaxytrucker.model.enumerations.Orientation;
import it.polimi.ingsw.galaxytrucker.network.VirtualServer;
import it.polimi.ingsw.galaxytrucker.ui.gui.controllerInterfaces.ShipControlController;
import it.polimi.ingsw.galaxytrucker.ui.view.ViewComponent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ShipControlControllerL2 implements ShipControlController {
    private Stage controlledStage;

    @FXML private Label playerNameLabel;
    @FXML private Label playerColorLabel;
    @FXML private Label notificationLabel;
    @FXML private Pane notificationPane;
    @FXML private Label errorLabel;
    @FXML private Pane errorPane;
    @FXML private Label gameStateLabel;
    @FXML private GridPane myGridPane;
    @FXML private Button player1ShipButton;
    @FXML private Button player2ShipButton;
    @FXML private Button player3ShipButton;
    @FXML private Button crewButton;
    @FXML private Button batteriesButton;
    @FXML private Button destroyButton;
    @FXML private Button flightBoardButton;
    @FXML private Button brownAlienButton;
    @FXML private Button purpleAlienButton;
    @FXML private Label lostComponentsLabel;

    private int selectedRow = -1;
    private int selectedColumn = -1;
    private StackPane lastSelectedCell = null;

    private Map<String, Image> componentImageMap = new HashMap<>();
    int gameID;
    String playerNickname;
    Color playerColor;
    VirtualServer server;

    @FXML
    public void initialize() {
        componentImageMap = GuiInterface.getInstance().loadImageMap("components");

        initializeGameInfo();
        initializeButtons();
        initializeAssembledComponents();
    }

    public void initializeGameInfo() {
        String gameState = GuiInterface.getInstance().getView().getGameState();
        String nn = GuiInterface.getInstance().getView().getNickname();
        Color c = GuiInterface.getInstance().getView().getColor();

        this.playerNickname = nn;
        this.playerColor = c;
        gameStateLabel.setText(gameState);
        playerNameLabel.setText(nn);
        playerColorLabel.setText(Color.convertColorIntoEmoji(c));
    }

    public void initializeButtons(){
        destroyButton.setDisable(true);
        crewButton.setDisable(true);
        batteriesButton.setDisable(true);
        purpleAlienButton.setDisable(true);
        brownAlienButton.setDisable(true);
    }

    public void initializeAssembledComponents(){
        List<List<ViewComponent>> assembledComponents = GuiInterface.getInstance().getView().getAssembledComponents(this.playerNickname);
        for(int i = 0; i < assembledComponents.size(); i++){
            for(int j = 0; j < assembledComponents.get(i).size(); j++){
                ViewComponent component = assembledComponents.get(i).get(j);
                if(component != null){
                    setImageOnGrid(component.getImageID(), component.getOrientation(), j, i);
                }
            }
        }
    }

    public void setImageOnGrid(String imageID, Orientation orientation, int column, int row){
        if(imageID.equals("000") || imageID.equals("003")){
            return;
        }
        Image image = componentImageMap.get(imageID);

        Button button = new Button();
        double buttonSize = 110;

        button.setPrefSize(buttonSize, buttonSize);
        button.setMinSize(buttonSize, buttonSize);
        button.setMaxSize(buttonSize, buttonSize);
        button.setStyle("-fx-padding: 0; -fx-background-color: transparent;");

        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(buttonSize);
        imageView.setFitHeight(buttonSize);
        imageView.setPreserveRatio(true);
        if(orientation.equals(Orientation.WEST)){
            imageView.setRotate((imageView.getRotate() - 90) % 360);
        }
        else if(orientation.equals(Orientation.SOUTH)){
            imageView.setRotate((imageView.getRotate() - 180) % 360);
        }
        else if(orientation.equals(Orientation.EAST)){
            imageView.setRotate((imageView.getRotate() - 270) % 360);
        }
        button.setGraphic(imageView);

        String btnId = UUID.randomUUID().toString();
        button.setUserData(btnId);

        myGridPane.add(button, column, row);
    }



    @FXML
    private void setupPlayer1ShipButton() {
        player1ShipButton.setOnAction(event -> {

        });
    }

    @FXML
    private void setupPlayer2ShipButton() {
        player2ShipButton.setOnAction(event -> {

        });
    }

    @FXML
    private void setupPlayer3ShipButton() {
        player3ShipButton.setOnAction(event -> {

        });
    }

    @FXML
    private void setupCrewButton() {
        crewButton.setOnAction(event -> {

        });
    }

    @FXML
    private void setupBatteriesButton() {
        batteriesButton.setOnAction(event -> {

        });
    }

    @FXML
    private void setupDestroyButton() {
        destroyButton.setOnAction(event -> {

        });
    }

    @FXML
    private void setupFlightBoardButton() {
        flightBoardButton.setOnAction(event -> {

        });
    }

    @FXML
    private void setupBrownAlienButton() {
        brownAlienButton.setOnAction(event -> {

        });
    }

    @FXML
    private void setupPurpleAlienButton() {
        purpleAlienButton.setOnAction(event -> {

        });
    }

    public int getSelectedRow() {
        return selectedRow;
    }

    public int getSelectedColumn() {
        return selectedColumn;
    }

    @Override
    public void updateDestroyedComponent(String nickname, int x, int y) throws Exception {

    }

    @Override
    public void updateCrewChange(String nickname, int x, int y, int change) throws Exception {

    }

    @Override
    public void updateBatteries(String nickname, int x, int y, int change) throws Exception {

    }

    @Override
    public void updateAlienChange(String nickname, int x, int y, boolean isPurple, boolean added) throws Exception {

    }

    @Override
    public void updateCardPicking() throws Exception {

    }

    @Override
    public void setControlledStage(Stage stage) {
        controlledStage = stage;
    }

    @Override
    public void setServer(VirtualServer server) {
        this.server = server;
    }

    @Override
    public void setPlayerInfo(int gameID, String playerNickname, Color color) {
        this.gameID = gameID;
        this.playerNickname = playerNickname;
        this.playerColor = color;
    }

    @Override
    public void notifyError(String error) throws Exception {

    }

    @Override
    public void notifyGamePhase(String gamePhase) throws Exception {

    }
}

