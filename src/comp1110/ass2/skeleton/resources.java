package comp1110.ass2.skeleton;

public class resources {
    enum resourceType {
        coconuts, bamboo, water, preciousStones
    }

    public final resourceType type;

    public resources(resourceType type) {
        this.type = type;
    }
}
