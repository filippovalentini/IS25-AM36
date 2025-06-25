package it.polimi.ingsw.galaxytrucker.ui.view;

import it.polimi.ingsw.galaxytrucker.model.enumerations.Color;

import java.util.HashMap;
import java.util.*;

public class ViewFlightBoard {
    Map<ViewPlayer, ViewPosition> positions = new HashMap<>();
    /**
     * Constructor for the flight board, initializes the positions of all players to null.
     * @param players the list of players in the game
     */
    public ViewFlightBoard(List<ViewPlayer> players) {
        for (ViewPlayer player : players) {
            positions.put(player, null);
        }
    }
    /**
     * Sets the position of a player on the flight board.
     * @param player the player whose position is being set
     * @param lap the lap number of the player's position
     * @param cell the cell number of the player's position
     */
    public void setPosition(ViewPlayer player, int lap, int cell) {
        this.positions.put(player, new ViewPosition(lap, cell));
    }

    //returns a map that for each player color associates the player's position on the flight board; it's
    //invoked by the gui flight board controller for visualization purposes
    /**
     * Returns a map of player colors to their corresponding cell positions on the flight board.
     * @return a map where keys are player colors and values are their respective cell positions
     */
    public Map<Color,Integer> getColorCellMap(){
        Map<Color,Integer> map = new HashMap<>();
        for(ViewPlayer player : positions.keySet()){
            Color color = player.getColor();
            Integer cell;
            if(positions.get(player) == null){
                cell = null;
            }
            else{
                cell = positions.get(player).getCell();
            }
            map.put(color, cell);
        }
        return map;
    }

    //visualizes the state of the ship board of a player
    /**
     * Visualizes the flight board, displaying the positions of all players.
     */
    public void visualize() {
        System.out.println("╔════════════════════════════╗");
        System.out.println("║        FLIGHT BOARD        ║");
        System.out.println("╚════════════════════════════╝");
        for (ViewPlayer player : positions.keySet()) {
            ViewPosition position = positions.get(player);
            System.out.println("👨‍🚀 Player: " + player.getNickname() + " " + Color.convertColorIntoEmoji(player.getColor()));
            if (position == null) {
                System.out.println("not positioned yet or has quit the game");
            }
            else{
                System.out.println("positioned at cell " + position.getCell() + " (lap " + position.getLap() + ")");
            }
        }
    }

    //sets to null the position of the player that has quit
    /**
     * Updates the flight board when a player quits, setting their position to null.
     * @param player the player who has quit
     */
    public void updatePlayerQuit(ViewPlayer player) {
        positions.put(player, null);
    }

    //updates the position of a player on the flight board
    /**
     * Updates the position of a player on the flight board.
     * @param player the player whose position is being updated
     * @param lap the new lap number for the player's position
     * @param cell the new cell number for the player's position
     */
    public void updatePlayerPosition(ViewPlayer player, int lap, int cell) {
        positions.get(player).setPosition(lap, cell);
    }
}
