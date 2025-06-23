package it.polimi.ingsw.galaxytrucker.model.shotClasses;

import it.polimi.ingsw.galaxytrucker.model.enumerations.Orientation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MeteorTest {
    private Meteor meteor;

    @BeforeEach
    public void setUp() {
        meteor = new Meteor(true, Orientation.NORTH);
    }

    //test isLarge()
    @Test
    public void testIsLarge() {
        assertTrue(meteor.isLarge());
    }
    //test GetOrientation()
    @Test
    public void testGetOrientation() {
        assertEquals(Orientation.NORTH, meteor.getOrientation());
    }

}
