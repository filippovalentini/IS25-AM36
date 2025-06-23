package it.polimi.ingsw.galaxytrucker.model.gameClasses;

import java.util.Set;

public class LevelTwoPosition extends Position {
    public static final Set<Integer> validStartingCells = Set.of(0,1,3,6);
    public LevelTwoPosition(int startingCell){
        super(startingCell);
    }

    @Override
    //performs a change of position of one step, forwards or backwards based on the direction parameter
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
