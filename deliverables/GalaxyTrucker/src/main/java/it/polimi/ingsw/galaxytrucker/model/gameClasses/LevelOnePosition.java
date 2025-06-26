package it.polimi.ingsw.galaxytrucker.model.gameClasses;

import java.util.Set;

/**
 * This class is used to manage the position of players on the flight board for Level One.
 */
public class LevelOnePosition extends Position {
    public static final Set<Integer> validStartingCells = Set.of(0,1,2,4);  //valid positions for ship placement

    /**
     * Constructor for LevelOnePosition.
     * @param startingCell
     */
    public LevelOnePosition(int startingCell) {
        super(startingCell);
    }

    /**
     * Performs a change of position of one step, forwards or backwards based on the direction parameter.
     * @param direction
     */
    @Override

    public void performStep(int direction){
        if (direction > 0) { //forward movement
            if (cell == 17) {
                cell = 0;
                lap++;
            } else {
                cell++;
            }
        } else { // backward movement
            if (cell == 0) {
                cell = 17;
                lap--;
            } else {
                cell--;
            }
        }
    }
}
