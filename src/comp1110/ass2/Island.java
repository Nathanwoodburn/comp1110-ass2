package comp1110.ass2;

/**
 * Island class
 * This class is used to store the information of an island
 */

public class Island {
    final int bonus;
    private Coord[] coords;

    /**
     * Constructor for Island class
     * This creates an island with a bonus and an empty array of coordinates
     * @param bonus the bonus of the island
     */
    public Island(int bonus) {
        this.bonus = bonus;
        this.coords = new Coord[0];
    }

    /**
     * Get the bonus of the island
     * @return int bonus of the island
     */
    public int getBonus() {
        return bonus;
    }

    /**
     * Get the coordinates of the island
     * @return Coord[] coordinates of the island
     */
    public Coord[] getCoords() {
        return coords;
    }

    /**
     * Check if the island contains a coordinate
     * @param coord the coordinate to be checked
     * @return boolean true if the island contains the coordinate
     */
    public boolean containsCoord(Coord coord) {
        for (Coord c : this.coords) {
            if (c.equals(coord)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Add a coordinate to the island
     * @param coord the coordinate to be added
     */
    public void addCoord(Coord coord) {

        if (this.containsCoord(coord)) {
            return;
        }
        Coord[] newCoords = new Coord[this.coords.length + 1];
        for (int i = 0; i < this.coords.length; i++) {
            newCoords[i] = this.coords[i];
        }
        newCoords[this.coords.length] = coord;
        this.coords = newCoords;
    }

    /**
     * Check if island is equal to another island
     * @param island the island to be compared to
     * @return boolean true if the island is equal to the other island
     */
    public boolean equals(Island island) {
        if (this.bonus != island.bonus) return false;
        if (this.coords.length != island.coords.length) return false;
        for (int i = 0; i < this.coords.length; i++) {
            if (!this.containsCoord(island.coords[i])) return false;
        }
        return true;
    }
}
