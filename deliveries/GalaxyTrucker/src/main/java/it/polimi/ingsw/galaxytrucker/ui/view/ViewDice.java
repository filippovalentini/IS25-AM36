package it.polimi.ingsw.galaxytrucker.ui.view;

import java.util.concurrent.ThreadLocalRandom;

public class ViewDice {
    private int dice1, dice2;
    private boolean throwable;

    public ViewDice() {
        this.dice1 = 2;
        this.dice2 = 2;
        this.throwable = true;
    }

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

    public void enableThrow(boolean enable) {
        this.throwable = enable;
    }

    public int getResult1() {
        return dice1;
    }

    public int getResult2() {
        return dice2;
    }

    public boolean areThrowable(){
        return throwable;
    }
}
