package it.polimi.ingsw.galaxytrucker.ui.gui;

import it.polimi.ingsw.galaxytrucker.model.enumerations.Color;
import it.polimi.ingsw.galaxytrucker.model.enumerations.Orientation;
import it.polimi.ingsw.galaxytrucker.network.VirtualServer;
import it.polimi.ingsw.galaxytrucker.ui.view.View;
import it.polimi.ingsw.galaxytrucker.ui.view.ViewPlayer;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
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
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ShipBuildingControllerL1 implements GuiController {
    private VirtualServer server;
    private View view;
    int gameID;
    private String playerNickname;
    Color color;
    int col;
    int row;
    int currentComponentImageID;
    //fare una mappa o qualcosa per identificare le shipboard, guarda la CliInterface e View


    @FXML
    private Pane handComponentArea;

    @FXML
    private TextField ipTextField;

    @FXML
    private Button handComponentButton;

    @FXML
    private GridPane myGridPane;

    @FXML
    private Button setButton;

    @FXML
    private Button flightBoardButton;

    @FXML
    private Button pickComponent;

    @FXML
    private Button reserveButton;

    @FXML
    private Button rotateButton;
    @FXML
    private Button viewShownComponentButton;
    @FXML
    private Button player1ShipButton;
    @FXML
    private Button player2ShipButton;
    @FXML
    private Button player3ShipButton;
    @FXML
    private Button mineShipButton;
    @FXML
    private Button discardButton;


    private Button lastDroppedButton = null;
    private Boolean isComponentPlaced = false;
    private Boolean firstComponent = true;
    Map<String, Button> draggableButtons = new HashMap<>();


    @FXML
    public void initialize() {


        // Registra questo controller nell'interfaccia GUI
        // GuiInterface.getInstance().setShipBuildingController(this);

        setupGridPaneDragOver();
        setupGridPaneDragDropped();
        setupSetButton();
        setupPickComponentButton();
        setupDiscardButton();
        setupflightBoardButton();

        //metodo per mettere le immagini sul bottone
        setupButtonImages();

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
                    Pane parent = (Pane) draggedButton.getParent();
                    parent.getChildren().remove(draggedButton);

                    double x = event.getX();
                    double y = event.getY();
                    col = getColumnIndexFromX(x);
                    row = getRowIndexFromY(y);

                    myGridPane.add(draggedButton, col, row);

                    lastDroppedButton = draggedButton;
                    success = true;
                }
            }

            event.setDropCompleted(success);
            event.consume();
        });
    }

    private void setupSetButton() {
        setButton.setOnAction(event -> {
           /* try {
                server.assembledComponent(gameID,playerNickname,row,col);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }*/

            //togliere,solo per prova
            if (lastDroppedButton != null) {
                lastDroppedButton.setOnDragDetected(null);
                isComponentPlaced = true;
            }
        });
    }

    private void setupPickComponentButton() {
        pickComponent.setOnAction(event -> {
            if (isComponentPlaced || firstComponent) {
               /* try {
                    server.pickHidden(gameID,playerNickname);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }*/
                Button newButton = new Button("handComponent");
                newButton.setPrefSize(handComponentArea.getPrefWidth(), handComponentArea.getPrefHeight());

                String btnId = UUID.randomUUID().toString();
                newButton.setUserData(btnId);
                draggableButtons.put(btnId, newButton);

                newButton.setOnDragDetected(event2 -> {
                    Dragboard db = newButton.startDragAndDrop(TransferMode.MOVE);
                    ClipboardContent content = new ClipboardContent();
                    content.putString(btnId);
                    firstComponent = false;
                    db.setContent(content);
                    event2.consume();
                });

                handComponentArea.getChildren().clear();
                handComponentArea.getChildren().add(newButton);
            }
        });
    }

    private void setupRotateButton() {
        rotateButton.setOnAction(event -> {
            if (lastDroppedButton != null) {
                /*try {
                    server.rotatePickedComponent(gameID, playerNickname);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }*/
                //rotateCurrentOrientation();
                //updateComponentRotation();
            }
        });
    }

    private void setupDiscardButton() {
        discardButton.setOnAction(event -> {
            if (lastDroppedButton != null) {
               /* try {
                    server.putShown(gameID, playerNickname);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }*/
                myGridPane.getChildren().remove(lastDroppedButton);
            }
        });
    }



    /*private void enableDrag(Button button) {
        button.setOnDragDetected(event -> {
            Dragboard db = button.startDragAndDrop(TransferMode.MOVE);
            ClipboardContent content = new ClipboardContent();
            content.putString("handComponent");
            db.setContent(content);
            event.consume();
        });
    }*/

    // Metodo per calcolare la colonna da coordinata x
    private int getColumnIndexFromX(double x) {
        double widthSoFar = 0;
        for (int i = 0; i < myGridPane.getColumnConstraints().size(); i++) {
            widthSoFar += myGridPane.getColumnConstraints().get(i).getPrefWidth();
            if (x < widthSoFar) {
                return i;
            }
        }
        // Se oltre la somma delle colonne, metti nell'ultima
        return myGridPane.getColumnConstraints().size() - 1;
    }

    // Metodo per calcolare la riga da coordinata y
    private int getRowIndexFromY(double y) {
        double heightSoFar = 0;
        for (int i = 0; i < myGridPane.getRowConstraints().size(); i++) {
            heightSoFar += myGridPane.getRowConstraints().get(i).getPrefHeight();
            if (y < heightSoFar) {
                return i;
            }
        }
        return myGridPane.getRowConstraints().size() - 1;
    }

    //putShown() per rilasciare i componente, mettilo su discard

    @Override
    public void notifyError(String errorMessage) throws Exception {

    }

  /*  public void updateAssembledComponentGUI(String nickname, int imageID, Orientation orientation, int x, int y){
        if (lastDroppedButton != null) {
            lastDroppedButton.setOnDragDetected(null);
            isComponentPlaced = true;
        }
    }


    public void updatePickedComponentGUI(int imageID, boolean released) {
       //devo prendere l immagine
    }

    private void rotateCurrentOrientation() {
        switch (currentOrientation) {
            case Orientation.NORTH:
                currentOrientation = Orientation.EAST;
                break;
            case Orientation.EAST:
                currentOrientation = Orientation.SOUTH;
                break;
            case Orientation.SOUTH:
                currentOrientation = Orientation.WEST;
                break;
            case Orientation.WEST:
                currentOrientation = Orientation.NORTH;
                break;
        }
    }

    private void updateComponentRotation() {
        if (currentHandComponent != null && currentHandComponent.getGraphic() instanceof ImageView) {
            ImageView imageView = (ImageView) currentHandComponent.getGraphic();
            double rotation = 0;

            switch (currentOrientation) {
                case NORTH:
                    rotation = 0;
                    break;
                case EAST:
                    rotation = 90;
                    break;
                case SOUTH:
                    rotation = 180;
                    break;
                case WEST:
                    rotation = 270;
                    break;
            }

            imageView.setRotate(rotation);
        }
    }*/


    public void setupflightBoardButton() {
        flightBoardButton.setOnAction(event -> {
            try {
                // Carica la nuova schermata
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/it/polimi/ingsw/galaxytrucker/flightBoardL1.fxml"));
                Parent root = fxmlLoader.load();

                // Ora FlightBoardControllerL1 ha il metodo setServer()
                FlightBoardControllerL1 controller = fxmlLoader.getController();
                controller.setServer(this.server);

                // Ottieni lo stage corrente dal bottone
                Stage stage = (Stage) flightBoardButton.getScene().getWindow();

                // Imposta la nuova scena
                Scene scene = new Scene(root, 1210, 740);
                stage.setScene(scene);
                stage.show();

            } catch (IOException e) {
                e.printStackTrace();
                System.err.println("Errore nel caricamento del FlightBoard: " + e.getMessage());
            }
        });
    }

    private void setupButtonImages() {
        try {
            //Bottone HandComponent
            setupButtonWithImage(handComponentButton, "/it/polimi/ingsw/galaxytrucker/images/tiles/back.jpg", "handComponent", 150, 150);

            /*// Bottone Set - icona di conferma
            setupButtonWithImage(setButton, "/icons/confirm.png", "Conferma", 16, 40);


            // Bottone Pick Component - icona di presa
            setupButtonWithImage(pickComponent, "images/tiles/back.jpg", "Prendi", 16, 16);

            // Bottone Rotate - icona di rotazione
            setupButtonWithImage(rotateButton, "/icons/rotate.png", "Ruota", 16, 16);

            // Bottone Discard - icona cestino
            setupButtonWithImage(discardButton, "/icons/trash.png", "Scarta", 16, 16);

            // Bottone Flight Board - icona navicella/volo
            setupButtonWithImage(flightBoardButton, "/icons/flight.png", "Volo", 16, 16);

            // Bottone Reserve - icona riserva
            setupButtonWithImage(reserveButton, "/icons/reserve.png", "Riserva", 16, 16);

            // Bottone View Component - icona occhio
            setupButtonWithImage(viewShownComponentButton, "/icons/view.png", "Visualizza", 16, 16);

            // Bottoni player - icone giocatori con colori diversi
            setupButtonWithImage(player1ShipButton, "/icons/player1.png", "Player 1", 20, 20);
            setupButtonWithImage(player2ShipButton, "/icons/player2.png", "Player 2", 20, 20);
            setupButtonWithImage(player3ShipButton, "/icons/player3.png", "Player 3", 20, 20);
            setupButtonWithImage(mineShipButton, "/icons/myship.png", "La Mia Nave", 20, 20);
            */

        } catch (Exception e) {
            System.err.println("Errore nel caricamento delle immagini: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void setupButtonWithImage(Button button, String imagePath, String text, float width, float height) {
        try {
            // Carica l'immagine
            Image image = new Image(getClass().getResourceAsStream(imagePath));
            ImageView imageView = new ImageView(image);

            // Imposta dimensioni
            imageView.fitWidthProperty().bind(button.widthProperty());
            imageView.fitHeightProperty().bind(button.heightProperty());
            //imageView.setFitWidth(width);
            //imageView.setFitHeight(height);
            imageView.setPreserveRatio(false);
            imageView.setSmooth(true);

            // Aggiungi immagine al bottone
            button.setGraphic(imageView);
            button.setText("");

            // Posiziona immagine sopra il testo (puoi cambiare la posizione)
            button.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);

        } catch (Exception e) {
            System.err.println("Impossibile caricare l'immagine: " + imagePath);
            // Mantieni solo il testo se l'immagine non è disponibile
            button.setText(text);
        }
    }

}