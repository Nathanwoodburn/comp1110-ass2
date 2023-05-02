package comp1110.ass2;

/**
 * Object to store coordinates
 * This stores the x and y coordinates of a point
 */
public record Coord(int y, int x) {
    /**
     * Constructor for the Coord object
     *
     * @param x x coordinate
     * @param y y coordinate
     */
    public Coord {
    }

    // region Getters and Setters

    /**
     * Get the x coordinate
     *
     * @return int x coordinate
     */
    @Override
    public int x() {
        return x;
    }

    /**
     * Get the y coordinate
     *
     * @return int y coordinate
     */
    @Override
    public int y() {
        return y;
    }

    // endregion

    /**
     * Check if two coordinates are equal
     *
     * @param coord Coord object to compare to
     * @return boolean true if equal, false otherwise
     */
    public boolean equals(Coord coord) {
        return (this.x == coord.x && this.y == coord.y);
    }

    /**
     * Check if two coordinates are adjacent (does not include diagonals)
     *
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
     *
     * @param coord Coord object to compare to
     */
    public boolean isAdjacentDiagonal(Coord coord) {
        if (isAdjacent(coord)) return true;
        if (this.x == coord.x - 1 && this.y == coord.y - 1) return true;
        if (this.x == coord.x - 1 && this.y == coord.y + 1) return true;
        if (this.x == coord.x + 1 && this.y == coord.y - 1) return true;
        return (this.x == coord.x + 1 && this.y == coord.y + 1);
    }

    /**
     * @param player Player to check owned by
     * @return ArrayList of adjacent coordinates
     */
    public Coord[] getClaimedAdjacent(Player player) {
        Coord[] adjacent = new Coord[8];
        adjacent[0] = new Coord(this.y, this.x + 1);
        adjacent[1] = new Coord(this.y, this.x - 1);
        adjacent[2] = new Coord(this.y + 1, this.x);
        adjacent[3] = new Coord(this.y - 1, this.x);
        adjacent[4] = new Coord(this.y + 1, this.x + 1);
        adjacent[5] = new Coord(this.y + 1, this.x - 1);
        adjacent[6] = new Coord(this.y - 1, this.x + 1);
        adjacent[7] = new Coord(this.y - 1, this.x - 1);

        Coord[] adjacentOwned = new Coord[8];
        int ownedCount = 0;

        for (Coord c : player.getPieces()) {
            for (int i = 0; i < adjacent.length; i++) {
                if (c.equals(adjacent[i])) {
                    adjacentOwned[i] = c;
                    ownedCount++;
                }
            }
        }
        Coord[] owned = new Coord[ownedCount];
        for (int i = 0; i < ownedCount; i++) {
            owned[i] = adjacentOwned[i];
        }
        return adjacentOwned;
    }


    /**
     * Get a string representation of the coordinate (y,x)
     *
     * @return String representation of the coordinate
     */
    @Override
    public String toString() {
        return y + "," + x;
    }
}
