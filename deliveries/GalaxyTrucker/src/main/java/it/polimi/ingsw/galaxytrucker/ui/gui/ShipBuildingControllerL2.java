package it.polimi.ingsw.galaxytrucker.ui.gui;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.galaxytrucker.model.enumerations.Color;
import it.polimi.ingsw.galaxytrucker.model.enumerations.Orientation;
import it.polimi.ingsw.galaxytrucker.network.VirtualServer;
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
import javafx.scene.control.TextField;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

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
    @FXML private Button viewShownComponentButton;
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

        rotateButton.setDisable(true);
        discardButton.setDisable(true);
        pickComponent.setDisable(false);
        reserveButton.setDisable(true);
        setButton.setDisable(true);
        componentImageMap = GuiInterface.getInstance().loadImageMap("components");

        showPlaceholderImage();
        setReservedButtonPlaceholders();
        showNotification("ASSEMBLING PHASE - build your ship");
    }

    public void setInitialCabin(){
        // Determina l'ID dell'immagine del componente in base al colore
        String componentImageId;

        if(this.color.equals(Color.RED)){
            componentImageId = "320";
        } else if(this.color.equals(Color.BLUE)){
            componentImageId = "318";
        } else if(this.color.equals(Color.GREEN)){
            componentImageId = "319";
        } else if(this.color.equals(Color.YELLOW)){
            componentImageId = "321";
        } else {
            showError("Player has an invalid color");
            return;
        }

        // Recupera l'immagine del componente dalla mappa
        Image cabinImage = componentImageMap.get(componentImageId);
        if (cabinImage == null) {
            showError("Cabin image not found for component ID: " + componentImageId);
            return;
        }

        Button cabinButton = new Button();
        double buttonSize = 110;

        cabinButton.setPrefSize(buttonSize, buttonSize);
        cabinButton.setMinSize(buttonSize, buttonSize);
        cabinButton.setMaxSize(buttonSize, buttonSize);
        cabinButton.setStyle("-fx-padding: 0; -fx-background-color: transparent;");

        // Crea l'ImageView con l'immagine della cabina
        ImageView cabinImageView = new ImageView(cabinImage);
        cabinImageView.setFitWidth(buttonSize);
        cabinImageView.setFitHeight(buttonSize);
        cabinImageView.setPreserveRatio(true);
        cabinButton.setGraphic(cabinImageView);

        // Genera un ID univoco per il bottone
        String btnId = UUID.randomUUID().toString();
        cabinButton.setUserData(btnId);

        // Posiziona la cabina nella cella centrale della griglia
        myGridPane.add(cabinButton, 3, 2);
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

    private void reserveComponentToButton(Button targetButton) {
        if (lastDroppedButton == null || !(lastDroppedButton.getGraphic() instanceof ImageView)) return;

        ImageView sourceImageView = (ImageView) lastDroppedButton.getGraphic();
        ImageView reservedView = new ImageView(sourceImageView.getImage());
        reservedView.setFitWidth(110);
        reservedView.setFitHeight(110);
        reservedView.setPreserveRatio(true);

        targetButton.setGraphic(reservedView);
        targetButton.setStyle("-fx-padding: 0; -fx-background-color: transparent;");
        targetButton.setPrefSize(110, 110);

        reservedComponentIds.put(targetButton, (String) lastDroppedButton.getUserData());

        myGridPane.getChildren().remove(lastDroppedButton);
        handComponentArea.getChildren().remove(lastDroppedButton);

        String btnId = (String) lastDroppedButton.getUserData();
        if (btnId != null) {
            draggableButtons.remove(btnId);
        }

        lastDroppedButton = null;
        isComponentPlaced = false;
        firstComponent = true;
        componentPicked = false;

        rotateButton.setDisable(true);
        discardButton.setDisable(true);
        reserveButton.setDisable(true);
        pickComponent.setDisable(false);
        setButton.setDisable(true);

        showPlaceholderImage();
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

    private boolean isPlaceholder(Button button) {
        if (button.getGraphic() instanceof ImageView imageView) {
            Image placeholder = componentImageMap.get("3");
            return imageView.getImage().equals(placeholder);
        }
        return false;
    }

    private boolean imageButtonCorrespondence(Button button, String imageId) {
        // Controlla se il bottone ha un'immagine
        if (!(button.getGraphic() instanceof ImageView imageView)) {
            return false;
        }

        // Recupera l'immagine dal bottone
        Image buttonImage = imageView.getImage();
        if (buttonImage == null) {
            return false;
        }

        // Recupera l'immagine dalla mappa usando l'ID
        Image componentImage = componentImageMap.get(imageId);
        if (componentImage == null) {
            return false;
        }

        // Confronta le due immagini
        return buttonImage.equals(componentImage);
    }

    private void setReservedButtonPlaceholders() {
        Image placeholder = componentImageMap.get("3");
        if (placeholder != null) {
            ImageView view0 = new ImageView(placeholder);
            view0.setFitWidth(110);
            view0.setFitHeight(110);
            view0.setPreserveRatio(true);
            reserved0Button.setGraphic(view0);
            reserved0Button.setStyle("-fx-padding: 0; -fx-background-color: transparent;");
            reserved0Button.setPrefSize(110, 110);

            ImageView view1 = new ImageView(placeholder);
            view1.setFitWidth(110);
            view1.setFitHeight(110);
            view1.setPreserveRatio(true);
            reserved1Button.setGraphic(view1);
            reserved1Button.setStyle("-fx-padding: 0; -fx-background-color: transparent;");
            reserved1Button.setPrefSize(110, 110);
        }
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
        setInitialCabin();
        playerNameLabel.setText(playerNickname);
        playerColorLabel.setText(Color.convertColorIntoEmoji(color));
    }

    //notifies a view about an error committed while executing a method on the remote server; the parameter
    //errorMessage describes the type of error
    @Override
    public void notifyError(String errorMessage) throws Exception{
        Platform.runLater(() -> {showError(errorMessage);});
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
                if (isComponentPlaced || firstComponent) {
                    Button newButton = new Button();
                    double buttonSize = 150;

                    newButton.setPrefSize(buttonSize, buttonSize);
                    newButton.setMinSize(buttonSize, buttonSize);
                    newButton.setMaxSize(buttonSize, buttonSize);
                    newButton.setStyle("-fx-padding: 0; -fx-background-color: transparent;");

                    Image image = componentImageMap.get(String.valueOf(imageID));

                    if (image != null) {
                        ImageView imageView = new ImageView(image);
                        imageView.setFitWidth(buttonSize);
                        imageView.setFitHeight(buttonSize);
                        imageView.setPreserveRatio(true);
                        imageView.setSmooth(true);
                        imageView.setCache(true);
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

                    rotateButton.setDisable(false);
                    discardButton.setDisable(false);
                    reserveButton.setDisable(false);
                    pickComponent.setDisable(true);
                    setButton.setDisable(true);
                }
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
                if (lastDroppedButton == null) return;

                if (isPlaceholder(reserved0Button)) {
                    reserveComponentToButton(reserved0Button);
                } else if (isPlaceholder(reserved1Button)) {
                    reserveComponentToButton(reserved1Button);
                } else {
                    System.out.println("Entrambe le riserve sono piene.");
                }
            }
            else{
                Button button;
                if(imageButtonCorrespondence(reserved0Button, String.valueOf(imageID))){
                    button = reserved0Button;
                }
                else{
                    button = reserved1Button;
                }
                // Controlla se il bottone non è un placeholder e se non c'è già un componente pescato
                if (!isPlaceholder(button) && !componentPicked) {
                    try {
                        // Recupera l'ID del componente riservato
                        String componentId = reservedComponentIds.get(button);
                        if (componentId == null) return;

                        // Crea il nuovo bottone con l'immagine del componente riservato
                        Button newButton = new Button();
                        double buttonSize = 150;

                        newButton.setPrefSize(buttonSize, buttonSize);
                        newButton.setMinSize(buttonSize, buttonSize);
                        newButton.setMaxSize(buttonSize, buttonSize);
                        newButton.setStyle("-fx-padding: 0; -fx-background-color: transparent;");

                        // Copia l'immagine dal bottone riservato
                        if (button.getGraphic() instanceof ImageView reservedImageView) {
                            ImageView newImageView = new ImageView(reservedImageView.getImage());
                            newImageView.setFitWidth(buttonSize);
                            newImageView.setFitHeight(buttonSize);
                            newImageView.setPreserveRatio(true);
                            newImageView.setSmooth(true);
                            newImageView.setCache(true);
                            newButton.setGraphic(newImageView);
                        }

                        // Genera nuovo ID per il drag&drop
                        String btnId = UUID.randomUUID().toString();
                        newButton.setUserData(btnId);
                        draggableButtons.put(btnId, newButton);

                        // Setup drag&drop per il nuovo bottone
                        newButton.setOnDragDetected(event2 -> {
                            Dragboard db = newButton.startDragAndDrop(TransferMode.MOVE);
                            ClipboardContent content = new ClipboardContent();
                            content.putString(btnId);
                            db.setContent(content);
                            firstComponent = false;
                            componentPicked = true;
                            event2.consume();
                        });

                        // Mostra il nuovo componente nell'area hand
                        handComponentArea.getChildren().clear();
                        handComponentArea.getChildren().add(newButton);

                        // LOGICA STACK: gestisci lo spostamento dei componenti riservati
                        if (button == reserved0Button) {
                            // Rimuovi l'ID del componente che stiamo pescando
                            reservedComponentIds.remove(reserved0Button);

                            // Se c'è un componente in reserved1, spostalo in reserved0
                            if (!isPlaceholder(reserved1Button)) {
                                // Copia il contenuto di reserved1 in reserved0
                                if (reserved1Button.getGraphic() instanceof ImageView reserved1ImageView) {
                                    ImageView newImageView = new ImageView(reserved1ImageView.getImage());
                                    newImageView.setFitWidth(110);
                                    newImageView.setFitHeight(110);
                                    newImageView.setPreserveRatio(true);
                                    reserved0Button.setGraphic(newImageView);
                                    reserved0Button.setStyle("-fx-padding: 0; -fx-background-color: transparent;");
                                    reserved0Button.setPrefSize(110, 110);

                                    // Trasferisci anche l'ID del componente
                                    String reserved1Id = reservedComponentIds.get(reserved1Button);
                                    if (reserved1Id != null) {
                                        reservedComponentIds.put(reserved0Button, reserved1Id);
                                        reservedComponentIds.remove(reserved1Button);
                                    }
                                }

                                // reserved1 diventa placeholder
                                setReservedButtonPlaceholder(reserved1Button);

                            } else {
                                // Se non c'è nulla in reserved1, reserved0 diventa placeholder
                                setReservedButtonPlaceholder(reserved0Button);
                            }

                        } else if (button == reserved1Button) {
                            // Se clicchi su reserved1, solo reserved1 diventa placeholder
                            setReservedButtonPlaceholder(reserved1Button);
                            reservedComponentIds.remove(reserved1Button);
                        }

                        // Aggiorna il riferimento al componente corrente
                        lastDroppedButton = newButton;

                        // Aggiorna lo stato dei bottoni
                        rotateButton.setDisable(false);
                        discardButton.setDisable(false);
                        reserveButton.setDisable(false);
                        pickComponent.setDisable(true);
                        setButton.setDisable(true);

                    } catch (Exception e) {
                        showError(e.getMessage());
                    }
                }
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

    //notifies the view that the hourglass has been turned around
    @Override
    public void updateStartNewCycle() throws Exception{}

    //notifies the view that the hourglass has finished running
    @Override
    public void updateFinishedCycle() throws Exception{}

    //invoked when the game switches to the ship placement phase, which means that the players can only
    //place their ship on the flight board
    @Override
    public void updateShipPlacement() throws Exception{}

    //notifies the view that all the players have concluded the assembling phase, which means that the players
    //enter the ship control phase
    @Override
    public void updateShipControl() throws Exception{}

    //notifies the view that a component of a player's ship board has been destroyed
    @Override
    public void updateDestroyedComponent(String nickname, int x, int y) throws Exception{}

    //notifies the view about a change in the number of crew of a cabin
    @Override
    public void updateCrewChange(String nickname, int x, int y, int change) throws Exception{}

    //notifies the view that a player has initialized a battery container with batteries
    @Override
    public void updateBatteries(String nickname, int x, int y, int change) throws Exception{}

    //notifies the view about a change in the number of aliens of a cabin
    @Override
    public void updateAlienChange(String nickname, int x, int y, boolean isPurple, boolean added) throws Exception{}



}