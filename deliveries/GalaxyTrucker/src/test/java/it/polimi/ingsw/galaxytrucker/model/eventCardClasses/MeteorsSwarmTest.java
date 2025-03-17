package it.polimi.ingsw.galaxytrucker.model.eventCardClasses;

import it.polimi.ingsw.galaxytrucker.model.enumerations.Orientation;
import it.polimi.ingsw.galaxytrucker.model.shotClasses.Meteor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MeteorsSwarmTest {
    private MeteorsSwarm meteorsSwarm;

    @BeforeEach
    void init(){
        Meteor mLarge = new Meteor(true, Orientation.SOUTH);
        Meteor mNotLarge1 = new Meteor(false, Orientation.EAST);
        Meteor mNotLarge2 = new Meteor(false, Orientation.WEST);
        List<Meteor> meteorsList = new ArrayList<>();
        meteorsList.add(mLarge);
        meteorsList.add(mNotLarge1);
        meteorsList.add(mNotLarge2);
        meteorsSwarm = new MeteorsSwarm(meteorsList, 0);
    }

    @Test
    void testHitShip(){

    }
}