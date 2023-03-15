package comp1110.ass2.skeleton;

public class board {
    public final island[] islands;
    public final stoneCircle[] stoneCircles;

    public board(island[] islands, stoneCircle[] stoneCircles) {
        // Needed to stop the compiler complaining about the final fields not being set
        this.islands = islands;
        this.stoneCircles = stoneCircles;
    }

}
