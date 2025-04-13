package it.polimi.ingsw.galaxytrucker.client;

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
            System.out.println("👨‍🚀 Player: " + player.getNickname() + " " + View.convertColor(player.getColor()));
            if (position == null) {
                System.out.println("not positioned yet");
            }
            else{
                System.out.println("positioned at cell " + position.getCell() + " (lap " + position.getLap() + ")");
            }
        }
    }
}
