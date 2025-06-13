package it.polimi.ingsw.galaxytrucker.ui.view;

import java.util.concurrent.ThreadLocalRandom;

public class ViewDice {
    private int dice1, dice2;
    private boolean valid;

    public ViewDice() {
        this.dice1 = 2;
        this.dice2 = 2;
        this.valid = false;
    }

    public void rollDice() {
        dice1 = ThreadLocalRandom.current().nextInt(1, 7);
        dice2 = ThreadLocalRandom.current().nextInt(1, 7);
        valid = true;
    }

    public void invalid(){
        valid = false;
    }

    public int getResult1() {
        return dice1;
    }

    public int getResult2() {
        return dice2;
    }

    public boolean validDice(){
        return valid;
    }
}
