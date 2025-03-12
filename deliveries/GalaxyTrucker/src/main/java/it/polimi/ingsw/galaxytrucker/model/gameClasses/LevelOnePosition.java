package it.polimi.ingsw.galaxytrucker.model.gameClasses;

import java.util.Set;

public class LevelOnePosition extends Position {
    public LevelOnePosition(int startingCell) {
        super(startingCell);
        validStartingCells = Set.of(0,1,2,4);
    }
    @Override
    public void changePosition(int steps) {
        if(steps > 0){
            for(int i = 0; i < steps; i++){
                if(cell == 17){
                    cell = 0;
                    lap++;
                }
                else{
                    cell++;
                }
            }
        }
        else{
            for(int i = 0; i < (-steps); i++){
                if(cell == 0){
                    cell = 17;
                    lap--;
                }
                else{
                    cell--;
                }
            }
        }
    }
}
