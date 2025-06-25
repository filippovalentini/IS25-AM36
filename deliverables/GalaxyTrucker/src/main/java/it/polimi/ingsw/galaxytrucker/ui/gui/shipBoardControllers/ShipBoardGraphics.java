package it.polimi.ingsw.galaxytrucker.ui.gui.shipBoardControllers;

import it.polimi.ingsw.galaxytrucker.model.enumerations.Color;
import it.polimi.ingsw.galaxytrucker.model.enumerations.Orientation;
import it.polimi.ingsw.galaxytrucker.ui.view.ViewComponent;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import java.util.List;



/**
 * This class contains all the methods needed to show a specific component of a ship board, including batteries, crew members, aliens, goods and providing the correct size and orientation of the component.
 */
public abstract class ShipBoardGraphics {
    @FXML protected GridPane myGridPane;

    //sets an image in a specific position of the grid pane and with a specific orientation
    /**
     * Sets an image on the grid pane at the specified column and row with the given orientation.
     *
     * @param imageID      The ID of the image to be set.
     * @param orientation  The orientation of the image (e.g., HORIZONTAL, VERTICAL).
     * @param column       The column index in the grid pane.
     * @param row          The row index in the grid pane.
     */
    public abstract void setImageOnGrid(String imageID, Orientation orientation, int column, int row);

    //sets all the graphic information regarding a component in a specific position of the grid pane
    /**
     * Sets a component on the grid pane at the specified row and column.
     * This method updates the grid with the component's image, orientation, and any additional attributes like batteries, crew members, aliens, or goods.
     *
     * @param component The ViewComponent to be set on the grid.
     * @param row       The row index in the grid pane.
     * @param column    The column index in the grid pane.
     */
    public void setComponentOnGrid(ViewComponent component, int row, int column){
        if(component != null){
            setImageOnGrid(component.getImageID(), component.getOrientation(), column, row);
            if(component.getBatteries() > 0){
                addBatteries(row,column, component.getBatteries());
            }else if(component.getCrew()>0){
                addCrewMembers(row,column, component.getCrew());
            }else if(component.isPurpleAlien()){
                addAlien(row,column,true);
            }else if(component.isBrownAlien()){
                addAlien(row,column,false);
            }else if(component.getNumberGoods() > 0){
                addGoods(row,column,component.getGoods());
            }
        }
    }

    //removes all the graphic information regarding a component from a specific position of the grid pane
    /**
     * Removes a component from the grid pane at the specified row and column.
     * This method searches for the component in the grid and removes it, including any associated images or overlays.
     *
     * @param row    The row index in the grid pane.
     * @param column The column index in the grid pane.
     */
    public void removeComponentFromGrid(int row, int column){
        for (Node node : myGridPane.getChildren()) {
            Integer colIndex = GridPane.getColumnIndex(node);
            Integer rowIndex = GridPane.getRowIndex(node);
            if (colIndex == null) colIndex = 0;
            if (rowIndex == null) rowIndex = 0;

            if (colIndex == column && rowIndex == row) {
                myGridPane.getChildren().remove(node);
                break;
            }
        }
    }

    //attaches battery images to a component on the grid pane

    /**
     * Adds battery images to a specific cell in the grid pane.
     * @param row
     * @param column
     * @param batteries
     */
    public void addBatteries(int row, int column, int batteries) {
        Platform.runLater(() -> {
            for (Node node : myGridPane.getChildren()) {
                Integer col = GridPane.getColumnIndex(node);
                Integer rw = GridPane.getRowIndex(node);
                if (col == null) col = 0;
                if (rw == null) rw = 0;

                if (col == column && rw == row && node instanceof StackPane cell) {
                    for (Node child : cell.getChildren()) {
                        if (child instanceof GridPane overlay && overlay.getId() != null &&
                                overlay.getId().equals("overlay-" + column + "-" + row)) {

                            overlay.add(getBatteryImageView(overlay), 0, 0);
                            if(batteries > 1){
                                overlay.add(getBatteryImageView(overlay), 1, 0);
                            }
                            if(batteries == 3){
                                overlay.add(getBatteryImageView(overlay), 0, 1);
                            }

                            return;
                        }
                    }
                }
            }
        });
    }

