package it.polimi.ingsw.galaxytrucker.model.gameClasses;

import java.util.Set;

/**
 * This class is used to manage the position of players on the flight board in Level Two.
 */
public class LevelTwoPosition extends Position {
    public static final Set<Integer> validStartingCells = Set.of(0,1,3,6);

    /**
     * Constructor for LevelTwoPosition.
     * @param startingCell
     */
    public LevelTwoPosition(int startingCell){
        super(startingCell);
    }

    /**
     * This methods performs a change of position of one step, forwards or backwards based on the direction parameter
     * @param direction
     */
    @Override
    public void performStep(int direction){
        if (direction > 0) { //forward movement
            if (cell == 23) {
                cell = 0;
                lap++;
            } else {
                cell++;
            }
        } else { // backward movement
            if (cell == 0) {
                cell = 23;
                lap--;
            } else {
                cell--;
            }
        }
    }
}
