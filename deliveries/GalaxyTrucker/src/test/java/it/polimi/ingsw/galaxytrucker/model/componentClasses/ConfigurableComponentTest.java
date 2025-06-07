package it.polimi.ingsw.galaxytrucker.model.componentClasses;

import it.polimi.ingsw.galaxytrucker.model.enumerations.Connector;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class ConfigurableComponentTest {

    private ConfigurableComponent configurableComponent1; //where isDouble is set to true
    private ConfigurableComponent configurableComponent2; //where isDouble is set to false
    @BeforeEach
    public void setUp() {
        List<Connector> connectors = new ArrayList<>();
        connectors.add(Connector.SMOOTH);
        connectors.add(Connector.SINGLE);
        connectors.add(Connector.DOUBLE);
        connectors.add(Connector.UNIVERSAL);
        configurableComponent1 = new ConfigurableComponent(true, 1, connectors );
        configurableComponent2 = new ConfigurableComponent(false, 1, connectors );
    }
    //test for isDouble method
    @Test
    public void testIsDouble_true() {
        assertTrue(configurableComponent1.isDouble());
    }


    @Test
    public void testIsDouble_false() {
        assertTrue(!configurableComponent2.isDouble());
    }
}