    //returns the image of a battery

    /**
     * Returns an ImageView containing the battery image, scaled to fit the specified overlay.
     * @param overlay
     * @return
     */
    public ImageView getBatteryImageView(GridPane overlay){
        Image battery = new Image(getClass().getResource("/it/polimi/ingsw/galaxytrucker/images/pieces/battery.png").toExternalForm());

        ImageView batteryImageView = new ImageView(battery); // Load the battery image
        batteryImageView.setFitWidth(overlay.getPrefWidth() / 2); // Set the width to half of the overlay's width
        batteryImageView.setFitHeight(overlay.getPrefHeight() / 2); // Set the height to half of the overlay's height
        batteryImageView.setPreserveRatio(true); // Preserve the aspect ratio of the image
        batteryImageView.setId("crew"); // Set an ID for the ImageView, if needed

        return batteryImageView; // Return the ImageView containing the battery image
    }

    //attaches crew member images to a component on the grid pane

    /**
     * Adds crew member images to a specific cell in the grid pane.
     * @param row
     * @param column
     * @param crew
     */
    public void addCrewMembers(int row, int column, int crew) {
        Platform.runLater(() -> { // This method runs on the JavaFX Application Thread
            for (Node node : myGridPane.getChildren()) { // Iterate through all nodes in the GridPane
                Integer col = GridPane.getColumnIndex(node); // Get the column index of the node
                Integer rw = GridPane.getRowIndex(node);     // Get the row index of the node
                if (col == null) col = 0;
                if (rw == null) rw = 0;

                if (col == column && rw == row && node instanceof StackPane cell) { // Check if the node is in the specified cell
                    for (Node child : cell.getChildren()) { // Iterate through the children of the StackPane
                        if (child instanceof GridPane overlay && overlay.getId() != null &&
                                overlay.getId().equals("overlay-" + column + "-" + row)) { // Check if the child is a GridPane with the correct overlay ID

                            overlay.add(getCrewMemberImageView(overlay), 0, 0);
                            if(crew > 1){
                                overlay.add(getCrewMemberImageView(overlay), 1, 0);
                            }

                            return;
                        }
                    }
                }
            }
        });
    }

    //returns the image of a crew member

    /**
     * Returns an ImageView containing the crew member image, scaled to fit the specified overlay.
     * @param overlay
     * @return
     */
    public ImageView getCrewMemberImageView(GridPane overlay){
        Image crewMember = new Image(getClass().getResource("/it/polimi/ingsw/galaxytrucker/images/pieces/crewMember.png").toExternalForm());

        ImageView crewMemberImageView = new ImageView(crewMember); // Load the crew member image
        crewMemberImageView.setFitWidth(overlay.getPrefWidth() / 2); // Set the width to half of the overlay's width
        crewMemberImageView.setFitHeight(overlay.getPrefHeight() / 2); // Set the height to half of the overlay's height
        crewMemberImageView.setPreserveRatio(true); // Preserve the aspect ratio of the image
        crewMemberImageView.setId("crew"); // Set an ID for the ImageView, if needed

        return crewMemberImageView;
    }

    //attaches images of cargo goods to a component on the grid pane

