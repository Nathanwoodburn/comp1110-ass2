package comp1110.ass2;

/**
 * Resource class
 * This class is used to store the information of a resource
 * This stores the type and the coordinate of the resource
 */
public class Resource {
    private char type;
    private final Coord coord;
    private boolean claimed;

    /**
     * Constructor for Resource class
     * This creates a resource with a type and a coordinate
     * @param type the type of the resource
     * @param coord the coordinate of the resource
     */
    public Resource(char type, Coord coord) {
        this.type = type;
        this.coord = coord;
        this.claimed = false;
    }

    /**
     * Get the type of the resource
     * @return char type of the resource
     */
    public char getType() {
        return type;
    }

    /**
     * Get the type of the resource
     * @return String type of the resource
     */
    public String getTypeString() {
        return switch (type) {
            case 'C' -> "Coconut";
            case 'B' -> "Bamboo";
            case 'W' -> "Water";
            case 'P' -> "Precious Stone";
            case 'S' -> "Statuette";
            default -> "Invalid";
        };
    }

    /**
     * Get the coordinate of the resource
     * @return Coord coordinate of the resource
     */
    public Coord getCoord() {
        return coord;
    }

    /**
     * Set the type of the resource
     * @param type char type of the resource
     */
    public void setType(char type) {
        this.type = type;
    }

    /**
     * Check if the resource is equal to another resource
     * @param resource resource to be compared
     * @return boolean true if the resources are equal
     */
    public boolean equals(Resource resource) {
        return (this.type == resource.type && this.coord.equals(resource.coord));
    }

    /**
     * Set the resource to be claimed
     * This is used to check if the resource has been claimed
     */
    public void setClaimed() {
        this.claimed = true;
    }

    /**
     * Check if the resource has been claimed
     * @return boolean true if the resource has been claimed
     */
    public boolean isAvailable() {
        return !this.claimed;
    }

    @Override
    public String toString() {
        return "Resource{" +
                "type=" + type +
                ", coord=" + coord +
                ", claimed=" + claimed +
                '}';
    }
}
