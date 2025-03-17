package it.polimi.ingsw.galaxytrucker.model.eventCardClasses;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AbandonedShipTest {
    private AbandonedShip abandonedShip;

    @BeforeEach
    void init(){
        abandonedShip = new AbandonedShip(2, 3, 1, 0);
    }

    @Test
    void testUseShip(){

    }

    @Test
    void testShouldNotUseShipAlreadyUsed(){

    }

    @Test
    void testLandingWithRequiredCrewAndCredits(){

    }

    @Test
    void testShouldNotLandingInvalidRequiredCrew(){}

    @Test
    void testShouldNotLandingInvalidRequiredCredits(){}


}