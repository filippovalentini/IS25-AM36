package it.polimi.ingsw.galaxytrucker.model.gameClasses;

import java.util.Set;

public class LevelTwoPosition extends Position {
    public LevelTwoPosition(int startingCell){
        super(startingCell);
        validStartingCells = Set.of(0,1,3,6);
    }
    @Override
    public void changePosition(int steps){
        if(steps > 0){
            for(int i = 0; i < steps; i++){
                if(cell == 23){
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
                    cell = 23;
                    lap--;
                }
                else{
                    cell--;
                }
            }
        }
    }
}
