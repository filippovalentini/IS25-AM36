package it.polimi.ingsw.galaxytrucker.model.enumerations;

/**
 * this class is used to describe the orientation of component tiles in the ship board and of meteors/cannon shots
 */
public enum Orientation {
    NORTH, EAST, SOUTH, WEST;

    /**
     * this method is used to check if the orientation is vertical
     * @return true if the orientation is vertical (NORTH or SOUTH), false otherwise
     */
    public boolean isVertical(){
        return this == NORTH || this == SOUTH;
    }

    /**
     * this method is used to check if the orientation is horizontal
     * @return true if the orientation is horizontal (EAST or WEST), false otherwise
     */
    public boolean isHorizontal(){
        return this == EAST || this == WEST;
    }

    /**
     * this method is used to convert the orientation into a string representation
     * @return the string representation of the orientation
     */
    @Override
    public String toString() {
        return switch (this) {
            case NORTH -> "NORTH";
            case SOUTH -> "SOUTH";
            case EAST -> "EAST";
            case WEST -> "WEST";
        };
    }

    /**
     * this method is used to convert a string into an orientation
     * @param orientation
     * @return the orientation associated to the string, or null if the string is not valid
     */
    public static Orientation convertToOrientation(String orientation){
        return switch (orientation) {
            case "NORTH" -> Orientation.NORTH;
            case "EAST" -> Orientation.EAST;
            case "SOUTH" -> Orientation.SOUTH;
            case "WEST" -> Orientation.WEST;
            default -> null;
        };
    }
}
