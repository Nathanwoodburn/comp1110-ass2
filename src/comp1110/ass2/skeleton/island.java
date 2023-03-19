package comp1110.ass2.skeleton;

public class island {
    public final int width;
    public final int height;
    public final int x;
    public final int y;
    public tile[][] tiles;
    public final int score;

    public island(int width, int height,int x, int y, int score, tile[][] tiles) {
        // Needed to stop the compiler complaining about the final fields not being set
        this.width = width;
        this.height = height;
        this.score = score;
        this.tiles = tiles;
        this.x = x;
        this.y = y;
    }
}
