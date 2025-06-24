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

//This class contains all the methods needed to show a specific component of a ship board, including batteries, crew
//members, aliens, goods and providing the correct size and orientation of the component.
public abstract class ShipBoardGraphics {
    @FXML protected GridPane myGridPane;

    //sets an image in a specific position of the grid pane and with a specific orientation
    public abstract void setImageOnGrid(String imageID, Orientation orientation, int column, int row);

    //sets all the graphic information regarding a component in a specific position of the grid pane
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
    public ImageView getBatteryImageView(GridPane overlay){
        Image battery = new Image(getClass().getResource("/it/polimi/ingsw/galaxytrucker/images/pieces/battery.png").toExternalForm());

        ImageView batteryImageView = new ImageView(battery);
        batteryImageView.setFitWidth(overlay.getPrefWidth() / 2);
        batteryImageView.setFitHeight(overlay.getPrefHeight() / 2);
        batteryImageView.setPreserveRatio(true);
        batteryImageView.setId("crew");

        return batteryImageView;
    }

    //attaches crew member images to a component on the grid pane
    public void addCrewMembers(int row, int column, int crew) {
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
    public ImageView getCrewMemberImageView(GridPane overlay){
        Image crewMember = new Image(getClass().getResource("/it/polimi/ingsw/galaxytrucker/images/pieces/crewMember.png").toExternalForm());

        ImageView crewMemberImageView = new ImageView(crewMember);
        crewMemberImageView.setFitWidth(overlay.getPrefWidth() / 2);
        crewMemberImageView.setFitHeight(overlay.getPrefHeight() / 2);
        crewMemberImageView.setPreserveRatio(true);
        crewMemberImageView.setId("crew");

        return crewMemberImageView;
    }

    //attaches images of cargo goods to a component on the grid pane
    public void addGoods(int row, int column, List<Color> goods) {
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

                            int numberGoods = goods.size();

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
