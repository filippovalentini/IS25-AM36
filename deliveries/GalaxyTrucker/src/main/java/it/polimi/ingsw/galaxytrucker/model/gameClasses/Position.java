package it.polimi.ingsw.galaxytrucker.model.gameClasses;
//this class is used to manage the position of players on the flight board
public class Position {
    private int lap;        //lap number
    private int cell;       //cell number

    public Position(int startingPosition){      //constructor, invoked when a player ends the assembling phase
        this.lap = 0;
        this.cell = startingPosition;
    }
    public int getLap() {
        return lap;
    }
    public int getCell() {
        return cell;
    }

    public void changePosition(int steps){      //updates the position of a player of a specified amount of steps (forwards or backwards)
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
