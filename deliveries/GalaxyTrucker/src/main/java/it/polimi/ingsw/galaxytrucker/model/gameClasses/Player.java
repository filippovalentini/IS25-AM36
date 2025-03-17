package it.polimi.ingsw.galaxytrucker.model.gameClasses;

import it.polimi.ingsw.galaxytrucker.model.enumerations.Color;

import java.util.List;

//this class is used to describe all the main information associated to a player
public class Player {
    private final String nickname;      //unique nickname
    private int credits;        //cosmic credits of the player
    private boolean hasAbandoned;       //true if the player has abandoned the game
    private ShipBoard shipBoard;        //ship board of the player

    public Player(String nickname, Color color, boolean levelOne) {       //constructor
        this.nickname = nickname;
        this.credits = 0;
        this.hasAbandoned = false;
        if(levelOne){
            shipBoard = new LevelOneShipBoard(color);
        }
        else{
            shipBoard = new LevelTwoShipBoard(color);
        }
    }
    public String getNickname() { //return a copy of the nickname
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
    public int getCrewCount(){
        return shipBoard.getNumberCrew();
    }
    public void removeShipBoardCrew(List<Integer> x, List<Integer> y, List<Integer> eachCabinCrew, int numberCrewToRemove) {
        shipBoard.removeCrewMembers(x, y, eachCabinCrew, numberCrewToRemove);
    }

    public void quitGame() {    //invoked when a player must leave the game
        this.hasAbandoned = true;
    }
    public void updateCredits(int update) {     //updates teh cosmic credits pf a player
        this.credits += update;
    }


}
