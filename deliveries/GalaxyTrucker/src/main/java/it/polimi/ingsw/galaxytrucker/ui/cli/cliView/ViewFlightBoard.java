package it.polimi.ingsw.galaxytrucker.ui.cli.cliView;

import it.polimi.ingsw.galaxytrucker.model.enumerations.Color;

import java.util.HashMap;
import java.util.*;

public class ViewFlightBoard {
    Map<ViewPlayer, ViewPosition> positions = new HashMap<>();

    public ViewFlightBoard(List<ViewPlayer> players) {
        for (ViewPlayer player : players) {
            positions.put(player, null);
        }
    }

    public void setPosition(ViewPlayer player, int lap, int cell) {
        this.positions.put(player, new ViewPosition(lap, cell));
    }

    //visualizes the state of the ship board of a player
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
    public void updatePlayerQuit(ViewPlayer player) {
        positions.put(player, null);
    }

    //updates the position of a player on the flight board
    public void updatePlayerPosition(ViewPlayer player, int lap, int cell) {
        positions.get(player).setPosition(lap, cell);
    }
}
