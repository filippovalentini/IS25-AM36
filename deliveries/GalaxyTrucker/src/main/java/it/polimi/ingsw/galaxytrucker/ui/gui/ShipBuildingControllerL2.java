package it.polimi.ingsw.galaxytrucker.ui.gui;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.galaxytrucker.model.enumerations.Color;
import it.polimi.ingsw.galaxytrucker.model.enumerations.Orientation;
import it.polimi.ingsw.galaxytrucker.network.VirtualServer;
import it.polimi.ingsw.galaxytrucker.ui.gui.controllerInterfaces.ShipBoardController;
import it.polimi.ingsw.galaxytrucker.ui.gui.controllerInterfaces.ShipBuildingController;
import it.polimi.ingsw.galaxytrucker.ui.view.ViewComponent;
import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
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

import javax.swing.text.View;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class ShipBuildingControllerL2 implements ShipBuildingController {

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
    private final Map<Button, String> reservedComponentIds = new HashMap<>();
    private Map<String, Image> componentImageMap = new HashMap<>();

    @FXML private Pane notificationPane;
    @FXML private Pane errorPane;
    @FXML private Label notificationLabel;
    @FXML private Label errorLabel;
    @FXML private Rectangle gameStateBackground;
    @FXML private Label gameStateLabel;
    @FXML private Pane handComponentArea;
    @FXML private TextField ipTextField;
    @FXML private Label playerNameLabel;
    @FXML private Label playerColorLabel;
    @FXML private Button handComponentButton;
    @FXML private GridPane myGridPane;
    @FXML private Button setButton;
    @FXML private Button flightBoardButton;
    @FXML private Button pickComponent;
    @FXML private Button reserveButton;
    @FXML private Button rotateButton;
    @FXML private Button reserved0Button;
    @FXML private Button reserved1Button;
    @FXML private Button shownComponentButton;
    @FXML private Button player1ShipButton;
    @FXML private Button player2ShipButton;
    @FXML private Button player3ShipButton;
    @FXML private Button mineShipButton;
    @FXML private Button discardButton;

    @FXML
    public void initialize() {
        setupGridPaneDragOver();
        setupGridPaneDragDropped();
        setupSetButton();
        setupPickComponentButton();
        setupDiscardButton();
        setupFlightBoardButton();
        setupRotateButton();
        setupReserveButton();
        setupReservedButtonsDrag();
        setupShownComponentsButton();
        setupOtherPlayerButton(player1ShipButton);
        setupOtherPlayerButton(player2ShipButton);
        setupOtherPlayerButton(player3ShipButton);

        componentImageMap = GuiInterface.getInstance().loadImageMap("components");
        showGameState(GuiInterface.getInstance().getView().getGameState());
        showPlaceholderImage();
        setReservedButtonPlaceholder(reserved0Button);
        setReservedButtonPlaceholder(reserved1Button);

        initializeShipBoard();
    }

    public void showGameState(String message){
        gameStateLabel.setText(message);
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
        List<ViewComponent> reservedComponents = GuiInterface.getInstance().getView().getReservedComponents(this.playerNickname);
        int numberReservedComponents = reservedComponents.size();
        if(numberReservedComponents > 0){
            initializeReserveComponentButton(reserved0Button, componentImageMap.get(reservedComponents.get(0).getImageID()));
            if(numberReservedComponents > 1){
                initializeReserveComponentButton(reserved1Button, componentImageMap.get(reservedComponents.get(1).getImageID()));
            }
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
        notificationLabel.setText(gameState);
        playerNameLabel.setText(nn);
        playerColorLabel.setText(Color.convertColorIntoEmoji(c));
    }

    public void initializeButtons(){
        if(GuiInterface.getInstance().getView().getPickedViewComponent() != null){
            pickComponent.setDisable(true);
            reserveButton.setDisable(false);
            rotateButton.setDisable(false);
            discardButton.setDisable(false);
        }
        else {
            pickComponent.setDisable(false);
            reserveButton.setDisable(true);
            rotateButton.setDisable(true);
            discardButton.setDisable(true);
        }
        reserved0Button.setDisable(false);
        reserved1Button.setDisable(false);
        shownComponentButton.setDisable(false);
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
        // Recupera l'immagine del componente dalla mappa
        if(imageID.equals("000") || imageID.equals("003")){
            return;
        }
        Image image = componentImageMap.get(imageID);
        if (image == null) {
            showError("Cabin image not found for component ID: " + imageID);
            return;
        }

        Button button = new Button();
        double buttonSize = 110;

        button.setPrefSize(buttonSize, buttonSize);
        button.setMinSize(buttonSize, buttonSize);
        button.setMaxSize(buttonSize, buttonSize);
        button.setStyle("-fx-padding: 0; -fx-background-color: transparent;");

        // Crea l'ImageView con l'immagine della cabina
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

        // Genera un ID univoco per il bottone
        String btnId = UUID.randomUUID().toString();
        button.setUserData(btnId);

        myGridPane.add(button, column, row);
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
                    // Rimozione sicura da qualunque contenitore
                    if (draggedButton.getParent() instanceof Pane parentPane) {
                        parentPane.getChildren().remove(draggedButton);
                    } else if (myGridPane.getChildren().contains(draggedButton)) {
                        myGridPane.getChildren().remove(draggedButton);
                    } else {
                        // Se proviene da uno dei bottoni riservati, ripristina il placeholder
                        if (reserved0Button.getGraphic() == draggedButton.getGraphic()) {
                            setReservedButtonPlaceholder(reserved0Button);
                        } else if (reserved1Button.getGraphic() == draggedButton.getGraphic()) {
                            setReservedButtonPlaceholder(reserved1Button);
                        }
                    }

                    // Calcola posizione nella griglia
                    colDroppedComponent = getColumnIndexFromX(event.getX());
                    rowDroppedComponent = getRowIndexFromY(event.getY());

                    // Impostazioni grafiche
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

                    // Aggiungi alla griglia
                    myGridPane.add(draggedButton, colDroppedComponent, rowDroppedComponent);
                    lastDroppedButton = draggedButton;

                    Glow glow = new Glow();
                    glow.setLevel(1);
                    draggedButton.setEffect(glow);
                    draggedButton.setOpacity(1);

                    // Aggiorna stato pulsanti
                    rotateButton.setDisable(false);
                    discardButton.setDisable(false);
                    reserveButton.setDisable(false);
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

    private void setReservedButtonPlaceholder(Button button) {
        Image placeholder = componentImageMap.get("3");
        if (placeholder != null) {
            ImageView placeholderView = new ImageView(placeholder);
            placeholderView.setFitWidth(110);
            placeholderView.setFitHeight(110);
            placeholderView.setPreserveRatio(true);
            button.setGraphic(placeholderView);
            button.setStyle("-fx-padding: 0; -fx-background-color: transparent;");
            button.setPrefSize(110, 110);
        }
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

    private void setupShownComponentsButton(){
        shownComponentButton.setOnAction(event -> {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/it/polimi/ingsw/galaxytrucker/shownComponents.fxml"));
                Parent root = fxmlLoader.load();

                ShownComponentsController controller = fxmlLoader.getController();
                controller.setServer(this.server);
                controller.setPlayerInfo(this.gameID, this.playerNickname, this.color);
                controller.setGameType(false);
                GuiInterface.getInstance().setShownComponentsController(controller);

                Stage stage = (Stage) flightBoardButton.getScene().getWindow();
                stage.setScene(new Scene(root, 1210, 740));
                stage.show();

            } catch (IOException e) {
                e.printStackTrace();
                System.err.println("Errore nel caricamento del FlightBoard: " + e.getMessage());
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

    private void setupReserveButton() {
        reserveButton.setOnAction(event -> {
            try{
                server.reserveComponent(this.gameID, this.playerNickname);
            }
            catch(Exception e){
                showError(e.getMessage());
            }
        });
    }

    private void initializeReserveComponentButton(Button targetButton, Image image) {
        ImageView reservedView = new ImageView(image);
        reservedView.setFitWidth(110);
        reservedView.setFitHeight(110);
        reservedView.setPreserveRatio(true);

        targetButton.setGraphic(reservedView);
        targetButton.setStyle("-fx-padding: 0; -fx-background-color: transparent;");
        targetButton.setPrefSize(110, 110);
    }

    private void setupReservedButtonsDrag() {
        setupClickFromReservedButton0();
        setupClickFromReservedButton1();
    }

    private void setupClickFromReservedButton0() {
        reserved0Button.setOnAction(event -> {
            try{
                server.pickReservedComponent(this.gameID, this.playerNickname, 0);
            }
            catch(Exception e){
                showError(e.getMessage());
            }
        });
    }

    private void setupClickFromReservedButton1() {
        reserved1Button.setOnAction(event -> {
            try{
                server.pickReservedComponent(this.gameID, this.playerNickname, 1);
            }
            catch(Exception e){
                showError(e.getMessage());
            }
        });
    }

    private void setupOtherPlayerButton(Button button) {
        button.setOnAction(event -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/it/polimi/ingsw/galaxytrucker/shipBoardL2.fxml"));
                ShipBoardController controller = new ShipBoardControllerL2(button.getText());
                loader.setController(controller);

                Parent root = loader.load();

                controller.setServer(this.server);
                controller.setPlayerInfo(this.gameID, this.playerNickname, this.color);
                GuiInterface.getInstance().setShipBoardController(controller);

                Stage stage = (Stage) button.getScene().getWindow();
                stage.setScene(new Scene(root, 1210, 740));
                stage.show();

            } catch (IOException e) {
                e.printStackTrace();
                System.err.println("Errore nel caricamento del FlightBoard: " + e.getMessage());
            }
        });
    }

    private boolean isPlaceholder(Button button) {
        if (button.getGraphic() instanceof ImageView imageView) {
            Image placeholder = componentImageMap.get("3");
            return imageView.getImage().equals(placeholder);
        }
        return false;
    }

    private boolean imageButtonCorrespondence(Button button, String imageId) {
        if (!(button.getGraphic() instanceof ImageView imageView)) {
            return false;
        }
        Image buttonImage = imageView.getImage();
        if (buttonImage == null) {
            return false;
        }
        Image componentImage = componentImageMap.get(imageId);
        if (componentImage == null) {
            return false;
        }

        return buttonImage.equals(componentImage);
    }

    public void setupFlightBoardButton() {
        flightBoardButton.setOnAction(event -> {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/it/polimi/ingsw/galaxytrucker/flightBoardL2.fxml"));
                Parent root = fxmlLoader.load();

                FlightBoardControllerL2 controller = fxmlLoader.getController();
                controller.setServer(this.server);
                controller.setPlayerInfo(this.gameID, this.playerNickname, this.color);
                GuiInterface.getInstance().setFlightBoardController(controller);

                Stage stage = (Stage) flightBoardButton.getScene().getWindow();
                stage.setScene(new Scene(root, 1210, 740));
                stage.show();

            } catch (IOException e) {
                e.printStackTrace();
                System.err.println("Errore nel caricamento del FlightBoard: " + e.getMessage());
            }
        });
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


    //
     //UPDATES launched by the server
    //

    @Override
    public void setServer(VirtualServer server) {
        this.server = server;
    }

    //invoked to set the players information needed for method invocation on server
    @Override
    public void setPlayerInfo(int gameID, String playerNickname, Color color){
        this.playerNickname = playerNickname;
        this.color = color;
        this.gameID = gameID;
    }

    //notifies a view about an error committed while executing a method on the remote server; the parameter
    //errorMessage describes the type of error
    @Override
    public void notifyError(String errorMessage) throws Exception{
        Platform.runLater(() -> showError(errorMessage));
    }

    //notifies the view about the fact that a component has been successfully picked/released (depending on
    //the value of the boolean parameter) by the corresponding player; the parameter imageID is needed for the
    //view in order to show the right component to the user
    @Override
    public void updatePickedComponent(int imageID, boolean released) throws Exception{
        Platform.runLater(() -> {
            if(released){
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
                    reserveButton.setDisable(true);
                    pickComponent.setDisable(false);
                    setButton.setDisable(true);

                    showPlaceholderImage();
                }
            }
            else{
                initializePickedComponent(String.valueOf(imageID), Orientation.NORTH);

                rotateButton.setDisable(false);
                discardButton.setDisable(false);
                reserveButton.setDisable(false);
                pickComponent.setDisable(true);
                setButton.setDisable(true);
            }
        });
    }

    //notifies the view about the fact that a player (identified by the nickname parameter) has picked a reserved
    //component/ reserved a component (depending on the value of the boolean parameter); the parameter imageID
    //is needed for the view in order to show the right component to the user
    @Override
    public void updateReservedComponent(String nickname, int imageID, boolean released) throws Exception{
        Platform.runLater(() -> {
            if(released){
                if (isPlaceholder(reserved0Button)) {
                    //reserveComponentToButton(reserved0Button);
                    initializeReserveComponentButton(reserved0Button, componentImageMap.get(String.valueOf(imageID)));
                    reserved0Button.setDisable(false);
                } else if (isPlaceholder(reserved1Button)) {
                    //reserveComponentToButton(reserved1Button);
                    initializeReserveComponentButton(reserved1Button, componentImageMap.get(String.valueOf(imageID)));
                    reserved1Button.setDisable(false);
                } else {
                    System.out.println("Entrambe le riserve sono piene.");
                }
            }
            else{
                if(imageButtonCorrespondence(reserved0Button, String.valueOf(imageID))){
                    ImageView buttonView = (ImageView) reserved1Button.getGraphic();
                    Image buttonImage = buttonView.getImage();
                    initializeReserveComponentButton(reserved0Button, buttonImage);
                }
                setReservedButtonPlaceholder(reserved1Button);
            }
        });
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
                reserveButton.setDisable(true);
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

    //notifies the view about a change in the game phase
    @Override
    public void notifyGamePhase(String gamePhase) {
        Platform.runLater(() -> {
            showGameState(gamePhase);
        });
    }

}