package it.polimi.ingsw.galaxytrucker.model.componentClasses;

import it.polimi.ingsw.galaxytrucker.model.enumerations.Connector;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;

public class StructuralTest {
    private Structural structural;

    @BeforeEach
    public void setUp(){
        List<Connector> connectors = new ArrayList<>();
        connectors.add(Connector.SMOOTH);
        connectors.add(Connector.SINGLE);
        connectors.add(Connector.DOUBLE);
        connectors.add(Connector.UNIVERSAL);
        structural= new Structural(1, connectors);
    }
    //test for clone method
    @Test
    public void cloneTest() {
        Structural clonedStructural = (Structural) structural.clone();
        assertEquals(structural.getImageID(), clonedStructural.getImageID());
        assertEquals(structural.getOrientation(), clonedStructural.getOrientation());
    }

}
