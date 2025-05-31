package it.polimi.ingsw.galaxytrucker.ui.gui;

import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class ShipBuildingControllerL2 {

    @FXML private Button player1ShipButton;
    @FXML private Button player2ShipButton;
    @FXML private Button player3ShipButton;
    @FXML private Button pickComponent;
    @FXML private Button setButton;
    @FXML private Button discardButton;
    @FXML private Button rotateButton;
    @FXML private Button setButton1;
    @FXML private Button flightBoardButton;
    @FXML private Button viewShownComponentButton;
    @FXML private Button handComponentButton;
    @FXML private Label messageBox1;
    @FXML private Label messageBox2;
    @FXML private GridPane myGridPane;
    @FXML private GridPane reservedGrid;

    private Map<String, Image> cardImageMap = new HashMap<>();
    private Map<String, Image> componentImageMap = new HashMap<>();

    @FXML
    public void initialize() {
        cardImageMap = loadImageMap("cards");
        componentImageMap = loadImageMap("components");
        setPickedComponent(componentImageMap.get("3"), 0);
    }

    private Map<String, Image> loadImageMap(String imageType) {
        Map<String, Image> result = new HashMap<>();

        try {
            // Carica lo stream del JSON
            InputStream jsonStream = getClass().getResourceAsStream("/it/polimi/ingsw/galaxytrucker/jsonImageMappings/" + imageType + ".json");

            if (jsonStream == null) {
                System.err.println("components.json non trovato!");
                return result;
            }

            // Parse JSON come Map<String, String>
            ObjectMapper mapper = new ObjectMapper();
            Map<String, String> idToPath = mapper.readValue(jsonStream, Map.class);

            for (Map.Entry<String, String> entry : idToPath.entrySet()) {
                String id = entry.getKey();
                String relativePath = entry.getValue(); // es. "tiles/tile42.png"
                String fullPath = "/it/polimi/ingsw/galaxytrucker/images/" + imageType + "/" + relativePath;

                InputStream imageStream = getClass().getResourceAsStream(fullPath);
                if (imageStream == null) {
                    System.err.println("Immagine mancante: " + fullPath);
                    continue;
                }

                Image image = new Image(imageStream);
                result.put(id, image);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }


    public Image getCardImage(String id) {
        return cardImageMap != null ? cardImageMap.get(id) : null;
    }

    public Image getComponentImage(String id) {
        return componentImageMap != null ? componentImageMap.get(id) : null;
    }

    public void setPickedComponent(Image componentImage, int rotationAngle) {
        if (componentImage != null) {
            ImageView imageView = new ImageView(componentImage);
            imageView.setFitWidth(150); // per mantenere dentro al bottone
            imageView.setFitHeight(150);
            imageView.setPreserveRatio(true);
            imageView.setRotate(rotationAngle);
            handComponentButton.setGraphic(imageView);
            handComponentButton.setText(""); // rimuove testo
        } else {
            messageBox1.setText("Componente con ID 907 non trovato.");
        }
    }

    @FXML
    private void handlePlayer1ShipButton(ActionEvent event) {
        // Handle action
    }

    @FXML
    private void handlePlayer2ShipButton(ActionEvent event) {
        // Handle action
    }

    @FXML
    private void handlePlayer3ShipButton(ActionEvent event) {
        // Handle action
    }

    @FXML
    private void handlePickComponent(ActionEvent event) {
        Image componentImage = componentImageMap.get("907");
        setPickedComponent(componentImage, 0);
    }

    @FXML
    private void handleSetButton(ActionEvent event) {
        // Handle action
    }

    @FXML
    private void handleDiscardButton(ActionEvent event) {
        // Handle action
    }

    @FXML
    private void handleRotateButton(ActionEvent event) {
        ImageView imageView = (ImageView) handComponentButton.getGraphic();
        if (imageView != null) {
            int rotationAngle = (int) imageView.getRotate();
            rotationAngle = (rotationAngle - 90) % 360;
            imageView.setRotate(rotationAngle);
            handComponentButton.setGraphic(imageView);
            handComponentButton.setText("");
        }
    }

    @FXML
    private void handleSetButton1(ActionEvent event) {
        // Handle action
    }

    @FXML
    private void handleFlightBoardButton(ActionEvent event) {
        // Handle action
    }

    @FXML
    private void handleViewShownComponentButton(ActionEvent event) {
        // Handle action
    }

    @FXML
    private void handleHandComponentButton(ActionEvent event) {
        // Handle action
    }
}
