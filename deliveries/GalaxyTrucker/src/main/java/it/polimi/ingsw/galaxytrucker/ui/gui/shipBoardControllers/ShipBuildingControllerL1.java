package it.polimi.ingsw.galaxytrucker.ui.gui.shipBoardControllers;

import it.polimi.ingsw.galaxytrucker.model.enumerations.Color;
import it.polimi.ingsw.galaxytrucker.model.enumerations.Orientation;
import it.polimi.ingsw.galaxytrucker.network.VirtualServer;
import it.polimi.ingsw.galaxytrucker.ui.gui.GuiInterface;
import it.polimi.ingsw.galaxytrucker.ui.gui.controllerInterfaces.ShipBoardController;
import it.polimi.ingsw.galaxytrucker.ui.gui.otherControllers.ShownComponentsController;
import it.polimi.ingsw.galaxytrucker.ui.gui.controllerInterfaces.ShipBuildingController;
import it.polimi.ingsw.galaxytrucker.ui.gui.flightBoardControllers.FlightBoardControllerL1;
import it.polimi.ingsw.galaxytrucker.ui.view.ViewComponent;
import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.util.*;

public class ShipBuildingControllerL1 implements ShipBuildingController {
    private Stage controlledStage;

    private VirtualServer server;

    private int gameID;
    private String playerNickname;
    private Color color;

    private int colDroppedComponent;
    private int rowDroppedComponent;
    private boolean isComponentPlaced = false;
    private boolean firstComponent = true;
    private boolean componentPicked = false;

    private Button lastDroppedButton = null;
    private final Map<String, Button> draggableButtons = new HashMap<>();
    private Map<String, Image> componentImageMap = new HashMap<>();

    // FXML Components - existing
    @FXML private Rectangle gameStateBackground;
    @FXML private Pane handComponentArea;
    @FXML private Button handComponentButton;
    @FXML private GridPane myGridPane;
    @FXML private Button setButton;
    @FXML private Button flightBoardButton;
    @FXML private Button pickComponent;
    @FXML private Button rotateButton;
    @FXML private Button viewShownComponentButton;
    @FXML private Button player1ShipButton;
    @FXML private Button player2ShipButton;
    @FXML private Button player3ShipButton;
    @FXML private Button discardButton;
    @FXML private Label playerNameLabel;
    @FXML private Label playerColorLabel;

    @FXML private Button shownComponentButton;

    // Da aggiungere
    @FXML private Pane errorPane;
    @FXML private Label gameStateLabel;
    @FXML private Label errorLabel;

    @FXML
    public void initialize() {
        setupGridPaneDragOver();
        setupGridPaneDragDropped();
        setupSetButton();
        setupPickComponentButton();
        setupDiscardButton();
        setupFlightBoardButton();
        setupRotateButton();
        setupViewShownComponentButton();
        setupShownComponentsButton();


        componentImageMap = GuiInterface.getInstance().loadImageMap("components");
        showPlaceholderImage();

        initializeShipBoard();
        setupOtherPlayerButton(player1ShipButton);
        setupOtherPlayerButton(player2ShipButton);
        setupOtherPlayerButton(player3ShipButton);
        setupImages();
    }

