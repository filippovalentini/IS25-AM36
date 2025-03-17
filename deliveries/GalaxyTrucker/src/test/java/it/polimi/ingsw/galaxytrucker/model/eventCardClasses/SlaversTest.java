package it.polimi.ingsw.galaxytrucker.model.eventCardClasses;

import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;

class SlaversTest {
    private Slavers slavers;

    @BeforeEach
    void init(){
        slavers = new Slavers(5,6, 3,1,0);

    }

    void testDefeat(){

    }

    void testShouldNotAttackIfDefeated(){

    }
}