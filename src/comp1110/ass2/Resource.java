package comp1110.ass2;

/**
 * Resource class
 * This class is used to store the information of a resource
 * This stores the type and the coordinate of the resource
 */
public class Resource {
    private char type;
    private Coord coord;

    /**
     * Constructor for Resource class
     * This creates a resource with a type and a coordinate
     * @param type the type of the resource
     * @param coord the coordinate of the resource
     */
    public Resource(char type, Coord coord) {
        this.type = type;
        this.coord = coord;
    }

    /**
     * Get the type of the resource
     * @return char type of the resource
     */
    public char getType() {
        return type;
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
     * Set the coordinate of the resource
     * @param coord Coord coordinate of the resource
     */
    public void setCoord(Coord coord) {
        this.coord = coord;
    }

    /**
     * Check if the resource is equal to another resource
     * @param resource Resource resource to be compared
     * @return boolean true if the resources are equal
     */
    public boolean equals(Resource resource) {
        return (this.type == resource.type && this.coord.equals(resource.coord));
    }

}
