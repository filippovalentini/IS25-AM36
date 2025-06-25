package it.polimi.ingsw.galaxytrucker.ui.gui.otherControllers;

import it.polimi.ingsw.galaxytrucker.model.enumerations.Color;
import it.polimi.ingsw.galaxytrucker.network.VirtualServer;
import it.polimi.ingsw.galaxytrucker.ui.gui.GuiInterface;
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
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/** * Controller for the Shown Components screen in the Galaxy Trucker game.
 * This controller manages the display of components available to players,
 * allows selection of components, and handles user interactions.
 */
public class ShownComponentsController implements GuiController {
    private Stage controlledStage;

    @FXML private GridPane componentGrid;
    @FXML private Button pickButton;
    @FXML private Rectangle errorBackground;
    @FXML private Label errorLabel;
    @FXML private Rectangle gameStateBackground;
    @FXML private Label gameStateLabel;
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
    /**
     * Initializes the controller, setting up the component grid and loading images.
     * Also sets up the pick button and back button actions.
     */
    @FXML
    public void initialize() {
        setupPickButton(); // Set up the pick button to handle component selection
        setUpBackButton(); // Set up the back button to return to the previous screen
        componentImageMap = GuiInterface.getInstance().loadImageMap("components"); // Load images for components from the resources

        List<Integer> currentShownComponents = GuiInterface.getInstance().getView().getShownComponents(); // Get the list of currently shown components from the game view
        for(Integer id : currentShownComponents){ // For each component ID in the list
            updateShownComponents(id, true); // Update the displayed components in the grid
        }

        showGameState(GuiInterface.getInstance().getView().getGameState()); // Display the current game state in the label

        componentGrid.setMinWidth(COLUMNS * (CELL_WIDTH + 10)); // Set the minimum width of the component grid based on the number of columns and cell width
        componentGrid.setPrefWidth(componentGrid.getMinWidth()); // Set the preferred width of the component grid to the minimum width
    }
    /**
     * Displays the current game state in the gameStateLabel.
     * @param message The message to display in the game state label.
     */
public void showGameState(String message){ // Display the current game state in the gameStateLabel
        gameStateLabel.setText(message); // Set the text of the game state label to the provided message
    }
    /**
     * Displays an error message in the errorLabel and errorBackground.
     * The error message fades in, stays visible for 3 seconds, and then fades out.
     * @param message The error message to display.
     */
    public void showError(String message) {
        Platform.runLater(() -> { // Run the error display logic on the JavaFX application thread
            errorLabel.setText(message); // Set the text of the error label to the provided message
            errorLabel.setVisible(true); // Make the error label visible
            errorBackground.setVisible(true); // Make the error background rectangle visible

            // Fade in
            FadeTransition fadeInLabel = new FadeTransition(Duration.millis(300), errorLabel);
            fadeInLabel.setFromValue(0.0);
            fadeInLabel.setToValue(1.0);

            FadeTransition fadeInRect = new FadeTransition(Duration.millis(300), errorBackground);
            fadeInRect.setFromValue(0.0);
            fadeInRect.setToValue(1.0);

            fadeInLabel.play();
            fadeInRect.play();

            // Wait 3 seconds, then fade out
            fadeInLabel.setOnFinished(event -> {
                PauseTransition wait = new PauseTransition(Duration.seconds(3));
                wait.setOnFinished(e -> {
                    FadeTransition fadeOutLabel = new FadeTransition(Duration.millis(600), errorLabel);
                    fadeOutLabel.setFromValue(1.0);
                    fadeOutLabel.setToValue(0.0);

                    FadeTransition fadeOutRect = new FadeTransition(Duration.millis(600), errorBackground);
                    fadeOutRect.setFromValue(1.0);
                    fadeOutRect.setToValue(0.0);

                    fadeOutLabel.setOnFinished(ev -> {
                        errorLabel.setVisible(false);
                        errorBackground.setVisible(false);
                    });

                    fadeOutLabel.play();
                    fadeOutRect.play();
                });
                wait.play();
            });
        });
    }
    /**
     * Creates an ImageView for a given Image, sets its size, and adds a click event handler.
     * The click event handler highlights the selected image and enables the pick button.
     * @param image The Image to be displayed in the ImageView.
     * @return The created ImageView with the specified image.
     */
    private ImageView createImageView(Image image) {
        ImageView imageView = new ImageView(image); // Create a new ImageView with the provided image
        imageView.setFitWidth(CELL_WIDTH); // Set the width of the ImageView to the specified cell width
        imageView.setFitHeight(CELL_HEIGHT); // Set the height of the ImageView to the specified cell height
        imageView.setPreserveRatio(true); // Preserve the aspect ratio of the image
        imageView.setSmooth(true); // Enable smooth scaling of the image

        imageView.setOnMouseClicked((MouseEvent event) -> { // Set up a click event handler for the ImageView
            if (selectedImageView != null) {
                selectedImageView.setEffect(null);
            }
            selectedImageView = imageView; // Set the clicked ImageView as the selected one
            Glow glow = new Glow(); // Create a new Glow effect to highlight the selected image
            glow.setLevel(1); // Set the glow level to maximum
            selectedImageView.setEffect(glow); // Apply the glow effect to the selected ImageView
            selectedImageView.setOpacity(1); // Ensure the selected image is fully opaque
            pickButton.setDisable(false); // Enable the pick button when an image is selected
        });

        return imageView; // Return the created ImageView
    }
    /**
     * Updates the displayed components in the component grid based on the provided image ID and release status.
     * If released is true, it adds the component to the grid; if false, it removes the component from the grid.
     * @param imageID The ID of the component image to be updated.
     * @param released Indicates whether the component is being released (added) or not (removed).
     */
    public void updateShownComponents(int imageID, boolean released) {
        Platform.runLater(() -> { // Run the update logic on the JavaFX application thread
            Image componentImage = componentImageMap.get(String.valueOf(imageID)); // Get the Image corresponding to the provided image ID from the componentImageMap
            if (released) { // If the component is being released (added to the grid)

                int count = componentGrid.getChildren().size(); // Get the current number of children in the component grid
                int col = count % COLUMNS; // Calculate the column index for the new component based on the current count
                int row = count / COLUMNS; // Calculate the row index for the new component based on the current count

                ImageView imageView = createImageView(componentImage); // Create an ImageView for the component image
                componentGrid.add(imageView, col, row); // Add the ImageView to the component grid at the calculated column and row indices
            }
            else{ // If the component is being removed from the grid
                if (componentImage == null) return; // If the component image is null, do nothing

                ImageView toRemove = null; // Initialize a variable to hold the ImageView to be removed
                for (javafx.scene.Node node : componentGrid.getChildren()) { // Iterate through the children of the component grid
                    if (node instanceof ImageView imageView) { // Check if the node is an ImageView
                        if (imageView.getImage().equals(componentImage)) { // If the ImageView's image matches the component image
                            toRemove = imageView; // Set the toRemove variable to this ImageView
                            break; // Exit the loop as we found the ImageView to remove
                        }
                    }
                }

                if (toRemove == null) return; // If no ImageView was found to remove, do nothing
                componentGrid.getChildren().remove(toRemove); // Remove the found ImageView from the component grid

                var remaining = new java.util.ArrayList<javafx.scene.Node>(componentGrid.getChildren()); // Create a new list containing the remaining children of the component grid
                componentGrid.getChildren().clear(); // Clear the component grid to re-add the remaining components

                for (int i = 0; i < remaining.size(); i++) { // Iterate through the remaining components
                    int col = i % COLUMNS;
                    int row = i / COLUMNS;
                    componentGrid.add(remaining.get(i), col, row);
                }
            }
            clearSelection();
        });
    }
    /**
     * Notifies the controller about an error, displaying it in the error label.
     * This method is called by the server to inform the client about errors.
     * @param message The error message to display.
     */
    public void notifyError(String message) { // Notifies the controller about an error, displaying it in the error label
        Platform.runLater(() -> {
            showError(message); // Show the error message in the error label
        });
    }
    /**
     * Sets up the pick button to handle component selection.
     * When clicked, it retrieves the selected component index and sends a pick request to the server.
     */
    @FXML
    public void setupPickButton() {
        pickButton.setOnAction(event -> { // Set up the pick button to handle component selection
            try{
                int position = getSelectedComponentIndex(); // Get the index of the selected component
                server.pickShown(this.gameId, this.playerNickname, position); // Send a pick request to the server with the game ID, player nickname, and selected component index
            }
            catch(Exception e){ // Catch any exceptions that occur during the pick request
                showError(e.getMessage()); // Show the error message in the error label
            }
        });
    }
    /**
     * Sets up the back button to return to the previous screen.
     * It loads the appropriate ShipBuildingController based on the game type (first flight or second flight).
     */
    @FXML
    public void setUpBackButton() {
        backButton.setOnAction(event -> { // Set up the back button to handle returning to the previous screen
            try {  // Try to load the appropriate ShipBuildingController based on the game type
                FXMLLoader fxmlLoader;
                if(this.firstFlight){
                    fxmlLoader = new FXMLLoader(getClass().getResource("/it/polimi/ingsw/galaxytrucker/fxml/mainScreens/shipBuildingL1.fxml"));
                }
                else{ // If it's the second flight, load the second level ship building screen
                    fxmlLoader = new FXMLLoader(getClass().getResource("/it/polimi/ingsw/galaxytrucker/fxml/mainScreens/shipBuildingL2.fxml"));
                }
                Parent root = fxmlLoader.load(); // Load the FXML file for the ship building screen
                ShipBuildingController controller = fxmlLoader.getController(); // Get the controller for the ship building screen
                controller.setServer(this.server); // Set the server for the controller
                controller.setPlayerInfo(this.gameId, this.playerNickname, this.color); // Set the player information for the controller
                GuiInterface.getInstance().setShipBuildingController(controller); // Set the ship building controller in the GUI interface

                controller.setControlledStage(controlledStage); // Set the controlled stage for the controller
                controlledStage.setScene(new Scene(root, 1210, 740)); // Set the scene for the controlled stage with the loaded root
                controlledStage.show(); // Show the controlled stage with the new scene

            } catch (IOException e) { // Catch any IO exceptions that occur during the loading of the FXML file
                showError(e.getMessage());
            }
        });
    }
    /**
     * Gets the index of the currently selected component in the component grid.
     * If no component is selected, it returns null.
     * @return The index of the selected component, or null if no component is selected.
     */
    public Integer getSelectedComponentIndex() { // Gets the index of the currently selected component in the component grid
        if (selectedImageView == null) return null; // If no component is selected, return null

        var children = componentGrid.getChildren(); // Get the list of children (ImageViews) in the component grid
        for (int i = 0; i < children.size(); i++) { // Iterate through the children of the component grid
            if (children.get(i) == selectedImageView) { // If the current child is the selected ImageView
                return i;
            }
        }

        return null;
    }
    /**
     * Clears the selection of the currently selected component.
     * If a component is selected, it removes the glow effect and disables the pick button.
     */
    public void clearSelection() {
        if (selectedImageView != null) { // If a component is currently selected
            selectedImageView.setEffect(null); // Remove the glow effect from the selected ImageView
            selectedImageView = null; // Set the selectedImageView to null to indicate no selection
            pickButton.setDisable(true); // Disable the pick button since no component is selected
        }
    }
    /**
     * Sets the game type (first flight or second flight) for the controller.
     * This determines which ship building screen to load when the back button is pressed.
     * @param firstFlight True if it's the first flight, false if it's the second flight.
     */
    public void setGameType(boolean firstFlight){
        this.firstFlight = firstFlight;
    }
    /**
     * Sets the controlled stage for the controller.
     * This is used to change the scene when navigating back to the ship building screen.
     * @param stage The Stage to be controlled by this controller.
     */
    @Override
    public void setControlledStage(Stage stage) {
        controlledStage = stage;
    }
    /**
     * Sets the server for the controller.
     * This is used to send requests to the server when a component is picked.
     * @param server The VirtualServer instance to be used by this controller.
     */
    @Override
    public void setServer(VirtualServer server) {
        this.server = server;
    }
    /**
     * Sets the player information for the controller, including game ID, player nickname, and color.
     * This is used to identify the player when sending requests to the server.
     * @param gameID The ID of the game.
     * @param playerNickname The nickname of the player.
     * @param color The color associated with the player.
     */
    @Override
    public void setPlayerInfo(int gameID, String playerNickname, Color color) {
        this.gameId = gameID;
        this.playerNickname = playerNickname;
        this.color = color;
    }

    //notifies the view about a change in the game phase
    /**
     * Notifies the controller about a change in the game phase.
     * This method is called by the server to inform the client about the current game phase.
     * @param gamePhase The current game phase to display.
     */
    @Override
    public void notifyGamePhase(String gamePhase) {
        Platform.runLater(() -> {
            showGameState(gamePhase);
        });
    }

}