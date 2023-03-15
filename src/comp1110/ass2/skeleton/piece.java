package comp1110.ass2.skeleton;

public class piece {
    enum pieceType {
        Settler, Villager
    }
    public final pieceType type;
    public final player owner;
    public tile placedOn;

public piece(pieceType type, player owner) {
        this.type = type;
        this.owner = owner;

    }

}
