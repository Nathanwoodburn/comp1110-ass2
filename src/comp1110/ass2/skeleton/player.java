package comp1110.ass2.skeleton;

public class player {
    public final String name;
    public final int age;
    public int score;
    public piece[] pieces;

public player(String name, int age) {
        this.name = name;
        this.age = age;
        this.score = 0;
    }
}
