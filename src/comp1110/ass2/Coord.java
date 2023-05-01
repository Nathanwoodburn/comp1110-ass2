package comp1110.ass2;

import java.util.ArrayList;

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
    public Coord(int y, int x) {
        this.x = x;
        this.y = y;
    }

    // region Getters and Setters
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

    // endregion

    /**
     * Check if two coordinates are equal
     * @param coord Coord object to compare to
     * @return boolean true if equal, false otherwise
     */
    public boolean equals(Coord coord) {
        return (this.x == coord.x && this.y == coord.y);
    }

    /**
     * Check if two coordinates are adjacent (does not include diagonals)
     * @param coord Coord object to compare to
     */
    public boolean isAdjacent(Coord coord) {
        if (this.y == coord.y) {
            return (this.x == coord.x - 1 || this.x == coord.x + 1);
        }
        if (this.x == coord.x) {
            return (this.y == coord.y - 1 || this.y == coord.y + 1);
        }
        return false;
    }

    /**
     * Check if two coordinates are adjacent (includes diagonals)
     * @param coord Coord object to compare to
     */
    public boolean isAdjacentDiagonal(Coord coord){
        if (isAdjacent(coord)) return true;
        if (this.x == coord.x - 1 && this.y == coord.y - 1) return true;
        if (this.x == coord.x - 1 && this.y == coord.y + 1) return true;
        if (this.x == coord.x + 1 && this.y == coord.y - 1) return true;
        return  (this.x == coord.x + 1 && this.y == coord.y + 1);
    }

    /**
     * Get a string representation of the coordinate (y,x)
     * @return String representation of the coordinate
     */
    @Override
    public String toString() {
        return y + "," + x;
    }
}
