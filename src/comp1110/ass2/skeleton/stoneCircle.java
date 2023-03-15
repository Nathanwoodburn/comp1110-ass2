package comp1110.ass2.skeleton;

public class stoneCircle {
    public final int x;
    public final int y;
    public boolean claimed;
    public player owner;

    public final resources[] resources;
    public final statuettes[] statuettes;

    public stoneCircle(int x, int y, resources[] resources, statuettes[] statuettes) {
        this.x = x;
        this.y = y;
        this.claimed = false;
        this.owner = null;
        this.resources = resources;
        this.statuettes = statuettes;
    }
}
