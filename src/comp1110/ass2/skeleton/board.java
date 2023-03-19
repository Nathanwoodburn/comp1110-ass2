package comp1110.ass2.skeleton;

public class board {
    public final island[] islands; // There will be 8 different sized island so maybe gonna have to add an islandSize method
    public final stoneCircle[] stoneCircles; // a place that contains resources and statuetes, has 32 of them on the board

    /* [Added Comment]
    // Maybe gonna need to implement a random method to randomize the type and amount of resources on different stoneCircles
    // Also maybe implement a method that turns every tiles beside the island tiles to be water tiles?
     */
    public board(island[] islands, stoneCircle[] stoneCircles) {
        // Needed to stop the compiler complaining about the final fields not being set
        this.islands = islands;
        this.stoneCircles = stoneCircles;
    }

}

