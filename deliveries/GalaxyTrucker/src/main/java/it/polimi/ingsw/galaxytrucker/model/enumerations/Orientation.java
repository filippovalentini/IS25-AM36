package it.polimi.ingsw.galaxytrucker.model.enumerations;
//this class is used to describe the orientation of component tiles in the ship board and of meteors/cannon shots
public enum Orientation {
    NORTH, EAST, SOUTH, WEST;

    public boolean isVertical(){
        return this == NORTH || this == SOUTH;
    }
    public boolean isHorizontal(){
        return this == EAST || this == WEST;
    }

    @Override
    public String toString() {
        return switch (this) {
            case NORTH -> "RED";
            case SOUTH -> "SOUTH";
            case EAST -> "EAST";
            case WEST -> "WEST";
        };
    }
}
