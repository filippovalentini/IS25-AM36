package it.polimi.ingsw.galaxytrucker.model.gameClasses;

import java.util.Set;

//this class is used to manage the position of players on the flight board
public abstract class Position {
    public static Set<Integer> validStartingCells;
    protected int lap;        //lap number
    protected int cell;       //cell number

    public Position(int startingCell){      //constructor, invoked when a player ends the assembling phase
        this.lap = 0;
        this.cell = startingCell;
    }
    public int getLap() {
        return lap;
    }
    public int getCell() {
        return cell;
    }

    public abstract void changePosition(int steps); //updates the position of a player of a specified amount of steps (forwards or backwards)

    public boolean higherThan(Position position) {   //determines if a position on the flight board is higher than another one
        if(this.lap > position.lap)
            return true;
        else return this.lap == position.lap && this.cell > position.cell;
    }

}