    /**
     * Adds cargo goods images to a specific cell in the grid pane.
     * @param row
     * @param column
     * @param goods
     */
    public void addGoods(int row, int column, List<Color> goods) {
        Platform.runLater(() -> { // This method runs on the JavaFX Application Thread
            for (Node node : myGridPane.getChildren()) { // Iterate through all nodes in the GridPane
                Integer col = GridPane.getColumnIndex(node); // Get the column index of the node
                Integer rw = GridPane.getRowIndex(node); // Get the row index of the node
                if (col == null) col = 0;
                if (rw == null) rw = 0;

                if (col == column && rw == row && node instanceof StackPane cell) { // Check if the node is in the specified cell
                    for (Node child : cell.getChildren()) { // Iterate through the children of the StackPane
                        if (child instanceof GridPane overlay && overlay.getId() != null &&
                                overlay.getId().equals("overlay-" + column + "-" + row)) { // Check if the child is a GridPane with the correct overlay ID

                            int numberGoods = goods.size(); // Get the number of goods to display

                            overlay.add(getCargoGoodImageView(overlay, goods.getFirst()), 0, 0);
                            if(numberGoods > 1){
                                overlay.add(getCargoGoodImageView(overlay, goods.get(1)), 1, 0);
                            }
                            if(numberGoods == 3){
                                overlay.add(getCargoGoodImageView(overlay, goods.get(2)), 0, 1);
                            }

                            return;
                        }
                    }
                }
            }
        });
    }

    //returns the image of a specific cargo good

    /**
     * Returns an ImageView containing the cargo good image based on its color, scaled to fit the specified overlay.
     * @param overlay
     * @param goodColor
     * @return
     */
    public ImageView getCargoGoodImageView(GridPane overlay, Color goodColor){
        Image goodImage;
        if (goodColor == Color.GREEN) {
            goodImage = new Image(getClass().getResource("/it/polimi/ingsw/galaxytrucker/images/pieces/green_good.png").toExternalForm());
        }
        else if (goodColor == Color.YELLOW) {
            goodImage = new Image(getClass().getResource("/it/polimi/ingsw/galaxytrucker/images/pieces/yellow_good.png").toExternalForm());
        }else if (goodColor == Color.RED) {
            goodImage = new Image(getClass().getResource("/it/polimi/ingsw/galaxytrucker/images/pieces/red_good.png").toExternalForm());
        }else {
            goodImage = new Image(getClass().getResource("/it/polimi/ingsw/galaxytrucker/images/pieces/blue_good.png").toExternalForm());
        }

        ImageView goodImageView = new ImageView(goodImage);
        goodImageView.setFitWidth(overlay.getPrefWidth() / 2);
        goodImageView.setFitHeight(overlay.getPrefHeight() / 2);
        goodImageView.setPreserveRatio(true);
        goodImageView.setId("crew");

        return goodImageView;
    }

    //attaches the image of an alien to a component on the grid pane

    /**
     * Adds an alien image to a specific cell in the grid pane.
     * @param row
     * @param column
     * @param isPurple
     */
    public void addAlien(int row, int column, boolean isPurple) {
        Platform.runLater(() -> {
            for (Node node : myGridPane.getChildren()) {
                Integer col = GridPane.getColumnIndex(node);
                Integer rw = GridPane.getRowIndex(node);
                if (col == null) col = 0;
                if (rw == null) rw = 0;

                if (col == column && rw == row && node instanceof StackPane cell) {
                    for (Node child : cell.getChildren()) {
                        if (child instanceof GridPane overlay && overlay.getId() != null &&
                                overlay.getId().equals("overlay-" + column + "-" + row)) {

                            overlay.add(getAlienImageView(overlay, isPurple), 0, 0);
                            return;
                        }
                    }
                }
            }
        });
    }

    //returns the image of an alien

    /**
     * Returns an ImageView containing the alien image based on its color, scaled to fit the specified overlay.
     * @param overlay
     * @param isPurple
     * @return
     */
    public ImageView getAlienImageView(GridPane overlay, boolean isPurple){
        Image alien;
        if (isPurple) {
            alien = new Image(getClass().getResource("/it/polimi/ingsw/galaxytrucker/images/pieces/purpleAlien.png").toExternalForm());
        }
        else {
            alien = new Image(getClass().getResource("/it/polimi/ingsw/galaxytrucker/images/pieces/brownAlien.png").toExternalForm());
        }

        ImageView alienImageView = new ImageView(alien);
        alienImageView.setFitWidth(overlay.getPrefWidth() / 2);
        alienImageView.setFitHeight(overlay.getPrefHeight() / 2);
        alienImageView.setPreserveRatio(true);
        alienImageView.setId("crew");

        return alienImageView;
    }
}
