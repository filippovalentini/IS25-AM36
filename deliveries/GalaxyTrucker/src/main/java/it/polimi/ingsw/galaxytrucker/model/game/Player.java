package it.polimi.ingsw.galaxytrucker.model.game;

import it.polimi.ingsw.galaxytrucker.model.enumerations.Color;

public class Player {
    private final String nickname;
    private int credits;
    private boolean hasAbandoned;
    private ShipBoard shipBoard;

    public Player(String nickname, Color color) {
        this.nickname = nickname;
        this.credits = 0;
        this.hasAbandoned = false;
        this.shipBoard = new ShipBoard(color);
    }
    public String getNickname() {
        return nickname;
    }
    public int getCredits() {
        return credits;
    }
    public void updateCredits(int update) {
        this.credits += update;
    }
    public boolean hasAbandoned() {
        return hasAbandoned;
    }
    public ShipBoard getShipBoard() {
        return shipBoard;
    }
    public void quitGame(boolean hasAbandoned) {
        this.hasAbandoned = true;
    }
}
