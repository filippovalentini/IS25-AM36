package it.polimi.ingsw.galaxytrucker.model.game;

public class Position {
    private int lap;
    private int cell;

    public Position(int startingPosition){
        this.lap = 0;
        this.cell = startingPosition;
    }
    public int getLap() {
        return lap;
    }
    public int getCell() {
        return cell;
    }
    public void changePosition(int steps){
        if(steps > 0){
            for(int i = 0; i < steps; i++){
                if(cell == 24){
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
                    cell = 24;
                    lap--;
                }
                else{
                    cell--;
                }
            }
        }
    }

}
