package it.polimi.ingsw.galaxytrucker.model.componentClasses;

import it.polimi.ingsw.galaxytrucker.model.enumerations.Connector;

import java.util.List;
/**
 * This class represents components that can have two configurations, such as single or double.
 * It extends the Component class and adds a boolean field to indicate if the component is double.
 */
public class ConfigurableComponent extends Component {      //classes of components which can have 2 configurations
    protected boolean isDouble;     //true if the component is "double" (if false, the component can be single or triple depending on its type)
    /**
     * Constructor for ConfigurableComponent.
     *
     * @param isDouble Indicates if the component is double.
     * @param imageID  The image ID of the component.
     * @param sides    The list of connectors for the component.
     */
    public ConfigurableComponent(boolean isDouble, int imageID, List<Connector> sides) {       //constructor
        super(imageID, sides);
        this.isDouble = isDouble;
    }

    /**
     * Checks if the component configuration is double.
     * @return true if the component is double, false otherwise.
     */
    public boolean isDouble() {
        return isDouble;
    }       //determines if the component configuration is double or not
}
