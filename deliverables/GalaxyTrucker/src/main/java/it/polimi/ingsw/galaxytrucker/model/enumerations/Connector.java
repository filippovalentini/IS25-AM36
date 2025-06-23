package it.polimi.ingsw.galaxytrucker.model.enumerations;

/**
 * this class is used to represent the different type of connectors that can be associated to the sides of a component.
 * the connector "SMOOTH" is needed not only to represent smooth sides, but in general component sides which are not
 * strictly associated to a connector
 */
public enum Connector {
    SMOOTH, SINGLE, DOUBLE, UNIVERSAL;

    /**
     * This method checks if the current connector is compatible with another connector.
     * @param other the other connector to check compatibility with
     * @return true if the connectors are compatible, false otherwise
     */
    public boolean compatibleWith(Connector other){
        if(this == SMOOTH){
            return other == SMOOTH;
        }
        else if(this == SINGLE){
            return other == SINGLE || other == UNIVERSAL;
        }
        else if(this == DOUBLE){
            return other == DOUBLE || other == UNIVERSAL;
        }
        else{
            return other != SMOOTH;
        }
    }
}
