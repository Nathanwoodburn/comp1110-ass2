package comp1110.ass2;

/**
 * Object to store coordinates
 * This stores the x and y coordinates of a point
 */
public class Coord {
    private int x;
    private int y;

    /**
     * Constructor for the Coord object
     * @param x x coordinate
     * @param y y coordinate
     */
    public Coord(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Get the x coordinate
     * @return int x coordinate
     */
    public int getX() {
        return x;
    }

    /**
     * Get the y coordinate
     * @return int y coordinate
     */
    public int getY() {
        return y;
    }

    /**
     * Set the x coordinate
     * @param x int x coordinate
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * Set the y coordinate
     * @param y int y coordinate
     */
    public void setY(int y) {
        this.y = y;
    }

    /**
     * Check if two coordinates are equal
     * @param coord Coord object to compare to
     * @return boolean true if equal, false otherwise
     */
    public boolean equals(Coord coord) {
        return (this.x == coord.x && this.y == coord.y);
    }

    /**
     * Get a string representation of the coordinate
     * @return String representation of the coordinate
     */
    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}