    public void initializeShipBoard(){
        initializeGameInfo();
        initializeAssembledComponents();
        initializeOtherComponents();
        initializeButtons();
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

    public void initializeOtherComponents(){
        ViewComponent pickedComponent = GuiInterface.getInstance().getView().getPickedViewComponent();
        if(pickedComponent != null){
            initializePickedComponent(String.valueOf(pickedComponent.getImageID()), pickedComponent.getOrientation());
        }
    }

    public void initializePickedComponent(String imageID, Orientation orientation){
        if (isComponentPlaced || firstComponent) {
            Button newButton = new Button();
            double buttonSize = 150;

            newButton.setPrefSize(buttonSize, buttonSize);
            newButton.setMinSize(buttonSize, buttonSize);
            newButton.setMaxSize(buttonSize, buttonSize);
            newButton.setStyle("-fx-padding: 0; -fx-background-color: transparent;");

            Image image = componentImageMap.get(imageID);

            if (image != null) {
                ImageView imageView = new ImageView(image);
                imageView.setFitWidth(buttonSize);
                imageView.setFitHeight(buttonSize);
                imageView.setPreserveRatio(true);
                imageView.setSmooth(true);
                imageView.setCache(true);
                if(orientation.equals(Orientation.WEST)){
                    imageView.setRotate((imageView.getRotate() - 90) % 360);
                }
                else if(orientation.equals(Orientation.SOUTH)){
                    imageView.setRotate((imageView.getRotate() - 180) % 360);
                }
                else if(orientation.equals(Orientation.EAST)){
                    imageView.setRotate((imageView.getRotate() - 270) % 360);
                }
                newButton.setGraphic(imageView);
            }

            String btnId = UUID.randomUUID().toString();
            newButton.setUserData(btnId);
            draggableButtons.put(btnId, newButton);

            newButton.setOnDragDetected(event2 -> {
                Dragboard db = newButton.startDragAndDrop(TransferMode.MOVE);
                ClipboardContent content = new ClipboardContent();
                content.putString(btnId);
                db.setContent(content);
                firstComponent = false;
                componentPicked = true;
                event2.consume();
            });

            handComponentArea.getChildren().clear();
            handComponentArea.getChildren().add(newButton);

            lastDroppedButton = newButton;
        }
    }

    public void initializeGameInfo(){
        String gameState = GuiInterface.getInstance().getView().getGameState();
        String nn = GuiInterface.getInstance().getView().getNickname();
        Color c = GuiInterface.getInstance().getView().getColor();

        this.playerNickname = nn;
        this.color = c;

        if (gameStateLabel != null) {
            gameStateLabel.setText(gameState);
        }
        if (playerNameLabel != null) {
            playerNameLabel.setText(nn);
        }
        if (playerColorLabel != null) {
            playerColorLabel.setText("██");
            playerColorLabel.setStyle(Color.convertColorIntoStyle(c));
        }
    }

    public void initializeButtons(){
        if(GuiInterface.getInstance().getView().getPickedViewComponent() != null){
            pickComponent.setDisable(true);
            rotateButton.setDisable(false);
            discardButton.setDisable(false);
        }
        else {
            pickComponent.setDisable(false);
            rotateButton.setDisable(true);
            discardButton.setDisable(true);
        }
        if (shownComponentButton != null) shownComponentButton.setDisable(false);
        setButton.setDisable(true);
        flightBoardButton.setDisable(false);

        List<String> otherPlayerNicknames = GuiInterface.getInstance().getView().getOtherPlayerNicknames();
        int numberOtherPlayers = otherPlayerNicknames.size();
        player1ShipButton.setDisable(false);
        player1ShipButton.setText(otherPlayerNicknames.get(0));
        if(numberOtherPlayers == 1){
            player2ShipButton.setDisable(true);
            player2ShipButton.setText("no player");
            player3ShipButton.setDisable(true);
            player3ShipButton.setText("no player");
        }
        if(numberOtherPlayers == 2){
            player2ShipButton.setDisable(false);
            player2ShipButton.setText(otherPlayerNicknames.get(1));
            player3ShipButton.setDisable(true);
            player3ShipButton.setText("no player");
        }
        if(numberOtherPlayers == 3){
            player2ShipButton.setDisable(false);
            player2ShipButton.setText(otherPlayerNicknames.get(1));
            player3ShipButton.setDisable(false);
            player3ShipButton.setText(otherPlayerNicknames.get(2));
        }
    }

    public void setImageOnGrid(String imageID, Orientation orientation, int column, int row){
        if(imageID.equals("000") || imageID.equals("003")){
            return;
        }
        Image componentImage = componentImageMap.get(imageID);
        if (componentImage == null) {
            showError("Component image not found for component ID: " + imageID);
            return;
        }

        Button componentButton = new Button();
        double buttonSize = 110;

        componentButton.setPrefSize(buttonSize, buttonSize);
        componentButton.setMinSize(buttonSize, buttonSize);
        componentButton.setMaxSize(buttonSize, buttonSize);
        componentButton.setStyle("-fx-padding: 0; -fx-background-color: transparent;");

        ImageView componentImageView = new ImageView(componentImage);
        componentImageView.setFitWidth(buttonSize);
        componentImageView.setFitHeight(buttonSize);
        componentImageView.setPreserveRatio(true);
        if(orientation.equals(Orientation.WEST)){
            componentImageView.setRotate((componentImageView.getRotate() - 90) % 360);
        }
        else if(orientation.equals(Orientation.SOUTH)){
            componentImageView.setRotate((componentImageView.getRotate() - 180) % 360);
        }
        else if(orientation.equals(Orientation.EAST)){
            componentImageView.setRotate((componentImageView.getRotate() - 270) % 360);
        }
        componentButton.setGraphic(componentImageView);

        String btnId = UUID.randomUUID().toString();
        componentButton.setUserData(btnId);

        myGridPane.add(componentButton, column, row);
    }

    public void showError(String message) {
        if (errorLabel != null && errorPane != null) {
            errorLabel.setText(message);
            fadeInThenOut(errorPane);
        } else {
            System.err.println("Error: " + message);
        }
    }

    private void fadeInThenOut(Pane pane) {
        pane.setOpacity(1.0);

        PauseTransition wait = new PauseTransition(Duration.seconds(3));
        wait.setOnFinished(event -> {
            FadeTransition fade = new FadeTransition(Duration.seconds(1.5), pane);
            fade.setFromValue(1.0);
            fade.setToValue(0.0);
            fade.play();
        });
        wait.play();
    }

    private void setupGridPaneDragOver() {
        myGridPane.setOnDragOver(event -> {
            if (event.getGestureSource() != myGridPane && event.getDragboard().hasString()) {
                event.acceptTransferModes(TransferMode.MOVE);
            }
            event.consume();
        });
    }

    private void setupGridPaneDragDropped() {
        myGridPane.setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();
            boolean success = false;

            if (db.hasString()) {
                String btnId = db.getString();
                Button draggedButton = draggableButtons.get(btnId);

                if (draggedButton != null) {
                    // Safe removal from any container
                    if (draggedButton.getParent() instanceof Pane parentPane) {
                        parentPane.getChildren().remove(draggedButton);
                    } else if (myGridPane.getChildren().contains(draggedButton)) {
                        myGridPane.getChildren().remove(draggedButton);
                    }

                    // Calculate grid position
                    colDroppedComponent = getColumnIndexFromX(event.getX());
                    rowDroppedComponent = getRowIndexFromY(event.getY());

                    // Graphics settings
                    double buttonSize = 110;
                    draggedButton.setPrefSize(buttonSize, buttonSize);
                    draggedButton.setMinSize(buttonSize, buttonSize);
                    draggedButton.setMaxSize(buttonSize, buttonSize);
                    draggedButton.setStyle("-fx-padding: 0; -fx-background-color: transparent;");

                    if (draggedButton.getGraphic() instanceof ImageView imageView) {
                        imageView.setFitWidth(buttonSize);
                        imageView.setFitHeight(buttonSize);
                        imageView.setPreserveRatio(true);
                    }

                    // Add to grid
                    myGridPane.add(draggedButton, colDroppedComponent, rowDroppedComponent);
                    lastDroppedButton = draggedButton;

                    Glow glow = new Glow();
                    glow.setLevel(1);
                    draggedButton.setEffect(glow);
                    draggedButton.setOpacity(1);

                    // Update button states
                    rotateButton.setDisable(false);
                    discardButton.setDisable(false);
                    pickComponent.setDisable(true);
                    setButton.setDisable(false);

                    handComponentArea.getChildren().clear();
                    showPlaceholderImage();

                    success = true;
                }
            }

            event.setDropCompleted(success);
            event.consume();
        });
    }

    private void setupSetButton() {
        setButton.setOnAction(event -> {
            try{
                server.assembledComponent(this.gameID, this.playerNickname, this.rowDroppedComponent, this.colDroppedComponent);
            }
            catch (Exception e) {
                showError(e.getMessage());
            }
        });
    }

    private void setupPickComponentButton() {
        pickComponent.setOnAction(event -> {
            try{
                server.pickHidden(gameID, playerNickname);
            }
            catch(Exception e){
                showError(e.getMessage());
            }
        });
    }

    private void setupRotateButton() {
        rotateButton.setOnAction(event -> {
            try{
                server.rotatePickedComponent(gameID, playerNickname);
            }
            catch(Exception e){
                showError(e.getMessage());
            }
        });
    }

    private void setupDiscardButton() {
        discardButton.setOnAction(event -> {
            try{
                server.putShown(this.gameID, this.playerNickname);
            }
            catch(Exception e){
                showError(e.getMessage());
            }
        });
    }



    private void setupShownComponentsButton(){
        if (shownComponentButton == null) {
            // Use viewShownComponentButton as fallback
            if (viewShownComponentButton != null) {
                shownComponentButton = viewShownComponentButton;
            }
        }

        Button buttonToUse = (shownComponentButton != null) ? shownComponentButton : viewShownComponentButton;

        if (buttonToUse != null) {
            buttonToUse.setOnAction(event -> {
                try {
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/it/polimi/ingsw/galaxytrucker/fxml/mainScreens/shownComponents.fxml"));
                    Parent root = fxmlLoader.load();

                    ShownComponentsController controller = fxmlLoader.getController();
                    controller.setServer(this.server);
                    controller.setPlayerInfo(this.gameID, this.playerNickname, this.color);
                    controller.setGameType(true);
                    GuiInterface.getInstance().setShownComponentsController(controller);

                    controller.setControlledStage(controlledStage);
                    controlledStage.setScene(new Scene(root, 1210, 740));
                    controlledStage.show();

                } catch (IOException e) {
                    e.printStackTrace();
                    System.err.println("Errore nel caricamento del ShownComponents: " + e.getMessage());
                }
            });
        }
    }

    public void setupFlightBoardButton() {
        flightBoardButton.setOnAction(event -> {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/it/polimi/ingsw/galaxytrucker/fxml/mainScreens/flightBoardL1.fxml"));
                Parent root = fxmlLoader.load();

                FlightBoardControllerL1 controller = fxmlLoader.getController();
                controller.setServer(this.server);
                controller.setPlayerInfo(this.gameID, this.playerNickname, this.color);
                GuiInterface.getInstance().setFlightBoardController(controller);

                controller.setControlledStage(controlledStage);
                controlledStage.setScene(new Scene(root, 1210, 740));
                controlledStage.show();

            } catch (IOException e) {
                e.printStackTrace();
                System.err.println("Errore nel caricamento del FlightBoard: " + e.getMessage());
            }
        });
    }

    public void setupViewShownComponentButton() {
        if (viewShownComponentButton != null) {
            setupShownComponentsButton(); // This will handle the logic
        }
    }

    private int getColumnIndexFromX(double x) {
        double widthSoFar = 0;
        for (int i = 0; i < myGridPane.getColumnConstraints().size(); i++) {
            widthSoFar += myGridPane.getColumnConstraints().get(i).getPrefWidth();
            if (x < widthSoFar) return i;
        }
        return myGridPane.getColumnConstraints().size() - 1;
    }

    private int getRowIndexFromY(double y) {
        double heightSoFar = 0;
        for (int i = 0; i < myGridPane.getRowConstraints().size(); i++) {
            heightSoFar += myGridPane.getRowConstraints().get(i).getPrefHeight();
            if (y < heightSoFar) return i;
        }
        return myGridPane.getRowConstraints().size() - 1;
    }

    private void showPlaceholderImage() {
        handComponentArea.getChildren().clear();
        Image placeholderImage = componentImageMap.get("3");
        if (placeholderImage != null) {
            ImageView placeholderView = new ImageView(placeholderImage);
            placeholderView.setFitWidth(150);
            placeholderView.setFitHeight(150);
            placeholderView.setPreserveRatio(true);
            placeholderView.setSmooth(true);
            placeholderView.setCache(true);
            handComponentArea.getChildren().add(placeholderView);
        }
    }

    private void setupImages() {
        try {
            //Bottone HandComponent
            setupButtonWithImage(handComponentButton, "/it/polimi/ingsw/galaxytrucker/images/components/back.jpg", "handComponent", 150, 150);

        } catch (Exception e) {
            System.err.println("Errore nel caricamento delle immagini: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void setupButtonWithImage(Button button, String imagePath, String text, float width, float height) {
        try {
            Image image = new Image(getClass().getResourceAsStream(imagePath));
            ImageView imageView = new ImageView(image);

            imageView.fitWidthProperty().bind(button.widthProperty());
            imageView.fitHeightProperty().bind(button.heightProperty());
            imageView.setPreserveRatio(false);
            imageView.setSmooth(true);

            button.setGraphic(imageView);
            button.setText("");
            button.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);

        } catch (Exception e) {
            System.err.println("Impossibile caricare l'immagine: " + imagePath);
            button.setText(text);
        }
    }

    // Interface implementations with Platform.runLater for thread safety

    private void setupOtherPlayerButton(Button button) {
        button.setOnAction(event -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/it/polimi/ingsw/galaxytrucker/fxml/mainScreens/shipBoardL1.fxml"));
                ShipBoardController controller = new ShipBoardControllerL1(button.getText());
                loader.setController(controller);

                Parent root = loader.load();

                controller.setServer(this.server);
                controller.setPlayerInfo(this.gameID, this.playerNickname, this.color);
                GuiInterface.getInstance().setShipBoardController(controller);

                controller.setControlledStage(controlledStage);
                controlledStage.setScene(new Scene(root, 1210, 740));
                controlledStage.show();

            } catch (IOException e) {
                e.printStackTrace();
                System.err.println("Errore nel caricamento del FlightBoard: " + e.getMessage());
            }
        });
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
    public void setPlayerInfo(int gameID, String playerNickname, Color color){
        this.playerNickname = playerNickname;
        this.color = color;
        this.gameID = gameID;
    }

    @Override
    public void notifyError(String errorMessage) throws Exception{
        Platform.runLater(() -> {showError(errorMessage);});
    }

    @Override
    public void notifyGamePhase(String gamePhase) throws Exception {

    }

    @Override
    public void updatePickedComponent(int imageID, boolean released) throws Exception {
        Platform.runLater(() -> {
            if (released) {
                if (lastDroppedButton != null) {
                    myGridPane.getChildren().remove(lastDroppedButton);
                    handComponentArea.getChildren().remove(lastDroppedButton);

                    String btnId = (String) lastDroppedButton.getUserData();
                    if (btnId != null) {
                        draggableButtons.remove(btnId);
                    }

                    lastDroppedButton = null;

                    firstComponent = true;
                    isComponentPlaced = false;
                    componentPicked = false;

                    rotateButton.setDisable(true);
                    discardButton.setDisable(true);
                    pickComponent.setDisable(false);
                    setButton.setDisable(true);

                    showPlaceholderImage();
                }
            } else {
                initializePickedComponent(String.valueOf(imageID), Orientation.NORTH);

                rotateButton.setDisable(false);
                discardButton.setDisable(false);
                pickComponent.setDisable(true);
                setButton.setDisable(true);
            }
        });

    }


    @Override
    public void updateReservedComponent(String nickname, int imageID, boolean released) throws Exception {
    }


    //notifies the view about the fact that the picked component of the corresponding player has been rotated
    @Override
    public void updateRotatePickedComponent() throws Exception{
        Platform.runLater(() -> {
            if (lastDroppedButton != null && lastDroppedButton.getGraphic() instanceof ImageView) {
                ImageView imageView = (ImageView) lastDroppedButton.getGraphic();
                imageView.setRotate((imageView.getRotate() - 90) % 360);
            }
        });
    }

    //notifies the view about the fact that a player (identified by the nickname parameter) has assembled a
    //component in position (x,y) of its ship board; the parameter imageID is needed for the view in order
    //to show the right component to the user
    @Override
    public void updateAssembledComponent(String nickname, int imageID, Orientation orientation, int x, int y) throws Exception{
        Platform.runLater(() -> {
            if (lastDroppedButton != null) {
                lastDroppedButton.setOnDragDetected(null);
                lastDroppedButton.setEffect(null);
                lastDroppedButton.setOpacity(1.0);
                isComponentPlaced = true;
                componentPicked = false;

                rotateButton.setDisable(true);
                discardButton.setDisable(true);
                pickComponent.setDisable(false);
                setButton.setDisable(true);
                lastDroppedButton = null;
            }
        });
    }

    //notifies the view about the fact that a player has finished the assembling phase and is
    //correctly positioned on the flight board; still, other players have to finish building their ships
    @Override
    public void updateFinishAssembling(String nickname, int position) throws Exception{}


    //invoked when the game switches to the ship placement phase, which means that the players can only
    //place their ship on the flight board
    @Override
    public void updateShipPlacement() throws Exception{}

    //notifies the view that all the players have concluded the assembling phase, which means that the players
    //enter the ship control phase
    @Override
    public void updateShipControl() throws Exception{}



}