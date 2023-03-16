package comp1110.ass2.skeleton;

public class tile {
    enum tileType {
        Land, Water
    }
    public int x;
    public int y;
    public final tileType type;
    public piece[] pieces;

    public tile(int x, int y, tileType type) {
        this.type = type;
        this.x = x;
        this.y = y;
    }

    // added comment
    // a placeOn method here?
}
