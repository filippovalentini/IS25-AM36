package it.polimi.ingsw.galaxytrucker.client;

public class ViewPosition {
    int lap;
    int cell;

    public ViewPosition(int lap, int cell) {
        setPosition(lap, cell);
    }

    public int getLap() {
        return lap;
    }

    public int getCell() {
        return cell;
    }

    public void setPosition(int lap, int cell) {
        this.lap = lap;
        this.cell = cell;
    }
}
