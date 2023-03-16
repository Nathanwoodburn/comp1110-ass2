package comp1110.ass2.skeleton;

public class player {
    public final String name;
    public final int age;
    public int score;
    public piece[] pieces;

    /* [Added comment]
    public resources[] playerResource; // to put into account what the players' resources are at the end of each turn
    // import the Scoring class to here to process the players' resources and update each of the player's scores accordingly
     */

    public player(String name, int age) {
        // Needed to stop the compiler complaining about the final fields not being set
        this.name = name;
        this.age = age;
        this.score = 0;
        this.pieces = null;
    }
}
