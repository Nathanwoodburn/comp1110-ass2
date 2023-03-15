package comp1110.ass2.skeleton;

public class stoneCircle {
    public final int x;
    public final int y;
    public boolean claimed;
    public final resources[] resources;
    public final int statuettes;

    public stoneCircle(int x, int y, resources[] resources, int numStatuettes) {
        // Needed to stop the compiler complaining about the final fields not being set
        this.x = x;
        this.y = y;
        this.claimed = false;
        this.resources = resources;
        this.statuettes = numStatuettes;
    }
}
