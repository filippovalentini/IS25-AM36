package it.polimi.ingsw.galaxytrucker.ui.gui;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.galaxytrucker.model.enumerations.Color;
import it.polimi.ingsw.galaxytrucker.network.VirtualServer;
import it.polimi.ingsw.galaxytrucker.ui.gui.controllerInterfaces.GuiController;
import it.polimi.ingsw.galaxytrucker.ui.gui.controllerInterfaces.ShipBuildingController;
import it.polimi.ingsw.galaxytrucker.ui.view.View;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ShipBuildingControllerL2 implements ShipBuildingController {

    private VirtualServer server;
    private View view;

    private int gameID;
    private String playerNickname;
    private Color color;

    private int col;
    private int row;
    private boolean isComponentPlaced = false;
    private boolean firstComponent = true;
    private boolean componentPicked = false;

    private Button lastDroppedButton = null;
    private final Map<String, Button> draggableButtons = new HashMap<>();
    private final Map<Button, String> reservedComponentIds = new HashMap<>();
    private Map<String, Image> cardImageMap = new HashMap<>();
    private Map<String, Image> componentImageMap = new HashMap<>();

    @FXML private Pane handComponentArea;
    @FXML private TextField ipTextField;
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

        cardImageMap = loadImageMap("cards");
        componentImageMap = loadImageMap("components");

        showPlaceholderImage();
        setReservedButtonPlaceholders();
    }

    public void setServer(VirtualServer server) {
        this.server = server;
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
                    col = getColumnIndexFromX(event.getX());
                    row = getRowIndexFromY(event.getY());

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
                    myGridPane.add(draggedButton, col, row);
                    lastDroppedButton = draggedButton;

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
            if (lastDroppedButton != null) {
                lastDroppedButton.setOnDragDetected(null);
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
            if (isComponentPlaced || firstComponent) {
                Button newButton = new Button();
                double buttonSize = 150;

                newButton.setPrefSize(buttonSize, buttonSize);
                newButton.setMinSize(buttonSize, buttonSize);
                newButton.setMaxSize(buttonSize, buttonSize);
                newButton.setStyle("-fx-padding: 0; -fx-background-color: transparent;");

                Image image = null;
                String randomKey = null;

                if (!componentImageMap.isEmpty()) {
                    Object[] keys = componentImageMap.keySet().toArray();
                    randomKey = (String) keys[(int) (Math.random() * keys.length)];
                    image = componentImageMap.get(randomKey);
                }

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
        });
    }

    private void setupRotateButton() {
        rotateButton.setOnAction(event -> {
            if (lastDroppedButton != null && lastDroppedButton.getGraphic() instanceof ImageView) {
                ImageView imageView = (ImageView) lastDroppedButton.getGraphic();
                imageView.setRotate((imageView.getRotate() - 90) % 360);
            }
        });
    }

    private void setupDiscardButton() {
        discardButton.setOnAction(event -> {
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
        });
    }

    private void setupReserveButton() {
        reserveButton.setOnAction(event -> {
            if (lastDroppedButton == null) return;

            if (isPlaceholder(reserved0Button)) {
                reserveComponentToButton(reserved0Button);
            } else if (isPlaceholder(reserved1Button)) {
                reserveComponentToButton(reserved1Button);
            } else {
                System.out.println("Entrambe le riserve sono piene.");
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
        setupDragFromReservedButton(reserved0Button);
        setupDragFromReservedButton(reserved1Button);
    }

    private void setupDragFromReservedButton(Button button) {
        button.setOnDragDetected(event -> {
            if (!componentPicked && !isPlaceholder(button)) {
                Button temp = new Button();
                temp.setGraphic(button.getGraphic());
                String btnId = reservedComponentIds.get(button);
                if (btnId == null) return;
                temp.setUserData(btnId);
                draggableButtons.put(btnId, temp);

                temp.setOnDragDetected(null);

                Dragboard db = button.startDragAndDrop(TransferMode.MOVE);
                ClipboardContent content = new ClipboardContent();
                content.putString(btnId);
                db.setContent(content);
                lastDroppedButton = temp;
                event.consume();
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

                Stage stage = (Stage) flightBoardButton.getScene().getWindow();
                stage.setScene(new Scene(root, 1210, 740));
                stage.show();

            } catch (IOException e) {
                e.printStackTrace();
                System.err.println("Errore nel caricamento del FlightBoard: " + e.getMessage());
            }
        });
    }

    private Map<String, Image> loadImageMap(String imageType) {
        Map<String, Image> result = new HashMap<>();

        try (InputStream jsonStream = getClass().getResourceAsStream(
                "/it/polimi/ingsw/galaxytrucker/jsonImageMappings/" + imageType + ".json")) {

            if (jsonStream == null) {
                System.err.println(imageType + ".json non trovato!");
                return result;
            }

            ObjectMapper mapper = new ObjectMapper();
            Map<String, String> idToPath = mapper.readValue(jsonStream, Map.class);

            for (Map.Entry<String, String> entry : idToPath.entrySet()) {
                String id = entry.getKey();
                String fullPath = "/it/polimi/ingsw/galaxytrucker/images/" + imageType + "/" + entry.getValue();

                try (InputStream imageStream = getClass().getResourceAsStream(fullPath)) {
                    if (imageStream == null) {
                        System.err.println("Immagine mancante: " + fullPath);
                        continue;
                    }
                    result.put(id, new Image(imageStream));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
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


}