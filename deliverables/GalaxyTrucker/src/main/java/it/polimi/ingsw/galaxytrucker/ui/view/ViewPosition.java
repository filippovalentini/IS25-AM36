package it.polimi.ingsw.galaxytrucker.ui.view;

/**
 * ViewPosition class represents a simplified version (client-side) of the Position class (server-side)
 */
public class ViewPosition {
    int lap;        //lap on the flight board
    int cell;       //actual position on the flight board

    /**
     * Constructor for ViewPosition.
     * @param lap
     * @param cell
     */
    public ViewPosition(int lap, int cell) {
        setPosition(lap, cell);
    }

    /**
     * Returns the lap of the position.
     * @return the lap number
     */
    public int getLap() {
        return lap;
    }

    /**
     * Returns the cell of the position.
     * @return the cell number
     */
    public int getCell() {
        return cell;
    }

    /**
     * Sets the position of the player.
     * @param lap
     * @param cell
     */
    public void setPosition(int lap, int cell) {
        this.lap = lap;
        this.cell = cell;
    }
}
