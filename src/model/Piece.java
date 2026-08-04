package model;

import java.util.List;

public abstract class Piece {
    private final Color color;
    public Piece(Color color) {
        this.color = color;
    }

    // Every piece will use its own rules to get legal moves
    public abstract List<Move> getLegalMoves(Board board, int row, int col);

    public Color getColor() {
        return color;
    }
}
