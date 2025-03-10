package it.polimi.ingsw.galaxytrucker.model.gameClasses;

import it.polimi.ingsw.galaxytrucker.model.enumerations.Color;
//this class is used to describe all the main information associated to a player
public class Player {
    private final String nickname;      //unique nickname
    private int credits;        //cosmic credits of the player
    private boolean hasAbandoned;       //true if the player has abandoned the game
    private ShipBoard shipBoard;        //ship board of the player

    public Player(String nickname, Color color) {       //constructor
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
    public boolean hasAbandoned() {
        return hasAbandoned;
    }
    public ShipBoard getShipBoard() {
        return shipBoard;
    }

    public void quitGame() {    //invoked when a player must leave the game
        this.hasAbandoned = true;
    }
    public void updateCredits(int update) {     //updates teh cosmic credits pf a player
        this.credits += update;
    }
}
