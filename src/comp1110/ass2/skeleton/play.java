package comp1110.ass2.skeleton;

public class play {
    enum gameMode {
        exploration, settlement
    }
    /**
     * This method is used to check if the placement of the tile is valid
     * It checks if the tile is placed on water or land and if the tile is placed on a tile that is already occupied.
     * It also checks if the tile is placed on a tile that is not adjacent to the tile that is already placed.
     *
     * Has input game mode
     */
    public boolean validPlacement(gameMode mode) {
        return false; // Needed to stop errors
    }


    /**
     * This method picks the player and gives them the first turn.
     * It then asks them to place a tile.
     * It then asks the next player to place a tile.
     */
    public static void main(String[] args) {

    }

    /**
     * This method will be used to get the player to place a tile.
     * It will use the validPlacement method to check if the placement is valid.
     * It will also use the placeTile method to place the tile.
     */
    public void playerTurn(player player){

    }


    /**
     * This method will be used to place a piece on a tile.
     */
    public void placePiece(tile tile, piece piece){

    }

    /**
     * This method will be used to claim a stone circle.
     */

    public void claimStoneCircle(stoneCircle stoneCircle, player player){

    }

    /**
     * This method will be used to check if exploration phase is over.
     */
    public boolean phaseOver() {
        return false; // Needed to stop errors
    }
}

