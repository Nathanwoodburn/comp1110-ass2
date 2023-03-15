package comp1110.ass2.skeleton;

public class tile {
    enum tileType {
        Land, Water
    }
    public final tileType type;
    public int x;
    public int y;
    public piece[] pieces;

    public tile(tileType type, int x, int y) {
        this.type = type;
        this.x = x;
        this.y = y;
    }
}
