package it.polimi.ingsw.galaxytrucker.model.eventCardClasses;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;

public class AdvancedEnemiesTest {
    private AdvancedEnemies advancedEnemies;
    @BeforeEach
    public void init(){ // Initialize an AdvancedEnemies object with arbitrary values
        advancedEnemies = new AdvancedEnemies(5, 10, 2, 1) {
        };

    }
    //test for isDefeated method
    @Test
    public void isDefeated_false() {
        assertFalse(advancedEnemies.isDefeated());
    } // The isDefeated method should return false initially, as no enemies have been defeated yet.
}
