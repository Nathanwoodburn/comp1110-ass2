package comp1110.ass2.skeleton;

public class piece {
    enum pieceType {
        Settler, Village
    }

    public final pieceType type;
    public final player owner;
    public tile placedOn;

    public piece(pieceType type, player owner) {
        // Needed to stop the compiler complaining about the final fields not being set
        this.type = type;
        this.owner = owner;
    }

}
