package comp1110.ass2.skeleton;

public class island {
    public final int width;
    public final int height;
    public tile[][] tiles;
    public final int score;

    public island(int width, int height, int score) {
        this.width = width;
        this.height = height;
        this.score = score;
        this.tiles = new tile[width][height];
    }
}
