package comp1110.ass2.skeleton;

public class resources {
    enum resourceType {
        coconuts, bamboo, water, preciousStones
    }

    public final resourceType type;

    public resources(resourceType type) {
        // Needed to stop the compiler complaining about the final fields not being set
        this.type = type;
    }
}
