package it.polimi.ingsw.galaxytrucker.ui.view;

import java.util.concurrent.ThreadLocalRandom;

public class ViewDice {
    private int dice1, dice2;
    private boolean throwable;
    /**
     * Constructor for ViewDice.
     * Initializes the dice values to 0 and sets throwable to true.
     */
    public ViewDice() {
        this.dice1 = 0;
        this.dice2 = 0;
        this.throwable = true;
    }

    /**
     * Rolls the dice and updates their values.
     * @throws Exception
     */
    public void rollDice() throws Exception {
        if(throwable) {
            dice1 = ThreadLocalRandom.current().nextInt(1, 7);
            dice2 = ThreadLocalRandom.current().nextInt(1, 7);
            enableThrow(false);
        }
        else {
            throw new Exception("You have already thrown the dice");
        }
    }

    /**
     * Enables or disables the ability to throw the dice.
     * @param enable
     */
    public void enableThrow(boolean enable) {
        this.throwable = enable;
    }

    /**
     * Returns the result of the first die.
     * @return the value of the first die
     */
    public int getResult1() {
        return dice1;
    }

    /**
     * Returns the result of the second die.
     * @return  the value of the second die
     */
    public int getResult2() {
        return dice2;
    }

    /**
     * Checks if the dice can be thrown.
     * @return true if the dice can be thrown, false otherwise
     */
    public boolean areThrowable(){
        return throwable;
    }
}
