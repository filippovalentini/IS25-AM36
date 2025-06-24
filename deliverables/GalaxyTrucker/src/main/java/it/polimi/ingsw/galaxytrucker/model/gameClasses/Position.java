package it.polimi.ingsw.galaxytrucker.model.gameClasses;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * This class is used to manage the position of players on the flight board
 */
public abstract class Position {
    public static Set<Integer> validStartingCells;  //valid initial cells for ship placement after the assembling phase
    protected int lap;        //lap number
    protected int cell;       //cell number

    /**
     * Constructor, invoked when a player starts the assembling phase
     * @param startingCell
     */
    public Position(int startingCell){      //constructor, invoked when a player ends the assembling phase
        this.lap = 0;
        this.cell = startingCell;
    }

    /**
     * Method that gets the lap
     * @return the lap number and the cell number of the player
     */
    public int getLap() {
        return lap;
    }
    /**
     * Method that gets the cell
     * @return the cell number of the player
     */
    public int getCell() {
        return cell;
    }

    /**
     * Method that updates the position of a player of a specified amount of steps (forwards or backwards)
     * @param busyCells
     * @param steps
     */
    public void changePosition(List<Integer> busyCells, int steps){
        List<Integer> busy = new ArrayList<>(busyCells);
        busy.remove(busyCells.indexOf(cell));

        int movesRemaining = Math.abs(steps);
        int direction = Integer.signum(steps);

        while (movesRemaining > 0) {
            performStep(direction);
            //if the new cell is busy we don't decrement the counter
            if (!busy.contains(cell)) {
                movesRemaining--;
            }
        }
        //after the movement, if the new position is busy we skip it
        while (busy.contains(cell)) {
            performStep(direction);
        }
    }

    /**
     * Method that performs a change of position of one step, forwards or backwards based on the direction parameter
     * @param direction
     */
    public abstract void performStep(int direction);

    /**
     * Method that determines if a position on the flight board is higher than another one
     * @param position
     * @return true if the current position is higher than the one passed as a parameter, false otherwise
     */
    public boolean higherThan(Position position) {
        if(this.lap > position.lap)
            return true;
        else return this.lap == position.lap && this.cell > position.cell;
    }

}
